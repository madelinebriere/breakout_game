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
    public static final Paint BACKGROUND = Color.FLORALWHITE;
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
    private Ball myBall; //decision made to use myBouncer instead of self-created Ball class
    private Paddle myPaddle;
    
    
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
    	 myBlocks = BlockReader.readBlocks(myLevel);
    	 myBall = Ball.buildStartBall();
    	 myPaddle = Paddle.buildStartPaddle();
    }
    
    public void setScene(int width, int height, Paint background)
    {
    	// create one top level collection to organize the things in the scene
        Group root = new Group();
        myScene = new Scene(root, width, height, background);// create a place to see the shapes
        
        //Add images to root in scene
        Block.addImages(myBlocks, root);
    	Ball.addImage(myBall, root);
    	Paddle.addImage(myPaddle, root);
    }

    
    // Change properties of shapes to animate them 
    // Note, there are more sophisticated ways to animate shapes, but these simple ways work fine to start.
    private void step (double elapsedTime) {
    	handleBall(); //move ball and react to any obstacles
    	handleSprites();
    }

    
    private void handleBall(){
       	//Update location based on x and y motion
    	//Must be in this method to avoid elapsedTime error
    	myBall.update();
	    	
	    //Check impact
	    myBall.paddleCheck(myPaddle); //check if ball has hit any paddles
	    myBall.brickCheck(myBlocks); //check if ball has hit any bricks
	    myBall.wallCheck(SIZE); //check if ball has hit any walls
    }
    
    private void handleSprites(){
    	checkBlocks(); //remove any hit blocks
    	checkBall(); //confirm the ball hasn't been removed from play
    	checkLevel();
    }
    
    private void checkBlocks(){
    	ArrayList<Block> toRemove = new ArrayList<Block>();
    	for(Block b: myBlocks){
    		if (b.isDead()) {b.getMyImage().setVisible(false);}
    	}
    	myBlocks.removeAll(toRemove);
    }
    
    private void checkBall(){
    	if(myBall.isDead()) {handleLife();}
    }
    
    private void checkLevel(){
    	if(numBlocks()==0){
    		levelUp();
    	}
    }
    
    private int numBlocks(){
    	int numBlocks=0;
    	for(Block b: myBlocks) {
    		if(!b.getClass().equals("ConcreteBlock")){numBlocks++;}
    	}
    	return numBlocks;
    }
    
    private void levelUp(){
    	myLevel++;
    }
    
    // What to do each time a key is pressed
    private void handleKeyInput (KeyCode code) {
    	//Controls for very first paddle are right and left keys
    	//Room left to add additional keys for extra paddles if necessary, not currently implemented
    	if (code == KeyCode.RIGHT) {//move paddle right
    		moveRight(myPaddle);
    	}
    	if (code == KeyCode.LEFT) {//move paddle left
    		moveLeft(myPaddle);
    	}
    	
    }
    
    public void moveRight(Paddle p)
    {
    	if(p.getX()+p.getWidth()<SIZE-PADDLE_STEP)
			p.setX(p.getX()+PADDLE_STEP);
		else if (myLevel>=PORTAL_LEVEL) //paddle portal on level 2
			p.setX(0);
    	//Otherwise paddle cannot move
    }
    
    public void moveLeft(Paddle p)
    {
    	if(p.getX()>PADDLE_STEP)
			p.setX(p.getX()-PADDLE_STEP);
		else if(myLevel>=PORTAL_LEVEL) //paddle portal on level 2
			p.setX(SIZE-p.getWidth());
    	//Otherwise paddle cannot move
    }

    // What to do each time a key is pressed
    private void handleMouseInput (double x, double y) {
   
    }
    
    //What to do when you fail
    private void handleLife() {
    	myLives--;
    	if(myLives>0) //if still in game, reset ball position
    	{
    		myBall=Ball.buildStartBall(); //reset balls
    		System.out.println("FAIL");
    	}
    	
    }

    /**
     * Start the program.
     */
    public static void main (String[] args) {
        launch(args);
    }
    
    
    
}