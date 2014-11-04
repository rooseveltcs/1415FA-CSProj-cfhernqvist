package topdownshooter;

public class GameMode_TD extends GameMode {
	
	public GameMode_TD(){
		name = "TDM";
	}
	
	void update2(){
		Actor a;
		for (int i = 0; i < s.actors.size(); i++)
			if ((a = s.actors.get(i)).active && !a.ingame && a.spawnTime <= 0 && a.isBot)
				a.spawn();
	}
	
}
