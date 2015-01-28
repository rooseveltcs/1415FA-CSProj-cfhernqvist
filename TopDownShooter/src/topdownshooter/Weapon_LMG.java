package topdownshooter;

import java.awt.Color;

public class Weapon_LMG extends Weapon {
	public Weapon_LMG ()
	{
		name = "Light Machine Gun";
		speed = 15;
		rate = 0.3;
		damage = 0.6;
		color = new Color(1, 0.8f, 0.7f);
		r1 = 0.15;
		r2 = 0.35;
		shots = 1;
		spread = 0.1;
		lightDist = 1.5;
		lightIntensity = 0.4;
		optimalRange = 10;
	}
}
