import java.awt.Font;
import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Main class for Breakout game
 * @author Maddie Briere 
 * Alterations made to explore possibility of using for game
 * 
 * Original version -- lab_bounce:
 * A basic example JavaFX program for the first lab.
 * Robert C. Duvall
 * 
 */
public class BreakoutMain extends Application {
    //General set-up variables
	public static final String TITLE = "Breakout Game";
    public static final Paint BACKGROUND = Color.WHITE;
    public static final int FRAMES_PER_SECOND = 60;
    public static final int SIZE = 600;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    public static final int KEY_INPUT_SPEED = 5;

    //Paddle power levels
    public static final int PORTAL_LEVEL = 2;//level where paddle is able to transport from one side to other
    public static final int SPEED_LEVEL = 3; //level where paddle is able to speed up as it moves in one direction
    
    //Starting game control values
    public static final int START_LIVES=3;
    public static final int START_POINTS=0;
    public static final int START_LEVEL=1;
    
    //Where to write points
    public static final int TEXT_X=20;
    public static final int TEXT_Y=SIZE-20;
    
    //Misc variables
    public static final int PADDLE_STEP=10;//step made by paddle each time left or right key is hit
 
    // some things we need to remember during our game
    private Scene myScene;
    private Text myPoints;
    
    /*All of the following number variables must be POSITIVE numbers*/
	private int myLives; //number of lives left
    private int myLevel; //must be number 1 - 4
    private int myTotal; //total score
    
    private ArrayList<Block> myBlocks; //blocks in game
    private ArrayList <Ball> myBalls; //decision made to use myBouncer instead of self-created Ball class
    private ArrayList<Paddle> myPaddles;
    
    /*
     * TRY TO GET RID OF PICS ARRAYLISTS
     */
	
    /**
     * 
     * RESTRUCTURE WITH GAME CLASS?
     * 
     * GET RID OF EXTRA VARIABLES IN GAMEPIECE CLASSES (X)
     * --> INCORPORATE IMAGEVIEW INFORMATION?
     * 
     * EITHER REFER TO PREVIOUS CODE OR LOOK UP TUTORIAL
     * 
     * CHANGE VARIABLE NAMES TO ADD MY
     */
    
    
    /**
     * Initialize what will be displayed and how it will be updated.
     */
    @Override
    public void start (Stage s) {
        // attach scene to the stage and display it
        Scene scene = setupGame(SIZE, SIZE, BACKGROUND);
        s.setScene(scene);
        s.setTitle(TITLE);
        s.setResizable(false); //to avoid complications due to resizing
        s.show();
        // attach "game loop" to timeline to play it
        KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY),
                                      e -> step(SECOND_DELAY));
        Timeline animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
    }

    // Create the game's "scene": what shapes will be in the game and their starting properties
    private Scene setupGame (int width, int height, Paint background) {
    
    	//set up variables
    	setStartVars();
    	setStartLists();
    	setScene(width, height, background);
        
        /*
        //Count points
        points = new Text(TEXT_X, TEXT_Y,""+totalPoints);
        points.setFill(Color.RED);
        root.getChildren().add(points);*/
        
        // respond to input
        myScene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        myScene.setOnMouseClicked(e -> handleMouseInput(e.getX(), e.getY()));
        return myScene;
    }
  
    private void setStartVars()
    {
    	//Miscellaneous variables
    	myLives=START_LIVES;//start with 3 lives
    	myLevel=START_LEVEL; //start on level 1
    	myTotal=START_POINTS;//start with no points
    }
    
    public void setStartLists()
    {
    	 myBlocks = Block.readBlocks(myLevel);
    	 myBalls = Ball.buildBalls();
    	 myPaddles = Paddle.buildPaddles();
    }
    
    public void setScene(int width, int height, Paint background)
    {
    	// create one top level collection to organize the things in the scene
        Group root = new Group();
        myScene = new Scene(root, width, height, background);// create a place to see the shapes
        
        //Add images to root in scene
        Block.addImages(myBlocks, root);
    	Ball.addImages(myBalls, root);
    	Paddle.addImages(myPaddles, root);
    }

    // Change properties of shapes to animate them 
    // Note, there are more sophisticated ways to animate shapes, but these simple ways work fine to start.
    private void step (double elapsedTime) {
    	//Update location based on x and y motion
    	//Must be in this method to avoid elapsedTime error
    	for(Ball b: myBalls)
    	{
	    	ImageView gameBall = b.getMyImage();
	    	int xMov = b.getMyXMov();
	    	int yMov = b.getMyYMov();
	    	
	    	//Move ball according to motion
    		if (xMov==1){gameBall.setX(gameBall.getX() + b.getMySpeed()* elapsedTime);} //move right
	    	if (xMov==-1){gameBall.setX(gameBall.getX() - b.getMySpeed()* elapsedTime);} //move left
	    	if (yMov==1){gameBall.setY(gameBall.getY() - b.getMySpeed()* elapsedTime);} //move up
	    	if (yMov==-1){gameBall.setY(gameBall.getY() + b.getMySpeed()* elapsedTime);} //move down
	    	
	    	//Check impacts
	    	wallCheck(b); //check if ball has hit any walls
	    	paddleCheck(b); //check if ball has hit any paddles
	    	brickCheck(b); //check if ball has hit any bricks
    	}
    }

    /*
     * Check if ball has hit any bricks
     */
    private void brickCheck(Ball b)
    {
	    	
    	/*	//Boundary logic --> check for blocks
    		
    		ArrayList<Block> toRemove = new ArrayList(); //used to avoid concurrentModificationException
	    	for(Block block: myBlocks)
	    	{
	        	*//**
	        	 * Check for actual overlap of ball on brick
	        	 *//*
	    		ImageView blockImage = block.getMyImage();
	    		
	    		double blx = blockImage.getX();
	        	double bly = blockImage.getY(); //block x & y
	        	double blw = blockImage.getBoundsInLocal().getWidth();//block width
	        	double blh = blockImage.getBoundsInLocal().getHeight();//block height
	        	
	        	int hits = block.getMyHits();
	    		if(hits>=1 && blockImage.intersects(b.getMyImage().getBoundsInLocal())) {block.setMyHits(block.getMyHits()-1);}
	    		else if (blockImage.intersects(b.getMyImage().getBoundsInLocal()))
	    		{
	    			myTotal+=block.getMyPoints();
	    			toRemove.add(block);
	    		}
	    		
	    	}
	    	
	    	for(Block block: toRemove) 
	    	{
	    		myBlocks.remove(block);
	    	}*/
    }
    
    private void wallCheck(Ball b)
    {
    	
	    	ImageView gameBall = b.getMyImage();
	    	
	    	//ball attributes
	    	double bx = gameBall.getX();
	    	double by = gameBall.getY();
	    	double width = gameBall.getBoundsInLocal().getWidth();
	    	double height = gameBall.getBoundsInLocal().getHeight();
	    	
	    	//Boundary logic --> check for walls
	    	if(bx<0){b.setMyXMov(1);}//if hits left wall, bounce back
	    	if(bx+width>SIZE){b.setMyXMov(-1);}//if hits right wall, bounce back
	    	if(by<0){b.setMyYMov(-1);} //if hits top, bounce back down
	    	if(by+height>SIZE){handleFail();} // if passes through bottom, game over
    	
    }
    
    private void paddleCheck(Ball b)
    {
    	for(Paddle p: myPaddles)
	    {
		   	switch (ballHitsPaddle(b,p)) //rebound ball if 1,2 or 3 (impact)
		   	{
	    		case 1: 
	    			b.setMyXMov(-1);
		   			break;
		   		case 2:
		   			b.setMyXMov(0);
		   			break;
		   		case 3:
		   			b.setMyXMov(1);
		   			break;
		   	}
	    }
    	
    }
    
    // What to do each time a key is pressed
    private void handleKeyInput (KeyCode code) {
    	//Controls for very first paddle are right and left keys
    	//Room left to add additional keys for extra paddles if necessary, not currently implemented
    	Paddle paddle = myPaddles.get(0);
    	if (code == KeyCode.RIGHT) {//move paddle right
    		moveRight(paddle);
    	}
    	if (code == KeyCode.LEFT) {//move paddle left
    		moveLeft(paddle);
    	}
    	
    	//TROUBLESHOOT
    	/**
    	if(code == KeyCode.UP && ballHitsPaddle()!=0)//Catch ball
    	{
    		xMov=0;
    		yMov=0;
    		upPress=true;
    	}
    	if(code == KeyCode.DOWN && upPress) //press down after having caught 
    	{
    		yMov=1;
    		upPress=false;
    	}*/
    	
    }
    
    public void moveLeft(Paddle p)
    {
    	ImageView paddlePic = p.getMyImage();
    	if(paddlePic.getX()+paddlePic.getBoundsInLocal().getWidth()<SIZE)
			paddlePic.setX(paddlePic.getX()+PADDLE_STEP);
		else if (myLevel>=PORTAL_LEVEL) //paddle portal on level 2
			paddlePic.setX(0);
    	//Otherwise paddle cannot move
    }
    
    public void moveRight(Paddle p)
    {
    	ImageView paddlePic = p.getMyImage();
    	if(paddlePic.getX()>0)
			paddlePic.setX(paddlePic.getX()-PADDLE_STEP);
		else if(myLevel>=PORTAL_LEVEL) //paddle portal on level 2
			paddlePic.setX(SIZE-paddlePic.getBoundsInLocal().getWidth());
    	//Otherwise paddle cannot move
    }

    // What to do each time a key is pressed
    private void handleMouseInput (double x, double y) {
   
    }
    
    //What to do when you fail
    private void handleFail() {
    	myLives--;
    	if(myLives>0) //if still in game, reset ball position
    	{
    		myBalls=Ball.buildBalls(); //reset balls
    	}
    	
    }

    /**
     * 
     * @param ball
     * @return
     */
    
    /*
     * Determines if and where ball has hit paddle
     * Returns: 0 - not hit, 1 - left hit, 2 - middle hit, 3 - right hit
     */
    private int ballHitsPaddle(Ball ball, Paddle paddle)
    {
    	ImageView gameBall = ball.getMyImage();
    	ImageView gamePaddle = paddle.getMyImage();
    	
    	//needed ball attributes
    	double bx = gameBall.getX();
    	
    	//paddle attributes
    	double px = gamePaddle.getX();
    	double py = gamePaddle.getY(); //paddle's x & y pos
    	double pw = gamePaddle.getBoundsInLocal().getWidth();//paddle width
    	double ph = gamePaddle.getBoundsInLocal().getHeight();//paddle height
    	
    	if(gameBall.intersects(px, py, pw, ph)) //ball hits paddle
    	{
    		ball.setMyYMov(1); //ball moves up no matter what
    		if(bx<(px+pw/3)) {return 1;} //ball hits left side, bounce up and to left
    		else if(bx<(px+(2*pw/3))){return 2;} //ball hits middle, go straight up
    		else if(bx<(px+pw)){return 3;}//ball hits left, bounce up and to right
    		
    	}
    	return 0; //default meaning no impact
    	
    }

    /**
     * Start the program.
     */
    public static void main (String[] args) {
        launch(args);
    }
    
    
    
}