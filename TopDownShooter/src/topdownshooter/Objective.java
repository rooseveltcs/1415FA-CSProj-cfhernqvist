package topdownshooter;

public class Objective {
	Bot bot;
	static int count = 4;
	String name;
	boolean canSee = false;
	
	int victimId = -1;
	Actor victim;
	Path path;
	
	
	public void update ()
	{
		
	}
	
	void setPath ()
	{
		if (victim == null)
			return;
		path = Main.situation.map.pathTo(bot.pos, victim.pos);
	}
	
	void setVictim ()
	{
		victimId = bot.bestVictim();
		if (victimId != -1)
			victim = Main.situation.actors.get(victimId);
		setPath();
	}
	
	void autoAgressive ()
	{
		if (Math.random() < 2*Settings.deltaTime)
			setVictim();
		
		if (victim == null)
		{
			setVictim();
			return;
		}
		
		canSee = !Main.situation.map.lineCast(bot.pos.clone(), victim.pos);
		if (canSee)
			bot.shootAt(victim);
		
		bot.point = victim.pos;
	}
	
	static Objective get (int i, Bot bot)
	{
		switch (i)
		{
		case 0:
			return new Objective_Attack(bot);
		case 1:
			return new Objective_Flee(bot);
		case 2:
			return new Objective_ReturnFlag(bot);
		case 3:
			return new Objective_CaptureFlag(bot);
		default:
			return null;
		}
	}
	
	double rating()
	{
		return 0;
	}
	
}
