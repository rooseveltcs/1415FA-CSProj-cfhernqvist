package topdownshooter;

public class GameMode {
	Situation s;
	String name;
	
	void update2 ()
	{
		
	}
	
	public void update (Situation s)
	{
		this.s = s;
		update2();
	}
	
	public void startMatch (Situation s, int teams, int playerTeam, int players)
	{
		s.setMatch(teams, 100, Settings.matchTime);
		while (players-- > 0)
			for (int i = 0; i < teams; i++)
				if (players == 0 && i == playerTeam)
					s.addActor(Actor.PLAYER, i, "Player");
				else
					s.addActor(Actor.BOT, i, Settings.teamName[i]+" "+(players+1));
	}
}
