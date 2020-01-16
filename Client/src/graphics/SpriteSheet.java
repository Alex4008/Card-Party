package graphics;

import java.awt.image.BufferedImage;

public class SpriteSheet {

	private BufferedImage image;
	
	public SpriteSheet(BufferedImage image) {
		this.image = image;
		
	}
	
	public BufferedImage grabImage(int col, int row) {
		
		BufferedImage img = image.getSubimage((col * 73) - 72, (row * 98) - 97, 73, 98);
		return img;
	}
	
	public BufferedImage grabImage(int col, int row, int width, int height) {
		
		BufferedImage img = image.getSubimage((col * 73) - 72, (row * 98) - 97, width, height);
		return img;
	}
	
	public BufferedImage grabUnoImage(int col, int row) {
		BufferedImage img = image.getSubimage((col * 86) - 86, (row * 128) - 128, 86, 128);
		return img;
	}
	
}