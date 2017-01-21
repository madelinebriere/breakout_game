
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class PopupMenu {
/*
 * Class intended for use in initial pop-up description and final fail popup
 * Based on http://www.javafxtutorials.com/tutorials/creating-a-pop-up-window-in-javafx/
 */
	public static final int DEFAULT_WIDTH = 480;
	public static final int DEFAULT_SIZE=500;
	
	private Stage thestage;
	private String startfile;
	private String infofile;
	private String gamename;
	private String character;
	private String message;
	
	public PopupMenu(String startfile, String infofile, String gamename, String character,
			String message){
		thestage= new Stage();
		this.startfile=startfile;
		this.infofile=infofile;
		this.gamename=gamename;
		this.character=character;
		this.message=message;
	}
	
	public static void fidoMenu(){
    	PopupMenu popup = new PopupMenu("src/instructions.txt", "src/info.txt",
    			"Fido's Breakout", "fido.gif", "Can you help Fido escape a Nyan Cat Attack?");
    	popup.startMenu();
    	popup.infoMenu();
    }
	   
	public void startMenu() {
		
			menu(false, startfile);
	 }
	
	public void infoMenu(){
		 	menu(true, infofile);
		  
	}
//boolean true if this is the final screen
	public void menu(boolean finalScreen, String file){
		String instruction = readInstructions(file);
		
		ArrayList<Node> labels = new ArrayList<Node>();
		if(!finalScreen){
			labels.addAll(characterLabels(gamename, character, message));
			labels.add(createTextArea(instruction));
		}//if start menu, read instructions
		else{
			labels.addAll(generateObjectLabels());
			labels.add(generateLevelInfo());
		}//else show information
        labels.add(createPageButton(finalScreen));
        
        //make pane
        FlowPane pane = createAndFillPane(labels);
        Scene scene = new Scene(pane, DEFAULT_SIZE, DEFAULT_SIZE);
        
	    //rest of code -
	   	thestage.setTitle(gamename);
	    thestage.setScene(scene);
	    thestage.setMinWidth(DEFAULT_SIZE);
	    thestage.showAndWait();
	}
	 
	private ArrayList<Label> characterLabels(String gameName, String characterFile, String message){
		Label lbl = new Label("Welcome to " + gameName);
        lbl.setFont(Font.font("Cambria", 20));
        Image image = new Image(getClass().getResourceAsStream(characterFile));
        Label character = new Label(message, new ImageView(image));
        ArrayList <Label>toRet = new ArrayList<Label>();
        toRet.add(lbl);
        toRet.add(character);
        return toRet;
	}
	
	private String readInstructions(String fileName){
		File file = new File(fileName);
		Scanner scan = new Scanner("");
		try {scan = new Scanner(file);} 
		catch (FileNotFoundException e) {System.out.println("Scanner file not found");}
		String instruction = "";
		while(scan.hasNextLine()){
			instruction+=scan.nextLine()+"\n";
		}
		scan.close();
		return instruction;
	}
	
	private TextArea createTextArea(String instruction){
		TextArea text = new TextArea(instruction);
		text.setWrapText(true);
		text.setMaxWidth(DEFAULT_WIDTH);
		text.setEditable(false);
		return text;
	}
	
	private ArrayList<Label> generateObjectLabels(){
		ArrayList<Label>labels = new ArrayList<Label>();
		labels.add(generateSingleLabel("normal.gif")); //normal brick
		labels.add(generateSingleLabel("strong.gif")); //strong brick
		labels.add(generateSingleLabel("double.gif"));
		labels.add(generateSingleLabel("concrete.gif"));
		labels.add(generateSingleLabel("paddlepower.gif"));
		labels.add(generateSingleLabel("pointpower.gif"));
		labels.add(generateSingleLabel("lifepower.gif"));
		return labels;
	}
	
	private Label generateSingleLabel(String name){
		Label label = new Label();
		Image image = new Image(getClass().getResourceAsStream(name));
		
		//Blocks
		if(name.equals("normal.gif")){label = new Label("Normal Block: 10 points, 1 hit", new ImageView(image));}
		if(name.equals("strong.gif")){label = new Label("Strong Block: 10 points, 2 hits", new ImageView(image));}
		if(name.equals("double.gif")){label = new Label("Double Block: 20 points, 1 hit", new ImageView(image));}
		if(name.equals("concrete.gif")){label = new Label("Concrete Block: No points, indestructible", new ImageView(image));}
		
		//Powerups
		if(name.equals("paddlepower.gif")){label = new Label("Larger paddle", new ImageView(image));}
		if(name.equals("pointpower.gif")){label = new Label("+50 points", new ImageView(image));}
		if(name.equals("lifepower.gif")){label = new Label("+1 life", new ImageView(image));}
		return label;
	}
	
	private TextArea generateLevelInfo(){
		String info = readInstructions(infofile);
		TextArea text = createTextArea(info);
		text.setMaxHeight(DEFAULT_WIDTH/3);
		return text;
	}
	
	private FlowPane createAndFillPane(ArrayList<Node> labels){
		//make pane
        FlowPane pane=new FlowPane();
        pane.setHgap(20);
        pane.setVgap(20);
        
        //set background color of pane
        pane.setStyle("-fx-background-color:lightcyan;-fx-padding:10px;");
      
        //add everything to pane
        pane.getChildren().addAll(labels);
        return pane;
	}
	
	private Button createPageButton(boolean finalScreen){
		Button btnscene;
		if(finalScreen){
			btnscene=new Button("Start Game");
		}
		else{
			btnscene=new Button("Next page");
		}
		btnscene.setOnAction(e-> StartButtonClicked(e));
        return btnscene;
	}

	public void StartButtonClicked(ActionEvent e){
	     thestage.close();
	 }
}
