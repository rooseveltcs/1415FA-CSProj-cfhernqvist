package topdownshooter;

public class Objective_CaptureFlag extends Objective {
	
	int victimId = -1;
	Bot bot;
	Actor victim;
	Path path;
	
	public Objective_CaptureFlag (Bot bot)
	{
		this.bot = bot;
		setPath();
		setVictim();
		name = "Capturing";
	}
	
	void setPath ()
	{
		path = Main.situation.map.pathTo(bot.pos, Main.situation.map.flagPoint((bot.team+1)%2));
	}
	
	void setVictim ()
	{
		victimId = bot.bestVictim();
		if (victimId != -1)
			victim = Main.situation.actors.get(victimId);
		setPath();
	}
	
	public void update ()
	{
		Situation s = Main.situation;
		bot.target = bot.pos.clone();
		
		if (path == null || path.p.length == 0)
		{
			setPath();
			return;
		}
		bot.target = path.next(bot.pos);
		
		
		autoAgressive();
	}
	
	double rating ()
	{
		if (path == null)
			return 0;
		return (Main.situation.gameMode == 2?0.2:0);
	}
	
}
