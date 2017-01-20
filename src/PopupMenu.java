
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PopupMenu {
/*
 * Class intended for use in initial pop-up description and final fail popup
 * Based on http://www.javafxtutorials.com/tutorials/creating-a-pop-up-window-in-javafx/
 */
	private FlowPane pane1;
	private Scene scene1;
	private Stage thestage;
	   
	public void start(Stage primaryStage) {
	     
	        thestage=primaryStage;
	        //can now use the stage in other methods
	        //make things to put on panes
	        Label lbl1 = new Label("Welcome to Fido's Breakout!");
	        lbl1.setFont(Font.font("Cambria", 20));
	        Image image = new Image(getClass().getResourceAsStream("fido.gif"));
	        Label corgi = new Label("Help Fido escape a Nyan Cat Attack!", new ImageView(image));
	        Label lbl2 = new Label("1) The main goal of this game is to help Fido escape the Nyan Cat Attack!\n" +
	        		"Fido's only weapon is his trusty, obstacle-destroying tennis ball"); 
	        Label lbl3 = new Label("2) Guide Fido and his paddle with the left and right keys! You can bounce the\n" +
	        		"tennis ball left, up and right by hitting the left, middle and right portions\n" +
	        		"of the paddle (respectively)");
	        Label lbl4 = new Label("3) Destroy blocks, earn points, and collect falling powerups!");
	        Label lbl5 = new Label("4) Avoid falling nyan cats! They will steal your points");
	        Label lbl6 = new Label("5) Leveling up means more nyan cats, more bricks and a faster moving ball!");
	        Label lbl7 = new Label("6) Luckily, you get more abilities and powerups as you get to higher levels!");
	        Label lbl8 = new Label("Are you ready to escape?");
	        		
	        Button btnscene1=new Button("Click to start!");
	        btnscene1.setOnAction(e-> ButtonClicked(e));
	        
	        //make pane
	        pane1=new FlowPane();
	        pane1.setHgap(20);
	        pane1.setVgap(20);
	        
	        //set background color of pane
	        pane1.setStyle("-fx-background-color:lightcyan;-fx-padding:10px;");
	      
	        //add everything to pane
	        pane1.getChildren().addAll(lbl1, corgi, lbl2, lbl3, lbl4, 
	        		lbl5, lbl6, lbl7, lbl8, btnscene1);
	        
	        
	        //make 2 scenes from 2 panes
	        scene1 = new Scene(pane1, 500, 500);
	        
		    //rest of code -
		    primaryStage.setTitle("Fido's Breakout");
		    primaryStage.setScene(scene1);
		    primaryStage.setMinWidth(400);
		    primaryStage.showAndWait();
	        
	 }
	 
	 public void ButtonClicked(ActionEvent e)
	 {
	     thestage.close();
	 }
}
