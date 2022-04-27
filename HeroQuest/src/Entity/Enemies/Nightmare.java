package Entity.Enemies;

import Entity.*;
import TileMap.TileMap;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;

import javax.imageio.ImageIO;

public class Nightmare extends Enemy {
private BufferedImage[] sprites;
	
	public Nightmare(TileMap tm) {
		
		super(tm);
		
		moveSpeed = 0.7;
		maxSpeed = 1;
		fallSpeed = 0.2;
		maxFallSpeed = 10.0;
		
		width = 85;
		height = 55;
		cwidth = 50;
		cheight = 44;
		
		health = maxHealth = 2;
		damage = 1;
		
		// load sprites
		try {
			
			BufferedImage sp1 = ImageIO.read(getClass().getResourceAsStream("/Sprites/Enemies/nightmare.galloping.png"));
			
			sprites = new BufferedImage[4];
			
			for(int i=0; i<4; i++)
			{
				sprites[i]=sp1.getSubimage(i*width, 0, width, height);
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(80);
		
		right = true;
		facingRight = false;
		
	}
	
	private void getNextPosition() {
		
		// movement
		if(left) {
			dx -= moveSpeed;
			if(dx < -maxSpeed) {
				dx = -maxSpeed;
			}
		}
		else if(right) {
			dx += moveSpeed;
			if(dx > maxSpeed) {
				dx = maxSpeed;
			}
		}
		
		// falling
		if(falling) {
			dy += fallSpeed;
		}
		
	}
	
	public void update() {
		
		// update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		// check flinching
		if(flinching) {
			long elapsed =
				(System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed > 400) {
				flinching = false;
			}
		}
		
		// if it hits a wall, go other direction
		if(right && dx == 0) {
			right = false;
			left = true;
			facingRight = true;
		}
		else if(left && dx == 0) {
			right = true;
			left = false;
			facingRight = false;
		}
		
		// update animation
		animation.update();
		
	}
	
	public void draw(Graphics2D g) {

		setMapPosition();
		
		super.draw(g);
		
	}
}
