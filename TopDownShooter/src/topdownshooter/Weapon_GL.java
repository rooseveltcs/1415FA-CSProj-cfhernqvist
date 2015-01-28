package topdownshooter;

import java.awt.Color;

public class Weapon_GL extends Weapon {
	
	public Weapon_GL ()
	{
		name = "Grenade Launcher";
		speed = 5;
		rate = 0.5;
		damage = 1.5;
		color = Color.black;
		r1 = 0.1;
		r2 = 0.13;
		shots = 1;
		spread = 0.1;
		lightDist = 1.5;
		lightIntensity = 0.4;
		lifeTime = 10;
		optimalRange = 11;
	}
	
	
	void end (Bullet b)
	{
		explode(b, 2);
		Main.situation.addExplosion(new ExplosionEffect(50, 0.1, 0.4, Color.red, b.pos, 0.3, 10));
		Main.situation.addExplosion(new ExplosionEffect(50, 0.1, 0.4, Color.orange, b.pos, 0.3, 10));
	}
	
	void flyEffect(Bullet b)
	{
		Main.situation.addExplosion(new ExplosionEffect(1, 0.05, 0.15, Color.orange, b.pos, 0.2, 4));
	}
	
}
