package topdownshooter;

import java.awt.Color;

public class Weapon_Shotgun extends Weapon {
	public Weapon_Shotgun ()
	{
		name = "Shotgun";
		speed = 20;
		rate = 0.4;
		damage = 0.23;
		color = Color.white;
		r1 = 0.1;
		r2 = 0.1;
		shots = 5;
		spread = 0.3;
		lightDist = 1;
		lightIntensity = 0.2;
		optimalRange = 3;
	}
}
