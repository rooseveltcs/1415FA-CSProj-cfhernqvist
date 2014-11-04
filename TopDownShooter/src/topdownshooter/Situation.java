package topdownshooter;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class Situation {
	static final int TYPE_NORMAL = 0, TYPE_ZOMBIES = 1;
	Map map;
	int playerIndex = -1, teams, maxScore;
	int gameMode;
	double matchTimeLeft;
	ArrayList<Actor> actors;
	ArrayList<Bullet> bullets;
	ArrayList<ExplosionEffect> effects;
	int[] teamScore;
	boolean spawnOnDeath = true;
	GameMode g;
	
	public Situation (){
		setMap(new Map ("asd", 3, 3, new String[]{"XXX", "X X", "XXX"}));
		actors = new ArrayList<>();
		bullets = new ArrayList<>();
		effects = new ArrayList<>();
		setMatch(2, 100, 300);
	}
	
	int addExplosion (ExplosionEffect ef)
	{
		for (int i = 0; i < effects.size(); i++)
			if (!effects.get(i).active)
			{
				effects.set(i, ef);
				return i;
			}
		effects.add(ef);
		return effects.size()-1;
	}
	
	void setMode(int gm){
		gameMode = gm;
		g = Settings.gameMode[gm];
	}
	
	int winningTeam (){
		int b = 0;
		for (int i = 1; i < teams; i++)
			if (teamScore[i] > teamScore[b])
				b = i;
		return b;
	}
	
	void setMap (Map map)
	{
		this.map = map;
	}
	
	void spawnPlayer ()
	{
		getPlayer().spawn();
	}
	
	Actor getPlayer ()
	{
		return actors.get(playerIndex);
	}
	
	int addBullet (Bullet bullet)
	{
		for (int i = 0; i < bullets.size(); i++)
			if (!bullets.get(i).active)
			{
				bullets.set(i, bullet);
				return i;
			}
		bullets.add(bullet);
		return bullets.size()-1;
	}
	
	void setMatch (int teams, int maxScore, double matchTime)
	{
		this.teams = teams;
		this.maxScore = maxScore;
		teamScore = new int[teams];
		matchTimeLeft = matchTime;
	}
	
	void startMatch (int teams, int playerTeam, int players)
	{
		g.startMatch(this, teams, playerTeam, players);
	}
	
	int addActor (int type, int team, String name)
	{
		if (type == Actor.PLAYER)
		{
			actors.add(new Player(1.5, 1.5, name, team, actors.size()));
			playerIndex = actors.size()-1;
		} else if (type == Actor.BOT) actors.add(new Bot(0, 0, name, team, actors.size()));
		else actors.add(new Zombie(0, 0, name, team, actors.size()));
		return actors.size()-1;
	}
	
	boolean bulletCast (Point p, double angle, double length, Bullet bullet)
	{
		int team = bullet.team;
		double dx = Math.cos(angle), dy = Math.sin(angle), step = 0.1;
		Point add = new Point(dx*step, dy*step);
		while (length > 0)
		{
			p.add(add);
			length -= step;
			int hitActor = bulletCollisionId(p, team);
			if (map.collisionPoint(p) || hitActor != -1)
			{
				if (hitActor != -1)
					bulletHit(hitActor, bullet);
				return true;
			}
		}
		return false;
	}
	
	void bulletHit(int id, Bullet bullet, double damage)
	{
		Actor actor = actors.get(id);
		actor.health -= damage/actor.l.healthMultiplier;
		if (actor.health < 0)
		{
			System.out.println(actor.name+" killed by "+actors.get(bullet.author).name);
			teamScore[bullet.team] ++;
			actor.deaths ++;
			actors.get(bullet.author).kills ++;
		}
	}
	
	void bulletHit(int id, Bullet bullet)
	{
		bulletHit(id, bullet, bullet.weapon.damage);
	}
	
	int bulletCollisionId (Point p, int team)
	{
		double dist = Settings.actorRadius;
		Actor a;
		for (int i = 0; i < actors.size(); i++)
			if ((a = actors.get(i)).ingame && a.active && a.team != team && ExtraMath.dist(a.pos, p) < dist)
				return i;
		return -1;
	}
	
	void draw (Graphics2D g)
	{
		map.setBounds();
		map.clearLight();
		//map.addLight(new Point(0, 0), Color.green, 15);
		for (int i = 0; i < actors.size(); i++)
			if (actors.get(i).active)
				actors.get(i).addLight();
		for (int i = 0; i < bullets.size(); i++)
			if (bullets.get(i).active)
				bullets.get(i).addLight();
		map.drawFloors(g);
		for (int i = 0; i < actors.size(); i++)
			if (i != playerIndex)
				actors.get(i).engineDraw(g);
		
		if (playerIndex != -1)
			actors.get(playerIndex).engineDraw(g);
		for (int i = 0; i < effects.size(); i++)
			if (effects.get(i).active)
				effects.get(i).draw(g);
		for (int i = 0; i < bullets.size(); i++)
			if (bullets.get(i).active)
				bullets.get(i).engineDraw(g);
		map.drawWalls(g);
		map.drawTops(g);
		//map.drawNodes(g);
		for (int i = 0; i < actors.size(); i++)
			actors.get(i).draw(g);
		
		if (Main.gameRunning && matchTimeLeft > 0 && !Main.paused && !getPlayer().ingame)
			GUI.drawSpawn(g);
		
	}
	
	void update ()
	{
		matchTimeLeft -= Settings.deltaTime;
		Actor a;
		Bullet b;
		g.update(this);
		/*if (spawnOnDeath)
			for (int i = 0; i < actors.size(); i++)
				if ((a = actors.get(i)).active && !a.ingame && a.spawnTime <= 0 && a.isBot)
					a.spawn();*/
		for (int i = 0; i < actors.size(); i++)
			if ((a = actors.get(i)).active)
				a.update();
		for (int i = 0; i < actors.size(); i++)
			if ((a = actors.get(i)).active)
				a.engineUpdate();
		for (int i = 0; i < bullets.size(); i++)
			if ((b = bullets.get(i)).active)
				b.engineUpdate();
		for (int i = 0; i < effects.size(); i++)
			if (effects.get(i).active)
				effects.get(i).update();
		if (matchTimeLeft < 0 && Main.gameRunning)
		{
			Main.pause();
			GUI.menuIndex = GUI.END;
		}
		
	}
	
	void updateCenter ()
	{
		if (playerIndex == -1)
		{
			Settings.cX = map.width/2;
			Settings.cY = map.height/2;
		} else {
			Settings.cX = actors.get(playerIndex).pos.x;
			Settings.cY = actors.get(playerIndex).pos.y;
		}
	}
}
