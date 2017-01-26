import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The main driver of the game. Launches the program.
 * 
 * @author Carl Dea Modified by Maddie Briere
 */
public class Runner extends Application {

	private BreakoutWorld world = new BreakoutWorld(60, "Breakout");
	private StartPopupMenu start = new StartPopupMenu("fido", "Fido's Breakout");
	private InfoPopupMenu info = new InfoPopupMenu("fido", "Fido's Breakout");

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {

		start.initialize();
		info.initialize();
		world.initialize(primaryStage);
		primaryStage.show();
		world.beginGameLoop();
	}

}