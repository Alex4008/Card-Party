package graphics;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import cards.Suit;
import cards.UnoColor;
import cards.UnoValue;
import cards.Value;
import main.Main;

public class Sprite {
	
	//2D array of cards
	private ArrayList<ArrayList<BufferedImage>> defaultCardSet = new ArrayList<ArrayList<BufferedImage>>();
	private ArrayList<ArrayList<BufferedImage>> unoCardSet = new ArrayList<ArrayList<BufferedImage>>();
	
	public Sprite(Main m) {
		//Load default sprites
		SpriteSheet ss = new SpriteSheet(m.getDefaultSpriteSheet());
		LoadDefaultSprites(m, ss);
		
		//Load uno sprites
		SpriteSheet ssUno = new SpriteSheet(m.getUnoSpriteSheet());
		LoadUnoSprites(m, ssUno);
	}
	
	private void LoadDefaultSprites(Main m, SpriteSheet ss) {
		defaultCardSet.add(new ArrayList<BufferedImage>());
		defaultCardSet.add(new ArrayList<BufferedImage>());
		defaultCardSet.add(new ArrayList<BufferedImage>());
		defaultCardSet.add(new ArrayList<BufferedImage>());
		
		for(int i = 0; i < 4; i++) {
			for(int j = 1; j <= 13; j++) {
				defaultCardSet.get(i).add(ss.grabImage(j, i + 1));
			}
		}
	}   
	
	private void LoadUnoSprites(Main m, SpriteSheet ss) {
		unoCardSet.add(new ArrayList<BufferedImage>()); //Red
		unoCardSet.add(new ArrayList<BufferedImage>()); //Yellow
		unoCardSet.add(new ArrayList<BufferedImage>()); //Green
		unoCardSet.add(new ArrayList<BufferedImage>()); //Blue
		unoCardSet.add(new ArrayList<BufferedImage>()); //Wilds
		
		for(int i = 0; i < 4; i++) {
			for(int j = 1; j <= 13; j++) {
				unoCardSet.get(i).add(ss.grabUnoImage(j, i + 1));
			}
		}
		
		unoCardSet.get(4).add(ss.grabUnoImage(14, 1));
		unoCardSet.get(4).add(ss.grabUnoImage(14, 2));
		
	}   
	
	//Public methods
	
	public BufferedImage getSprite(Suit s, Value v) {
		return defaultCardSet.get(s.num - 1).get(v.num - 1);
	}
	
	public BufferedImage getUnoSprite(UnoColor c, UnoValue v) {
		if(c == UnoColor.BLACK) {
			if(v == UnoValue.WILD) return unoCardSet.get(4).get(0);
			else if(v == UnoValue.DRAWFOUR) return unoCardSet.get(4).get(1);
		}
		return unoCardSet.get(c.num - 1).get(v.num);
	}
	
	public static BufferedImage rotate(BufferedImage image, double rotation) {
		rotation = Math.toRadians(rotation);
		double locX = image.getWidth() / 2;
		double locY = image.getHeight() / 2;
		double diff = Math.abs(image.getWidth() - image.getHeight());
		
		//To correct the set of origin point and the overflow
		double unitX = Math.abs(Math.cos(rotation));
		double unitY = Math.abs(Math.sin(rotation));

		double correctUx = unitX;
		double correctUy = unitY;
		
		//if the height is greater than the width, so you have to 'change' the axis to correct the overflow
		if(image.getWidth() < image.getHeight()){
		    correctUx = unitY;
		    correctUy = unitX;
		}
		
		//translate the image center to same diff that dislocates the origin, to correct its point set
		AffineTransform objTrans = new AffineTransform();
		objTrans.translate(correctUx * diff, correctUy*diff);
		objTrans.rotate(rotation, locX, locY);

		AffineTransformOp op = new AffineTransformOp(objTrans, AffineTransformOp.TYPE_BILINEAR);

		return op.filter(image,  null);
	}
	
	public static BufferedImage flipHorizontal(BufferedImage image) {
		// Flip the image horizontally
		AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
		tx.translate(-image.getWidth(null), 0);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		return op.filter(image, null);
	}
	
	public static BufferedImage flipVertical(BufferedImage image) {
		// Flip the image vertically
		AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
		tx.translate(0, -image.getHeight(null));
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		return op.filter(image, null);

	}
}
