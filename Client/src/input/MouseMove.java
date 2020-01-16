package input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

import main.Main;

public class MouseMove extends JPanel implements MouseMotionListener{

	private static final long serialVersionUID = 1L;
	Main main;
	
	public MouseMove(Main main) {
		this.main = main;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		main.mouseLoc.setLocation((int) e.getX(), (int) e.getY());
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		main.mouseLoc.setLocation((int) e.getX(), (int) e.getY());
		
	}

}
