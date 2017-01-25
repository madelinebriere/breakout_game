import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Class dedicated to holding information about the GameWorld,
 * and returning it in a neat fashion for display in 
 * BreakoutWorld
 * 
 * Attempts were made to connect this class to the GamePiece class
 * so that updates would be covered by the general update code 
 * in BreakoutWorld ... however, this class did not share enough 
 * in common with other GamePieces to warrant connection
 * 
 * @author maddiebriere
 *
 */

public class InfoBox {

	private Text myText;
	private Rectangle myBox;
	private int myLevel = 0;
	private int myLives = 0;
	private int myPoints = 0;

	public InfoBox(int size) {
		setText(size);
		setBox(size);
	}

	public void setText(int size) {
		Text text = new Text(150, size - 13, "");
		text.setFill(Color.BLACK);
		text.setFont(Font.font("Cambria", 15));
		myText = text;
	}

	public void setBox(int size) {
		myBox = new Rectangle(140, size - 30, size - 280, 25);
		myBox.setFill(Color.WHITE);
		myBox.setArcWidth(7);
		myBox.setArcHeight(7);
		myBox.setStroke(Color.BLACK);
	}

	/**
	 * Determines if any of the input parameters has been altered from the last
	 * update
	 * 
	 * @param level
	 * @param points
	 * @param lives
	 * @return true if something has changed, false otherwise
	 */
	public boolean changed(int level, int points, int lives) {
		return (level != myLevel || points != myPoints || lives != myLives);
	}

	// getters and setters
	public Text getMyText() {
		return myText;
	}

	public Rectangle getMyBox() {
		return myBox;
	}

	public void setLevel(int level) {
		myLevel = level;
		myText.setText(myText.getText() + "   Level: " + level);
	}

	public void setLives(int lives) {
		myLives = lives;
		myText.setText(myText.getText() + "   Lives: " + lives);
	}

	public void setPoints(int points) {
		myPoints = points;
		myText.setText(myText.getText() + "   Points: " + points);
	}

	public int getMyLevel() {
		return myLevel;
	}

	public void setMyLevel(int myLevel) {
		this.myLevel = myLevel;
	}

	public int getMyLives() {
		return myLives;
	}

	public void setMyLives(int myLives) {
		this.myLives = myLives;
	}

	public int getMyPoints() {
		return myPoints;
	}

	public void setMyPoints(int myPoints) {
		this.myPoints = myPoints;
	}

}
