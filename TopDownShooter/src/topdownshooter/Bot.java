package topdownshooter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class Bot extends Actor {
	
	int victimId = -1;
	Objective objective;
	boolean moved = false;
	double dodgeDist = 1.5;
	Point aim;
	
	public Bot  (double x, double y, String name, int team, int id)
	{
		pos = new Point(x, y);
		aim = pos.clone();
		this.name = name;
		this.team = team;
		this.id = id;
		setLoadout((int)(4*Math.random()));
		newRandomTarget();
	}
	
	void newRandomTarget()
	{
		target = Main.situation.map.randomPos();
	}
	
	int newRandomVictim ()
	{
		ArrayList<Integer> possibles = new ArrayList<>();
		Actor a;
		for (int i = 0; i < Main.situation.actors.size(); i++)
			if ((a = Main.situation.actors.get(i)).active && a.ingame && a.team != team)
				possibles.add(i);
		if (possibles.size() == 0)
			return -1;
		return possibles.get((int)(Math.random()*possibles.size()));
	}
	
	int bestVictim ()
	{
		int best = -1;
		double bestRating = 0, newRating;
		Actor a;
		for (int i = 0; i < Main.situation.actors.size(); i++)
		{
			a = Main.situation.actors.get(i);
			newRating = 1/(ExtraMath.dist(pos, a.pos));
			if (a.hasFlag)
				newRating += 0.1;
			if (!Main.situation.map.lineCast(pos.clone(), a.pos))
				newRating *= 10;
			if (a.active && a.ingame && a.team != team
			 && newRating > bestRating)
			{
				bestRating = newRating;
				best = i;
			}
		}
		if (best == -1 || bestRating == 0)
			return newRandomVictim();
		return best;
	}
	
	@Override
	void update ()
	{
		if (objective == null || Math.random() < 20*Settings.deltaTime)
			evaluate();
		target = pos.clone();
		objective.update();
		dodge();
		moveTowards(target.x, target.y, speed*Settings.deltaTime, true);
	}
	
	void dodge()
	{
		Situation s = Main.situation;
		int bullet = -1;
		double bestDist = 100;
		for (int i = 0; i < s.bullets.size(); i ++)
		{
			Bullet b = s.bullets.get(i);
			if (b.team == team || !b.active)
				continue;
			double dist = ExtraMath.manDist(b.pos, pos);
			if (dist < dodgeDist && dist < bestDist)
			{
				bullet = i;
				bestDist = dist;
			}
		}
		if (bullet == -1)
			return;
		Point vector = pos.subtractNew(s.bullets.get(bullet).pos);
		target = pos.addNew(vector.scale(100));
	}
	
	void aimAt (Point p)
	{
		double div = 0.1/Settings.deltaTime;
		aim.x += (p.x-aim.x)/div;
		aim.y += (p.y-aim.y)/div;
	}
	
	void shootAt (Actor victim)
	{
		int best = 0;
		for (int i = 1; i < 2; i++)
			if (Settings.weapon[l.weapon[i]].rating(ExtraMath.dist(pos, victim.pos))
					> Settings.weapon[l.weapon[best]].rating(ExtraMath.dist(pos, victim.pos)))
				best = i;
		aimAt(shootPoint(victim, Settings.weapon[l.weapon[best]]));
		shoot(aim, l.weapon[best]);
	}
	
	Point shootPoint(Actor a, Weapon w)
	{
		Point p = a.pos.clone();
		double factor = ExtraMath.dist(a.pos, pos)/(w.speed*Settings.deltaTime);
		factor *= Math.random();
		Point delta = a.pos.subtractNew(a.prev);
		p.add(delta.scale(factor));
		return p;
	}
	
	void evaluate ()
	{
		Objective[] o = new Objective[Objective.count];
		int best = 0;
		double bestRating = 0;
		for (int i = 0; i < Objective.count; i++)
		{
			o[i] = Objective.get(i, this);
			double rating;
			if ((rating = o[i].rating()) > bestRating)
			{
				best = i;
				bestRating = rating;
				
			}
			//System.out.println("i = "+i+", r = "+rating);
		}
		//System.out.println("evaluated "+name+", best = "+o[best].name);
		objective = o[best];
	}
	
	void draw (Graphics2D g)
	{
		if (objective == null )
			return;
		g.setFont(GUI.fsmall);
		g.setColor(Color.orange);
		int sx = ExtraMath.sX(pos.x+Settings.actorRadius, 0), sy = ExtraMath.sY(pos.y-Settings.actorRadius, 0);
		g.drawString(objective.name, sx, sy);
	}
	
}
