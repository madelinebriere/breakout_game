import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Block extends GamePiece{

	public static final int BLOCK_POINTS=10;
	//Brick grid variables
    public static final double BRICK_ROW_INC = 30; //distance between rows
    public static final double BRICK_COL_INC = 85; // distance between cols
	/*
	 * Block types:
	 * normal
	 * strong -double hit
	 * point - double points
	 * concrete - not breakable
	 * POWER UPS: Drops power up when destroyed
	 * 	question - answer guess
	 * 	laser - laser beam
	 * 	paddle - larger paddle
	 * 
	 */
	private int myHits;
	private int myPoints;
	

	public Block (int row, int col, String type)
	{
		super(col*BRICK_COL_INC, row*BRICK_ROW_INC, type);
		//Assign variables unique to Block
		if (type.equals("strong.gif")){myHits=2;} //allow for two hits
		else {myHits=1;}//all other blocks
		if(type.equals("double.gif")){myPoints=2*BLOCK_POINTS;}
		else{myPoints=BLOCK_POINTS;}
	}


	public static ArrayList<Block> readBlocks(int level)
    {
    	
    	File file= new File("");
    	ArrayList<Block> toRet = new ArrayList<Block>();
    	
    	if(level==1){file = new File("src/blocks1.txt");}
    	if(level==2){file = new File("src/blocks2.txt");}
    	if(level==3){file = new File("src/blocks3.txt");}
    	if(level==4){file = new File("src/blocks4.txt");}
    	
    	Scanner scan = new Scanner("");
		try {scan = new Scanner(file);} 
		catch (FileNotFoundException e) {System.out.println("Scanner file not found");}
    	
    	int line=0;
    	while(scan.hasNextLine())
    	{
    		String next = scan.nextLine();
    		String [] row = next.split(" ");
    		for(int i=0; i<row.length; i++)
    		{
    			//create new block with type as define, row=line and column=i
    			toRet.add(new Block(line+1, i+1, row[i]));
    		}
    		line++;
    	}
    	return toRet;
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
