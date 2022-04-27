package Entity.Enemies;

import Entity.*;
import TileMap.TileMap;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;

import javax.imageio.ImageIO;


public class Skull extends Enemy {
private BufferedImage[] sprites;
	
	public Skull(TileMap tm) {
		
		super(tm);
		
		moveSpeed = 0.6;
		maxSpeed = 0.6;
		fallSpeed = 0.2;
		maxFallSpeed = 10.0;
		
		width = 39;
		height = 46;
		cwidth = 18;
		cheight = 30;
		
		health = maxHealth = 10;
		damage = 1;
		
		// load sprites
		try {
			
			BufferedImage sp1 = ImageIO.read(getClass().getResourceAsStream("/Sprites/Enemies/skull.png"));
			
			sprites = new BufferedImage[8];
			for(int i=0; i<8; i++) {
			sprites[i] = sp1.getSubimage(i*width,0,width,height);
			}
			
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(100);
		
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
