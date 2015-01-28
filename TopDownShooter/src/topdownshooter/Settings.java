package topdownshooter;

import java.awt.Color;

public class Settings {
	static int width = 800, height = 600, gS = 10;
	static int playersOnTeam = 1, teamsInPlay = 2, playerTeam = 0, matchTime = 60;
	static int fps = 60;
	static long deltaMillis;
	static int tilesOnScreen = 30;
	static int flagBonus = 20;
	static double deltaTime;
	static double cX = 0, cY = 0;
	static double actorSpeed = 4, spawnDelay = 1;
	static double heightFactor = 0.1; //height factor for 3d
	static double actorRadius = 0.3;
	static double actorLightDist = 4, bulletLightDist = 2;
	static Color[] teamColor = {Color.blue, Color.red, Color.green, Color.cyan
		, Color.orange, Color.magenta, Color.pink, Color.yellow, Color.gray};
	static String[] teamName = {"Alpha", "Bravo", "Charlie", "Delta", "Echo", "Foxtrot"
		, "Golf", "Hotel", "India"};
	static Color floorColor = new Color(250, 250, 250);
	static Color wallBottom = new Color(120, 120, 120);
	static Color wallTop = new Color(80, 80, 80);
	static Color wallSurface = new Color (40, 40, 40);
	static Color ambientLight = new Color(10, 10, 10);
	static boolean shadows = true, light = true, antiAlias = true;
	static int interpolationQuality = 1;
	static int startMap = 2, menuMap = 2, startGameMode = 0;
	
	static GameMode[] gameMode = {
		new GameMode_TD(),
		new GameMode_Zombies(),
		new GameMode_CF()
	};
	
	static Weapon[] weapon = {
		new Weapon_AR(),
		new Weapon_Shotgun(),
		new Weapon_SMG(),
		new Weapon_SR(),
		new Weapon_Laser(),
		new Weapon_Pistol(),
		new Weapon_LMG(),
		new Weapon_Grenade(),
		new Weapon_GL()
	};
	
	static Loadout[] loadout = {
		new Loadout_Assault(),
		new Loadout_Spec(),
		new Loadout_Recon(),
		new Loadout_Tank(),
		new Loadout_Knife()
	};
	
	static Map[] maps = {
		new Map("Small", 13, 10, new String[]{
				"XXXXXXXXXXXXX",
				"X     3     X",
				"X  X     X  X",
				"X  XX   XX  X",
				"X0    X X  1X",
				"X0  X X    1X",
				"X0 XX   XX 1X",
				"X  X     X  X",
				"X     2     X",
				"XXXXXXXXXXXXX"}),
		new Map ("Pac man", 28, 24, new String[]{
				"XXXXXXXXXXXXXXXXXXXXXXXXXXXX",
				"X            XX            X",
				"X XXXX XXXXX XX XXXXX XXXX X",
				"X XXXX XXXXX XX XXXXX XXXX X",
				"X     1              2     X",
				"X XXXX XX XXXXXXXX XX XXXX X",
				"X      XX    XX    XX      X",
				"X  XXX XXXXX XX XXXXX XXX  X",
				"X    X XX          XX X    X",
				"X    X XX XXX  XXX XX X    X",
				"XXXXXX XX X      X XXXXXX  X",
				"X         X      X         X",
				"XXXXXXXXX X      X XX XXXXXX",
				"X    X XX XXXXXXXX XX X    X",
				"X    X XX          XX X    X",
				"X  XXX XX XXXXXXXX XX XXX  X",
				"X     0      XX      3     X",
				"X XXXX XXXXX XX XXXXX XXXX X",
				"X   XX                XX   X",
				"XXX XX XX XXXXXXXX XX XX XXX",
				"X      XX    XX    XX      X",
				"X XXXXXXXXXX XX XXXXXXXXXX X",
				"X                          X",
				"XXXXXXXXXXXXXXXXXXXXXXXXXXXX"}),
		new Map ("Stuff", 19, 19, new String[]{
				"XXXXXXXXXXXXXXXXXXX",
				"X0    X  4  X    3X",
				"X   X XX   XX X   X",
				"X   X         X   X",
				"X XXX         XXX X",
				"X        X        X",
				"XXX             XXX",
				"X X    X   X    X X",
				"X                 X",
				"X5   X   8   X   6X",
				"X                 X",
				"X X    X   X    X X",
				"XXX             XXX",
				"X        X        X",
				"X XXX         XXX X",
				"X   X         X   X",
				"X   X XX   XX X   X",
				"X2    X  7  X    1X",
				"XXXXXXXXXXXXXXXXXXX"
		}),
		new Map ("mazeish", 19, 19, new String[]{
				"XXXXXXXXXXXXXXXXXXX",
				"X0 X              X",
				"X  X  X       X   X",
				"X     XXXXXXXXX   X",
				"XXX   X       X   X",
				"X         X       X",
				"X         XXXXXXXXX",
				"XX XXXX           X",
				"X    1X           X",
				"XXXXXXXXXXXXX     X",
				"X 1  X   X        X",
				"X    X X X        X",
				"X    X X X  XXXXXXX",
				"X    X X X        X",
				"X    X X          X",
				"X      XXXXXXXX   X",
				"XX XXXXX      X   X",
				"X                 X",
				"XXXXXXXXXXXXXXXXXXX"
		}),
		new Map ("Capture", 21, 10, new String[]{
				"XXXXXXXXXXXXXXXXXXXXX",
				"X0        X        1X",
				"X0  X     X     X  1X",
				"X0  X           X  1X",
				"XX XX     X     XX XX",
				"X         X         X",
				"X     XXX   XXX     X",
				"X  A             B  X",
				"X    X         X    X",
				"XXXXXXXXXXXXXXXXXXXXX"
		})
	};
	
	static public void init ()
	{
		deltaMillis = 1000/fps;
		deltaTime = deltaMillis/1000.0;
		//deltaTime /= 2;
		for (int i = 0; i < maps.length; i++)
		{
			maps[i].findNodes();
			//maps[i].pathTo(new Point(0, 0), new Point(maps[i].width/2, maps[i].height/2));
		}
	}
}
