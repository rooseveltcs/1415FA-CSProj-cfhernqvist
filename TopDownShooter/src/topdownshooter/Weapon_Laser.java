package topdownshooter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class Weapon_Laser extends Weapon {
	public Weapon_Laser ()
	{
		name = "Laser";
		speed = 2000;
		rate = 0.02;
		damage = 0.025;
		color = Color.red;
		r1 = 0.2;
		r2 = 0.5;
		shots = 1;
		spread = 0;
		lightDist = 1;
		lightIntensity = 0.7;
		lifeTime = 0.1;
		laserish = true;
		optimalRange = 70;
		rangeForgiveness = 10;
	}
	
	@Override
	void end(Bullet b)
	{
		Main.situation.addExplosion(new ExplosionEffect(1, 0.02, 0.05, Color.red, b.pos, 0.3, 3));
	}
}
