package topdownshooter;

public class Input {
	static boolean[] keyDown = new boolean[1000];//should be about enough
	
	static boolean[] mouseDown = new boolean[1000];//meh
	
	static int mouseX, mouseY;
	
	static synchronized void pressed (int id)
	{
		keyDown[id] = true;
	}
	
	static synchronized void released (int id)
	{
		keyDown[id] = false;
	}
}
