package Entity;

import java.awt.image.BufferedImage;

public class Animation {
	
	private BufferedImage[] frames;	//array to hold all the frames
	private int currentFrame;
	
	private long startTime;	//timer between frames
	private long delay;		//how long between each frame
	
	private boolean playedOnce;	
	
	//Constructor
	public Animation() {
		playedOnce = false;
	}
	
	public void setFrames(BufferedImage[] frames) {
		this.frames = frames;
		currentFrame = 0;
		startTime = System.nanoTime();
		playedOnce = false;
	}
	
	public void setDelay(long d) { delay = d; }
	public void setFrame(int i) { currentFrame = i; }
	
	public void update() {
		
		if(delay == -1)		//mean there is no animation
			return;
		
		//how long it's been since the last frame came up
		long elapsed = (System.nanoTime() - startTime) / 1000000;
		
		//we have to move to next frame
		if(elapsed > delay) {			
			currentFrame++;
			startTime = System.nanoTime();
		}
		if(currentFrame == frames.length) {
			currentFrame = 0;
			playedOnce = true;
		}
		
	}
	
	//get the current frame number
	public int getFrame() { return currentFrame; }
	//get the image that we need to draw
	public BufferedImage getImage() { return frames[currentFrame]; }
	//the animation has played or not
	public boolean hasPlayedOnce() { return playedOnce; }
	
}
















