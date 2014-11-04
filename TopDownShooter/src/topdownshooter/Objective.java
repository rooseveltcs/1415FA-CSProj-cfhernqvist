package topdownshooter;

public class Objective {
	Bot bot;
	int objectives;
	
	public void update ()
	{
		
	}
	
	static Objective get (int i, Bot bot)
	{
		switch (i)
		{
		case 0:
			return new Objective_Attack(bot);
		default:
			return null;
		}
	}
	
}
