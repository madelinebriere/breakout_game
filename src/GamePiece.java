import java.awt.Point;
import java.util.ArrayList;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GamePiece {
	private double myX;
	private double myY;
	private String myType; //string for picture of ball type
	private ImageView myImage;
	
	public GamePiece (double x, double y, String type)
	{
		myX=x;
		myY=y;
		myType=type;
		myImage = this.buildImage();
		
	}
    
	/**
	 * 
	 * @param pieces, a List of pieces
	 * Add all pieces to a Group in some scene
	 */
    public static <T extends GamePiece> void addImages(ArrayList<T> pieces, Group root)
    {
    	for(GamePiece g: pieces){root.getChildren().add(g.myImage);}
    }
    
    /**
     * Use when variables change or image must be created in the first place
     * @return ImageView with GamePiece variables
     */
    public ImageView buildImage() 
    {
    	Image im3 = new Image(getClass().getClassLoader().getResourceAsStream(myType));
    	ImageView toRet = new ImageView(im3);
    	/**
    	 * 
    	 * 
    	 * Scale needed?
    	 */
    	toRet.setX(myX - toRet.getBoundsInLocal().getWidth() / 2);
    	toRet.setY(myY - toRet.getBoundsInLocal().getHeight() / 2);
    	return toRet;
    }

	public double getMyX() {
		return myX;
	}

	public void setMyX(double myX) {
		this.myX = myX;
		this.myImage = buildImage(); //reset image
	}

	public double getMyY() {
		return myY;
	}

	public void setMyY(double myY) {
		this.myY = myY;
		this.myImage = buildImage();//reset image
	}

	public String getMyType() {
		return myType;
	}

	public void setMyType(String myType) {
		this.myType = myType;
		this.myImage = buildImage();//reset image
	}

	public ImageView getMyImage() {
		return myImage;
	}

	public void setMyImage(ImageView myImage) {
		this.myImage = myImage;
	}
	
	
}
