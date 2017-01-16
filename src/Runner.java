import javafx.application.Application;
import javafx.stage.Stage;
 
/**
 * The main driver of the game.
 * @author cdea
 */
public class Runner extends Application {
 
    BreakoutWorld world = new BreakoutWorld(60, "Breakout");
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
 
    @Override
    public void start(Stage primaryStage) {
        // setup title, scene, stats, controls, and actors.
        world.initialize(primaryStage);
 
        // kick off the game loop
        world.beginGameLoop();
 
        // display window
        primaryStage.show();
    }
 
}