package Entity.Enemies;

import Entity.*;
import TileMap.TileMap;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;

import javax.imageio.ImageIO;

import java.util.ArrayList;

public class Skeleton extends Enemy {

	
	private boolean attack;
	private int attackDamage;
	private int attackRange;
	
	private ArrayList<BufferedImage[]> sprites;
	private final int[] numFrames = { 11, 13, 18, 8, 15};
	
	
	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int ATTACK = 2;
	private static final int HIT = 3;
	private static final int DEAD = 4;
	
	//Constructor
	public Skeleton(TileMap tm) {
		
		super(tm);
		
		moveSpeed = 0.2;
		maxSpeed = 1.2;
		stopSpeed = 0.4;
		fallSpeed = 0.2;
		maxFallSpeed = 4.0;
		
		
		width = 20;
		height = 32; 
		cwidth = 20;
		cheight = 30; 
		
		health = maxHealth = 30;
		damage = 1;
		attackDamage = 1;
		attackRange = 40;
		
		boss = true;
	
	try {
		BufferedImage idleSheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Enemies/Skeleton Idle.png"));
		BufferedImage walkSheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Enemies/Skeleton Walk.png"));
		BufferedImage attackSheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Enemies/Skeleton Attack.png"));
		BufferedImage hitSheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Enemies/Skeleton Hit.png"));
		BufferedImage deadSheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Enemies/Skeleton Dead.png"));
		
		sprites = new ArrayList<BufferedImage[]>();
		
		for(int i = 0; i < 5; i++) {
			
			BufferedImage[] bi = new BufferedImage[numFrames[i]];
			
			for(int j = 0; j < numFrames[i]; j++) {
				if(i == IDLE)
					bi[j] = idleSheet.getSubimage(j * 24, 0, 24, 32);	//24 ; 32
				if(i == WALKING)
					bi[j] = walkSheet.getSubimage(j * 22, 0, 22, 33); //22 ; 33
				if(i == ATTACK)
					bi[j] = attackSheet.getSubimage(j * 43, 0, 43, 37);
				if(i == HIT)
					bi[j] = hitSheet.getSubimage(j * 30, 0, 30, 32);
				if(i == DEAD)
					bi[j] = deadSheet.getSubimage(j * 33, 0, 33, 32);
			}
			sprites.add(bi);
		}
		
	}
	catch(Exception e) {
		e.printStackTrace();
	}
	
	animation = new Animation();
	currentAction = IDLE;
	animation.setFrames(sprites.get(IDLE));
	animation.setDelay(100);
	
	right = false;
	facingRight = false;
	}
	
	public int getHealth() { return health; }
	public int getMaxHealth() { return maxHealth; }
	
	public void setAttack() {
		attack = true;
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
		
		
		if( (currentAction == ATTACK || currentAction == DEAD || currentAction == HIT) && !(falling)) {
			dx = 0;
		}
		
		// falling
		if(falling) {			
			dy += fallSpeed;
			if(dy > maxFallSpeed) dy = maxFallSpeed;
 		}
	}
	
	public void checkPlayer(Player player) {
	
		// range 
		if(player.getx() < x - 80 || player.getx() > x + 80) {
			right = false;
			left = false;
			dx = 0;
			return;
		}
		
		
		// enemy movement
		if(player.getx() > x) {
			right = true;
			left = false;
		}
		if(player.getx() < x) {
			left = true;
			right = false;
		}		
		
		// enemy attack
		if(facingRight) {
			if(player.getx() > x && player.getx() < x + attackRange && player.gety() > y - height / 2 && player.gety() < y + height / 2) {
				attack = true;
				player.hit(attackDamage);
			}
		}
		else {
			if(player.getx() < x && player.getx() > x - attackRange && player.gety() > y - height / 2 && player.gety() < y + height / 2) {
				attack = true;
				player.hit(attackDamage);
			}
		}
	}
	
	
	
	public void update() {
		
		// update position		
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
	
		// check attack has stopped
		if(currentAction == ATTACK) {
			if(animation.hasPlayedOnce()) attack = false;
		}
		
		if(currentAction == DEAD) {
			if(animation.hasPlayedOnce()) removeB = true;
		}
		
		// check flinching
		if(flinching) {
			long elapsed =	(System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed > 400) {
				flinching = false;
			}
		}
		
		// set animation
		
		if(dead) {
			if(currentAction != DEAD) {
				currentAction = DEAD;
				animation.setFrames(sprites.get(DEAD));
				animation.setDelay(80);
				width = 33;
			}
		}
		else if(flinching) {
			if(currentAction != HIT) {
				currentAction = HIT;
				animation.setFrames(sprites.get(HIT));
				animation.setDelay(80);
				width = 30;
			}
		}
		else if(attack) {
			if(currentAction != ATTACK) {
				currentAction = ATTACK;
				animation.setFrames(sprites.get(ATTACK));
				animation.setDelay(50);
				width = 43;
			}		
		}
		
		else if(left || right) {
			if(currentAction != WALKING) {
				currentAction = WALKING;
				animation.setFrames(sprites.get(WALKING));
				animation.setDelay(40);
				width = 22;
			}
		}
		else {
			if(currentAction != IDLE) {
				currentAction = IDLE;
				animation.setFrames(sprites.get(IDLE));
				animation.setDelay(100);
				width = 24;
			}
		}
		
		// update animation
		animation.update();
		
		// set direction
		if(currentAction != ATTACK && currentAction != DEAD && currentAction != HIT) {
			if(right) facingRight = true;
			if(left) facingRight = false;
		}
		
	}
	
	public void draw(Graphics2D g) {
		
		setMapPosition();
		
		if(flinching) {
			long elapsed =	(System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed / 100 % 2 == 0) 
			return;		
		}
		
		super.draw(g);
		
	}
		
}
	