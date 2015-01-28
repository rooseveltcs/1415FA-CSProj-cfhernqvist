package topdownshooter;

public class Loadout_Spec extends Loadout {
	public Loadout_Spec()
	{
		weapon = new int[]{1, 4, 7};
		name = "Spec ops";
		letter = 'S';
		speedMultiplier = 1.2;
		healthMultiplier = 0.8;
		regenMultiplier = 2;
	}
}
