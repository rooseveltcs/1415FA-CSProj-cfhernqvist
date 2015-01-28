package topdownshooter;

public class Loadout_Knife extends Loadout {
	public Loadout_Knife()
	{
		weapon = new int[]{5, 4, 5};
		name = "Light";
		letter = 'L';
		speedMultiplier = 2.5;
		healthMultiplier = 0.1;
		regenMultiplier = 3;
	}
}
