import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class InfoBox{

	private Text myText;
	private Rectangle myBox;
	private int myLevel=0;
	private int myLives=0;
	private int myPoints=0;
	

	public InfoBox(int size) {
		Text text = new Text(10,size-65, "");
		text.setFill(Color.ALICEBLUE);
		text.setFont(new Font(15));
		myText = text;
		myBox = new Rectangle(5, size-65,90,60);
		myBox.setFill(Color.DARKCYAN);
		myBox.setArcWidth(7);
		myBox.setArcHeight(7);;
		myBox.setStroke(Color.BLACK);
	}
	
	public Rectangle getMyBox() {
		return myBox;
	}

	public void setMyBox(Rectangle myBox) {
		this.myBox = myBox;
	}

	public boolean changed(int level, int points, int lives)
	{
		if(level!=myLevel || points!=myPoints || lives!=myLives){return true;}
		else {return false;}
	}
	
	public void setLevel(int level){
		myLevel=level;
		myText.setText(myText.getText()+"\nLevel: "+level);
	}
	
	public void setLives(int lives){
		myLives=lives;
		myText.setText(myText.getText()+"\nLives: "+lives);
	}
	
	public void setPoints(int points){
		myPoints=points;
		myText.setText(myText.getText()+"\nPoints: "+points);
	}

	public Text getMyText() {
		return myText;
	}

	public void setMyText(Text myText) {
		this.myText = myText;
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
