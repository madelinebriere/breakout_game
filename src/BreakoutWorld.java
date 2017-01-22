import java.util.ArrayList; 
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
 * Basic gameloop application, implementing an adaptation of Breakout
 * Based on code by Carl Dea (cdea)
 */

public class BreakoutWorld {
 
	public static final Color COLOR_1= Color.ALICEBLUE;
	public static final Color COLOR_2=Color.LIGHTCYAN;
	public static final Color COLOR_3=Color.LIGHTBLUE;
	public static final Color COLOR_4=Color.LIGHTSKYBLUE;
	
	public static final int SIZE = 500;
    
    //Starting game control values
    public static final int START_LIVES=3;
    public static final int START_POINTS=0;
    public static final int START_LEVEL=1;
    public static final int LAST_LEVEL=4;
    
    //Misc variables
    public static final int POINTS_AWARDED = 50;
    public static final int CAT_THRESH = 2000;
    
    /*All of the following number variables must be POSITIVE numbers*/
	private int myLives; //number of lives left
    private int myLevel; //must be number 1 - 4
    private int myTotal; //total score
    
    //private ArrayList<Block> myBlocks = new ArrayList<Block>(); //blocks in game
    private Ball myBall; //decision made to use myBouncer instead of self-created Ball class
    private Paddle myPaddle;
    private InfoBox myBox;
    private Cat myHeadCat;
    
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
             updateSprites();
             checkCollisions();//update based on collisions
             checkLevel();//New level/ game-over?
             checkPaddle();//confirm that the paddle hasn't been replaced & doesn't need new image
             checkCats(); //Add more cats if needed
             cleanupPieces();// removed dead things
         }
    }
 
    /**
     * Initialize the game world by update the JavaFX Stage.
     * @param primaryStage
     */
    public void initialize(final Stage primaryStage){
    	stage=primaryStage;
    	primaryStage.setTitle(getWindowTitle());
    	primaryStage.setAlwaysOnTop(true);
 
        // Create the scene
        setSceneNodes(new Group());
        setGameSurface(new Scene(getSceneNodes(), SIZE, SIZE));
        getGameSurface().setFill(COLOR_1);
        primaryStage.setScene(getGameSurface());
        
        //Initialize and add variables/pieces
    	setStartVars();
    	setStartParts();
        
        // Setup Game input
        setupInput(primaryStage);
    }
    
    public void setStartParts()
    {
    	 ArrayList<Block> myBlocks = BlockReader.readBlocks(myLevel); //blocks dependent on level
    	 Powerup.setPowers(myLevel, myBlocks); //apply powerups to blocks
    	 addPieces(myBlocks);
    	 addPiece(myBall=Ball.buildStartBall(SIZE, myLevel)); //ball speed dependent on level
    	 addPiece(myPaddle=Paddle.buildStartPaddle(SIZE)); //paddle dependent upon size of frame
    	 myHeadCat=addCat();
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
 
    public InfoBox setBoxValues(){
    	InfoBox info = new InfoBox(SIZE);
    	info.setLevel(myLevel);
    	info.setLives(myLives);
    	info.setPoints(myTotal);
    	return info;
    }
    
    public Cat addCat(){
    	Cat cat = Cat.buildCat(SIZE);
    	addPiece(cat);
    	return cat;
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
    		myPaddle.setRightMoveToDefault();
    		myPaddle.setLeftMove(0);
    	}
    	if (code == KeyCode.LEFT) {//move paddle left
    		myPaddle.setLeftMoveToDefault();
    		myPaddle.setRightMove(0);
    	}
    	
    	if (code == KeyCode.L){myLives++;}
    	if (code == KeyCode.P){myTotal++;}
    	if(code == KeyCode.R){levelReset();}
    	if(code == KeyCode.N){gameReset();}
    	
    	if (code == KeyCode.DIGIT1){levelUp(1);}
    	if (code == KeyCode.DIGIT2){levelUp(2);}
    	if (code==KeyCode.DIGIT3){levelUp(3);}
    	if(code==KeyCode.DIGIT4){levelUp(4);}
	}
	
	/*
	 * Called when key is RELEASED
	 * Only relevant for left and right (so that paddle moves when you press down on a key)
	 */
	private void handleReleaseKeyInput(KeyCode code) {
		//Controls for very first paddle are right and left keys
    	//Room left to add additional keys for extra paddles if necessary, not currently implemented
    	if (code == KeyCode.RIGHT){myPaddle.setRightMove(0);}
        if (code == KeyCode.LEFT) {myPaddle.setLeftMove(0);}
	}
    
	
    public void checkCats(){
    	if(myHeadCat.timeToAttack(myLevel)){//math dictates that more cats show up per level
    		myHeadCat = addCat();
    	}
    }
    
    public void checkPaddle(){
    	if(myPaddle.getLeftMove()>0 || myPaddle.getRightMove()>0){//if the paddle is moving, update
	    	removePiece(myPaddle); //remove to replace with new paddle
	    	myPaddle = myPaddle.resetImage(); //reset image
	    	addPiece(myPaddle); //add back
    	}
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
                	 ((Ball)pieceA).bounceOffBlock((Block)pieceB); //alter ball path
                	 takeBlockDamage((Block)pieceB); //update block with damage
                 }
                 if(pieceA instanceof Ball && pieceB instanceof Paddle){
                	 ((Ball)pieceA).bounceOffPaddle((Paddle)pieceB); //alter ball path
                 }
                 if(pieceA instanceof Paddle && pieceB instanceof Powerup){
                	 applyPowerup((Powerup)pieceB);
                 }
                 if(pieceA instanceof Paddle && pieceB instanceof Cat){
                	 takeCatDamage((Cat)pieceB); //respond to successful nyan cat attack
                 }
                 return true; //collision has occurred
             }
         }
    	 
         return false;
    }
    
    public void takeBlockDamage(Block block){
    	 block.takeHit();
    	 if(block.isDestroyed()) {removeBlock(block);}//if block is destroyed
    }
    
    public void takeCatDamage(Cat c){
    	myTotal-=20; //lose 20 points
    	removePiece(c);
    }
    
    public void applyPowerup(Powerup p){
    	if(p.getMyType().equals("pointpower.gif")){myTotal+=POINTS_AWARDED;}
    	if(p.getMyType().equals("lifepower.gif")){myLives++;}
    	if(p.getMyType().equals("paddlepower.gif")){myPaddle.setReset(true);}
    	removePiece(p);
    }

    public void removeBlock(Block b){
    	 myTotal+=(b).getMyPoints(); //update total
		 removePiece(b);//remove block from scene
    	 if(b.getMyPower()!=null){addPiece(b.getMyPower());}//drop powerup if there is one
    }
    
    public void checkLevel(){
    	if(numBlocks()==0){//if no more blocks, this level has been cleared
    		levelUp(myLevel+1);
    	}
    	if(myBall.offScreen(SIZE)){//if ball falls off screen, level has been lost
    		levelFail();
    	}
    }
    
    private void gameReset(){
    	setStartVars();
    	levelUp(1);
    }
    
    private void levelUp(int level){
    	if(level>LAST_LEVEL){handleWin();}
    	else{
	    	myLevel=level;
	    	setColor(level);
			gameManager.removeAllPieces();
			getSceneNodes().getChildren().clear();
	    	setStartParts();
    	}
    }
    
    public void levelFail(){
    	myLives--;
    	if(myLives==0){handleFail();}
    	else{levelReset();}
    }
    
    public void levelReset(){
    	removePiece(myBall);
    	removePiece(myPaddle);
    	myBall = Ball.buildStartBall(SIZE,myLevel);
    	myPaddle = Paddle.buildStartPaddle(SIZE);
    	addPiece(myBall);
    	addPiece(myPaddle);
    }
    
    private int numBlocks(){
    	int numBlocks=0;
    	if(myTotal==0){return 1;}//no points made, no blocks could be removed
    	for(GamePiece g: getGameManager().getAllPieces()){
	    	if(g!=null && g instanceof Block && !g.getClass().getName().equals("ConcreteBlock")){numBlocks++;}
    	}
    	return numBlocks;
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
    	piece.update(SIZE, myLevel); //internal changes by piece
    	if(piece instanceof Paddle){
    		
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