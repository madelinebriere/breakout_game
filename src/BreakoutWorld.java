import java.util.ArrayList;

import com.sun.prism.paint.Color;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
 
/**
 * This application demonstrates a JavaFX 2.x Game Loop.
 * Shown below are the methods which comprise of the fundamentals to a
 * simple game loop in JavaFX:
*
 *  <strong>initialize()</strong> - Initialize the game world.
 *  <strong>beginGameLoop()</strong> - Creates a JavaFX Timeline object containing the game life cycle.
 *  <strong>updateSprites()</strong> - Updates the sprite objects each period (per frame)
 *  <strong>checkCollisions()</strong> - Method will determine objects that collide with each other.
 *  <strong>cleanupSprites()</strong> - Any sprite objects needing to be removed from play.
 *
 * @author cdea
 */


public class BreakoutWorld {
 
	public static final int SIZE = 600;
	
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
	
	/** The JavaFX Scene as the game surface */
    private Scene gameSurface;
    /** All nodes to be displayed in the game window. */
    private Group sceneNodes;
    /** The game loop using JavaFX's <code>Timeline</code> API.*/
    private static Timeline gameLoop;
    /** Number of frames per second. */
    private final int framesPerSecond;
    /** Title in the application window.*/
    private final String windowTitle;
 
    /**
     * The sprite manager.
     */
    private final GameManager gameManager = new GameManager();
 
    /**
     * Constructor that is called by the derived class. This will
     * set the frames per second, title, and setup the game loop.
     * @param fps - Frames per second.
     * @param title - Title of the application window.
     */
    public BreakoutWorld(final int fps, final String title) {
        framesPerSecond = fps;
        windowTitle = title;
        // create and set timeline for the game loop
        buildAndSetGameLoop();
    }
 
    /**
     * Builds and sets the game loop ready to be started.
     */
    protected final void buildAndSetGameLoop() {
 
        final Duration oneFrameAmt = Duration.millis(1000/getFramesPerSecond());
        final KeyFrame oneFrame = new KeyFrame(oneFrameAmt,
            new EventHandler<ActionEvent>() {
 
                @Override
                public void handle(javafx.event.ActionEvent event) {
 
                    // update actors
                    updateSprites();
 
                    // check for collision
                    checkCollisions();
 
                    //New level?
                    checkLevel();
                    
                    // removed dead things
                    cleanupPieces();
 
                }
        }); // oneFrame
 
        // sets the game world's game loop (Timeline)
        Timeline animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(oneFrame);
        animation.play();
        setGameLoop(animation);
    }
 
    /**
     * Initialize the game world by update the JavaFX Stage.
     * @param primaryStage
     */
    public void initialize(final Stage primaryStage){
    	//Initialize variables
    	setStartVars();
    	setLists();
    	
    	primaryStage.setTitle(getWindowTitle());
        //primaryStage.setFullScreen(true);
 
        // Create the scene
        setSceneNodes(new Group());
        setGameSurface(new Scene(getSceneNodes(), SIZE, SIZE));
        primaryStage.setScene(getGameSurface());
        
        // Setup Game input
        setupInput(primaryStage);
        addParts();
    }
    
    private void addParts(){
    	getGameManager().addPieces(myBall);
        getSceneNodes().getChildren().add(myBall.getMyImage());
        
        getGameManager().addPieces(myPaddle);
        getSceneNodes().getChildren().add(myPaddle.getMyImage());
        
        for(Block b: myBlocks)
        {
        	getGameManager().addPieces(b);
        	getSceneNodes().getChildren().add(b.getMyImage());
        }
    }
    
    private void setStartVars()
    {
    	//Miscellaneous variables
    	myLives=START_LIVES;//start with 3 lives
    	myLevel=START_LEVEL; //start on level 1
    	myTotal=START_POINTS;//start with no points
    }
    

    public void setLists()
    {
    	 myBlocks = BlockReader.readBlocks(myLevel);
    	 myBall = Ball.buildStartBall();
    	 myPaddle = Paddle.buildStartPaddle(SIZE);
    }
 
    
    public void setupInput(Stage stage)
    {
        // respond to input
        stage.getScene().setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        stage.getScene().setOnMouseClicked(e -> handleMouseInput(e.getX(), e.getY()));
        
    }
    private Object handleMouseInput(double x, double y) {
		// TODO Auto-generated method stub
		return null;
	}

	private void handleKeyInput(KeyCode code) {
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

    public void checkLevel(){
    	if(numBlocks()==0){
    		myLevel++;
    		gameManager.removeAllPieces();
    		getSceneNodes().getChildren().clear();
        	setLists();
        	addParts();
    	}
    	if(myBall.ballBelowPaddle(myPaddle)){levelReset();}
    }
    
    private int numBlocks(){
    	int numBlocks=0;
    	if(!myBlocks.isEmpty())
    	{
	    	for(Block b: myBlocks) {
	    		if(b!=null && !b.getClass().getName().equals("ConcreteBlock")){numBlocks++;}
	    	}
    	}
    	return numBlocks;
    }
    
	/**Kicks off (plays) the Timeline objects containing one key frame
     * that simply runs indefinitely with each frame invoking a method
     * to update sprite objects, check for collisions, and cleanup sprite
     * objects.
     *
     */
    public void beginGameLoop() {
        getGameLoop().play();
    }
 
    /**
     * Updates each game sprite in the game world. This method will
     * loop through each sprite and passing it to the handleUpdate()
     * method. The derived class should override handleUpdate() method.
     *
     */
    protected void updateSprites() {
        for (GamePiece piece: gameManager.getAllPieces()){
            handleUpdate(piece);
        }
    }
 
    /** Updates the sprite object's information to position on the game surface.
     * @param sprite - The sprite to update.
     */
    protected void handleUpdate(GamePiece piece) {
    	piece.update();
    	if(piece instanceof Ball){
    		((Ball)piece).wallCheck(SIZE);//if the ball has fallen off the screen
    	}
    }
    
    public void levelReset(){
    	gameManager.removePieces(myBall,myPaddle); //remove old ball and paddle
    	getSceneNodes().getChildren().remove(myBall.getMyImage());
    	getSceneNodes().getChildren().remove(myPaddle.getMyImage());
    	myLives--;
    	if(myLives==0){
    		handleFail();
    	}
    	else{
    		myBall = Ball.buildStartBall();
    		myPaddle = Paddle.buildStartPaddle(SIZE);
    		gameManager.addPieces(myBall,myPaddle);
    		getSceneNodes().getChildren().add(myBall.getMyImage());
    		getSceneNodes().getChildren().add(myPaddle.getMyImage());
    		
    	}
    	
    }
    
    public void handleFail(){
    	System.out.println("Failure");
    }
 
    /**
     * Checks each game sprite in the game world to determine a collision
     * occurred. The method will loop through each sprite and
     * passing it to the handleCollision()
     * method. The derived class should override handleCollision() method.
     *
     */
    protected void checkCollisions() {
        // check other sprite's collisions
        gameManager.resetCollisionsToCheck();
        // check each sprite against other sprite objects.
        for (GamePiece pieceA: gameManager.getCollisionsToCheck()){
            for (GamePiece pieceB: gameManager.getAllPieces()){
                if (handleCollision(pieceA, pieceB)) {
                    // The break helps optimize the collisions
                    //  The break statement means one object only hits another
                    // object as opposed to one hitting many objects.
                    // To be more accurate comment out the break statement.
                    break;
                }
            }
        }
    }
 
    /**
     * When two objects collide this method can handle the passed in sprite
     * objects. By default it returns false, meaning the objects do not
     * collide.
     * @param spriteA - called from checkCollision() method to be compared.
     * @param spriteB - called from checkCollision() method to be compared.
     * @return boolean True if the objects collided, otherwise false.
     */
    protected boolean handleCollision(GamePiece pieceA, GamePiece pieceB) {
    	 if (pieceA != pieceB) {
             if (pieceA.collide(pieceB)) {
                 if (pieceA instanceof Ball && pieceB instanceof Block) {
                	 if(((Block)pieceB).handleCollision()) //if the block is destroyed
                	 {
	                	 getGameManager().addPiecesToBeRemoved(pieceB);
	                	 getSceneNodes().getChildren().remove(pieceB.getMyImage()); //remove block
	                	 myBlocks.remove(pieceB);
                	 }
                     return true; //collision has occurred
                 }
                 if(pieceA instanceof Ball && pieceB instanceof Paddle){
                	 ((Ball)pieceA).paddleCheck((Paddle)pieceB);
                	 return true;
                 }
             }
         }
  
         return false;
    }
 
    /**
     * Sprites to be cleaned up.
     */
    protected void cleanupPieces() {
        gameManager.cleanupPieces();
    }
 
    /**
     * Returns the frames per second.
     * @return int The frames per second.
     */
    protected int getFramesPerSecond() {
        return framesPerSecond;
    }
 
    /**
     * Returns the game's window title.
     * @return String The game's window title.
     */
    public String getWindowTitle() {
        return windowTitle;
    }
 
    /**
     * The game loop (Timeline) which is used to update, check collisions, and
     * cleanup sprite objects at every interval (fps).
     * @return Timeline An animation running indefinitely representing the game
     * loop.
     */
    protected static Timeline getGameLoop() {
        return gameLoop;
    }
 
    /**
     * The sets the current game loop for this game world.
     * @param gameLoop Timeline object of an animation running indefinitely
     * representing the game loop.
     */
    protected static void setGameLoop(Timeline gameLoop) {
        BreakoutWorld.gameLoop = gameLoop;
    }
 
    /**
     * Returns the sprite manager containing the sprite objects to
     * manipulate in the game.
     * @return SpriteManager The sprite manager.
     */
    protected GameManager getGameManager() {
        return gameManager;
    }
 
    /**
     * Returns the JavaFX Scene. This is called the game surface to
     * allow the developer to add JavaFX Node objects onto the Scene.
     * @return
     */
    public Scene getGameSurface() {
        return gameSurface;
    }
 
    /**
     * Sets the JavaFX Scene. This is called the game surface to
     * allow the developer to add JavaFX Node objects onto the Scene.
     * @param gameSurface The main game surface (JavaFX Scene).
     */
    protected void setGameSurface(Scene gameSurface) {
        this.gameSurface = gameSurface;
    }
 
    /**
     * All JavaFX nodes which are rendered onto the game surface(Scene) is
     * a JavaFX Group object.
     * @return Group The root containing many child nodes to be displayed into
     * the Scene area.
     */
    public Group getSceneNodes() {
        return sceneNodes;
    }
 
    /**
     * Sets the JavaFX Group that will hold all JavaFX nodes which are rendered
     * onto the game surface(Scene) is a JavaFX Group object.
     * @param sceneNodes The root container having many children nodes
     * to be displayed into the Scene area.
     */
    protected void setSceneNodes(Group sceneNodes) {
        this.sceneNodes = sceneNodes;
    }
 
}