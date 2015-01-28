package topdownshooter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Map {
	
	class Node {
		int x, y;
		Point p;
		int[] edge;
		Node (int x, int y){
			this.x = x;
			this.y = y;
			p = new Point(x+0.5, y+0.5);
		}
	}
	
	class ListNode {
		double dist;
		int node;
		ListNode(double dist, int node)
		{
			this.dist = dist;
			this.node = node;
		}
	}
	
	int width, height;
	int sx1, sx2, sy1, sy2;
	int nodes;
	String[] map;
	Color[][] light;
	Node[] node;
	double[][] edge;
	double[] path_dist;
	int[] path_parent;
	PriorityQueue<ListNode>path_queue;
	String name;
	
	void path_addNode(int n, int parent, double dist)
	{
		if (path_dist[n] < dist && path_dist[n] != -1)
			return;
		path_parent[n] = parent;
		path_dist[n] = dist;
		path_queue.add(new ListNode(dist, n));
	}
	
	Path pathTo (Point from, Point to)
	{
		path_queue = new PriorityQueue<>(1, new Comparator<ListNode>(){
			@Override
			public int compare(ListNode a, ListNode b) {
				try {
					return a.dist < b.dist ? -1:1;
				} catch (Exception ex) {return -1;}
			}
		});
		
		if (solid(get(from.x, from.y)) || solid(get(to.x, to.y)))
			return new Path();
		
		path_dist = new double[nodes];
		path_parent = new int[nodes];
		Arrays.fill(path_dist, -1);
		Path pt = new Path();
		for (int i = 0; i < node.length-1; i++)
			node[i].edge[node[i].edge.length-1] = -1;
		node[nodes-1] = new Node((int)to.x, (int)to.y);
		node[nodes-1].p = to;
		for (int i = 0; i < node.length-1; i++)
			if (!lineCast(node[i].p.clone(), node[nodes-1].p))
			{
				node[i].edge[node[i].edge.length-1] = nodes-1;
				edge[i][nodes-1] = ExtraMath.dist(node[i].p, node[nodes-1].p);
				edge[nodes-1][i] = edge[i][nodes-1];
			}
		for (int i = 0; i < node.length; i++)
			if (!lineCast(node[i].p.clone(), from))
				path_addNode(i, -2, ExtraMath.dist(node[i].p, from));
		while (!path_queue.isEmpty() && path_queue.peek() != null)
		{
			int n = path_queue.poll().node;
			if (n == nodes-1)
				break;
			for (int i = 0; i < node[n].edge.length; i++)
				if (node[n].edge[i] != -1)
					path_addNode(node[n].edge[i], n, path_dist[n]+edge[n][node[n].edge[i]]);
			
		}
		ArrayList<Point> points = new ArrayList<>();
		int current = nodes-1;
		while (current != -2 && points.size() < nodes)
		{
			points.add(node[current].p);
			current = path_parent[current];
		}
		points.add(from.clone());
		pt.p = new Point[points.size()];
		for (int i = 0; i < pt.p.length; i++)
			pt.p[i] = points.get(pt.p.length-1-i);
		pt.length = path_dist[nodes-1];
		return pt;
	}
	
	void findNodes ()
	{
		ArrayList<Node> nodeList = new ArrayList<>();
		for (int x = 1; x < width-1; x++)
			for (int y = 1; y < height-1; y++)
			{
				if (solid(get(x, y)))
					continue;
				if ((solid(get(x-1, y-1)) && !solid(get(x, y-1)) && !solid(get(x-1, y)))
						||(solid(get(x+1, y-1)) && !solid(get(x, y-1)) && !solid(get(x+1, y)))
						||(solid(get(x+1, y+1)) && !solid(get(x, y+1)) && !solid(get(x+1, y)))
						||(solid(get(x-1, y+1)) && !solid(get(x, y+1)) && !solid(get(x-1, y))))
					nodeList.add(new Node(x, y));
			}
		nodes = nodeList.size()+1;
		System.out.println(nodes);
		node = nodeList.toArray(new Node[nodes]);
		
		edge = new double[nodes][nodes];
		for (int i = 0; i < nodes; i++)
		{
			edge[i] = new double[nodes];
			Arrays.fill(edge[i], -1);
		}
		
		for (int i = 0; i < nodes-1; i++)
		{
			ArrayList<Integer> edgeList = new ArrayList<>();
			for (int j = 0; j < nodes-1; j++)
				if (j != i && (!lineCast(node[i].p.clone(), node[j].p)))
				{
					edge[i][j] = ExtraMath.dist(node[i].p, node[j].p);
					edge[j][i] = edge[i][j];
					edgeList.add(j);
				}
			node[i].edge = new int[edgeList.size()+1];
			node[i].edge[node[i].edge.length-1] = -1;
			for (int j = 0; j < edgeList.size(); j++)
				node[i].edge[j] = edgeList.get(j);
		}
	}
	
	void drawNodes (Graphics2D g)
	{
		int w = (int)(Settings.gS/3);
		g.setColor(Color.magenta);
		//pathTo(new Point(1.5, 1.5), new Point(width/2, height/2));
		//pathTo(new Point(1.5, 1.5), new Point(ExtraMath.rX(Input.mouseX, 0), ExtraMath.rY(Input.mouseY, 0)));
		for (int i = 0; i < nodes; i++)
		{
			g.drawOval(ExtraMath.sX(node[i].p)-w, ExtraMath.sY(node[i].p)-w, w*2, w*2);
			if (i == nodes-1)
				continue;
			for (int j = 0; j < node[i].edge.length; j++)
				if (node[i].edge[j] != -1)
					g.drawLine(ExtraMath.sX(node[i].p), ExtraMath.sY(node[i].p)
							, ExtraMath.sX(node[node[i].edge[j]].p), ExtraMath.sY(node[node[i].edge[j]].p));
		}
	}
	
	public Map (String name, int w, int h, String[] s){
		width = w;
		height = h;
		map = s;
		light = new Color[w+1][h+1];
		clearLight();
		this.name = name;
	}
	
	void setBounds ()
	{
		int rx = Settings.width/(Settings.gS*2)+1, ry = Settings.height/(Settings.gS*2)+1;
		sx1 = (int)Settings.cX-rx;
		sx2 = (int)Settings.cX+rx+1;
		sy1 = (int)Settings.cY-ry;
		sy2 = (int)Settings.cY+ry+1;
	}
	
	void clearLight ()
	{
		Color c = Settings.ambientLight;
		if (!Settings.light)
			c = Color.white;
		for (int x = 0; x < width+1; x++)
			for (int y = 0; y < height+1; y++)
				light[x][y] = c;
	}
	
	void addLight (Point p, Color c, double dist, float intensity)
	{
		if (!Settings.light)
			return;
		int x1 = (int)Math.max((p.x-dist), 1);
		int x2 = (int)Math.min((p.x+dist), width-1);
		int y1 = (int)Math.max((p.y-dist), 1);
		int y2 = (int)Math.min((p.y+dist), height-1);
		for (int x = x1; x <= x2; x++)
			for (int y = y1; y <= y2; y++)
			{
				if (Settings.shadows)
				{
					Point t = new Point(x, y);
					if (p.x < t.x) t.x -= 0.1;
					if (p.x > t.x) t.x += 0.1;
					if (p.y < t.y) t.y -= 0.1;
					if (p.y > t.y) t.y += 0.1;
					if (lineCast(p.clone(), t))
						continue;
				}
				light[x][y] = ExtraMath.blendLight(light[x][y]
						, ExtraMath.multiplyColor(c, intensity*ExtraMath.distanceFactor(ExtraMath.dist(p, new Point(x, y)), dist)));
			}
	}
	
	Point spawnPoint (int team)
	{
		ArrayList<Point> list = new ArrayList<>();
		for (int x = 0; x < width; x++)
			for (int y = 0; y < height; y++)
				if ((int)(get(x, y)-'0') == team)
					list.add(new Point(x + 0.5, y + 0.5));
		if (list.size() > 0)
			return list.get((int)(Math.random()*list.size()));
		return new Point(1.5, 1.5);
	}
	
	Point flagPoint (int team)
	{
		ArrayList<Point> list = new ArrayList<>();
		for (int x = 0; x < width; x++)
			for (int y = 0; y < height; y++)
				if ((int)(get(x, y)-'A') == team)
					list.add(new Point(x + 0.5, y + 0.5));
		if (list.size() > 0)
			return list.get((int)(Math.random()*list.size()));
		return spawnPoint(team);
	}
	
	Point randomPos ()
	{
		ArrayList<Point> list = new ArrayList<>();
		for (int x = 0; x < width; x++)
			for (int y = 0; y < height; y++)
				if (!solid(get(x, y)))
					list.add(new Point(x + 0.5, y + 0.5));
		if (list.size() > 0)
			return list.get((int)(Math.random()*list.size()));
		return new Point(1.5, 1.5);
	}
	
	char get (int x, int y)
	{
		if (!inBounds(x, y))
			return 'X';
		return map[y].charAt(x);
	}
	
	char get(double x, double y)
	{
		return get((int)x, (int)y);
	}
	
	boolean inBounds (int x, int y)
	{
		return (x >= 0 && y >= 0 && x < width && y < height);
	}
	
	void drawTops (Graphics2D g)
	{
		int w = ExtraMath.sX(1, 2)-ExtraMath.sX(0, 2)+1;
		g.setColor(Color.GRAY);
		for (int x = Math.max(0, sx1); x < Math.min(width, sx2); x++)
			for (int y = Math.max(0, sy1); y < Math.min(height, sy2); y++)
				if (get(x, y) == 'X')
				{
					Color col = Settings.wallSurface;
					int sx = ExtraMath.sX(x, 2), sy = ExtraMath.sY(y, 2);
					drawColorQuad(g
							, new Point[]{new Point(sx, sy), new Point(sx+w, sy), new Point(sx+w, sy+w), new Point(sx, sy+w)}
							, new Color[]{ExtraMath.blendDiffuse(col, light[x][y])
								, ExtraMath.blendDiffuse(col, light[x+1][y])
								, ExtraMath.blendDiffuse(col, light[x+1][y+1])
								, ExtraMath.blendDiffuse(col, light[x][y+1])}
							, Settings.interpolationQuality);
				}
	}
	
	void drawWalls (Graphics2D g)
	{
		int cx = (int)Settings.cX, cy = (int)Settings.cY;
		int bx = (sx2-sx1)/2, by = (sy2-sy1)/2;
		for (int ax = bx; ax >= 0; ax--)
			for (int ay = by; ay >= 0; ay --)
			{
				if (ax != 0)
				{
					if (inBounds(cx-ax, cy-ay) && get(cx-ax, cy-ay) == 'X')
						drawWall(cx-ax, cy-ay, 3, g);
					if (inBounds(cx-ax, cy+ay) && get(cx-ax, cy+ay) == 'X' && ay != 0)
						drawWall(cx-ax, cy+ay, 3, g);
					
					if (inBounds(cx+ax, cy-ay) && get(cx+ax, cy-ay) == 'X')
						drawWall(cx+ax, cy-ay, 2, g);
					if (inBounds(cx+ax, cy+ay) && get(cx+ax, cy+ay) == 'X' && ay != 0)
						drawWall(cx+ax, cy+ay, 2, g);
				}
				if (ay != 0)
				{
					if (inBounds(cx-ax, cy-ay) && get(cx-ax, cy-ay) == 'X')
						drawWall(cx-ax, cy-ay, 1, g);
					if (inBounds(cx+ax, cy-ay) && get(cx+ax, cy-ay) == 'X' && ax != 0)
						drawWall(cx+ax, cy-ay, 1, g);
					
					if (inBounds(cx-ax, cy+ay) && get(cx-ax, cy+ay) == 'X')
						drawWall(cx-ax, cy+ay, 0, g);
					if (inBounds(cx+ax, cy+ay) && get(cx+ax, cy+ay) == 'X' && ax != 0)
						drawWall(cx+ax, cy+ay, 0, g);
				}
			}
	}
	
	
	void drawWall(int x, int y, int dir, Graphics2D g)//top, bottom, left, right
	{
		int x1 = x, x2 = x, y1 = y, y2 = y;
		double h = 2;
		if (dir > 1)
		{
			y2 = y+1;
			if (dir == 3)
			{
				x1 ++;
				x2 ++;
			}
		} else {
			x2 = x+1;
			if (dir == 1)
			{
				y1 ++;
				y2 ++;
			}
		}
		int [] xc = {ExtraMath.sX(x1, 0), ExtraMath.sX(x2, 0), ExtraMath.sX(x2, h), ExtraMath.sX(x1, h)};
		int [] yc = {ExtraMath.sY(y1, 0), ExtraMath.sY(y2, 0), ExtraMath.sY(y2, h), ExtraMath.sY(y1, h)};
		Color top = Settings.wallTop, bottom = Settings.wallBottom;
		if (dir > 1)
		{
			top = top.brighter();
			bottom = bottom.brighter();
		}
		drawColorQuad(g
				, new Point[]{new Point(xc[0], yc[0]), new Point(xc[1], yc[1]), new Point(xc[2], yc[2]), new Point(xc[3], yc[3])}
				, new Color[]{ExtraMath.blendDiffuse(bottom, light[x1][y1])
				, ExtraMath.blendDiffuse(bottom, light[x2][y2])
				, ExtraMath.blendDiffuse(top, light[x2][y2])
				, ExtraMath.blendDiffuse(top, light[x1][y1])},
				Settings.interpolationQuality);
	}
	
	void drawFloors (Graphics2D g)
	{
		int w = Settings.gS;
		for (int x = Math.max(0, sx1); x < Math.min(width, sx2); x++)
			for (int y = Math.max(0, sy1); y < Math.min(height, sy2); y++)
			{
				char c = get(x, y);
				if (c == 'X')
					continue;
				Color col = Settings.floorColor;
				if (Character.isDigit(c))
					col = Settings.teamColor[(int)(c-'0')].darker();
				int sx = ExtraMath.sX(x, 0), sy = ExtraMath.sY(y, 0);
				drawColorQuad(g
						, new Point[]{new Point(sx, sy), new Point(sx+w, sy), new Point(sx+w, sy+w), new Point(sx, sy+w)}
						, new Color[]{ExtraMath.blendDiffuse(col, light[x][y])
							, ExtraMath.blendDiffuse(col, light[x+1][y])
							, ExtraMath.blendDiffuse(col, light[x+1][y+1])
							, ExtraMath.blendDiffuse(col, light[x][y+1])}
						, Settings.interpolationQuality);
			}
	}
	
	boolean lineCast (Point p, double angle, double length)
	{
		double dx = Math.cos(angle), dy = Math.sin(angle), step = 0.1;
		Point add = new Point(dx*step, dy*step);
		while (length > 0)
		{
			if (length > step)
				p.add(add);
			else
				p.add(new Point(dx*length, dy*length));
			length -= step;
			if (collisionPoint(p))
				return true;
		}
		return false;
	}
	
	boolean lineCast (Point p, Point p2)
	{
		return lineCast (p, Math.atan2(p2.y-p.y, p2.x-p.x), ExtraMath.dist(p, p2));
	}
	
	boolean collisionPoint (Point p)
	{
		return collisionPoint(p.x, p.y);
	}
	
	boolean collisionPoint (double x, double y)
	{
		return solid(get(x, y));
	}
	
	boolean collisionAt (double x, double y)
	{
		double r = Settings.actorRadius;
		return (collisionPoint(x-r, y-r) || collisionPoint(x-r, y+r) || collisionPoint(x+r, y-r) || collisionPoint(x+r, y+r));
	}
	
	boolean collisionAt (Point p)
	{
		return collisionAt(p.x, p.y);
	}
	
	static boolean solid(char c)
	{
		return (c == 'X');
	}
	
	static void drawColorQuad (Graphics2D g, Point[] ps, Color[]cs, int res)
	{
		int aw = (int)Math.pow(2, res);
		Point[][] p = new Point[aw+1][aw+1];
		Color[][] c = new Color[aw+1][aw+1];
		p[0][0] = ps[0]; c[0][0] = cs[0];
		p[aw][0] = ps[1]; c[aw][0] = cs[1];
		p[aw][aw] = ps[2]; c[aw][aw] = cs[2];
		p[0][aw] = ps[3]; c[0][aw] = cs[3];
		setQuadCoors (p, c, 0, 0, aw);
		for (int x = 0; x < aw; x++)
			for (int y = 0; y < aw; y++)
				drawQuad(g
				, new Point[]{p[x][y], p[x+1][y], p[x+1][y+1], p[x][y+1]}
				, c[x][y]);
	}
	
	static void setQuadCoors (Point[][]p, Color[][]c, int x1, int y1, int w)
	{
		if (w == 1)
			return;
		int x2 = x1+w/2, y2 = y1+w/2;
		int x3 = x1+w, y3 = y1+w;
		p[x2][y1] = Point.middle(p[x1][y1], p[x3][y1]);
		c[x2][y1] = ExtraMath.interpolate(c[x1][y1], c[x3][y1], 0.5f);
		
		p[x2][y3] = Point.middle(p[x1][y3], p[x3][y3]);
		c[x2][y3] = ExtraMath.interpolate(c[x1][y3], c[x3][y3], 0.5f);
		
		p[x1][y2] = Point.middle(p[x1][y1], p[x1][y3]);
		c[x1][y2] = ExtraMath.interpolate(c[x1][y1], c[x1][y3], 0.5f);
		
		p[x3][y2] = Point.middle(p[x3][y1], p[x3][y3]);
		c[x3][y2] = ExtraMath.interpolate(c[x3][y1], c[x3][y3], 0.5f);
		
		p[x2][y2] = Point.middle(p[x2][y1], p[x2][y3]);
		c[x2][y2] = ExtraMath.interpolate(c[x2][y1], c[x2][y3], 0.5f);
		
		w /= 2;
		
		setQuadCoors(p, c, x1, y1, w);
		setQuadCoors(p, c, x1+w, y1, w);
		setQuadCoors(p, c, x1, y1+w, w);
		setQuadCoors(p, c, x1+w, y1+w, w);
	}
	
	static void drawQuad (Graphics2D g, Point[]ps, Color c)
	{
		int[] xs = new int[4], ys = new int[4];
		for (int i = 0; i < 4; i++)
		{
			xs[i] = (int)ps[i].x;
			ys[i] = (int)ps[i].y;
		}
		Polygon p = new Polygon(xs, ys, 4);
		g.setColor(c);
		g.fillPolygon(p);
	}
	
}
