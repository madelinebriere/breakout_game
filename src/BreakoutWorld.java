import java.util.ArrayList;

import com.sun.javafx.tk.Toolkit.Task;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
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

/**
 * eliminate need for myBall, myPaddle and myBlocks??
 *
 */

public class BreakoutWorld {
 
	public static final Color COLOR_1= Color.ALICEBLUE;
	public static final Color COLOR_2=Color.LIGHTCYAN;
	public static final Color COLOR_3=Color.LIGHTBLUE;
	public static final Color COLOR_4=Color.LIGHTSKYBLUE;
	
	public static final int SIZE = 500;
	
	//Paddle power levels
    public static final int PORTAL_LEVEL = 2;//level where paddle is able to transport from one side to other
    public static final int SPEED_LEVEL = 3; //level where paddle is able to speed up as it moves in one direction
    
    //Starting game control values
    public static final int START_LIVES=1;
    public static final int START_POINTS=0;
    public static final int START_LEVEL=1;
    
    //Misc variables
    public static final int PADDLE_STEP=5;//step made by paddle each time left or right key is hit
    public static final int POINTS_AWARDED = 50;
    
    public static final int CAT_THRESH = 2000;
    
    /*All of the following number variables must be POSITIVE numbers*/
	private int myLives; //number of lives left
    private int myLevel; //must be number 1 - 4
    private int myTotal; //total score
    
    private ArrayList<Block> myBlocks = new ArrayList<Block>(); //blocks in game
    private Ball myBall; //decision made to use myBouncer instead of self-created Ball class
    private Paddle myPaddle;
    private InfoBox myBox;
    
    private int catTimer = 0; //keep track of when to drop cats
    
    //Key booleans to implement smooth motions
    private boolean leftHold=false; //is the left key being held down?
    private boolean rightHold=false; //is the right key being held down?
    
	/** The JavaFX Scene as the game surface */
    private Scene gameSurface;
    /** All nodes to be displayed in the game window. */
    private Group sceneNodes;
    /**Keep track of stage for popups*/
    private Stage stage;
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
                	if(myLives>0){generalUpdates();}
                }
        }); // oneFrame
 
        // sets the game world's game loop (Timeline)
        Timeline animation = new Timeline();
        animation.setCycleCount(Animation.INDEFINITE);
        animation.getKeyFrames().add(oneFrame);
        setGameLoop(animation);
    }
    
    public void generalUpdates(){
    	 if(myBall!=null & myPaddle!=null){//easy check to avoid any nullpointers
         	 updateBox();
         	// update actors
             updateSprites();
             // check for collision
             checkCollisions();
             //New level?
             checkLevel();
             //confirm that the paddle hasn't been replaced & doesn't need new image
             checkPaddle();
             //Add more cats
             checkCats();
             // removed dead things
             cleanupPieces();}
    }
 
    /**
     * Initialize the game world by update the JavaFX Stage.
     * @param primaryStage
     */
    public void initialize(final Stage primaryStage){
    	stage=primaryStage;
    	primaryStage.setTitle(getWindowTitle());
    	primaryStage.setAlwaysOnTop(true);
        //primaryStage.setFullScreen(true);
 
        // Create the scene
        setSceneNodes(new Group());
        setGameSurface(new Scene(getSceneNodes(), SIZE, SIZE));
        getGameSurface().setFill(COLOR_1);
        primaryStage.setScene(getGameSurface());
        
        //Initialize and add variables/pieces
    	setStartVars();
    	setLists();
    	addParts();
        
        // Setup Game input
        setupInput(primaryStage);
    }
    
    private void addParts(){
    	addPiece(myBall);
        addPiece(myPaddle);
        addPieces(myBlocks);
        addBox();
    }
    
    private void addPiece(GamePiece piece){
    	getGameManager().addPieces(piece);
        getSceneNodes().getChildren().add(piece.getMyImage());
    }
    
    private <T extends GamePiece> void addPieces(ArrayList<T> pieces){
    	for(GamePiece g: pieces){addPiece(g);}
    }
    
    private void removePiece(GamePiece piece){
    	getGameManager().addPiecesToBeRemoved(piece); //remove old ball and paddle
    	getSceneNodes().getChildren().remove(piece.getMyImage());
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
    	 myBlocks = BlockReader.readBlocks(myLevel); //blocks dependent on level
    	 Powerup.setPowers(myLevel, myBlocks); //apply powerups to blocks
    	 myBall = Ball.buildStartBall(SIZE, myLevel); //ball speed dependent on level
    	 myPaddle = Paddle.buildStartPaddle(SIZE); //paddle dependent upon size of frame
    }
 
    public InfoBox setBoxValues(){
    	InfoBox info = new InfoBox(SIZE);
    	info.setLevel(myLevel);
    	info.setLives(myLives);
    	info.setPoints(myTotal);
    	return info;
    	
    }
    
    public void addCat(){
    	Cat cat = Cat.buildCat(SIZE);
    	addPiece(cat);
    }
    
    public void addBox(){
    	myBox = setBoxValues();
    	getSceneNodes().getChildren().add(myBox.getMyBox());
    	getSceneNodes().getChildren().add(myBox.getMyText());
    }
    
    public void updateBox(){
    	if(myBox!=null && myBox.changed(myLevel, myTotal, myLives))//only update if something has changed
    	{
	    	getSceneNodes().getChildren().remove(myBox.getMyText());
	    	getSceneNodes().getChildren().remove(myBox.getMyBox());
	    	addBox();
    	}
    }
    
    public void setupInput(Stage stage)
    {
        stage.getScene().setOnKeyPressed(e -> handlePressKeyInput(e.getCode()));
        stage.getScene().setOnKeyReleased(e -> handleReleaseKeyInput(e.getCode()));
        
    }

	private void handlePressKeyInput(KeyCode code) {
		//Controls for very first paddle are right and left keys
    	//Room left to add additional keys for extra paddles if necessary, not currently implemented
		if (code == KeyCode.RIGHT) {//move paddle right
    		moveRight(myPaddle);
    		rightHold=true;
    		myPaddle.setMovingRight(true);
    		myPaddle.setMovingLeft(false);
    	}
    	if (code == KeyCode.LEFT) {//move paddle left
    		moveLeft(myPaddle);
    		leftHold=true;
    		myPaddle.setMovingRight(false);
    		myPaddle.setMovingLeft(true);
    	}
    	if (code == KeyCode.L){
    		myLives++;
    	}
    	if (code == KeyCode.P){
    		myTotal++;
    	}
    	if(code == KeyCode.R){
    		pieceReset();
    	}
    	if (code == KeyCode.DIGIT1){
    		levelUp(1);
    	}
    	if (code == KeyCode.DIGIT2){
    		levelUp(2);
    	}
    	if (code==KeyCode.DIGIT3){
    		levelUp(3);
    	}
    	if(code==KeyCode.DIGIT4){
    		levelUp(4);
    	}
	}
	
	/*
	 * Called when key is RELEASED
	 * Only relevant for left and right (so that paddle moves when you press down on a key)
	 */
	private void handleReleaseKeyInput(KeyCode code) {
		//Controls for very first paddle are right and left keys
    	//Room left to add additional keys for extra paddles if necessary, not currently implemented
    	if (code == KeyCode.RIGHT){
    		rightHold=false;
    	}
    		
        if(code == KeyCode.LEFT) {
        	leftHold=false;
        	
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
    
	public void handleFail(){
		finalMessage("You lose!");
	}
	public void handleWin(){
		finalMessage("You win!");
	}
	
	public void finalMessage(String string){
	    getSceneNodes().getChildren().clear();
	    getGameManager().removeAllPieces();
	    
	    Text text = new Text(3*SIZE/7, SIZE/2, string);
	    text.setFill(Color.DARKORCHID);
		text.setFont(Font.font("Cambria", 20));
		text.setScaleX(3);
		text.setScaleY(3);
	    getSceneNodes().getChildren().add(text);
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
                	 ((Ball)pieceA).blockBounce((Block)pieceB); //alter ball path
                	 if(((Block)pieceB).handleCollision()) {brickHit((Block)pieceB);}//if block is destroyed
                 }
                 if(pieceA instanceof Ball && pieceB instanceof Paddle){
                	 ((Ball)pieceA).paddleCheck((Paddle)pieceB);
                 }
                 if(pieceA instanceof Paddle && pieceB instanceof Powerup){
                	 powerup((Powerup)pieceB, (Paddle)pieceA);
                 }
                 if(pieceA instanceof Paddle && pieceB instanceof Cat){
                	 catHit((Cat)pieceB, (Paddle)pieceA);
                 }
                 return true; //collision has occurred
             }
         }
  
         return false;
    }
    public void catHit(Cat c, Paddle p){
    	myTotal-=20; //lose 20 points
    	removePiece(c);
    }
    
    public void powerup(Powerup p, Paddle pad){
    	if(p.getMyType().equals("pointpower.gif")){myTotal+=POINTS_AWARDED;}
    	if(p.getMyType().equals("lifepower.gif")){myLives++;}
    	if(p.getMyType().equals("paddlepower.gif")){myPaddle.setReset(true);}
    	removePiece(p);
    }
    
    public void checkCats(){
    	if(catTimer*myLevel >= CAT_THRESH){//math dictates that more cats show up per level
    		addCat();
    		catTimer=0;
    	}
    	else{
    		catTimer++;
    	}
    }
    
    public void checkPaddle(){
    	removePiece(myPaddle); //remove to replace with new paddle
    	if(myPaddle.isReset()){ // if paddlePower picked up
        	if(myPaddle.isMovingRight()){myPaddle=Paddle.buildLongRight(myPaddle);}
        	else{myPaddle=Paddle.buildLongLeft(myPaddle);}
    	}
    	else{ //reset image to correct direction
    		if(myPaddle.isMovingLeft()){myPaddle=Paddle.buildLeft(myPaddle);}
        	else{myPaddle=Paddle.buildRight(myPaddle);}
    	}
    	addPiece(myPaddle);
    }

    public void brickHit(Block b){
    	 myTotal+=(b).getMyPoints(); //update total
		 removePiece(b);//remove block from scene
    	 myBlocks.remove(b); // remove block from list
    	 if((b).getMyPower()!=null){//if the block holds a powerup
    		 addPiece(b.getMyPower());
    		 //add powerup to board
    	 }
    }
    
    public void resetVars(){
    	myPaddle.setReset(false);
    	myPaddle.setMovingLeft(true);
        myPaddle.setMovingRight(false);
        leftHold=false;
        rightHold=false;
        catTimer=0;
    }
    
    public void levelFail(){
    	myLives--;
    	if(myLives==0){
    		handleFail();
    	}
    	else{
    		resetVars();
    		pieceReset();
    	}
    }
    
    public void pieceReset(){
    	removePiece(myBall);
    	removePiece(myPaddle);
    	myBall = Ball.buildStartBall(SIZE,myLevel);
    	myPaddle = Paddle.buildStartPaddle(SIZE);
    	addPiece(myBall);
    	addPiece(myPaddle);
    	
    }

    public void checkLevel(){
    	if(numBlocks()==0){//if no more blocks, this level has been cleared
    		levelUp(myLevel+1);
    	}
    	if(myBall.ballBelowPaddle(myPaddle)){//if ball falls below paddle, level has been lost
    		levelFail();
    	}
    }
    
    private int numBlocks(){
    	int numBlocks=0;
    	if(myTotal==0){return 1;}//no points made, no blocks could be removed
    	if(!(myBlocks.size()==0))//if there are no blocks and it is not before initialization
    	{
	    	for(Block b: myBlocks) {
	    		if(b!=null && !b.getClass().getName().equals("ConcreteBlock")){numBlocks++;}
	    	}
    	}
    	return numBlocks;
    }
    
    private void levelUp(int level){
    	if(level>=5){handleWin();}
    	else{
	    	resetVars();
	    	myLevel=level;
	    	setColor(level);
			gameManager.removeAllPieces();
			getSceneNodes().getChildren().clear();
	    	setLists();//reset lists
	    	addParts(); //add items to screen
    	}
    }
    
    private void setColor(int level){
    	Scene set = getGameSurface();
    	switch(level){
    	case 1:
    		set.setFill(COLOR_1);
    		break;
    	case 2:
    		set.setFill(COLOR_2);
    		break;
    	case 3:
    		set.setFill(COLOR_3);
    		break;
    	case 4:
    		set.setFill(COLOR_4);
    		break;
    		
    	}
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
    	if(piece instanceof Paddle){
    		if(leftHold) {moveLeft((Paddle)piece);}
    		if(rightHold) {moveRight((Paddle)piece);}
    	}
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