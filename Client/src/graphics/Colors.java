package graphics;

import java.awt.Color;

public class Colors {
	public static final Color unoMainRed = new Color(237, 28, 36);
	public static final Color unoRed = new Color(255, 85, 85);
	public static final Color unoYellow = new Color(255, 170, 0);
	public static final Color unoGreen = new Color(85, 170, 85);
	public static final Color unoBlue = new Color(85, 85, 255);
	
	public static Color tintColor(Color c, double tintValue) {
		int newR = (int) (c.getRed() + (255 - c.getRed()) * tintValue);
		int newG = (int) (c.getGreen() + (255 - c.getGreen()) * tintValue);
		int newB = (int) (c.getBlue() + (255 - c.getBlue()) * tintValue);
		
		return new Color(newR, newG, newB);
	}
	
	public static Color shadeColor(Color c, double shadeValue) {
		int newR = (int) (c.getRed() * (1 - shadeValue));
		int newG = (int) (c.getGreen() * (1 - shadeValue));
		int newB = (int) (c.getBlue() * (1 - shadeValue));
		
		return new Color(newR, newG, newB);
	}
}
