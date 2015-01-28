package topdownshooter;

public class Objective_Attack extends Objective {
	
	
	public Objective_Attack (Bot bot)
	{
		this.bot = bot;
		setVictim();
		setPath();
		name = "Attacking";
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
		
		autoAgressive();
		
		
		if (!canSee || ExtraMath.dist(bot.pos, victim.pos) 
				> Settings.weapon[bot.l.weapon[0]].optimalRange)
			bot.target = path.next(bot.pos);
		
	}
	
	public double rating ()
	{
		if (path == null || victim == null)
			return 0;
		return 1/(path.length*victim.health);
	}
}
