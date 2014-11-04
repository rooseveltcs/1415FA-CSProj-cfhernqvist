package topdownshooter;

import java.awt.Color;

public class Weapon_SMG extends Weapon {
	public Weapon_SMG ()
	{
		name = "SMG";
		speed = 10;
		rate = 0.08;
		damage = 0.26;
		color = Color.green;
		r1 = 0.07;
		r2 = 0.2;
		shots = 1;
		spread = 0.2;
		lightDist = 1.3;
		lightIntensity = 0.35;
		optimalRange = 4;
	}
}
