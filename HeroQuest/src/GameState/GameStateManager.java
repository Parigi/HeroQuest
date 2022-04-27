package GameState;

import java.awt.Color;
import Main.GamePanel;

/**
 * Gestire la transizione degli stati (unload e load)
 *
 */

public class GameStateManager {
	
	private GameState[] gameStates;
	private int currentState;
	private static int prevState;
	
	private PauseState pauseState;
	private boolean paused;
	
	public static final int NUMGAMESTATES = 8;
	public static final int MENUSTATE = 0;
	public static final int HELPSTATE = 1;
	public static final int LEVEL1STATE = 2;
	public static final int LEVEL1SCORE = 3;
	public static final int LEVEL2STATE = 4;
	public static final int LEVEL2SCORE = 5;
	public static final int LOSESTATE = 7;
	
	//Constructor
	public GameStateManager() {
		
		gameStates = new GameState[NUMGAMESTATES];
		
		pauseState = new PauseState(this);
		paused = false;
		
		currentState = MENUSTATE;
		loadState(currentState);
		
	}
	
	private void loadState(int state) {
		if(state == MENUSTATE)
			gameStates[state] = new MenuState(this);
		if(state == HELPSTATE)
			gameStates[state] = new HelpState(this);
		if(state == LEVEL1STATE)
			gameStates[state] = new Level1State(this);
		if(state == LEVEL1SCORE)
			gameStates[state] = new Level1Score(this);
		if(state == LEVEL2STATE)
			gameStates[state] = new Level2State(this);
		if(state == LEVEL2SCORE)
			gameStates[state] = new Level2Score(this);
		if(state == LOSESTATE)
			gameStates[state] = new LoseState(this);
	}
	
	private void unloadState(int state) {
		gameStates[state] = null;
	}
	
	// per cambiare state
	public void setState(int state) {
		prevState = currentState;
		unloadState(currentState);
		currentState = state;
		loadState(currentState);
	}
	
	public void setPaused(boolean b) { paused = b; }
	public int getPrev() { return prevState; }
	
	public void update() {
		
		if(paused) {
			pauseState.update();
			return;
		}		
		if(gameStates[currentState] != null) 
			gameStates[currentState].update();
	}
	
	public void draw(java.awt.Graphics2D g) {
		
		if(paused) {
			pauseState.draw(g);
			return;
		}
		
		if(gameStates[currentState] != null) 
			gameStates[currentState].draw(g);
		else {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		}
	}
	
}









