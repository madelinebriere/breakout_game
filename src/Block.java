import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class Block extends GamePiece{


	public static final int BLOCK_POINTS=10;
	//Brick grid variables
    public static final double BRICK_ROW_INC = 30; //distance between rows
    public static final double BRICK_COL_INC = 85; // distance between cols
	
	protected int myHits; //how many 
	protected int myPoints;

	public Block(double row, double col, String type) {
		super(col*BRICK_COL_INC, row*BRICK_ROW_INC, type);
	}
	
	public abstract boolean handleCollision(); 
	//Each try of block must react to a collision in a unique way
	//Changes hits based on the type of block it is (e.g., if concrete, no change)
	//returns whether its destroyed or not
	
	
	@Override
	public void update() {
		//No movement necessary for blocks
	}

    public int getMyHits() {
		return myHits;
	}


	public void setMyHits(int myHits) {
		this.myHits = myHits;
	}
	
	public int getMyPoints() {
		return myPoints;
	}


	public void setMyPoints(int myPoints) {
		this.myPoints = myPoints;
	}


}
