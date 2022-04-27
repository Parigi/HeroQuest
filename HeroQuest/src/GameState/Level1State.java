package GameState;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Main.GamePanel;
import TileMap.*;
import Entity.*;
import Entity.Enemies.*;
import Audio.AudioPlayer;

import java.awt.*;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Entity.Title;

import Handlers.Keys;

public class Level1State extends GameState {
	
	private TileMap tileMap;
	private Background bg;

	private Player player;
	
	private ArrayList<Enemy> enemies;
	private ArrayList<Explosion> explosions;
	
	private HUD hud;
	private BufferedImage titleText;
	private Title title;
	private Teleport teleport;
	
	private ArrayList<HealingPotion> potions;
	private ArrayList<ManaPotion> mpotions;
	
	private AudioPlayer bgMusic;
	
	private static int numenemies;
	private static int boss;
	private static int kill;
	private static long startTime;
	private static long endTime;
	private static long time;
	
	public Level1State(GameStateManager gsm) {
		super(gsm);
		init();
	}
	
	public void init() {
		
		// map
		tileMap = new TileMap(30);
		tileMap.loadTiles("/Tilesets/grasstileset.gif");
		tileMap.loadMap("/Maps/map.txt");
		tileMap.setPosition(0, 0);
		tileMap.setTween(1);	
		
		
		// bacgkround
		bg = new Background("/Backgrounds/sfondo.gif", 0.1); 
		
		// player
		player = new Player(tileMap);
		player.setPosition(80, 140);
		
		// enemies
		populateEnemies();
	
		// explosions
		explosions = new ArrayList<Explosion>();
		
		// potions
		insertPotion();
		insertManaPotion();
		
		// hud
		hud = new HUD(player);
		startTime = System.currentTimeMillis();
		
		// title and subtitle
		try {
			titleText = ImageIO.read(getClass().getResourceAsStream("/HUD/Level1.png"));
			title = new Title(titleText.getSubimage(0, 0, 120, 45));
			title.sety(60);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		title.begin();
		
		// teleport
		teleport = new Teleport(tileMap);
		teleport.setPosition(850, 225);
		
		// audio
		bgMusic = new AudioPlayer("/Music/level1-1.mp3");
		bgMusic.play();
		
	}
	
	
	private void populateEnemies() {
		
		enemies = new ArrayList<Enemy>();
		
		Skull sk;
		Slugger sl;
		Nightmare n;
		
		sk = new Skull(tileMap);
		sk.setPosition(220, 200);
		enemies.add(sk);
		
		sl = new Slugger(tileMap);
		sl.setPosition(860, 200);
		enemies.add(sl);
		
		n = new Nightmare(tileMap);
		n.setPosition(500, 200);
		enemies.add(n);
		
		numenemies = 3;
		kill = 0;
		boss = 0;
		
	}
	
	private void insertPotion() {
		
		potions = new ArrayList<HealingPotion>();
		HealingPotion p;
		
		Point[] points = new Point[] { new Point(160, 170) };
		for(int i = 0; i < points.length; i++)
		{
			p = new HealingPotion(tileMap);
			p.setPosition(points[i].x, points[i].y);
			potions.add(p);
		}
	}
	
	private void insertManaPotion() {
		
		mpotions = new ArrayList<ManaPotion>();
		ManaPotion p;
		
		Point[] points = new Point[] { new Point(1450, 200), new Point(380, 110) };
		for(int i = 0; i < points.length; i++) {
			p = new ManaPotion(tileMap);
			p.setPosition(points[i].x, points[i].y);
			mpotions.add(p);
		}
	}
	
	public void update() {
		
		// check Keys
		handleInput();
		
		// player is dead
		if(player.getHealth() == 0 || player.gety() > tileMap.getHeight()) {
			gsm.setState(GameStateManager.LOSESTATE);
			bgMusic.stop();
		}
		
		// move title and subtitle
		if(title != null) {
			title.update();
			if(title.shouldRemove())
				title = null;
		}
		
		// update player
		player.update();
		
		// camera on the player
		tileMap.setPosition(GamePanel.WIDTH / 2 - player.getx(), GamePanel.HEIGHT / 2 - player.gety());
		
		// set background
		bg.setPosition(tileMap.getx(), tileMap.gety());
		
		// attack enemies
		player.checkAttack(enemies);
		
		// check potions
		player.checkPotion(potions);
		player.checkManaPotion(mpotions);
		
		// update all enemies
		for(int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			e.update();
			if(e.isDead()) {
				enemies.remove(i);
				i--;
				explosions.add(new Explosion(e.getx(), e.gety()));
				kill++;
			}
		}
		
		// healing potion
		for(int i = 0; i < potions.size(); i++)	{
			HealingPotion p = potions.get(i);
			p.update();
			if(p.isTaken()) {
				potions.remove(i);
				i--;
			}
		}
				
		//mana potion
		for(int i = 0; i < mpotions.size(); i++) {
			ManaPotion p = mpotions.get(i);
			p.update();
			if(p.isTaken()) {
				mpotions.remove(i);
				i--;
			}
		}
		
		// update explosions
		for(int i = 0; i < explosions.size(); i++) {
			explosions.get(i).update();
			if(explosions.get(i).shouldRemove()) {
				explosions.remove(i);
				i--;
			}
		}
		
		// update teleport
		teleport.update();
		
		
		// check if end of level		
		if(teleport.contains(player)) {
			endTime = System.currentTimeMillis();
			time = (endTime - startTime) / 1000;
			gsm.setState(GameStateManager.LEVEL1SCORE);
			bgMusic.stop();
		}
		
	}
	
	public void draw(Graphics2D g) {
		
		bg.draw(g);
		
		tileMap.draw(g);
		
		player.draw(g);
		
		for(int i = 0; i < enemies.size(); i++) {
			enemies.get(i).draw(g);
		}
		
		for(int i = 0; i < explosions.size(); i++) {
			explosions.get(i).setMapPosition((int)tileMap.getx(), (int)tileMap.gety());
			explosions.get(i).draw(g);
		}
		
		teleport.draw(g);
		
		for(int i = 0; i < potions.size(); i++) {
			potions.get(i).draw(g);
		}
		
		for(int i = 0; i < mpotions.size(); i++) {
			mpotions.get(i).draw(g);
		}
		
		hud.draw(g);
		
		if(title != null) title.draw(g);
		
	}
	
	public void handleInput() {
		if(Keys.isPressed(Keys.ESCAPE))
			gsm.setPaused(true);
		player.setUp(Keys.keyState[Keys.UP]);
		player.setLeft(Keys.keyState[Keys.LEFT]);
		player.setDown(Keys.keyState[Keys.DOWN]);
		player.setRight(Keys.keyState[Keys.RIGHT]);
		player.setJumping(Keys.keyState[Keys.BUTTON1]);
		if(Keys.isPressed(Keys.BUTTON3)) player.setAttack();
		if(Keys.isPressed(Keys.BUTTON4)) player.setFiring();
		
	}
	
	public static int getEnem() { return numenemies; }
	public static int getKill() { return kill; }
	public static int getBoss() { return boss; }
	public static long getTime() { return time; }
	
}












