import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The main driver of the game.
 * 
 * @author Carl Dea Modified by Maddie Briere
 */
public class Runner extends Application {

	BreakoutWorld world = new BreakoutWorld(60, "Breakout");

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {

		PopupMenu.fidoMenu();// start menu
		world.initialize(primaryStage);
		primaryStage.show();
		world.beginGameLoop();
	}

}