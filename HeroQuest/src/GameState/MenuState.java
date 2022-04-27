package GameState;

import TileMap.Background;
import java.awt.*;
import Handlers.Keys;

/**
 * disegno il menu, possibilità di muoversi
 *
 */
public class MenuState extends GameState {
	
	private Background bg;
	
	private int currentChoice = 0;
	private String[] options = {
		"Start",
		"Help",
		"Exit"
	};
	
	private Font titleFont;
	
	private Font font;
	
	// constructor
	public MenuState(GameStateManager gsm) {
		

		super(gsm);
		
		try {
			
			bg = new Background("/Backgrounds/sfondo.gif", 1); 	
			bg.setVector(0, 0); 								 
			
			titleFont = new Font("Century Gothic", Font.PLAIN, 32);			
			font = new Font("Arial", Font.PLAIN, 15);
			
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
		
		g.setColor(Color.WHITE);
		g.setFont(titleFont);
		g.drawString("HeroQuest", 80, 70);
		
		// draw menu options
		g.setFont(font);
		for(int i = 0; i < options.length; i++) { 
			if(i == currentChoice) {		
				g.setColor(Color.RED);
			}
			else {
				g.setColor(Color.WHITE);		
			}
			g.drawString(options[i], 83, 140 + i * 20);
		}
		
	}
	
	private void select() {
		if(currentChoice == 0) {
			gsm.setState(GameStateManager.LEVEL1STATE);
		}
		if(currentChoice == 1) {
			gsm.setState(GameStateManager.HELPSTATE);
		}
		if(currentChoice == 2) {
			System.exit(0);		
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










