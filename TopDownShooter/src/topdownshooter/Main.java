package topdownshooter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

public class Main {
	
	static JFrame frame;
	static Situation situation;
	static Panel panel;
	static boolean gameRunning = false, paused = false;
	static Timer timer;
	
	public static void main(String[] Args)
	{
		Settings.init();
		initWindow();
		initMenu();
		initTimer();
	}
	
	static void initGame ()
	{
		situation = new Situation ();
		situation.setMode(Settings.startGameMode);
		situation.setMap(Settings.maps[Settings.startMap]);
		gameRunning = true;
		paused = false;
		situation.startMatch(Settings.teamsInPlay, Settings.playerTeam, Settings.playersOnTeam);
	}
	
	static void endGame ()
	{
		initMenu();
	}
	
	static void initMenu (){
		gameRunning = false;
		paused = false;
		situation = new Situation ();
		situation.setMode(0);
		situation.setMap(Settings.maps[Settings.menuMap]);
		situation.startMatch(4, -1, 5);
		situation.updateCenter();
		GUI.menuIndex = GUI.MAIN;
	}
	
	static void initWindow ()
	{
		frame = new JFrame();
		frame.setVisible(true);
		frame.setSize(Settings.width, Settings.height);
		frame.setContentPane(panel = new Panel());
		frame.addKeyListener(panel);
		panel.addMouseMotionListener(panel);
		panel.addMouseListener(panel);
		frame.setTitle("Fredrik's epic TDS");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel.setBackground(Color.black);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);  
	}
	
	static void initTimer ()
	{
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask(){
			@Override
			public void run(){
				update();
			}
		}, 10, Settings.deltaMillis);
	}
	
	static void draw (Graphics2D g)
	{
		if (situation == null)
			return;
		if (gameRunning)	
			situation.updateCenter();
		else
			updateCenter();
		if (Settings.antiAlias)
			g.addRenderingHints(new RenderingHints(
		             RenderingHints.KEY_TEXT_ANTIALIASING,
		             RenderingHints.VALUE_TEXT_ANTIALIAS_ON));
		
		situation.draw(g);
		
		g.addRenderingHints(new RenderingHints(
             RenderingHints.KEY_TEXT_ANTIALIASING,
             RenderingHints.VALUE_TEXT_ANTIALIAS_ON));
		if (!gameRunning || paused)
			GUI.drawMenu(g);	
		else
			GUI.drawHUD(g);
		g.addRenderingHints(new RenderingHints(
	             RenderingHints.KEY_TEXT_ANTIALIASING,
	             RenderingHints.VALUE_TEXT_ANTIALIAS_OFF));
		g.setColor(Color.red);
		//g.drawString(""+situation.map.pathTo(new Point(1.5, 1.5), new Point(ExtraMath.rX(Input.mouseX, 0), ExtraMath.rY(Input.mouseY, 0))).length, 20, 20);
	}
	
	static void updateCenter()
	{
		double tcx = situation.map.width*(0.4+0.2*Input.mouseX/Settings.width);
		double tcy = situation.map.height*(0.4+0.2*Input.mouseY/Settings.height);
		Settings.cX += (tcx-Settings.cX)*Settings.deltaTime;
		Settings.cY += (tcy-Settings.cY)*Settings.deltaTime;
	}
	
	static void close ()
	{
		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	}
	
	static void update ()
	{
		Settings.width = panel.getWidth();
		Settings.height = panel.getHeight();
		
		if (Input.keyDown[KeyEvent.VK_ESCAPE])
			if (!gameRunning)
				close();
			else
			{
				if (!paused)
					pause();
				else
					unPause();
				Input.keyDown[KeyEvent.VK_ESCAPE] = false;
			}
		
		Settings.gS = (int)(panel.getWidth()/Settings.tilesOnScreen);
		if (!paused)
			situation.update();
		panel.repaint();
	}
	
	
	static void pause ()
	{
		paused = true;
		GUI.menuIndex = GUI.PAUSED;
	}
	
	static void unPause ()
	{
		paused = false;
	}
}
