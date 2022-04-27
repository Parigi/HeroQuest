package GameState;

import Handlers.Keys;
import Main.GamePanel;

import java.awt.*;

public class LoseState extends GameState {
	
	private Font font;
	private Font font2;
	private int prevState;
	
	private int currentChoice = 0;
	private String[] options = {
			"Retry!",
			"Menu"
	};
	
	public LoseState(GameStateManager gsm) {
		super(gsm);
		prevState = gsm.getPrev();
		try {
			font = new Font("Arial", Font.PLAIN, 18);
			font2 = new Font("Arial", Font.PLAIN, 12);
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
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
		g.drawString("...GAME OVER...", 100, 90);
		g.drawString("Retry?", 100, 110);
		
		g.setFont(font2);
		for(int i = 0; i < options.length; i++) {  
			if(i == currentChoice) {		
				g.setColor(Color.RED);
			}
			else {
				g.setColor(Color.WHITE);		
			}
			g.drawString(options[i], 100, 180 + i * 20);
		}
		
	}
	
	private void select() {
		if(currentChoice == 0) {
			gsm.setState(prevState);
		}
		if(currentChoice == 1) {
			gsm.setState(GameStateManager.MENUSTATE);
		}
	}
	
	public void handleInput() {
		if(Keys.isPressed(Keys.ENTER))
			select();
		
		if(Keys.isPressed(Keys.UP)) {
			if(currentChoice > 0) 
				currentChoice--;			
		}
		
		if(Keys.isPressed(Keys.DOWN)) {
			if(currentChoice < options.length - 1) {
				currentChoice++;
			}
		}
	}

}
