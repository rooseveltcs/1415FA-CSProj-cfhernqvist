package topdownshooter;

public class GameMode_CF extends GameMode {
	
	public GameMode_CF(){
		name = "Capture the flag";
	}
	
	void update2(){
		Actor a;
		for (int i = 0; i < s.actors.size(); i++)
			if ((a = s.actors.get(i)).active && !a.ingame && a.spawnTime <= 0 && a.isBot)
				a.spawn();
	}
	
	public void startMatch (Situation s, int teams, int playerTeam, int players)
	{
		teams = 2;
		s.teams = 2;
		s.setMatch(teams, 100, Settings.matchTime);
		while (players-- > 0)
			for (int i = 0; i < teams; i++)
				if (players == 0 && i == playerTeam)
					s.addActor(Actor.PLAYER, i, "Player");
				else
					s.addActor(Actor.BOT, i, Settings.teamName[i]+" "+(players+1));
		s.setMap(Settings.maps[4]);
		for (int i = 0; i < teams; i++)
			s.flags.add(new Flag(i));
	}
	
}
