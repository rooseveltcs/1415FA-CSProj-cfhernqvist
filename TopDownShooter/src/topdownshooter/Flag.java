package topdownshooter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;

public class Flag {
	Point pos= new Point(0, 0);
	int team, heldBy = -2;
	double rot = 0;
	
	
	public Flag(int team){
		this.team = team;
		goBack();
	}
	
	public void update()
	{
		rot += Settings.deltaTime;
		if (Math.random()/Settings.deltaTime < 30)
			Main.situation.addExplosion(new ExplosionEffect(1, 0.03, 0.06
					, ExtraMath.blendAlpha(Settings.teamColor[team], 0.3f), pos, 0.9, 2));
		Situation s = Main.situation;
		for (int i = 0; i < s.actors.size() && heldBy < 0; i++)
		{
			Actor a = s.actors.get(i);
			if (!a.active || !a.ingame)
				continue;
			if (ExtraMath.dist(a.pos, pos) < 0.5)
			{
				if (heldBy == -1)
				{
					if (team == a.team)
						goBack();
					else
						attatch(i);
				}
				if (heldBy == -2 && team != a.team)
				{
					attatch(i);
				}
			}
		}
		if (heldBy >= 0)
		{
			Actor a = Main.situation.actors.get(heldBy);
			pos = a.pos.clone();
			if (!a.active || !a.ingame)
				goBack();
		}
		for (int i = 0; i < s.flags.size() && heldBy >= 0; i++)
		{
			Actor a = Main.situation.actors.get(heldBy);
			Flag f = s.flags.get(i);
			if (f == this)
				continue;
			if (ExtraMath.dist(pos, f.pos) < 0.5 && f.heldBy < 0)
			{
				s.teamScore[f.team] += Settings.flagBonus;
				a.kills += Settings.flagBonus;
				Main.situation.addExplosion(new ExplosionEffect(100, 0.06, 0.1
						, Settings.teamColor[f.team], pos, 0.9, 12));
				goBack();
			}
		}
		
	}
	
	public final void goBack ()
	{
		pos = Main.situation.map.flagPoint(team);
		if (heldBy >= 0)
			Main.situation.actors.get(heldBy).hasFlag = false;
		heldBy = -2;
	}
	
	public final void attatch (int i)
	{
		heldBy = i;
		Main.situation.actors.get(heldBy).hasFlag = true;
	}
	
	public void draw(Graphics2D g){
		double r = ExtraMath.sX(0.5, 0)-ExtraMath.sX(0, 0);
		int w = (int)(r*Settings.gS*2);
		int cx = ExtraMath.sX(pos.x, 0), cy = ExtraMath.sY(pos.y, 0);
		double cos = Math.cos(rot), sin = Math.sin(rot);
		g.setColor(Color.gray);
		g.setStroke(new BasicStroke(3));
		g.drawLine((int)(cx+r*cos), (int)(cy+r*sin), (int)(cx-r*cos), (int)(cy-r*sin));
		g.setStroke(new BasicStroke(1));
		g.setColor(Settings.teamColor[team]);
		Polygon p = new Polygon(
				new int[]{(int)(cx+r*cos), (int)(cx+r*cos*0.2), (int)(cx+r*0.8*Math.cos(rot-1))},
				new int[]{(int)(cy+r*sin), (int)(cy+r*sin*0.2), (int)(cy+r*0.8*Math.sin(rot-1))},
				3);
		g.fillPolygon(p);
		//g.fillArc(sx, sy, w, w, (int)(360*rot), (int)(360*rot+60));
	}

	public void addLight() {
		Main.situation.map.addLight(pos, Settings.teamColor[team], 2+0.7*Math.abs(rot%3-1.5), 0.5f);
		
	}
	
}
