package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import TileMap.TileMap;

public class HealingPotion extends MapObject {
	private BufferedImage[] potion;
	protected boolean taken;
	private int heal;
	public HealingPotion (TileMap tm) {
	
	
	super(tm);
	width = 21;
	height = 21;
	cwidth = 6;
	cheight = 6;
	heal=1;
	taken=false;
	
	try {
		BufferedImage sp1 = ImageIO.read(getClass().getResourceAsStream("/Potion/healingpotion.png"));
		potion=new BufferedImage[1];
		potion[0] = sp1.getSubimage(0,0,width,height);
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
	animation = new Animation();
	animation.setFrames(potion);
	animation.setDelay(300);
	}
	
	
	public boolean isTaken()
	{
		return taken;
	}
	
	public void setTaken(boolean taken)
	{
		this.taken=taken;
		
	}
	
	public void update()
	{
		animation.update();
	}
	
	public void draw(Graphics2D g) {
		
		//if(notOnScreen()) return;
		
		setMapPosition();
		
		super.draw(g);
		
	}
	
	public int getHeal()
	{
		return heal;
	}
}
