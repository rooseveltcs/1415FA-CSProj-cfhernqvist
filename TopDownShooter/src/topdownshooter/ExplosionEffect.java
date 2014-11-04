package topdownshooter;

import java.awt.Color;
import java.awt.Graphics2D;

public class ExplosionEffect {
	
	
	
	class Particle {
		boolean active;
		Point ps;
		Point vector;
		float alpha;
		double size, sp;
		
		public Particle (){
			active = true;
			ps = pos.clone();
			sp = speed*Math.random();
			vector = Point.baseVector(Math.random()*100);
			size = Math.random()*(maxSize-minSize)+minSize;
		}
		
		public void update()
		{
			if (!active)
				return;
			ps.add(vector.clone().scale(sp*Settings.deltaTime));
			alpha = (float)(time/initTime);
			if (initTime-time > 0.1 && Main.situation.map.collisionAt(ps))
				active = false;
		}
		
		public void draw(Graphics2D g)
		{
			if (!active)
				return;
			g.setColor(ExtraMath.blendAlpha(color, alpha));
			int sx = ExtraMath.sX(ps), sy = ExtraMath.sY(ps), w = ExtraMath.sX(size, 0)-ExtraMath.sX(0, 0);
			g.fillRect(sx-w, sy-w, 2*w, 2*w);
		}
		
	}
	
	int particles;
	Color color;
	double minSize, maxSize;
	double speed;
	Point pos;
	double time = 0, initTime;
	boolean active = true;
	Particle[] p;
	
	public ExplosionEffect(int particles, double minSize, double maxSize, Color color, Point pos, double time, double speed){
		this.particles = particles;
		this.minSize = minSize;
		this.maxSize = maxSize;
		this.color = color;
		this.pos = pos;
		initTime = time;
		this.time = time;
		this.speed = speed;
		p = new Particle[particles];
		for (int i = 0; i < particles; i++)
			p[i] = new Particle();
	}
	
	public void update ()
	{
		time -= Settings.deltaTime;
		for (int i = 0; i < particles; i++)
			p[i].update();
		if (time <= 0)
			active = false;
	}
	
	public void draw (Graphics2D g)
	{
		for (int i = 0; i < particles; i++)
			p[i].draw(g);
	}
}
