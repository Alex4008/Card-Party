package main;


import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseInput implements MouseListener{

	Main main;
	
	public MouseInput(Main main) {
		this.main = main;
	}
	
	public void mouseClicked(MouseEvent e) {
		main.clickType = e.getButton();
		main.setClicked(true);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		Main.mouseHeld = false;
		main.setMouseOnScreen(true);
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		main.setMouseOnScreen(false);
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		main.clickType = e.getButton();
		Main.mouseHeld = true;
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		Main.mouseHeld = false;
		main.setClicked(true);
		
	}
	
}