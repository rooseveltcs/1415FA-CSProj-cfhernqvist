package topdownshooter;

public class Loadout_Tank extends Loadout {
	public Loadout_Tank()
	{
		weapon = new int[]{6, 8, 7};
		name = "Heavy";
		letter = 'H';
		speedMultiplier = 0.65;
		healthMultiplier = 2.5;
		regenMultiplier = 0.5;
	}
}
