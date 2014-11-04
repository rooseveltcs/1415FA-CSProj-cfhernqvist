package topdownshooter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class Weapon {
	double speed, rate, damage;
	Color color;
	double r1, r2;
	double spread, lightDist, lightIntensity;
	double optimalRange = 5, rangeForgiveness = 1;
	int shots;
	String name;
	double lifeTime = 10;
	boolean laserish = false;
	
	
	void bulletUpdate (Bullet bullet)
	{
		if (laserish)
		{
			laserUpdate(bullet);
			return;
		}
		Situation s = Main.situation;
		int team = bullet.team;
		double dx = Math.cos(bullet.angle), dy = Math.sin(bullet.angle), step = 0.1;
		Point add = new Point(dx*step, dy*step);
		double length = speed*Settings.deltaTime;
		flyEffect(bullet);
		while (length > 0)
		{
			bullet.pos.add(add);
			length -= step;
			int hitActor = s.bulletCollisionId(bullet.pos, team);
			if (s.map.collisionPoint(bullet.pos) || hitActor != -1)
			{
				if (hitActor != -1)
					s.bulletHit(hitActor, bullet);
				bullet.pos.subtract(add);
				this.end(bullet);
				bullet.active = false;
				return;
			}
		}
	}
	
	void laserUpdate (Bullet bullet)
	{
		if (!bullet.damaging)
			return;
		Situation s = Main.situation;
		int team = bullet.team;
		double dx = Math.cos(bullet.angle), dy = Math.sin(bullet.angle), step = 0.1;
		Point add = new Point(dx*step, dy*step);
		while (true)
		{
			int hitActor = s.bulletCollisionId(bullet.pos, team);
			if (s.map.collisionPoint(bullet.pos) || hitActor != -1)
			{
				if (hitActor != -1)
					s.bulletHit(hitActor, bullet);
				bullet.damaging = false;
				s.addExplosion(new ExplosionEffect(10, 0.1, 0.2, Color.white, bullet.pos, 0.3, 5));
				return;
			}
			bullet.pos.add(add);
		}
	}
	
	void bulletDraw (Bullet b, Graphics2D g)
	{
		if (laserish)
		{
			laserDraw(b, g);
			return;
		}
		int steps = 5;
		double r = r2, delta = (r2-r1)/steps;
		Color c = ExtraMath.interpolate(color, Settings.teamColor[b.team], 0.4f);
		g.setColor(ExtraMath.blendAlpha(c, 0.2f));
		for (int i = 0; i < steps; i++)
		{
			r -= delta;
			int w = ExtraMath.sX(r, 0)-ExtraMath.sX(0, 0);
			if (i == steps-1)
				g.setColor(color);
			g.fillOval(ExtraMath.sX(b.pos)-w, ExtraMath.sY(b.pos)-w, 2*w, 2*w);
		}
	}
	
	void laserDraw (Bullet b, Graphics2D g)
	{
		int steps = 2;
		double r = b.weapon.r2, delta = (b.weapon.r2-b.weapon.r1)/steps;
		Color c = ExtraMath.interpolate(b.weapon.color, Settings.teamColor[b.team], 0.4f);
		//g.setColor(ExtraMath.blendAlpha(c, 0.2f));
		for (int i = 0; i < steps; i++)
		{
			r -= delta;
			int w = (int)((ExtraMath.sX(r, 0)-ExtraMath.sX(0, 0))*(b.timer/lifeTime)*r2/(steps+1.0));
			g.setColor(c);
			if (i == steps-1)
				g.setColor(b.weapon.color);
			g.setColor(ExtraMath.blendAlpha(g.getColor(), (float)(b.timer/lifeTime)));
			g.setStroke(new BasicStroke(w));
			g.drawLine(ExtraMath.sX(b.pos), ExtraMath.sY(b.pos), ExtraMath.sX(b.start), ExtraMath.sY(b.start));
			g.setStroke(new BasicStroke(1));
		}
	}
	
	void explode (Bullet b, double range)
	{
		Situation s = Main.situation;
		Actor a;
		double dist;
		for (int i = 0; i < s.actors.size(); i++)
			if ((a = s.actors.get(i)).active && (dist = ExtraMath.dist(b.pos, a.pos)) < range
					&& !s.map.lineCast(a.pos.clone(), b.pos))
				s.bulletHit(a.id, b, damage*((range-dist)/range));
	}
	
	double rating (double dist)
	{
		return rangeForgiveness/Math.abs(dist-optimalRange);
	}
	
	void end(Bullet b)
	{
		//b.weapon.end(b);
		Main.situation.addExplosion(new ExplosionEffect(3, 0.05, 0.1, color, b.pos, 0.3, 5));
	}
	
	void flyEffect (Bullet b)
	{
		
	}
}
