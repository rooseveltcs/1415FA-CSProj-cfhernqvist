package topdownshooter;

import java.awt.Color;
import java.awt.Graphics2D;

public class Path {
	int current = 0;
	double length = 0;
	Point[] p = new Point[0];
	
	void draw(Graphics2D g){
		int w = (int)(Settings.gS/3);
		for (int i = 0; i < p.length; i++)
		{
			g.setColor(Color.red);
			if (i == current)
				g.setColor(Color.yellow);
			g.drawOval(ExtraMath.sX(p[i])-w, ExtraMath.sY(p[i])-w, w*2, w*2);
			g.setColor(Color.red);
			if (i != 0)
				g.drawLine(ExtraMath.sX(p[i]), ExtraMath.sY(p[i]), ExtraMath.sX(p[i-1]), ExtraMath.sY(p[i-1]));
		}
	}
	
	Point next (Point c)
	{
		if (current < p.length-1 && ExtraMath.dist(p[current], c) < 0.2)
			current ++;
		return p[current];
	}
}
