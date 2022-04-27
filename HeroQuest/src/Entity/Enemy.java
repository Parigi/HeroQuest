package Entity;

import TileMap.TileMap;

public class Enemy extends MapObject {
	
	protected int health;
	protected int maxHealth;
	protected boolean dead;
	protected boolean boss;
	protected boolean removeB;
	protected int damage;		//contact damage to the player
	
	protected boolean flinching;
	protected long flinchTimer;
	
	//contructor 
	public Enemy(TileMap tm) {
		super(tm);
	}
	
	public boolean isDead() { return dead; }
	public void checkPlayer(Player player) {}
	public int getDamage() { return damage; }
	public boolean isBoss() { return boss; }
	public boolean shouldRemoveB() { return removeB; }
	
	
	public void hit(int damage) {
		if(dead || flinching) return;	//if it's dead or flinching it can't get hit
		health -= damage;
		if(health < 0) health = 0;
		if(health == 0) dead = true;
		flinching = true;
		flinchTimer = System.nanoTime();
	}
	
	public void update() {}
	
}














