package Entity;

import TileMap.*;
import Audio.AudioPlayer;

import java.util.ArrayList;
import javax.imageio.ImageIO;	//for reading in the sprite sheets
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class Player extends MapObject {
	
	// player stuff
	private int health;
	private int maxHealth;
	private int fire;
	private int maxFire;
	
	//private boolean dead;	
	private boolean flinching;
	private long flinchTimer;
	
	// fireball
	private boolean firing;
	private int fireCost;
	private int fireBallDamage;
	private ArrayList<FireBall> fireBalls;	
	
	// attack
	private boolean attack;
	private int atkDamage;
	private int atkRange;
	
	private boolean items;	
	
	// animation actions
	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int JUMPING = 2;
	private static final int FALLING = 3;
	private static final int FIREBALL = 4;
	private static final int ATTACK = 5;
	private static final int ITEM =6;
	
	private BufferedImage [] jmp;
	private BufferedImage [] idle;
	private BufferedImage [] fall;
	private BufferedImage [] run;
	private BufferedImage [] cast;
	private BufferedImage [] atk;
	private BufferedImage [] item;
	
	private HashMap<String, AudioPlayer> sfx;
	
	// constructor
	public Player(TileMap tm) {
		
		super(tm);		
		
		width = 50;
		height = 37;
		
		cwidth = 20;
		cheight = 30;
		
		moveSpeed = 0.3;
		maxSpeed = 1.6;
		stopSpeed = 0.4;
		fallSpeed = 0.15;
		maxFallSpeed = 4.0; 
		jumpStart = -4.8;	
		stopJumpSpeed = 0.3;
		
		facingRight = true;
		
		health = maxHealth = 5;
		fire = maxFire = 2500;
		
		fireCost = 200;
		fireBallDamage = 5;
		fireBalls = new ArrayList<FireBall>();
		
		atkDamage = 8;
		atkRange = 40;
		
		// load sprites
		try {		
			
			//carico gli sprites di idle
			
			BufferedImage sp01 = ImageIO.read(getClass().getResourceAsStream("/Adventurer/idle1.png"));	
			BufferedImage sp02 = ImageIO.read(getClass().getResourceAsStream("/Adventurer/idle2.png"));	
			BufferedImage sp03 = ImageIO.read(getClass().getResourceAsStream("/Adventurer/idle3.png"));	
			BufferedImage sp04 = ImageIO.read(getClass().getResourceAsStream("/Adventurer/idle4.png"));	
			
			// carico gli sprites di jump
				
			BufferedImage sp13 = ImageIO.read(getClass().getResourceAsStream("/Adventurer/jmp2.png"));	
						
			//carico gli sprites di run
			
			BufferedImage sp21 = ImageIO.read(getClass().getResourceAsStream("/Adventurer/run0.png"));	
			BufferedImage sp22 = ImageIO.read(getClass().getResourceAsStream("/Adventurer/run1.png"));	
			BufferedImage sp23 = ImageIO.read(getClass().getResourceAsStream("/Adventurer/run2.png"));	
			BufferedImage sp24 = ImageIO.read(getClass().getResourceAsStream("/Adventurer/run3.png"));	
			BufferedImage sp25 = ImageIO.read(getClass().getResourceAsStream("/Adventurer/run4.png"));	
			BufferedImage sp26 = ImageIO.read(getClass().getResourceAsStream("/Adventurer/run5.png"));	
			
			
			//carico gli sprites di fall 
			BufferedImage sp30 = ImageIO.read(getClass().getResourceAsStream("/Adventurer/fall0.png"));	
			BufferedImage sp31 = ImageIO.read(getClass().getResourceAsStream("/Adventurer/fall1.png"));	
			
			
			//carico gli sprites di cast
			BufferedImage sp41=ImageIO.read(getClass().getResourceAsStream("/Adventurer/cast0.png"));
			BufferedImage sp42=ImageIO.read(getClass().getResourceAsStream("/Adventurer/cast1.png"));
			BufferedImage sp43=ImageIO.read(getClass().getResourceAsStream("/Adventurer/cast2.png"));
			BufferedImage sp44=ImageIO.read(getClass().getResourceAsStream("/Adventurer/cast3.png"));
			
			
			//carico gli sprites di attacco
		
			BufferedImage sp54=ImageIO.read(getClass().getResourceAsStream("/Adventurer/atk11.png"));
			BufferedImage sp55=ImageIO.read(getClass().getResourceAsStream("/Adventurer/atk12.png"));
			BufferedImage sp56=ImageIO.read(getClass().getResourceAsStream("/Adventurer/atk13.png"));
			BufferedImage sp57=ImageIO.read(getClass().getResourceAsStream("/Adventurer/atk14.png"));
			
			//carico sprites di utilizzo item
			BufferedImage sp60=ImageIO.read(getClass().getResourceAsStream("/Adventurer/item0.png"));
			BufferedImage sp61=ImageIO.read(getClass().getResourceAsStream("/Adventurer/item1.png"));
			BufferedImage sp62=ImageIO.read(getClass().getResourceAsStream("/Adventurer/item2.png"));
			
			idle = new BufferedImage [4];
			jmp = new BufferedImage [1];
			run = new BufferedImage [6];
			fall=new BufferedImage[2];
			cast=new BufferedImage [4];
			atk=new BufferedImage[4];
			item=new BufferedImage[3];
			
			//sprites di idle
			idle[0]=sp01.getSubimage(0, 0, width, height);
			idle[1]=sp02.getSubimage(0, 0, width, height);
			idle[2]=sp03.getSubimage(0, 0, width, height);
			idle[3]=sp04.getSubimage(0, 0, width, height);
			
			//sprites di jump
			jmp[0]=sp13.getSubimage(0, 0,width,height);
						
			//sprites di run
			run[0]=sp21.getSubimage(0, 0, width, height);
			run[1]=sp22.getSubimage(0, 0, width, height);
			run[2]=sp23.getSubimage(0, 0, width, height);
			run[3]=sp24.getSubimage(0, 0, width, height);
			run[4]=sp25.getSubimage(0, 0, width, height);
			run[5]=sp26.getSubimage(0, 0, width, height);
			
			//sprites di fall
			fall[0]=sp30.getSubimage(0, 0, width, height);
			fall[1]=sp31.getSubimage(0, 0, width, height);
			
			//sprites di cast
			cast[0]=sp41.getSubimage(0, 0, width, height);
			cast[1]=sp42.getSubimage(0, 0, width, height);
			cast[2]=sp43.getSubimage(0, 0, width, height);
			cast[3]=sp44.getSubimage(0, 0, width, height);
			
			//sprites di attack
			atk[0]=sp54.getSubimage(0,0, width, height);
			atk[1]=sp55.getSubimage(0,0, width, height);
			atk[2]=sp56.getSubimage(0,0, width, height);
			atk[3]=sp57.getSubimage(0,0, width, height);
			
			//sprites di item
			item[0]=sp60.getSubimage(0, 0, width, height);
			item[1]=sp61.getSubimage(0, 0, width, height);
			item[2]=sp62.getSubimage(0, 0, width, height);
							
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		animation = new Animation();
		//animazione di partenza
		currentAction = IDLE;		
		animation.setFrames(idle);
		animation.setDelay(400);
		
		sfx = new HashMap<String, AudioPlayer>();
		sfx.put("jump", new AudioPlayer("/SFX/jump.mp3"));
		sfx.put("scratch", new AudioPlayer("/SFX/scratch.mp3"));
		
	}
	
	public int getHealth() { return health; }
	public int getMaxHealth() { return maxHealth; }
	public int getFire() { return fire; }
	public int getMaxFire() { return maxFire; }
	
	public void setItem() {
		items = true;
	}
	
	public void setFiring() { 
		firing = true;
	}
	
	// non posso smettere di attaccare, devo completare l'azione
	public void setAttack() {
		attack = true;
	}	
	
	public void checkAttack(ArrayList<Enemy> enemies) {
		
		// loop through enemies
		for(int i = 0; i < enemies.size(); i++) {
			
			Enemy e = enemies.get(i);
			
			// scratch attack
			if(attack) {
				if(facingRight) { //controllo se l'ho colpito (attacco a destra)
					if(e.getx() > x &&	e.getx() < x + atkRange && 	e.gety() > y - height / 2 && e.gety() < y + height / 2) {
						e.hit(atkDamage);		//se ho colpito chiamo la funzione hit del nemico passandogli come valore il danno fatto da scratching
					}
				}
				else {	//controllo se l'ho colpito (attacco a sinistra)
					if(e.getx() < x &&	e.getx() > x - atkRange &&	e.gety() > y - height / 2 && e.gety() < y + height / 2) {
						e.hit(atkDamage);
					}
				}
			}
			
			// fireballs
			for(int j = 0; j < fireBalls.size(); j++) {		
				if(fireBalls.get(j).intersects(e)) {		
					e.hit(fireBallDamage);					
					fireBalls.get(j).setHit();				
					break;
				}
			}
			
			// check enemy collision
			if(intersects(e)) {			
				hit(e.getDamage());		
			}
			
		}
		
	}
	
	// controllo se prendo la pozione curativa
	public void checkPotion(ArrayList<HealingPotion> potion) {
		for(int i = 0; i < potion.size(); i++) {
			HealingPotion p = potion.get(i);
			if(intersects(p)) {
				heal(p.getHeal());
				p.setTaken(true);
				items = true;
			}	
		}		
	}
	
	// controllo se prendo la pozione per il mana
	public void checkManaPotion(ArrayList<ManaPotion> potion) {
		for(int i = 0; i < potion.size(); i++) {
			ManaPotion p = potion.get(i);
			if(intersects(p)) {
				fire(p.getMana());
				p.setTaken(true);
				items=true;
			}		
		}		
	}
	
	public void fire(int mana) {
		fire += mana;
		if(fire >= maxFire) 
			fire = maxFire;
	}
	
	public void heal(int healings) {
		health += healings;
		if(health >= maxHealth)
			health = maxHealth;
	}
	
	
	public void hit(int damage) {
		if(flinching) return;
		health -= damage;
		if(health < 0) health = 0;
		//if(health == 0) dead = true;
		flinching = true;
		flinchTimer = System.nanoTime();
	}

	
	// utilizzata nell'update
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
		else {		
			if(dx > 0) {
				dx -= stopSpeed;
				if(dx < 0) {
					dx = 0;
				}
			}
			else if(dx < 0) {
				dx += stopSpeed;
				if(dx > 0) {
					dx = 0;
				}
			}
		}
		
		// cannot move while attacking, except in air
		if( (currentAction == ATTACK || currentAction == FIREBALL || currentAction == ITEM) && !(jumping || falling)) {
			dx = 0;
		}
		
		// jumping
		if(jumping && !falling) {
			sfx.get("jump").play();
			dy = jumpStart;
			falling = true;
		}
		
		// falling
		if(falling) {
			
			dy += fallSpeed;
			
			if(dy > 0) jumping = false;
			if(dy < 0 && !jumping) dy += stopJumpSpeed;
			
			if(dy > maxFallSpeed) dy = maxFallSpeed;
			
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
		if(currentAction == FIREBALL) {
			if(animation.hasPlayedOnce()) firing = false;
		}
		if(currentAction == ITEM) {
			if(animation.hasPlayedOnce()) items = false;
		}
		
		// fireball attack
		
		if(fire > maxFire) fire = maxFire;
		if(firing && currentAction != FIREBALL) {
			if(fire > fireCost) {		//controllo di avere abbastanza energia 
				fire -= fireCost;
				FireBall fb = new FireBall(tileMap, facingRight);	//creo la fireball
				fb.setPosition(x, y);
				fireBalls.add(fb);		//add to arraylist
			}
		}
		
		// update fireballs
		for(int i = 0; i < fireBalls.size(); i++) {		//faccio un loop per controllare tutte le fireball esistenti
			fireBalls.get(i).update();
			if(fireBalls.get(i).shouldRemove()) {
				fireBalls.remove(i);		//rimuovo dalla lista e quindi dal gioco
				i--;
			}
		}
		
		// check done flinching
		if(flinching) {
			long elapsed =	(System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed > 1000) {
				flinching = false;
			}
		}
		
		// set animation
		if(items) {
			if(currentAction != ITEM) {
				currentAction = ITEM;
				animation.setFrames(item);
				animation.setDelay(100);
			}
		}
		else if(attack) {
			if(currentAction != ATTACK) {
				sfx.get("scratch").play();
				currentAction = ATTACK;
				animation.setFrames(atk);
				animation.setDelay(50);
			}
		}
		else if(firing) {
			if(currentAction != FIREBALL) {
				currentAction = FIREBALL;
				animation.setFrames(cast);
				animation.setDelay(100);
			}
		}
		else if(dy > 0) {
			if(currentAction != FALLING) {
				currentAction = FALLING;
				animation.setFrames(fall);
				animation.setDelay(100); //100
			}
		}
		else if(dy < 0 || jumping) {  
			if(currentAction != JUMPING) {
				currentAction = JUMPING;
				animation.setFrames(jmp);
				animation.setDelay(-1);
			}
		}
		else if(left || right) {
			if(currentAction != WALKING) {
				currentAction = WALKING;
				animation.setFrames(run);
				animation.setDelay(80);
			}
		}
		else {			
			if(currentAction != IDLE) {
				currentAction = IDLE;
				animation.setFrames(idle);
				animation.setDelay(100);
			}
		}
		
		animation.update();		
		
		// set direction
		if(currentAction != ATTACK && currentAction != FIREBALL) {
			if(right) facingRight = true;
			if(left) facingRight = false;
		}
		
	}
	
	public void draw(Graphics2D g) {
		
		setMapPosition();		
		
		// draw fireballs
		for(int i = 0; i < fireBalls.size(); i++) {
			fireBalls.get(i).draw(g);
		}
		
		// draw player
		if(flinching) {
			long elapsed =	(System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed / 100 % 2 == 0) 
				return;		
		}
		
		super.draw(g);
		
	}
	
}

















