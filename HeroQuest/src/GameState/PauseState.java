package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import Handlers.Keys;
import Main.GamePanel;

public class PauseState extends GameState{

	private Font font;
	
	public PauseState(GameStateManager gsm) {
		super(gsm);
		font = new Font("Century Gothic", Font.PLAIN, 14);
	}
	
	public void init() {} 
	
	public void update() {

		handleInput();
	}
	
	public void draw(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		g.setColor(Color.WHITE);
		g.setFont(font);
		g.drawString("PAUSE", 90, 90);
		g.drawString("W for the menu", 10, 10);
	}
	
	public void handleInput() {
		if(Keys.isPressed(Keys.ESCAPE))
			gsm.setPaused(false);
		if(Keys.isPressed(Keys.BUTTON1)) {
			gsm.setPaused(false);
			gsm.setState(GameStateManager.MENUSTATE);
		}
	}
	

	
	
}
