package topdownshooter;

public class Objective_Attack extends Objective {
	
	int victimId = -1;
	Bot bot;
	Actor victim;
	Path path;
	
	public Objective_Attack (Bot bot)
	{
		this.bot = bot;
		victimId = bot.newRandomVictim();
	}
	
	void setPath ()
	{
		if (victim == null)
			return;
		path = Main.situation.map.pathTo(bot.pos, victim.pos);
	}
	
	void updatePath ()
	{
		
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
		if (Math.random() < 2*Settings.deltaTime)
			setVictim();
		if (victim == null)
		{
			setVictim();
			return;
		}
		if (path == null || path.p.length == 0)
		{
			setPath();
			return;
		}
		
		boolean canSee = !s.map.lineCast(bot.pos.clone(), victim.pos);
		if (canSee)
			bot.shootAt(victim);
		else
			bot.target = path.next(bot.pos);
		bot.point = victim.pos;
	}
}
