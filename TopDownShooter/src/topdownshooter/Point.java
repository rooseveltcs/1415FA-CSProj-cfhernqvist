package topdownshooter;

public class Point {
	double x, y, z;
	public Point(double x, double y)
	{
		this.x = x;
		this.y = y;
		this.z = 0;
	}
	
	public Point(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	
	public Point addNew (Point p)
	{
		return new Point(x+p.x, y+p.y);
	}
	
	public void add (Point p)
	{
		x += p.x;
		y += p.y;
	}
	
	public void subtract (Point p)
	{
		x -= p.x;
		y -= p.y;
	}
	
	public Point subtractNew (Point p)
	{
		return new Point(x-p.x, y-p.y);
	}
	
	public Point clone ()
	{
		return new Point(x, y, z);
	}
	
	public String toString ()
	{
		return "("+x+", "+y+", "+z+")";
	}
	
	static Point middle(Point p1, Point p2)
	{
		return new Point((p1.x+p2.x)/2, (p1.y+p2.y)/2, (p1.z+p2.z)/2);
	}
	
	public Point scale (double factor)
	{
		return new Point(x*factor, y*factor, z*factor);
	}
	
	static Point baseVector (double dir)
	{
		return new Point(Math.cos(dir), Math.sin(dir));
	}
}
