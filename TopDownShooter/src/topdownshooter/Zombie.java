package topdownshooter;

public class Zombie extends Actor {
	public Zombie  (double x, double y, String name, int team, int id)
	{
		pos = new Point(x, y);
		this.name = name;
		this.team = team;
		this.id = id;
		setLoadout(0);
	}
	
	Path p;
	
	@Override
	void update ()
	{
		speed = 2;
		
		if (p == null || Math.random()/Settings.deltaTime < 0.5)
			p = Main.situation.map.pathTo(pos, Main.situation.getPlayer().pos);
		if (p.p.length == 0)
			return;
		target = p.next(pos);
		moveTowards(target.x, target.y, speed*Settings.deltaTime, true);
	}
}
