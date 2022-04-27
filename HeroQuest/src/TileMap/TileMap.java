package TileMap;

import java.awt.*;
import java.awt.image.*;

import java.io.*;
import javax.imageio.ImageIO;

import Main.GamePanel;

/**
 * disegnare tile e mappa
 * disegniamo solo parte della mappa
 *
 */

public class TileMap {
	
	// position
	private double x;
	private double y;
	
	// bounds
	private int xmin;
	private int ymin;
	private int xmax;
	private int ymax;
	
	private double tween; 
	
	// map
	private int[][] map;
	private int tileSize;
	private int numRows;
	private int numCols;
	private int width;
	private int height;
	
	// tileset
	private BufferedImage tileset;
	private int numTilesAcross;
	private Tile[][] tiles;
	
	// drawing, disegniamo solo una parte della mappa ogni volta, quella presente sullo schermo
	private int rowOffset;	 //tell us which row to start drawing
	private int colOffset;
	private int numRowsToDraw;	//how many rows to draw
	private int numColsToDraw;
	
	// constructor
	public TileMap(int tileSize) {
		this.tileSize = tileSize;
		numRowsToDraw = GamePanel.HEIGHT / tileSize + 2;	//HEIGHT = 240
		numColsToDraw = GamePanel.WIDTH / tileSize + 2;		//WIDTH = 320
		tween = 0.07;  
	}
	
	// individuiamo tutti i Tiles presenti nella gif e li definiamo o come NORMAL o come BLOCKED
	public void loadTiles(String s) {
		
		try {

			tileset = ImageIO.read(getClass().getResourceAsStream(s));
			numTilesAcross = tileset.getWidth() / tileSize;
			tiles = new Tile[4][numTilesAcross];
			
			BufferedImage subimage;
			for(int col = 0; col < numTilesAcross; col++) {
				
				subimage = tileset.getSubimage(col * tileSize, 0, tileSize, tileSize);
				tiles[0][col] = new Tile(subimage, Tile.NORMAL);
			}
			
			BufferedImage ts1= ImageIO.read(getClass().getResourceAsStream("/Tilesets/ts1.png"));
			tiles[1][0]=new Tile(ts1,Tile.BLOCKED);
			BufferedImage ts2= ImageIO.read(getClass().getResourceAsStream("/Tilesets/ts2.png"));
			tiles[1][1]=new Tile(ts2,Tile.BLOCKED);
			BufferedImage ts3= ImageIO.read(getClass().getResourceAsStream("/Tilesets/ts3.png"));
			tiles[1][2]=new Tile(ts3,Tile.BLOCKED);
			BufferedImage ts4= ImageIO.read(getClass().getResourceAsStream("/Tilesets/ts4.png"));
			tiles[1][3]=new Tile(ts4,Tile.BLOCKED);
			BufferedImage ts5= ImageIO.read(getClass().getResourceAsStream("/Tilesets/ts5.png"));
			tiles[1][4]=new Tile(ts5,Tile.BLOCKED);
			BufferedImage ts6= ImageIO.read(getClass().getResourceAsStream("/Tilesets/ts6.png"));
			tiles[1][5]=new Tile(ts6,Tile.BLOCKED);
			BufferedImage ts7= ImageIO.read(getClass().getResourceAsStream("/Tilesets/ts7.png"));
			tiles[1][6]=new Tile(ts7,Tile.BLOCKED);
			BufferedImage ts8= ImageIO.read(getClass().getResourceAsStream("/Tilesets/ts8.png"));
			tiles[1][7]=new Tile(ts8,Tile.BLOCKED);
			BufferedImage ts9= ImageIO.read(getClass().getResourceAsStream("/Tilesets/ts9.png"));
			tiles[1][8]=new Tile(ts9,Tile.BLOCKED);
			BufferedImage ts10= ImageIO.read(getClass().getResourceAsStream("/Tilesets/ts10.png"));
			tiles[1][9]=new Tile(ts10,Tile.BLOCKED);
			BufferedImage ts11= ImageIO.read(getClass().getResourceAsStream("/Tilesets/ts11.png"));
			tiles[1][10]=new Tile(ts11,Tile.BLOCKED);
			BufferedImage ts12= ImageIO.read(getClass().getResourceAsStream("/Tilesets/ts12.png"));
			tiles[1][11]=new Tile(ts12,Tile.BLOCKED);
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	//BufferedReader per leggere da un file di caratteri
	public void loadMap(String s) {
		
		try {
			
			//load of map file
			InputStream in = getClass().getResourceAsStream(s);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			numCols = Integer.parseInt(br.readLine());	//first line
			numRows = Integer.parseInt(br.readLine());	//second line
			map = new int[numRows][numCols];
			width = numCols * tileSize;
			height = numRows * tileSize;
			
			xmin = GamePanel.WIDTH - width;
			xmax = 0;
			ymin = GamePanel.HEIGHT - height;
			ymax = 0;
			
			String delims = "\\s+";	//white space
			for(int row = 0; row < numRows; row++) {
				String line = br.readLine();
				String[] tokens = line.split(delims); 
				for(int col = 0; col < numCols; col++) {
					map[row][col] = Integer.parseInt(tokens[col]);
				}
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public int getTileSize() { return tileSize; }
	public double getx() { return x; }
	public double gety() { return y; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public int getNumRows() { return numRows; }
	public int getNumCols() { return numCols; }
	
	public int getType(int row, int col) {
		int rc = map[row][col];
		int r = rc / numTilesAcross;
		int c = rc % numTilesAcross;
		return tiles[r][c].getType();
	}
	
	public void setTween(double d) { tween = d; } 
	
	public void setPosition(double x, double y) {
		
		this.x += (x - this.x) * tween;
		this.y += (y - this.y) * tween;
		
		fixBounds();
		
		colOffset = (int)-this.x / tileSize;
		rowOffset = (int)-this.y / tileSize;
		
	}
	
	private void fixBounds() {
		if(x < xmin) x = xmin;
		if(y < ymin) y = ymin;
		if(x > xmax) x = xmax;
		if(y > ymax) y = ymax;
	}
	
	public void draw(Graphics2D g) {
		
		for(int row = rowOffset; row < rowOffset + numRowsToDraw; row++) {
			
			if(row >= numRows) break;
			
			for(int col = colOffset; col < colOffset + numColsToDraw; col++) {
				
				if(col >= numCols) break;
				
				if(map[row][col] == 0) continue;
				
				int rc = map[row][col];
				int r = rc / numTilesAcross;
				int c = rc % numTilesAcross; 
				
				g.drawImage(tiles[r][c].getImage(), (int)x + col * tileSize, (int)y + row * tileSize, null);
				
			}
			
		}
		
	}
	
}



















