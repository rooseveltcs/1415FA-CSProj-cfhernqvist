package topdownshooter;

import java.awt.Color;

public class ExplosionEffect {
	
	class Particle {
		boolean active;
		Point pos;
		Point vector;
		float alpha;
		double size;
	}
	
	int particles;
	Color color;
	double minSize, maxSize;
	Point pos;
	double time, initTime;
	public ExplosionEffect(int particles, double minSize, double maxSize, Color color, Point pos, double time){
		this.particles = particles;
		this.minSize = minSize;
		this.maxSize = maxSize;
		this.color = color;
		this.pos = pos;
		this.time = time;
		initTime = time;
		
	}
}
