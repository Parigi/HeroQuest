package GameState;

import TileMap.Background;
import java.awt.*;

import Handlers.Keys;

public class HelpState extends GameState {
	
	private Background bg;
	private Color color;
	private Font font1;
	private Font font2;
	
	public HelpState(GameStateManager gsm) {
		
		super(gsm);
		try {
			
			bg = new Background("/Backgrounds/sfondo.gif", 1);
			bg.setVector(0, 0);
			
			font1 = new Font("Arial", Font.PLAIN, 18);
			font2 = new Font("Arial", Font.PLAIN, 12);
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	
	}
	
	public void init() {}
	
	public void update() {
		bg.update();
		handleInput();
	}
	
	public void draw(Graphics2D g) {
		
		bg.draw(g);
		
		g.setColor(color);
		g.setFont(font1);
		g.drawString("COMANDI", 110, 90);
		
		g.setFont(font2);
		g.drawString("FIREBALL: F", 120, 140);
		g.drawString("MELEE: R", 120, 160);
		g.drawString("JUMP: W", 120, 180);
		g.drawString("MOVE: A (left) D (right)", 120, 200);
		g.drawString("Press Esc for the Menu", 10, 10);
		
		
	}
	
	public void handleInput() {
		if(Keys.isPressed(Keys.ESCAPE))
			gsm.setState(GameStateManager.MENUSTATE);
	}
	
}
