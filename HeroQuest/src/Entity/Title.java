package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import Main.GamePanel;

public class Title {
	
	public BufferedImage image;
	
	public int count;
	private boolean done;
	private boolean remove;
	
	private double x;
	private double y;
	private double dx;
	
	private int width;
	
	// constructor with string
	public Title(String s) {
		
		try {
			image = ImageIO.read(getClass().getResourceAsStream(s));
			width = image.getWidth();
			x = -width;
			done = false;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// constructor with bi
	public Title(BufferedImage image) {
		this.image = image;
		width = image.getWidth();
		x = -width;
		done = false;
	}
	
	public void sety(double y) {
		this.y = y;
	}
	
	public void setx(double x) {
		this.x = x;
	}
	
	public void begin() {
		dx = 10;
	}
	
	public boolean shouldRemove() {
		return remove;
	}
	
	// slide 
	public void update() {
		if(!done) {
			if(x >= (GamePanel.WIDTH - width) / 2) {
				x= (GamePanel.WIDTH - width) / 2;
				count++;
				if(count >= 120)
					done = true;
			}
			else
				x += dx;
		}
		else {
			x += dx;
			if(x > GamePanel.WIDTH)		// out of the screen, I can remove it
				remove = true;
		}		
	}
	
	public void draw(Graphics2D g) {
		g.drawImage(image, (int)x, (int)y, null);
	}

}
