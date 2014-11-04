package topdownshooter;

import java.awt.Color;

public class Weapon_AR extends Weapon {
	public Weapon_AR ()
	{
		name = "Assault Rifle";
		speed = 12;
		rate = 0.15;
		damage = 0.4;
		color = Color.white;
		r1 = 0.1;
		r2 = 0.3;
		shots = 1;
		spread = 0.1;
		lightDist = 1.5;
		lightIntensity = 0.4;
		optimalRange = 5;
	}
}
