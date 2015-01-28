package topdownshooter;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class Actor {
	double health = 1, speed = Settings.actorSpeed, spawnTime = 0.1;
	double shootTimer = 0;
	Point pos, target, prev, point;
	int victim = 0, weapon = 0, loadout = 0;
	int kills = 0, deaths = 0, team, id;
	String name;
	static int PLAYER = 0, BOT = 1, ZOMBIE = 2;
	boolean active = true, ingame = false, isBot = true, hasFlag = false;
	Loadout l;
	
	
	void setLoadout (int loadout)
	{
		this.loadout = loadout;
		l = Settings.loadout[loadout];
		weapon = l.weapon[0];
		speed = Settings.actorSpeed*l.speedMultiplier;
	}
	
	void spawn ()
	{
		pos = Main.situation.map.spawnPoint(team);
		pos.add(Point.baseVector(Math.random()*100).scale(0.1));
		prev = pos.clone();
		health = 1;
		ingame = true;
		if (isBot)
			setLoadout((int)(4.5*Math.random()));
	}
	
	void despawn ()
	{
		ingame = false;
		spawnTime = Settings.spawnDelay;
		hasFlag = false;
		Main.situation.addExplosion(
				new ExplosionEffect(15, 0.1, 0.2, Settings.teamColor[team], pos, 0.5, 4));
	}
	
	void moveTowards (Point p, double max, boolean maximize)
	{
		moveTowards(p.x, p.y, max, maximize);
	}
	
	void moveTowards (double tx, double ty, double max, boolean maximize)
	{
		Point rprev = pos.clone();
		double x = pos.x, y = pos.y;
		double r = Settings.actorRadius;
		double dx = tx-x, dy = ty-y, px = x, py = y;
		double dist = ExtraMath.dist(0, 0, dx, dy);
		if (dist <= max)
		{
			dx = tx-x;
			dy = ty-y;
		} else {
			double factor = max/dist;
			dx *= factor;
			dy *= factor;
		}
		
		double rx = x-Math.floor(x)-0.5;
		double ry = y-Math.floor(y)-0.5;
		double limx = Math.min(Math.max(-rx-0.5+r+0.00001, dx), -rx+0.5-r-0.00001);
		double limy = Math.min(Math.max(-ry-0.5+r+0.00001, dy), -ry+0.5-r-0.00001); // deltas to nearest wall
		
		boolean stopx = false, stopy = false;
		
		if ((tx != x) && Main.situation.map.collisionAt(x+dx, y))
		{
			stopx = true;
			if (!Main.situation.map.collisionAt(x+limx, y))
				x += limx;
		} else x += dx;
		
		if ((ty != y) && Main.situation.map.collisionAt(x, y+dy))
		{
			stopy = true;
			if (!Main.situation.map.collisionAt(x, y+limy))
				y += limy;
		} else y += dy;
		
		pos.x = x;
		pos.y = y;
		
		double movedDist; //only for AI's, makes sure they move as far towards objective as possible
		if (maximize && !(x == tx && y == ty) && (movedDist = ExtraMath.dist(px, py, x, y)) < max)
		{
			moveTowards(stopx ? x : x+dx*10, stopy ? y : y+dy*10, max-movedDist, false);
		}
		prev = rprev;
	}
	
	void addLight ()
	{
		Main.situation.map.addLight(pos, Settings.teamColor[team], Settings.actorLightDist, 0.5f);
	}
	
	void engineDraw (Graphics2D g)
	{
		if (!ingame)
			return;
		
		int w = (int)(Settings.actorRadius*Settings.gS*2);
		int m = (int)(w*0.1);
		int sx = ExtraMath.sX(pos.x-Settings.actorRadius, 0), sy = ExtraMath.sY(pos.y-Settings.actorRadius, 0);
		g.setColor(Color.BLACK);
		g.fillOval(sx, sy, w, w);
		g.setColor(Settings.teamColor[team].darker().darker());
		g.fillOval(sx+m, sy+m, w-2*m, w-2*m);
		g.setColor(ExtraMath.interpolate(Settings.teamColor[team], Color.white, 0.9f));
		g.fillArc(sx+m, sy+m, w-2*m, w-2*m, 0, (int)(360*health));
		g.setColor(Color.black);
		g.fillOval(sx+2*m, sy+2*m, w-4*m, w-4*m);
		g.setFont(new Font("Arial", Font.PLAIN, (int)(w*0.5)));
		g.setColor(Color.white);
		g.drawString(""+l.letter, sx+(int)(w*0.4), sy+(int)(w*0.7));
		//if (point != null)
		//	drawLaser(g, point);
	}
	
	void update () //overridden by subclasses
	{
		
	}
	
	void draw (Graphics2D g)
	{
		
	}
	
	void shoot (Point towards, int weapon)
	{
		if (shootTimer > 0)
			return;
		Weapon w = Settings.weapon[weapon];
		this.weapon = weapon;
		for (int i = 0; i < w.shots; i++)
			Main.situation.addBullet(new Bullet (pos.clone(), ExtraMath.direction(pos, towards)-w.spread/2+Math.random()*w.spread, weapon, team, id));
		shootTimer = w.rate;
	}
	
	void drawLaser (Graphics2D g, Point p)
	{
		Point end = pos.clone();
		Main.situation.map.lineCast(end, ExtraMath.direction(pos, p), 1000);
		g.drawLine(ExtraMath.sX(pos), ExtraMath.sY(pos), ExtraMath.sX(end), ExtraMath.sY(end));
	}
	
	void drawLaser(Graphics2D g, double dir)
	{
		drawLaser (g, pos.addNew(Point.baseVector(dir).scale(1000)));
	}
	
	void engineUpdate()
	{
		if (!ingame)
		{
			spawnTime -= Settings.deltaTime;
			return;
		}
		health = Math.min(health+0.1*l.regenMultiplier*Settings.deltaTime, 1);
		if (shootTimer > 0)
			shootTimer -= Settings.deltaTime;
		if (health <= 0)
			despawn();
		avoidCrowd();
		//moveTowards(target.x, target.y, speed*Settings.deltaTime, isBot);
	}
	
	void avoidCrowd()
	{
		Situation s = Main.situation;
		int j = -1;
		double dist = Settings.actorRadius*2, sb = 100, sp;
		for (int i = 0; i < s.actors.size(); i++)
			if (i != id && s.actors.get(i).ingame && (sp = ExtraMath.manDist(s.actors.get(i).pos, pos)) < dist && sp < sb)
			{
				sb = sp;
				j = i;
			}
		if (j == -1)
			return;
		Point p = s.actors.get(j).pos;
		moveTowards(pos.addNew(Point.baseVector(ExtraMath.direction(p, pos))
				.scale(Settings.actorRadius*2-sb))
				, ExtraMath.dist(pos, p), false);
	}
}
