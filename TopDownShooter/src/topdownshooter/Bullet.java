package topdownshooter;

import java.awt.Color;
import java.awt.Graphics2D;

public class Bullet {
	boolean active = true, damaging = true;
	int type, team, author;
	double angle;
	double timer, speed;
	Point pos;
	Point start;
	
	Weapon weapon;
	
	public Bullet (Point pos, double angle, int type, int team, int author)
	{
		this.pos = pos;
		start = pos.clone();
		this.angle = angle;
		this.type = type;
		this.team = team;
		this.author = author;
		weapon = Settings.weapon[type];
		speed = weapon.speed;
		timer = weapon.lifeTime;
	}
	
	void engineDraw (Graphics2D g)
	{
		weapon.bulletDraw(this, g);
		/*
		int steps = 5;
		double r = weapon.r2, delta = (weapon.r2-weapon.r1)/steps;
		Color c = ExtraMath.interpolate(weapon.color, Settings.teamColor[team], 0.4f);
		g.setColor(ExtraMath.blendAlpha(c, 0.2f));
		for (int i = 0; i < steps; i++)
		{
			r -= delta;
			int w = ExtraMath.sX(r, 0)-ExtraMath.sX(0, 0);
			if (i == steps-1)
				g.setColor(weapon.color);
			g.fillOval(ExtraMath.sX(pos)-w, ExtraMath.sY(pos)-w, 2*w, 2*w);
		}*/
	}
	
	void engineUpdate ()
	{
		timer -= Settings.deltaTime;
		weapon.bulletUpdate(this);
		if (timer <= 0)
			active = false;
		//if (Main.situation.bulletCast(pos, angle, weapon.speed*Settings.deltaTime, this))
		//	hit();
	}
	
	void addLight ()
	{
		Main.situation.map.addLight(pos, weapon.color, weapon.lightDist, (float)weapon.lightIntensity);
	}
	
	void hit ()
	{
		active = false;
	}
}
