package topdownshooter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class Weapon_SR extends Weapon {
	public Weapon_SR ()
	{
		name = "Sniper Rifle";
		speed = 2000;
		rate = 1;
		damage = 0.6;
		color = Color.white;
		r1 = 0.1;
		r2 = 1.5;
		shots = 1;
		spread = 0.01;
		lightDist = 4;
		lightIntensity = 0.7;
		lifeTime = 0.3;
		laserish = true;
		optimalRange = 8;
	}
	
	/*
	void bulletDraw (Bullet b, Graphics2D g)
	{
		int steps = 2;
		double r = b.weapon.r2, delta = (b.weapon.r2-b.weapon.r1)/steps;
		Color c = ExtraMath.interpolate(b.weapon.color, Settings.teamColor[b.team], 0.4f);
		//g.setColor(ExtraMath.blendAlpha(c, 0.2f));
		for (int i = 0; i < steps; i++)
		{
			r -= delta;
			int w = (int)((ExtraMath.sX(r, 0)-ExtraMath.sX(0, 0))*(b.timer/lifeTime)*5.0/(steps+1.0));
			g.setColor(c);
			if (i == steps-1)
				g.setColor(b.weapon.color);
			g.setColor(ExtraMath.blendAlpha(g.getColor(), (float)(b.timer/lifeTime)));
			g.setStroke(new BasicStroke(w));
			g.drawLine(ExtraMath.sX(b.pos), ExtraMath.sY(b.pos), ExtraMath.sX(b.start), ExtraMath.sY(b.start));
			g.setStroke(new BasicStroke(1));
		}
	}
	
	void bulletUpdate (Bullet bullet)
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
	}*/
}
