package topdownshooter;

public class Loadout_Assault extends Loadout {
	public Loadout_Assault()
	{
		weapon = new int[]{0, 2, 7};
		name = "Assault";
		letter = 'A';
		speedMultiplier = 1;
		healthMultiplier = 1;
		regenMultiplier = 1;
	}
}
