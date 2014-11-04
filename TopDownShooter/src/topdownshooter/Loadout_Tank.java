package topdownshooter;

public class Loadout_Tank extends Loadout {
	public Loadout_Tank()
	{
		weapon = new int[]{6, 8, 7};
		name = "Heavy";
		letter = 'H';
		speedMultiplier = 0.6;
		healthMultiplier = 2;
	}
}
