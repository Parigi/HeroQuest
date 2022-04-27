package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import Handlers.Keys;
import Main.GamePanel;

public class Level1Score extends GameState{
		
	private Color titleColor;
	private Font titleFont;
		
	private Font font;
	private Font font2;
		
	public Level1Score(GameStateManager gsm) {
			
		super(gsm);
			
		try {
			titleColor = Color.WHITE;
			titleFont = new Font("Times New Roman", Font.PLAIN, 28);
			font = new Font("Arial", Font.PLAIN, 14);
			font2 = new Font("Arial", Font.PLAIN, 10);
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
		
		g.setColor(titleColor);
		g.setFont(titleFont);
		g.drawString("LEVEL COMPLETE!", 40, 90);
			
		g.setFont(font);
		g.setColor(Color.WHITE);
		g.drawString("Enemies Killed: " + Level1State.getKill() + "/" + Level1State.getEnem(), 70, 165);
		g.drawString("Boss Killed: " + Level1State.getBoss() + "/" + Level1State.getBoss(), 70, 185);
		
		g.drawString("Time:  " + Level1State.getTime() + "  seconds", 70, 205);
			
		g.setFont(font2);
		g.drawString("Enter for the next level or Esc for the Menu", 50, 220);
	}
		
	public void handleInput() {
		if(Keys.isPressed(Keys.ENTER))
			gsm.setState(GameStateManager.LEVEL2STATE);
		if(Keys.isPressed(Keys.ESCAPE))
			gsm.setState(GameStateManager.MENUSTATE);
	}
		
		
}


