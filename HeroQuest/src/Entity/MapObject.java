package Entity;

import Main.GamePanel;
import TileMap.TileMap;
import TileMap.Tile;

import java.awt.Rectangle;

public abstract class MapObject {
	
	// tile stuff
	protected TileMap tileMap;
	protected int tileSize;
	protected double xmap;
	protected double ymap;
	
	// position and vector
	protected double x;
	protected double y;
	protected double dx;	
	protected double dy;
	
	// dimensions
	protected int width;
	protected int height;
	
	// collision box 
	protected int cwidth;	
	protected int cheight;
	
	// collision
	protected int currRow;
	protected int currCol;
	protected double xdest;
	protected double ydest;
	protected double xtemp;	
	protected double ytemp;
	protected boolean topLeft;
	protected boolean topRight;
	protected boolean bottomLeft;
	protected boolean bottomRight;
	
	// animation
	protected Animation animation;
	protected int currentAction;	//to tell us which animation are you currently using
	protected int previousAction;	
	protected boolean facingRight;	//if it's false we have to flip the sprite 
	
	// movement
	protected boolean left;		//is going left	
	protected boolean right;	//is going right
	protected boolean up;		//....
	protected boolean down;
	protected boolean jumping;
	protected boolean falling;
	
	// movement attributes
	protected double moveSpeed;		//how fast the object accelerate
	protected double maxSpeed;		//how fast the object can go
	protected double stopSpeed;		//deceleration speed
	protected double fallSpeed;		//like gravity
	protected double maxFallSpeed;
	protected double jumpStart;		//how high the object can jump
	protected double stopJumpSpeed;	//longer press higher the jump
	
	// constructor
	//basically set the tile map and get the tile size
	public MapObject(TileMap tm) {
		tileMap = tm;
		tileSize = tm.getTileSize(); 
	}
	
	public boolean intersects(MapObject o) {
		Rectangle r1 = getRectangle();		
		Rectangle r2 = o.getRectangle();	
		return r1.intersects(r2);
	}
	
	public boolean contains(MapObject o) {
		Rectangle r1 = getRectangle();
		Rectangle r2 = o.getRectangle();
		return r1.contains(r2);
	}
	
	// (x,y) = angolo alto sinistro del rettangolo, + (lunghezza, altezza)
	public Rectangle getRectangle() {
		return new Rectangle((int)x - cwidth, (int)y - cheight,	cwidth,	cheight);
	}
	
	//need for check TileMapCollision
	public void calculateCorners(double x, double y) {
		
		int leftTile = (int)(x - cwidth / 2) / tileSize;
		// minus one so we don't step over into the next column
		int rightTile = (int)(x + cwidth / 2 - 1) / tileSize;
		int topTile = (int)(y - cheight / 2) / tileSize;
		// minus one because we don't want to go downwards into the next tile
		int bottomTile = (int)(y + cheight / 2 - 1) / tileSize;
		
		
		//AGGIUNTO IN CASO DI CONTATTO COL FONDO == MORTE
		if(topTile < 0 || bottomTile >= tileMap.getNumRows() ||
				leftTile < 0 || rightTile >= tileMap.getNumCols()) {
				topLeft = topRight = bottomLeft = bottomRight = false;
				return;
			}
		
		int tl = tileMap.getType(topTile, leftTile);		//topleft
		int tr = tileMap.getType(topTile, rightTile);		//topright
		int bl = tileMap.getType(bottomTile, leftTile);		//bottomleft
		int br = tileMap.getType(bottomTile, rightTile);	//bottomright
		
		//valgono 1 se sono BLOCKED, 0 altrimenti
		topLeft = tl == Tile.BLOCKED;	
		topRight = tr == Tile.BLOCKED;
		bottomLeft = bl == Tile.BLOCKED;
		bottomRight = br == Tile.BLOCKED;
		
	}
	
	//ha incontrato un oggetto solido?
	//sta cadendoo è sulla mappa?
	public void checkTileMapCollision() {
		
		currCol = (int)x / tileSize;
		currRow = (int)y / tileSize;
		
		// next position of the object
		xdest = x + dx;		
		ydest = y + dy;
		
		xtemp = x;
		ytemp = y;
		
		calculateCorners(x, ydest);
		if(dy < 0) {			//we are going upwards
			if(topLeft || topRight) {		//so we check the top two corners (are they BLOCKED?)
				dy = 0;	//se metto diverso da 0 imprime un'accelerazione verso il basso
				ytemp = currRow * tileSize + cheight / 2;	//we have to set the object JUST BELOW the tile that it just hit
			}
			else {
				ytemp += dy;	//otherwise we are free to keep going upwards
			}
		}
		if(dy > 0) {		//we are going downwards
			if(bottomLeft || bottomRight) {		//so we check the bottom two corners 
				dy = 0;
				falling = false;
				ytemp = (currRow + 1) * tileSize - cheight / 2;
			}
			else {
				ytemp += dy;	//otherwise we are free to keep falling
			}
		}
		
		calculateCorners(xdest, y);
		if(dx < 0) {		//we are going left
			if(topLeft || bottomLeft) {		//so we check the left corners
				dx = 0;
				xtemp = currCol * tileSize + cwidth / 2;
			}
			else {
				xtemp += dx;
			}
		}
		if(dx > 0) {		//we are going right
			if(topRight || bottomRight) {		//so we check the two right corners
				dx = 0;				
				xtemp = (currCol + 1) * tileSize - cwidth / 2;
			}
			else {
				xtemp += dx;
			}
		}
		
		if(!falling) {		//we have to check the ground while going
			calculateCorners(x, ydest + 1);
			if(!bottomLeft && !bottomRight) {
				falling = true;
			}
		}
		
	}
	
	public int getx() { return (int)x; }
	public int gety() { return (int)y; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public int getCWidth() { return cwidth; }
	public int getCHeight() { return cheight; }
	
	//GLOBAL position
	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}
	public void setVector(double dx, double dy) {
		this.dx = dx;		//se aumento non accelera... perché?
		this.dy = dy;
	}
	
	//where to actually draw the character
	//how far the tile map has moved in order offset the player back onto the screen
	public void setMapPosition() {
		xmap = tileMap.getx();		//se lo aumento lo disegna un po' più a destra rispetto a dove dovrebbe essere
		ymap = tileMap.gety();
	}
	
	//LOCAL POSITION : (x + xmap, y + ymap)
	
	public void setLeft(boolean b) { left = b; }
	public void setRight(boolean b) { right = b; }
	public void setUp(boolean b) { up = b; }
	public void setDown(boolean b) { down = b; }
	public void setJumping(boolean b) { jumping = b; }
	
	//the object is on the screen? 
	//because we want to draw only the objects on the screen
	public boolean notOnScreen() {
		return x + xmap + width < 0 ||
			x + xmap - width > GamePanel.WIDTH ||
			y + ymap + height < 0 || 
			y + ymap - height > GamePanel.HEIGHT;
	}
	
	public void draw(java.awt.Graphics2D g) {
		if(facingRight) {
			g.drawImage(animation.getImage(), (int)(x + xmap - width / 2), (int)(y + ymap - height / 2), null);
		}
		else {		//flip the sprite
			g.drawImage(animation.getImage(), (int)(x + xmap - width / 2 + width), (int)(y + ymap - height / 2), -width, height, null);
		}
	}
	
}
















