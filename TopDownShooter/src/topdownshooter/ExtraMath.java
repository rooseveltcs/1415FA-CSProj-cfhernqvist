package topdownshooter;

import java.awt.Color;

public class ExtraMath {
	static double dist (double x1, double y1, double x2, double y2)
	{
		return Math.sqrt(Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2));
	}
	
	static double dist (Point p1, Point p2)
	{
		return dist(p1.x, p1.y, p2.x, p2.y);
	}
	
	static double direction (Point p1, Point p2)
	{
		return Math.atan2(p2.y-p1.y, p2.x-p1.x);
	}
	
	static Point toScreen (Point p)
	{
		return new Point(sX(p.x, p.z), sY(p.y, p.z));
	}
	
	static Point toReal (Point p, double z)
	{
		return new Point(rX(p.x, z), rY(p.y, z), z);
	}
	
	static int sX (double x, double z) //x coordinate on screen given x and height
	{
		return (int)((x-Settings.cX)*(1+z*Settings.heightFactor)*Settings.gS)+Settings.width/2;	
	} 
	static int sY (double y, double z) //y coordinate on screen given y and height
	{
		return (int)((y-Settings.cY)*(1+z*Settings.heightFactor)*Settings.gS)+Settings.height/2;	
	}
	static int sX(Point p)
	{
		return sX(p.x, p.z);
	}
	static int sY(Point p)
	{
		return sY(p.y, p.z);
	}
	
	static double rX (double sX, double z)
	{
		return (sX-Settings.width/2)/((1+z*Settings.heightFactor)*Settings.gS)+Settings.cX;
	}
	
	static double rY (double sY, double z)
	{
		return (sY-Settings.height/2)/((1+z*Settings.heightFactor)*Settings.gS)+Settings.cY;
	}
	
	static Color multiplyColor (Color c, float f)
	{
		if (f > 1)
			f = 1;
		float[] a = c.getRGBColorComponents(new float[3]);
		return new Color(a[0]*f, a[1]*f, a[2]*f);
	}
	
	static float distanceFactor (double dist, double maxDist)
	{
		if (dist > maxDist)
			return 0f;
		return (float)((maxDist-dist)/maxDist);
	}
	
	static Color interpolate (Color c1, Color c2, float f)
	{
		float[] a = new float[]{0, 0, 0};
		float[] a1 = c1.getRGBColorComponents(new float[3]);
		float[] a2 = c2.getRGBColorComponents(new float[3]);
		for (int i = 0; i < 3; i++)
			a[i] = a1[i]*f+a2[i]*(1-f);
		return new Color(a[0], a[1], a[2]);
	}
	
	static Color blendLight (Color c1, Color c2)
	{
		float[] a = new float[]{0, 0, 0};
		float[] a1 = c1.getRGBColorComponents(new float[3]);
		float[] a2 = c2.getRGBColorComponents(new float[3]);
		for (int i = 0; i < 3; i++)
			a[i] = a2[i]+a1[i]*(1-a2[i]);
		return new Color(a[0], a[1], a[2]);
	}
	
	static Color blendDiffuse (Color c1, Color c2)
	{
		float[] a1 = c1.getRGBColorComponents(new float[3]);
		float[] a2 = c2.getRGBColorComponents(new float[3]);
		return new Color(a1[0]*a2[0], a1[1]*a2[1], a1[2]*a2[2]);
	}
	
	static Color blendAlpha (Color c, float alpha)
	{
		float[] a = c.getRGBColorComponents(new float[3]);
		return new Color(a[0], a[1], a[2], alpha);
	}
	
	static double manDist(Point p1, Point p2) //Manhattan distance kinda
	{
		return Math.abs(p1.x-p2.x)+Math.abs(p1.y-p2.y);
	}
	
	static double maxBrightness(Color c)
	{
		float[] a = c.getRGBColorComponents(new float[3]);
		return Math.max(a[0], Math.max(a[1], a[2]));
	}
	
}
