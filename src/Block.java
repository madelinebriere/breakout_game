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
    public static final double BRICK_ROW_INC = 25; //distance between rows
    public static final double BRICK_COL_INC = 70; // distance between cols
	
	protected int myHits; //how many 
	protected int myPoints;
	protected Powerup myPower;

	public Block(double row, double col, String type) {
		super(col*BRICK_COL_INC, row*BRICK_ROW_INC, type);
		myPower=null;
	}
	
	
	public abstract boolean isDestroyed(); 
	//Each try of block must react to a collision in a unique way
	//Changes hits based on the type of block it is (e.g., if concrete, no change)
	//returns whether its destroyed or not
	
	
	@Override
	public void update(int size, int level) {
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
	
	public Powerup getMyPower() {
		return myPower;
	}

	public void setMyPower(Powerup myPower) {
		this.myPower = myPower;
	}


}
