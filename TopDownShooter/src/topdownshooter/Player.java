package topdownshooter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class Player extends Actor {
	
	public Player  (double x, double y, String name, int team, int id)
	{
		pos = new Point(x, y);
		this.name = name;
		this.team = team;
		this.id = id;
		isBot = false;
		setLoadout(0);
	}
	
	@Override
	void update (){
		health = 1;
		if (!ingame)
			return;
		target = pos.clone();
		if (Input.keyDown[KeyEvent.VK_S] || Input.keyDown[KeyEvent.VK_DOWN])
			target.y += 1000;
		if (Input.keyDown[KeyEvent.VK_W] || Input.keyDown[KeyEvent.VK_UP])
			target.y -= 1000;
		if (Input.keyDown[KeyEvent.VK_A] || Input.keyDown[KeyEvent.VK_LEFT])
			target.x -= 1000;
		if (Input.keyDown[KeyEvent.VK_D] || Input.keyDown[KeyEvent.VK_RIGHT])
			target.x += 1000;
		if (Input.mouseDown[MouseEvent.BUTTON1])
			shoot(ExtraMath.toReal(new Point(Input.mouseX, Input.mouseY), 0), l.weapon[0]);
		if (Input.mouseDown[MouseEvent.BUTTON2])
			shoot(ExtraMath.toReal(new Point(Input.mouseX, Input.mouseY), 0), l.weapon[2]);
		if (Input.mouseDown[MouseEvent.BUTTON3])
			shoot(ExtraMath.toReal(new Point(Input.mouseX, Input.mouseY), 0), l.weapon[1]);
		moveTowards(target, speed*Settings.deltaTime, false);
	}
	
	@Override
	void draw(Graphics2D g) {
		if (!ingame)
			return;
		g.setColor(Settings.teamColor[team]);
		double spread = Settings.weapon[weapon].spread;
		if (spread != 0)
		{
			double dir = ExtraMath.direction(pos, ExtraMath.toReal(new Point(Input.mouseX, Input.mouseY), 0));
			g.setColor(ExtraMath.blendAlpha(Color.white, 0.3f));
			drawLaser(g, dir-Settings.weapon[weapon].spread);
			drawLaser(g, dir+Settings.weapon[weapon].spread);
		}
		//drawLaser(g, ExtraMath.toReal(new Point(Input.mouseX, Input.mouseY), 0));
		//Path p = Main.situation.map.pathTo(pos, new Point(ExtraMath.rX(Input.mouseX, 0), ExtraMath.rY(Input.mouseY, 0)));
		//Main.situation.map.drawNodes(g);
		//p.draw(g);
		g.setColor(Color.white);
		g.setFont(GUI.fnormal);
		//g.drawString("Dist player > mouse: "+p.length, 20, 20);
		//g.drawLine(ExtraMath.sX(pos.x, 0),  ExtraMath.sY(pos.y, 0), Input.mouseX, Input.mouseY);
	}
}
