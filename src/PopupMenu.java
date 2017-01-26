// This entire file is part of my masterpiece.
// Madeline Briere
/**
 * This class creates a basic popup, with its subclasses adding more
 * specificity to the actual content of the popup. It is based on
 * http://www.javafxtutorials.com/tutorials/creating-a-pop-up-window-in-
 * javafx/
 * 
 * An important element of this design is that it relies upon
 * the input prefix to access all of the files. That is,
 * if you enter "fido," then the files accessed will be 
 * fidoinstructions.txt, fidoinfo.txt, etc. This is true for its 
 * subclasses as well.
 * 
 * Why this is part of my code masterpiece:
 *
 * The prefix read in by the PopupMenu constructor
 * allows a user to decide which files will be read in -
 * hence, if the user saves their files with each corresponding
 * prefix, they can access the information for several different
 * games, even programs in general
 *  -- this allows a great amount of flexibility and makes this
 *  code extremely reusable
 * 
 * Subclasses of this class can do almost anything -- they simply
 * look for respective files with the correct prefix and set up
 * the popup as requested (e.g., title, textbook, button). 
 * 
 * Moreover, this class is dedicated to a single, clear purpose --
 * to create and fill a popup-menu. It is not bogged down by unneeded
 * dependencies. It deals with all of the implementation without outside
 * communication -- following the "Tell the Other Guy" motto
 * 
 * Finally, this code is readable and concise. 
 * Each available method is named so
 * that a user extending this class knows what capabilities they
 * have inherited. Likewise, following the DRY motto, no code is repeated.
 * Each method serves a clear and distinct purpose.
 * 
 * @author maddiebriere
 */

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
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public abstract class PopupMenu {

	public static final int TEXT_WIDTH = 480;
	public static final int DEFAULT_SIZE = 500;

	private Stage myStage;
	private String myTitle;
	private String myPrefix;


	public PopupMenu(String prefix, String title) {
		myStage = new Stage();
		myPrefix = prefix;
		myTitle = title;
	}

	public void initialize() {
		setStage(buildScene());
	}

	private void setStage(Scene scene) {
		myStage.setTitle(myTitle);
		myStage.setScene(scene);
		myStage.setMinWidth(DEFAULT_SIZE);
		myStage.showAndWait();
	}

	private Scene buildScene() {
		FlowPane pane = createAndFillPane(generateContent());
		return new Scene(pane, DEFAULT_SIZE, DEFAULT_SIZE);
	}
	
	/**
	 * Unique to each popup -- must be implemented by
	 * any subclass
	 * @return an ArrayList of all Nodes to be added to the menu
	 */
	protected abstract ArrayList<Node> generateContent();

	protected String[] readFileByLine(String fileName) {
		Scanner scan = generateScanner(fileName);
		ArrayList<String> lines = new ArrayList<String>();
		while (scan.hasNextLine()) {
			lines.add(scan.nextLine());
		}
		scan.close();
		String[] toRet = new String[lines.size()];
		return lines.toArray(toRet);
	}

	protected String readFileInFull(String fileName) {
		return arrayToString(readFileByLine(fileName));
	}

	private String arrayToString(String[] s) {
		String toRet = "";
		for (int i = 0; i < s.length; i++) {
			toRet += s[i] + "\n";
		}
		return toRet;
	}

	private Scanner generateScanner(String fileName) {
		Scanner scan = new Scanner("");
		try {
			scan = new Scanner(new File(fileName));
		} catch (FileNotFoundException e) {
			System.out.println("Scanner file not found");
		}
		return scan;
	}

	protected TextArea generateText(String instruction) {
		TextArea text = new TextArea(instruction);
		text.setWrapText(true);
		text.setMaxWidth(TEXT_WIDTH);
		text.setEditable(false);
		return text;
	}

	protected ArrayList<Label> generateLabels(String[] fileInput) {
		ArrayList<Label> toRet = new ArrayList<Label>();
		for (int i = 0; i < fileInput.length; i++) {
			toRet.add(generateLabel(fileInput[i]));
		}
		return toRet;
	}

	/**
	 * A label is generate for a single string line formatted:
	 * name.gif This is my descriptive line.
	 * Where name.gif is an image file name
	 * 
	 * @param line
	 * a string representing a single line
	 * @return a Label with the description of the item
	 * and an ImageView of the item
	 */
	protected Label generateLabel(String line) {
		int index = line.indexOf(" ");
		String name = line.substring(0, index);
		String description = line.substring(index + 1);
		return new Label(description, new ImageView(name));
	}

	private FlowPane createAndFillPane(ArrayList<Node> labels) {
		FlowPane pane = new FlowPane();
		stylePane(pane);
		pane.getChildren().addAll(labels);
		return pane;
	}
	
	private void stylePane(FlowPane pane){
		pane.setHgap(20);
		pane.setVgap(20);
		pane.setStyle("-fx-background-color:lightcyan;-fx-padding:10px;");
	}

	protected Button generatePageButton(String buttonMessage) {
		Button button = new Button(buttonMessage);
		button.setOnAction(e -> StartButtonClicked(e));
		return button;
	}

	protected void StartButtonClicked(ActionEvent e) {
		myStage.close();
	}
	
	public String getMyPrefix() {
		return myPrefix;
	}

	public void setMyPrefix(String myPrefix) {
		this.myPrefix = myPrefix;
	}
	
}
