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
 * 
 * @author maddiebriere
 */

public class BreakoutWorld {

	// Colors corresponding to each level of the game
	public static final Color COLOR_1 = Color.ALICEBLUE;
	public static final Color COLOR_2 = Color.LIGHTCYAN;
	public static final Color COLOR_3 = Color.LIGHTBLUE;
	public static final Color COLOR_4 = Color.LIGHTSKYBLUE;

	// Set screen size (square)
	public static final int SIZE = 500;

	// Starting game control values
	public static final int START_LIVES = 9;
	public static final int START_POINTS = 0;
	public static final int START_LEVEL = 1;
	public static final int LAST_LEVEL = 4;

	// Misc variables
	public static final int POINTS_AWARDED = 50;
	public static final int POINTS_LOST = 20;

	/* All of the following number variables must be POSITIVE numbers */
	private int myLives; // number of lives left
	private int myLevel; // must be number 1 - 4
	private int myTotal; // total score

	// Major game pieces to keep track of outside of the gameManager
	private Ball myBall;
	private Paddle myPaddle;
	private InfoBox myBox;
	private Cat myHeadCat;

	// General set-up variables
	private Scene gameSurface;
	private Group sceneNodes;
	private Stage stage;
	private static Timeline gameLoop;
	private final int framesPerSecond;
	private final String windowTitle;

	// Manages all gamePieces and updates
	private final GameManager gameManager = new GameManager();

	public BreakoutWorld(final int fps, final String title) {
		framesPerSecond = fps;
		windowTitle = title;
		buildAndSetGameLoop();
	}

	/**
	 * Creates game loop (TimeLine) with EventHandler set to perform updates
	 * each iteration
	 */
	private final void buildAndSetGameLoop() {

		final Duration oneFrameAmt = Duration.millis(1000 / getFramesPerSecond());
		final KeyFrame oneFrame = new KeyFrame(oneFrameAmt, new EventHandler<ActionEvent>() {

			@Override
			public void handle(javafx.event.ActionEvent event) {
				if (myLives > 0) {
					generalUpdates();
				}
			}
		}); // oneFrame

		// sets the game world's game loop (Timeline)
		Timeline animation = new Timeline();
		animation.setCycleCount(Animation.INDEFINITE);
		animation.getKeyFrames().add(oneFrame);
		setGameLoop(animation);
	}

	// Method that does all of the work -- simulates all GamePiece
	// actions/interactions
	public void generalUpdates() {
		if (myBall != null & myPaddle != null) {// easy check to avoid any
												// nullpointers
			updateBox();
			updatePieces();
			checkCollisions();// update based on collisions
			checkLevel();// New level/ game-over?
			checkPaddle();// update paddle image if needed
			checkCats(); // Add more cats if needed
			cleanupPieces();// removed dead things
		}
	}

	/**
	 * Updates the primary stage with the initial players, builds start lists
	 * and variables
	 * 
	 * @param primaryStage
	 *            modified for game
	 */
	public void initialize(final Stage primaryStage) {
		stage = primaryStage;
		primaryStage.setTitle(getWindowTitle());
		primaryStage.setAlwaysOnTop(true);

		// Create the scene
		setSceneNodes(new Group());
		setGameSurface(new Scene(getSceneNodes(), SIZE, SIZE));
		getGameSurface().setFill(COLOR_1);
		primaryStage.setScene(getGameSurface());

		// Initialize& add variables/pieces/input
		setStartVars();
		setStartParts();
		setupInput(primaryStage);
	}

	private void setStartVars() {
		myLives = START_LIVES;// start with 9 lives
		myLevel = START_LEVEL; // start on level 1
		myTotal = START_POINTS;// start with no points
	}

	private void setStartParts() {
		ArrayList<Block> myBlocks = BlockReader.readBlocks(myLevel);
		Powerup.setPowers(myLevel, myBlocks); // apply powerups to blocks
		addPieces(myBlocks);
		addPiece(myBall = Ball.buildStartBall(SIZE, myLevel)); // start ball
		addPiece(myPaddle = Paddle.buildStartPaddle(SIZE)); // start paddle
		myHeadCat = addCat(); // add first cat
		addBox(); // add information box
	}

	/**
	 * Set up keyboard input
	 */
	private void setupInput(Stage stage) {
		stage.getScene().setOnKeyPressed(e -> handlePressKeyInput(e.getCode()));
		stage.getScene().setOnKeyReleased(e -> handleReleaseKeyInput(e.getCode()));
	}

	/**
	 * Performs action correlating to key pressed or does nothing
	 * 
	 * @param code
	 *            representing key pressed
	 */
	private void handlePressKeyInput(KeyCode code) {
		checkMoveKeys(code);
		checkCheatKeys(code);
	}

	private void checkMoveKeys(KeyCode code) {
		if (code == KeyCode.RIGHT) {
			myPaddle.startRightMove();
			myPaddle.stopLeftMove();
		}
		if (code == KeyCode.LEFT) {
			myPaddle.startLeftMove();
			myPaddle.stopRightMove();
		}
	}

	private void checkCheatKeys(KeyCode code) {
		checkLetterCheats(code);
		checkLevelCheats(code);
	}

	private void checkLetterCheats(KeyCode code) {
		if (code == KeyCode.L) {
			myLives++;
		}
		if (code == KeyCode.P) {
			myTotal++;
		}
		if (code == KeyCode.R) {
			levelReset();
		}
		if (code == KeyCode.N) {
			gameReset();
		}
	}

	private void checkLevelCheats(KeyCode code) {
		if (code == KeyCode.DIGIT1) {
			levelUp(1);
		}
		if (code == KeyCode.DIGIT2) {
			levelUp(2);
		}
		if (code == KeyCode.DIGIT3) {
			levelUp(3);
		}
		if (code == KeyCode.DIGIT4) {
			levelUp(4);
		}
	}

	/**
	 * Only relevant for left and right (so that paddle moves when you press
	 * down on a key). Used for smooth motion of paddle so that the paddle
	 * continues to move, even when the keyboard doesn't register a key being
	 * pressed or released. Halts the paddle as soon as you release the left or
	 * right key
	 * 
	 * @param code:
	 *            keyboard input
	 */
	private void handleReleaseKeyInput(KeyCode code) {
		if (code == KeyCode.RIGHT) {
			myPaddle.stopRightMove();
		}
		if (code == KeyCode.LEFT) {
			myPaddle.stopLeftMove();
		}
	}

	/**
	 * Create a new InfoBox and add current information to it
	 * 
	 * @return InfoBox created
	 */
	private InfoBox setBoxValues() {
		InfoBox info = new InfoBox(SIZE);
		info.setLevel(myLevel);
		info.setLives(myLives);
		info.setPoints(myTotal);
		return info;
	}

	/**
	 * Creates InfoBox and adds it's components to the scene Note that InfoBox
	 * IS NOT held in the gameManager List...InfoBox is not classified as a
	 * GamePiece
	 **/
	private void addBox() {
		myBox = setBoxValues();
		getSceneNodes().getChildren().add(myBox.getMyBox());
		getSceneNodes().getChildren().add(myBox.getMyText());
	}

	/**
	 * Removes previously added components from scene
	 */
	private void removeBox() {
		getSceneNodes().getChildren().remove(myBox.getMyText());
		getSceneNodes().getChildren().remove(myBox.getMyBox());
	}

	/**
	 * Updates the InfoBox if any information (e.g., #lives) has changed
	 */
	private void updateBox() {
		if (myBox != null && myBox.changed(myLevel, myTotal, myLives)) {
			removeBox();
			addBox();
		}
	}

	/**
	 * Updates each GamePiece in the game world. This method will loop through
	 * each sprite and passing it to the handleUpdate() method. The derived
	 * class should override handleUpdate() method.
	 *
	 */
	private void updatePieces() {
		for (GamePiece piece : gameManager.getAllPieces()) {
			handleUpdate(piece);
		}
	}
	
	private void handleUpdate(GamePiece piece) {
		piece.update(SIZE, myLevel); // internal changes by piece
	}

	/**
	 * Checks each GamePiece in the BreakoutWorld to determine if a collision
	 * occurred. Iterated so that there are no repeats
	 */
	private void checkCollisions() {
		gameManager.resetCollisionsToCheck();
		for (GamePiece pieceA : gameManager.getCollisionsToCheck()) {
			for (GamePiece pieceB : gameManager.getAllPieces()) {
				if (handleCollision(pieceA, pieceB)) {
					break;
				}
			}
		}
	}

	/**
	 * Handles collisions based on what type of GamePiece pieceA and pieceB are
	 * 
	 * @param pieceA
	 * @param pieceB
	 * @return boolean True if the objects collided, otherwise false.
	 */
	private boolean handleCollision(GamePiece pieceA, GamePiece pieceB) {
		if (pieceA != pieceB) {
			if (pieceA.collide(pieceB)) {
				if (pieceA instanceof Ball && pieceB instanceof Block) {
					((Ball) pieceA).bounceOffBlock((Block) pieceB); // alter
																	// ball path
					takeBlockDamage((Block) pieceB); // update block with damage
				}
				if (pieceA instanceof Ball && pieceB instanceof Paddle) {
					((Ball) pieceA).bounceOffPaddle((Paddle) pieceB); // alter
																		// ball
																		// path
				}
				if (pieceA instanceof Paddle && pieceB instanceof Powerup) {
					applyPowerup((Powerup) pieceB);
				}
				if (pieceA instanceof Paddle && pieceB instanceof Cat) {
					takeCatDamage((Cat) pieceB); // respond to successful nyan
													// cat attack
				}
				return true; // collision has occurred
			}
		}
		return false;
	}

	private void takeBlockDamage(Block block) {
		block.takeHit();
		if (block.isDestroyed()) {
			removeBlock(block);
		} // if block is destroyed
	}

	private void takeCatDamage(Cat c) {
		myTotal -= POINTS_LOST;
		removePiece(c);
	}

	private void applyPowerup(Powerup p) {
		if (p.getMyType().equals("pointpower.gif")) {
			myTotal += POINTS_AWARDED;
		}
		if (p.getMyType().equals("lifepower.gif")) {
			myLives++;
		}
		if (p.getMyType().equals("paddlepower.gif")) {
			myPaddle.setReset(true);
			paddleRefresh();
		}
		removePiece(p);
	}

	private void removeBlock(Block b) {
		myTotal += (b).getMyPoints(); // update total
		removePiece(b);// remove block from scene
		if (b.getMyPower() != null) {
			addPiece(b.getMyPower());
		} // drop powerup if there is one
	}

	/**
	 * Checks if the timer stored in the HeadCat object has reached the
	 * appropriate threshold If so, another cat is created and added to the
	 * scene
	 */
	private void checkCats() {
		if (myHeadCat.timeToAttack(myLevel)) {
			myHeadCat = addCat();
		}
	}

	/**
	 * Creates a cat and adds it to the scene/GAME_ACTORS list
	 * 
	 * @return Cat (built using Cat buildCat() method)
	 */
	private Cat addCat() {
		Cat cat = Cat.buildCat(SIZE);
		addPiece(cat);
		return cat;
	}

	/**
	 * Updates the image stored in the list of nodes to the correct image
	 * Different images for left versus right motion
	 */
	private void checkPaddle() {
		if (myPaddle.getLeftMove() > 0 || myPaddle.getRightMove() > 0) {
			paddleRefresh();
		}
	}

	private void checkLevel() {
		if (numBlocks() == 0) {// if no more blocks, this level has been cleared
			levelUp(myLevel + 1);
		}
		if (myBall.offScreen(SIZE)) {// if ball falls off screen, level has been
										// lost
			levelFail();
		}
	}

	private void gameReset() {
		setStartVars();
		levelUp(1);
	}

	private void levelUp(int level) {
		if (level > LAST_LEVEL) {
			handleWin();
		} else {
			myLevel = level;
			setColor(level);
			gameManager.removeAllPieces();// refresh the list of actors
			getSceneNodes().getChildren().clear(); // clear the screen
			setStartParts();// reset the game world
		}
	}

	private void levelFail() {
		myLives--;
		if (myLives == 0) {
			handleFail();
		} else {
			levelReset();
		}
	}

	private void levelReset() {
		ballReset();
		paddleReset();
	}

	private void ballReset() {
		removePiece(myBall);
		myBall = Ball.buildStartBall(SIZE, myLevel);
		addPiece(myBall);
	}

	/**
	 * Build new paddle
	 */
	private void paddleReset() {
		removePiece(myPaddle);
		myPaddle = Paddle.buildStartPaddle(SIZE);
		addPiece(myPaddle);
	}

	/**
	 * Update old paddle
	 */
	private void paddleRefresh() {
		removePiece(myPaddle); // remove to replace with new paddle
		myPaddle = myPaddle.resetImage(); // reset image
		addPiece(myPaddle); // add back
	}

	/**
	 * Counts how many blocks (that can be destroyed) remain on the screen
	 * 
	 * @return number of blocks to be hit
	 */
	private int numBlocks() {
		int numBlocks = 0;
		if (myTotal == 0) {
			return 1;
		} // no points made, no blocks could be removed
		for (GamePiece g : getGameManager().getAllPieces()) {
			if (g != null && g instanceof Block && !g.getClass().getName().equals("ConcreteBlock")) {
				numBlocks++;
			}
		}
		return numBlocks;
	}

	private void setColor(int level) {
		Scene set = getGameSurface();
		switch (level) {
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

	private void detachInput() {
		stage.getScene().setOnKeyPressed(null);
		stage.getScene().setOnKeyReleased(null);
	}

	private void handleFail() {
		detachInput();
		finalMessage("You lose!");

	}

	private void handleWin() {
		detachInput();
		finalMessage("You win!");
	}

	private void finalMessage(String string) {
		getSceneNodes().getChildren().clear();
		getGameManager().removeAllPieces();

		Text text = new Text(3 * SIZE / 7, SIZE / 2, string);
		text.setFill(Color.DARKORCHID);
		text.setFont(Font.font("Cambria", 20));
		text.setScaleX(3);
		text.setScaleY(3);
		getSceneNodes().getChildren().add(text);
	}

	private void addPiece(GamePiece piece) {
		getGameManager().addPieces(piece);
		getSceneNodes().getChildren().add(piece.getMyImage());
	}

	private <T extends GamePiece> void addPieces(ArrayList<T> pieces) {
		for (GamePiece g : pieces) {
			addPiece(g);
		}
	}

	private void removePiece(GamePiece piece) {
		getGameManager().addPiecesToBeRemoved(piece);
		getSceneNodes().getChildren().remove(piece.getMyImage());
	}

	// Called in Runner to initiate animation
	protected void beginGameLoop() {
		getGameLoop().play();
	}

	// Getters and setters for BreakoutWorld items
	protected static Timeline getGameLoop() {
		return gameLoop;
	}

	protected static void setGameLoop(Timeline gameLoop) {
		BreakoutWorld.gameLoop = gameLoop;
	}

	// Pieces marked to be removed are handled
	protected void cleanupPieces() {
		gameManager.cleanupPieces();
	}

	protected int getFramesPerSecond() {
		return framesPerSecond;
	}

	public String getWindowTitle() {
		return windowTitle;
	}

	protected GameManager getGameManager() {
		return gameManager;
	}

	public Scene getGameSurface() {
		return gameSurface;
	}

	protected void setGameSurface(Scene gameSurface) {
		this.gameSurface = gameSurface;
	}

	public Group getSceneNodes() {
		return sceneNodes;
	}

	protected void setSceneNodes(Group sceneNodes) {
		this.sceneNodes = sceneNodes;
	}

}