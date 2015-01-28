package topdownshooter;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class GUI {
	
	static int menuIndex = 0, subIndex = 0, w = Settings.width, h = Settings.height;
	static Font fnormal = new Font("Calibri", Font.PLAIN, 12)
	, fsmall = new Font("Calibri", Font.PLAIN, 12)
	, flarge = new Font("Calibri", Font.PLAIN, 24)
	, fhuge = new Font("Calibri", Font.PLAIN, 32);
	static Graphics2D g;
	static final int MAIN = 0, SETTINGS = 1, PLAY = 2, PAUSED = 3, END = 4, SPAWN = 5;
	
	static void drawMenu (Graphics2D g2)
	{
		g = g2;
		w = Settings.width;
		h = Settings.height;
		g.setColor(new Color(0, 0, 0, 0.5f));
		//g.fillRect(0, 0, w, h);
		
		int mw = 400, mh = 65, mx = w/2-mw/2, my = h-mh;
		
		int i = 0;
		int bh = 40, bw = mw-20, bm = 10;
		switch(menuIndex)
		{
		case MAIN:
			window(mx, my-50, mw, mh+50, "Top Down Shooter");
			int buttons = 3;
			if (button(mx+bm+(i++)*(mw-bm)/buttons, my, (mw-bm)/buttons-10, bh, "Play"))
				menuIndex = PLAY;
			if (button(mx+bm+(i++)*(mw-bm)/buttons, my, (mw-bm)/buttons-10, bh, "Settings"))
				menuIndex = SETTINGS;
			if (button(mx+bm+(i++)*(mw-bm)/buttons, my, (mw-bm)/buttons-10, bh, "Quit"))
				Main.close();
			g.setFont(fsmall);
			g.drawString(" Fredrik Hernqvist 2014", mx+10, h-5);
			break;
		case PLAY:
			mh = 500;
			my = h-mh;
			window(mx, my-50, mw, mh+50, "Play");
			Settings.startGameMode = changeButtons(mx+bm, my+(bh+bm)*(i++), bw, bh, 
					Settings.gameMode[Settings.startGameMode].name, 0, Settings.gameMode.length-1, Settings.startGameMode);
			Settings.teamsInPlay = changeButtons(mx+bm, my+(bh+bm)*(i++), bw, bh, 
					"Teams: "+Settings.teamsInPlay, 2, 8, Settings.teamsInPlay);
			Settings.playersOnTeam = changeButtons(mx+bm, my+(bh+bm)*(i++), bw, bh, 
					"Team size: "+Settings.playersOnTeam, 1, 10, Settings.playersOnTeam);
			Settings.playerTeam = changeButtons(mx+bm, my+(bh+bm)*(i++), bw, bh, 
					"Player team: "+(Settings.playerTeam+1), 0, Settings.teamsInPlay-1, Settings.playerTeam);
			drawMap(mx+bm, my+(bh+bm)*i, bw, 2*bh+bm, Settings.maps[Settings.startMap]);
			i += 2;
			Settings.startMap = changeButtons(mx+bm, my+(bh+bm)*(i++), bw, bh, 
					"Map: "+Settings.maps[Settings.startMap].name+" ("+(Settings.startMap+1)+")", 0, Settings.maps.length-1, Settings.startMap);
			Settings.matchTime = 10*changeButtons(mx+bm, my+(bh+bm)*(i++), bw, bh, 
					"Time: "+Settings.matchTime+"s", 1, 60, Settings.matchTime/10);
			if (button(mx+bm, my+(bh+bm)*(i++), bw, bh, "Start Game"))
				Main.initGame();
			if (button(mx+bm, h-bh-bm, bw, bh, "Back"))
				menuIndex = MAIN;
			break;
		case SETTINGS:
			mh = 250;
			my = h-mh;
			window(mx, my-50, mw, mh+50, "Settings");
			if (button(mx+bm, my+(bh+bm)*(i++), bw, bh, "Shadows: "+Settings.shadows))
				Settings.shadows = !Settings.shadows;
			if (button(mx+bm, my+(bh+bm)*(i++), bw, bh, "Light: "+Settings.light))
				Settings.light = !Settings.light;
			Settings.interpolationQuality = changeButtons(mx+bm, my+(bh+bm)*(i++), bw, bh, "Quality: "+Settings.interpolationQuality, 0, 3, Settings.interpolationQuality);
			if (button(mx+bm, my+(bh+bm)*(i++), bw, bh, "Anti aliasing: "+Settings.antiAlias))
				Settings.antiAlias = !Settings.antiAlias;
			
			if (button(mx+bm, h-bh-bm, bw, bh, "Back"))
				menuIndex = Main.gameRunning ? PAUSED:MAIN;
			break;
		case PAUSED:
			mh = 200;
			my = h-mh;
			window(mx, my-50, mw, mh+50, "Paused");
			if (button(mx+bm, my+(bh+bm)*(i++), bw, bh, "Resume"))
				Main.unPause();
			if (button(mx+bm, my+(bh+bm)*(i++), bw, bh, "Settings"))
				menuIndex = SETTINGS;
			if (button(mx+bm, h-bh-bm, bw, bh, "Back to menu"))
				Main.endGame();
			break;
		case END:
			drawScoreboard();
			if (button(mx+bm, h-bh-bm, bw, bh, "Back to menu"))
				Main.endGame();
			int t = Main.situation.winningTeam();
			g.setFont(fhuge);
			String text = "Team "+Settings.teamName[t]+"("+(t+1)+") wins!";
			int tx = w/2-text.length()*8, ty = h/2;
			g.setColor(Settings.teamColor[t]);
			g.drawString(text, tx+1, ty+1);
			g.setColor(Color.white);
			g.drawString(text, tx, ty);
			break;
		}
		
	}
	
	static void drawSpawn (Graphics2D g2)
	{
		g = g2;
		int mw = 600, mh = 600, mx = w/2-mw/2, my = h-mh;
		int bh = 40, bw = mw-20, bm = 10;
		int mw1 = (int)(mw*0.3), mw2 = mw-mw1-20;
		int mx2 = mx+mw1+10;
		
		Situation s = Main.situation;
		Actor player = s.getPlayer();
		window(mx, my-50, mw, mh, "---");
		
		g.setFont(flarge);
		g.drawString("Loadout: ", mx+20, my+25);
		for (int j = 0; j < Settings.loadout.length; j++)
			if (button(mx+bm, my+(bh+bm)*j+50, mw1-20, bh, Settings.loadout[j].name))
				player.setLoadout(j);
		if ((button(mx+bm, my+mh-2*bh-2*bm, bw, bh
				, "Spawn"+(player.spawnTime <= 0 ? " (space)":" in "+(int)(player.spawnTime+1)))
				|| Input.keyDown[KeyEvent.VK_SPACE]) && player.spawnTime <= 0)
			s.spawnPlayer();
		
		Loadout l = player.l;
		
		window(mx2, my, mw2, mh-120, "Current: "+l.name);
		int i = 0;
		int space = 30;
		g.setFont(fnormal);
		g.drawString("Weapon 1: "+Settings.weapon[l.weapon[0]].name,mx2+10, my+50+25+space*(i++));
		g.drawString("Weapon 2: "+Settings.weapon[l.weapon[1]].name,mx2+10, my+50+25+space*(i++));
		g.drawString("Special: "+Settings.weapon[l.weapon[2]].name,mx2+10, my+50+25+space*(i++));
		g.drawString("Speed: "+(int)(l.speedMultiplier*100)+"%",mx2+10, my+50+25+space*(i++));
		g.drawString("Health: "+(int)(l.healthMultiplier*100)+"%",mx2+10, my+50+25+space*(i++));
		g.drawString("Regeneration: "+(int)(l.regenMultiplier*100)+"%",mx2+10, my+50+25+space*(i++));
	}
	
	static void drawHUD (Graphics2D g2)
	{
		g = g2;
		w = Settings.width;
		h = Settings.height;
		Situation situation = Main.situation;
		int x = 5;
		int y = h-30;
		int d = 100;
		for (int i = 0; i < situation.teams; i++)
		{
			g.setColor(ExtraMath.blendAlpha(Settings.teamColor[i].darker(), 0.5f));
			g.fillRect(x+d*i, y, d, 30);
			g.setColor(Color.white);
			g.drawString(Settings.teamName[i]+": "+situation.teamScore[i], x+d*i+10, y+20);
		}
		g.setColor(Color.white);
		g.setFont(fhuge);
		g.drawString(""+Math.max((int)(situation.matchTimeLeft+1), 0), x+5, y-15);
		if (Input.keyDown[KeyEvent.VK_E])
			drawScoreboard();
	}
	
	static void drawScoreboard ()
	{
		Situation s = Main.situation;
		ArrayList[] ars = new ArrayList[s.teams];
		for (int t = 0; t < s.teams; t ++)
			ars[t] = new ArrayList<Integer>();
		for (int j = 0; j < s.actors.size(); j++)
			ars[s.actors.get(j).team].add(j);
		
		int x = 50, y = 30, w = GUI.w-2*x, h = GUI.h-100;
		window(x, y, w, h, "Scoreboard");
		y += 40;
		x += 2;
		w -= 4;
		int d = 15, i = 0, tx = 5, ty = 11, dx = 130;
		g.setFont(fsmall);
		g.setColor(ExtraMath.blendAlpha(Color.white, 0.2f));
		g.fillRect(x, y+d*i, w, d);
		g.setColor(Color.white);
		g.drawString("Player", x+tx, y+d*i+ty);
		g.drawString("Kills", x+tx+dx*1, y+d*i+ty);
		g.drawString("Deaths", x+tx+dx*2, y+d*i+ty);
		g.drawString("K/D", x+tx+dx*3, y+d*i+ty);
		i++;
		for (int t = 0; t < s.teams; t ++)
		{
			g.setColor(ExtraMath.blendAlpha(Settings.teamColor[t].darker(), 0.5f));
			g.fillRect(x, y+d*i, w, d);
			g.setColor(Color.white);
			g.drawString(Settings.teamName[t], x+tx, y+d*i+ty);
			g.drawString(""+s.teamScore[t], x+tx+dx*1, y+d*i+ty);
			i ++;
			for (int a = 0; a < ars[t].size(); a++)
			{
				Actor ac = s.actors.get((Integer)(ars[t].get(a)));
				g.setColor(ExtraMath.blendAlpha((i%2 == 0) ? Color.gray:Color.black, 0.1f));
				g.fillRect(x, y+d*i, w, d);
				g.setColor(Color.white);
				g.drawString(ac.name, x+tx, y+d*i+ty);
				g.drawString(""+ac.kills, x+tx+dx*1, y+d*i+ty);
				g.drawString(""+ac.deaths, x+tx+dx*2, y+d*i+ty);
				g.drawString((ac.deaths == 0)?"N":""+((double)ac.kills/ac.deaths), x+tx+dx*3, y+d*i+ty);
				i++;
			}
		}
		
	}
	
	static boolean button (int x, int y, int w, int h, String text)
	{
		boolean mouseIn = mouseInArea(x, y, w, h);
		g.setColor(Color.black);
		if (mouseIn)
			g.setColor(Color.gray);
		g.fillRect(x, y, w, h);
		g.setColor(new Color(1, 1, 1, 0.4f));
		g.drawRect(x, y, w, h);
		//g.drawRect(x+1, y+1, w-2, h-2);
		g.setColor(Color.white);
		g.setFont(flarge);
		g.drawString(text, x+w/2-text.length()*5, y+h/2+5);
		if (mouseIn && Input.mouseDown[MouseEvent.BUTTON1])
		{
			Input.mouseDown[MouseEvent.BUTTON1] = false;
			return true;
		}
		return false;
	}
	
	static int changeButtons (int x, int y, int w, int h, String text, int min, int max, int current)
	{
		if (button(x, y, w/6, h, "<"))
			current --;
		if (button(x+5*w/6, y, w/6, h, ">"))
			current ++;
		current = Math.min(Math.max(current, min), max);
		g.setFont(flarge);
		g.drawString(text, x+1*w/6+20, y+h/2+5);
		
		return current;
	}
	
	static void drawMap (int x, int y, int w, int h, Map map)
	{
		double f = (double)w/map.width;
		if (f > (double)h/map.height)
			f = (double)h/map.height;
		x = x+(w-(int)(map.width*f))/2;
		for (int gx = 0; gx < map.width; gx++)
			for (int gy = 0; gy < map.height; gy++)
			{
				Color c = Color.black;
				if (Map.solid(map.get(gx, gy)))
					c = Color.white;
				if (Character.isDigit(map.get(gx, gy)))
					c = Settings.teamColor[(int)(map.get(gx, gy)-'0')];
				g.setColor(c);
				g.fillRect(x+(int)(gx*f), y+(int)(gy*f), 1+(int)((gx+1)*f-(int)gx*f), 1+(int)((gy+1)*f-(int)gy*f));
			}
	}
	
	static void window (int x, int y, int w, int h, String headline)
	{
		g.setColor(new Color(0, 0, 0, 0.8f));
		g.fillRect(x, y, w, h);
		g.setColor(new Color(1, 1, 1, 0.4f));
		g.fillRect(x, y, w, 40);
		g.drawRect(x, y, w, h);
		g.drawRect(x+1, y+1, w-2, h-2);
		g.setColor(Color.white);
		g.setFont(flarge);
		g.drawString(headline, x+(int)(w*0.5)-headline.length()*5, y+25);
	}
	
	static boolean mouseInArea (int x, int y, int w, int h)
	{
		return (Input.mouseX >= x && Input.mouseY >= y && Input.mouseX < x+w && Input.mouseY < y+h);
	}
	
}
