package topdownshooter;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

public class Panel extends JPanel implements KeyListener, MouseMotionListener, MouseListener{
	
	@Override
	public void paintComponent (Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		Main.draw(g2);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		Input.pressed(e.getKeyCode());
		e.consume();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		Input.released(e.getKeyCode());
		e.consume();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		e.consume();
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		updateMousePos(e);
		e.consume();
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		try{
			updateMousePos(e);
			e.consume();
		} catch (Exception ex){
			System.out.println(ex.getMessage());
			Main.close();
		}
	}
	
	void updateMousePos (MouseEvent e)
	{
		
		Input.mouseX = e.getX();
		Input.mouseY = e.getY();
	}
	
	void updateMouseClick (MouseEvent e, boolean down)
	{
		Input.mouseDown[e.getButton()] = down;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		e.consume();
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		e.consume();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		e.consume();
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		updateMouseClick(e, true);
		e.consume();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		updateMouseClick(e, false);
	}
}
