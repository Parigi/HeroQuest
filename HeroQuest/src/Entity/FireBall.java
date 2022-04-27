package Entity;

import TileMap.TileMap;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class FireBall extends MapObject {
	
	private boolean hit; 	//fireball has hit something?
	private boolean remove;	//we should remove the fireball from the game?
	private BufferedImage[] sprites;	//per le animazioni
	private BufferedImage[] hitSprites;
	
	//Constructor, boolean right for the direction
	public FireBall(TileMap tm, boolean right) {
		
		super(tm);
		
		//direzione
		facingRight = right;
		
		//movimento
		moveSpeed = 3.8;
		if(right) dx = moveSpeed;
		else dx = -moveSpeed;
		
		//dimensioni
		width = 30;
		height = 30;
		cwidth = 14;
		cheight = 14;
		
		// load sprites
		try {
			
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/fireball.gif"));
			
			sprites = new BufferedImage[4];
			for(int i = 0; i < sprites.length; i++) {
				sprites[i] = spritesheet.getSubimage(i * width,	0, width, height);
			}
			
			hitSprites = new BufferedImage[3];
			for(int i = 0; i < hitSprites.length; i++) {
				hitSprites[i] = spritesheet.getSubimage(i * width, height, width, height);
			}
			
			animation = new Animation();
			animation.setFrames(sprites);
			animation.setDelay(70);
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	//has hit something? 
	public void setHit() {
		if(hit) return;
		hit = true;
		animation.setFrames(hitSprites);
		animation.setDelay(70);
		dx = 0;		//serve per "fermare" la seconda animazione	 
	}
	
	public boolean shouldRemove() { return remove; } 
	
	//controllo collisioni e setto la nuova posizione
	//eventualmente setto hit e faccio l'updat dell'animazione
	public void update() {
		
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		if(dx == 0 && !hit) {		//dal codice di TileMap quando un oggetto ha una collisione dx viene settato a 0
			setHit();
		}
		
		animation.update();
		if(hit && animation.hasPlayedOnce()) {
			remove = true;
		}
		
	}
	
	public void draw(Graphics2D g) {
		
		setMapPosition();
		
		super.draw(g);	//funzione da MapObject (sx o dx?)
		
	}
	
}


















