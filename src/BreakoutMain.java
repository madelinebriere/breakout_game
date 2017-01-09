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
    public static final String TITLE = "Breakout Game";
    public static final Paint BACKGROUND = Color.WHITE;
    public static final int FRAMES_PER_SECOND = 60;
    public static final int SIZE = 400;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    public static final int KEY_INPUT_SPEED = 5;
    public static final int PADDLE_Y_POS=350;
    public static final int BARRIER_OFFSET=15;

    // some things we need to remember during our game
    private Scene myScene;
    private Ball gameBall;
    private Rectangle paddle; //REDUNDANT
    private int paddleSize=50;//changeable throughout game
    
    /**
     * Initialize what will be displayed and how it will be updated.
     */
    @Override
    public void start (Stage s) {
        // attach scene to the stage and display it
        Scene scene = setupGame(SIZE, SIZE, BACKGROUND);
        s.setScene(scene);
        s.setTitle(TITLE);
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
        // create one top level collection to organize the things in the scene
        Group root = new Group();
        gameBall = new Ball();
        
        // create a place to see the shapes
        myScene = new Scene(root, width, height, background);
        
        // make some shapes and set their properties
        // x and y represent the top left corner of the ball, so center it
        gameBall.getMyBouncer().setX(width / 2 - gameBall.getMyBouncer().getBoundsInLocal().getWidth() / 2);
        gameBall.getMyBouncer().setY(height / 2 - gameBall.getMyBouncer().getBoundsInLocal().getHeight() / 2);
       
        //Draw the paddle
        paddle = 
        		new Rectangle (SIZE/2,PADDLE_Y_POS, paddleSize, paddleSize/4);
        paddle.setFill(Color.DARKSLATEBLUE);
        
        // order added to the group is the order in which they are drawn
        root.getChildren().add(gameBall.getMyBouncer());
        root.getChildren().add(paddle);
        
        // respond to input
        myScene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        myScene.setOnMouseClicked(e -> handleMouseInput(e.getX(), e.getY()));
        return myScene;
    }

    // Change properties of shapes to animate them 
    // Note, there are more sophisticated ways to animate shapes, but these simple ways work fine to start.
    private void step (double elapsedTime) {
        // update attributes
    	
    	ImageView b = gameBall.getMyBouncer();
    	
    	//Update location based on x and y motion
    	//move right
    	if (gameBall.getMyXMov()==1){b.setX(b.getX() + gameBall.getMySpeed() * elapsedTime);}
    	//move left
    	if (gameBall.getMyXMov()==-1){b.setX(b.getX() - gameBall.getMySpeed() * elapsedTime);}
    	//move up
    	if (gameBall.getMyYMov()==1){b.setY(b.getY() - gameBall.getMySpeed() * elapsedTime);}
    	//move down
    	if (gameBall.getMyYMov()==-1){b.setY(b.getY() + gameBall.getMySpeed() * elapsedTime);}
       
    	//Check that ball is still in bounds and rebound if not
    	if(b.getX()<0){gameBall.setMyXMov(1);}//if hits left wall, bounce back
    	if(b.getX()>(SIZE-BARRIER_OFFSET)){gameBall.setMyXMov(-1);}//if hits right wall, bounce back
    	if(b.getY()<0){gameBall.setMyYMov(-1);} //if hits top, bounce back down
    	if(b.getY()>(SIZE-BARRIER_OFFSET)){System.out.println("FAIL");}
    	
        
    }

    // What to do each time a key is pressed
    private void handleKeyInput (KeyCode code) {
    	if (code == KeyCode.RIGHT) {
    		paddle.setX(paddle.getX()+10);
    	}
    	if (code == KeyCode.LEFT) {
    		paddle.setX(paddle.getX()-10);
    	}
    }

    // What to do each time a key is pressed
    private void handleMouseInput (double x, double y) {
   
    }

    /**
     * Start the program.
     */
    public static void main (String[] args) {
        launch(args);
    }
    
}