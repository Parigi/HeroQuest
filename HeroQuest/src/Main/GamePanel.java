package Main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;

import GameState.GameStateManager;
import Handlers.Keys;

/**
 * - Dimensioni del JPanel
 * - Avvio del game loop
 * - Game State Manager
 *
 */

//@SuppressWarnings("serial")
public class GamePanel extends JPanel implements Runnable, KeyListener {		
	
	// dimensions
	public static final int WIDTH = 364;	
	public static final int HEIGHT = 240;	
	public static final int SCALE = 3;		
	
	// game thread
	private Thread thread;	
	private boolean running;
	private int FPS = 60;
	private long targetTime = 1000 / FPS;		//16.67 
	
	// image
	private BufferedImage image;
	private Graphics2D g;
	
	// game state manager
	private GameStateManager gsm;
	
		
	// constructor
	public GamePanel() {
		super(); 
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE)); 	// JPanel dimensions
		setFocusable(true);	//enable keys
		requestFocus();
	}
	
	public void addNotify() {		
		super.addNotify();
		if(thread == null) {
			thread = new Thread(this);	
			addKeyListener(this);
			thread.start();
		}
	}
	
	
	private void init() {
		
		image = new BufferedImage(WIDTH , HEIGHT, BufferedImage.TYPE_INT_RGB); 
		g = (Graphics2D) image.getGraphics();
		
		running = true;	
		
		gsm = new GameStateManager();	//start gsm
		
	}
	
	
	public void run() {
		
		init();
		
		long start;
		long elapsed;
		long wait;
		
		// game loop
		while(running) {
			
			start = System.nanoTime();
			
			update();
			draw();
			drawToScreen();
			
			elapsed = System.nanoTime() - start;	
			
			wait = targetTime - elapsed / 1000000;	
			if(wait < 0) wait = 5;					
			
			try {
				Thread.sleep(wait);					
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	private void update() {
		gsm.update();	
		Keys.update();
	}
	private void draw() {
		gsm.draw(g);	
	}
	private void drawToScreen() {
		Graphics g2 = getGraphics();	
		g2.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);	
		g2.dispose();
			
	}
	
	public void keyTyped(KeyEvent key) {}
	public void keyPressed(KeyEvent key) {	
		Keys.keySet(key.getKeyCode(), true);
	}
	
	public void keyReleased(KeyEvent key) {
		Keys.keySet(key.getKeyCode(), false);
	}
	
}
















