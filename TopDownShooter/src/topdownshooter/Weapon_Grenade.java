package topdownshooter;

import java.awt.Color;

public class Weapon_Grenade extends Weapon {
	
	public Weapon_Grenade ()
	{
		name = "Grenade";
		speed = 10;
		rate = 1;
		damage = 1.5;
		color = Color.orange;
		r1 = 0.1;
		r2 = 0.6;
		shots = 1;
		spread = 0.1;
		lightDist = 1.5;
		lightIntensity = 0.4;
		lifeTime = 1;
	}
	
	void bulletUpdate (Bullet bullet)
	{
		Situation s = Main.situation;
		double length = speed*Settings.deltaTime*(bullet.timer/lifeTime);
		if (s.map.lineCast(bullet.pos, bullet.angle, length))
		{
			boolean h = false, v = false;
			if (!s.map.collisionPoint(bullet.pos.addNew(new Point(-0.1, 0, 0)))
					|| !s.map.collisionPoint(bullet.pos.addNew(new Point(0.1, 0, 0))))
				v = true;
			if (!s.map.collisionPoint(bullet.pos.addNew(new Point(0, -0.1, 0)))
					|| !s.map.collisionPoint(bullet.pos.addNew(new Point(0, 0.1, 0))))
				h = true;
			if (v)
				bullet.angle = Math.PI-bullet.angle;
			if (h)
				bullet.angle = -bullet.angle;
			//bullet.angle += Math.PI;
		}
		if (bullet.timer < 0)
		{
			explode(bullet, 3);
			s.addExplosion(new ExplosionEffect(50, 0.1, 0.4, color, bullet.pos, 0.7, 10));
			end(bullet);
			bullet.active = false;
		}
	}
	
	
	
}
