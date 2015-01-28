package topdownshooter;

import java.awt.Color;

public class Weapon_Pistol extends Weapon {
	public Weapon_Pistol ()
	{
		name = "Pistol";
		speed = 8;
		rate = 0.25;
		damage = 0.3;
		color = Color.gray;
		r1 = 0.05;
		r2 = 0.3;
		shots = 1;
		spread = 0.2;
		lightDist = 1;
		lightIntensity = 0.4;
		optimalRange = 1;
	}
}
