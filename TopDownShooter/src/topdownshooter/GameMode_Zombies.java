package topdownshooter;

public class GameMode_Zombies extends GameMode {
	
	public GameMode_Zombies(){
		name = "Zombies!";
	}
	
	void update2(){
		Actor a;
		for (int i = 0; i < s.actors.size(); i++)
			if ((a = s.actors.get(i)).active && !a.ingame && a.spawnTime <= 0 && a.isBot)
				a.spawn();
	}
	
	public void startMatch (Situation s, int teams, int playerTeam, int players)
	{
		s.setMatch(2, 100, Settings.matchTime);
		s.addActor(Actor.PLAYER, 0, "Player");
		for (int i = 0; i < 100; i++)
			s.addActor(Actor.ZOMBIE, 1, "Zombie "+i);
	}
	
}
