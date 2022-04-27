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

public class Level2State extends GameState {
	
	private TileMap tileMap;
	private Background bg;	
	
	private Player player;
	
	private ArrayList<Enemy> enemies;
	private ArrayList<Explosion> explosions;
	
	private HUD hud;
	private BufferedImage titleText;
	private Title title;
	private Teleport teleport;

	private BufferedImage bossText;
	private Title titleBoss;
	private boolean played;
	
	private AudioPlayer bgMusic;
	
	private static boolean bossDead;
	private static int numenemies;
	private static int bosses;
	private static int kill;
	private static long startTime;
	private static long endTime;
	private static long time;
	

	public Level2State(GameStateManager gsm) {
		super(gsm);
		init();
	}
	
	public void init() {
		
		// map
		tileMap = new TileMap(30);
		tileMap.loadTiles("/Tilesets/grasstileset.gif");
		tileMap.loadMap("/Maps/mappa.txt");
		tileMap.setPosition(0, 0);
		tileMap.setTween(1);	
				
		// bacgkround
		bg = new Background("/Backgrounds/sfondo.gif", 0.1); //movescale ms = 0.1
		
		// player
		player = new Player(tileMap);
		player.setPosition(80, 140);
		
		// enemies
		populateEnemies();
		bossDead = false;
	
		// explosions
		explosions = new ArrayList<Explosion>();
		
		// hud
		hud = new HUD(player);
		startTime = System.currentTimeMillis();
		
		// title
		try {
			titleText = ImageIO.read(getClass().getResourceAsStream("/HUD/Level2.png"));
			title = new Title(titleText.getSubimage(0, 0, 184, 52));
			title.sety(60);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		title.begin();
		
		// teleport
		teleport = new Teleport(tileMap);
		teleport.setPosition(1000, 225); 
		
		try {
			bossText = ImageIO.read(getClass().getResourceAsStream("/HUD/TitleSkeleton.png"));
			titleBoss = new Title(bossText.getSubimage(0, 0, 112, 46));
			titleBoss.sety(60);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		played = false;		
		
		bgMusic = new AudioPlayer("/Music/level1-1.mp3");
		bgMusic.play();
		
	}
	
	
	private void populateEnemies() {
		
		enemies = new ArrayList<Enemy>();
		
		Skeleton s;
		Skull sk;
		
		Point[] points = new Point[] {
			new Point(200, 210),
			new Point(980, 140)		
		};
		
		for(int i = 0; i < points.length; i++) {
	
			if(i == points.length - 1) {
				s = new Skeleton(tileMap);
				s.setPosition(points[i].x, points[i].y);
				enemies.add(s);
				continue;
			}
			
			sk = new Skull(tileMap);
			sk.setPosition(points[i].x, points[i].y);
			enemies.add(sk);
		}
		
		numenemies = 1;
		kill = 0;
		bosses = 1;
		
	}
	
	public void update() {
		
		// check keys
		handleInput();
		
		// player is dead
		if(player.getHealth() == 0 || player.gety() > tileMap.getHeight()) {
			gsm.setState(GameStateManager.LOSESTATE);
		}
			
		// move title and subtitle
		if(title != null) {
			title.update();
			if(title.shouldRemove())
				title = null;
		}
		
		// boss title ? 
		if(player.getx() == 900 && played == false) {
			titleBoss.begin();
			played = true;
		}	
		
		
		if(titleBoss != null && played == true) {
			titleBoss.update();
			if(titleBoss.shouldRemove())
				titleBoss = null;
		}
		
		player.update();
		
		// camera on the player
		tileMap.setPosition(GamePanel.WIDTH / 2 - player.getx(), GamePanel.HEIGHT / 2 - player.gety());
		
		// set background
		bg.setPosition(tileMap.getx(), tileMap.gety());
		
		// attack enemies
		player.checkAttack(enemies);
		
		// update all enemies
		for(int i = 0; i < enemies.size(); i++) {
			
			Enemy e = enemies.get(i);
			e.checkPlayer(player);
			
			e.update();
			
			if(e.isDead() && !(e.isBoss()) ) {
				enemies.remove(i);
				i--;
				explosions.add(new Explosion(e.getx(), e.gety()));
			}
			
			if(e.isDead() && e.isBoss() && e.shouldRemoveB()) {
				bossDead = true;
				enemies.remove(i);
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
		
		teleport.update();
		
		// check if end of level
		if(teleport.contains(player) && bossDead == true) {
			endTime = System.currentTimeMillis();
			time = (endTime - startTime) / 1000;
			gsm.setState(GameStateManager.LEVEL2SCORE);
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
		
		if(bossDead == true) {
			teleport.draw(g);
		}
	
		hud.draw(g);
	
		if(title != null) title.draw(g);
		
		if(titleBoss != null && played == true) titleBoss.draw(g);		
		
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
	public static int getBoss() { return bosses; }
	public static long getTime() { return time; }
	
	
}
