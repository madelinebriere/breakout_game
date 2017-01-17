import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class InfoBox{

	private Text myText;
	private int myLevel=0;
	private int myLives=0;
	private int myPoints=0;
	

	public InfoBox(int size) {
		Text text = new Text(10,size-60, "");
		text.setFill(Color.BLUE);
		text.setFont(new Font(15));
		myText = text;
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
