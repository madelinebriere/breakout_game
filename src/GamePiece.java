import java.awt.Point;
import java.util.ArrayList;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class GamePiece {
	private ImageView myImage; //Each GamePiece has an image that represents 
	
	public GamePiece (double x, double y, String type)
	{
		myImage = buildImage(type, x, y);
	}
    
	/**
	 * All GamePieces will have to be updated in their own ways
	 */
	public abstract void update();
	
	/**
	 * Check if this GamePiece has collided with another GamePiece
	 */
	public boolean collide(GamePiece other)
	{
		if(this.myImage.intersects(other.myImage.getBoundsInLocal())) {return true;}
		else {return false;}
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
    
    public static <T extends GamePiece> void addImage(GamePiece piece, Group root)
    {
    	root.getChildren().add(piece.myImage);
    }
    
    
    /**
     * Use when variables change or image must be created in the first place
     * @return ImageView with GamePiece variables
     */
    public ImageView buildImage(String type, double x, double y) 
    {
    	Image im3 = new Image(getClass().getClassLoader().getResourceAsStream(type));
    	ImageView toRet = new ImageView(im3);
    	toRet.setX(x - toRet.getBoundsInLocal().getWidth() / 2);
    	toRet.setY(y - toRet.getBoundsInLocal().getHeight() / 2);
    	return toRet;
    }

    /**
     * @return x position of center of GamePiece
     */
	public double getX() {
		return myImage.getX();
	}
	/**
     * @return y position of center of GamePiece
     */
	public double getY() {
		return myImage.getY();
	}

	public double getCenterX(){
		return getX()+this.getWidth() / 2;
	}
	
	public double getCenterY(){
		return getY()+this.getHeight() / 2;
	}
	
	public void setX(double x) {
		myImage.setX(x);
	}


	public void setY(double y) {
		myImage.setY(y);
	}

	public ImageView getMyImage() {
		return myImage;
	}

	public void setMyImage(ImageView myImage) {
		this.myImage = myImage;
	}
	
	public double getHeight()
	{
		return myImage.getBoundsInLocal().getHeight();
	}
	
	public double getWidth()
	{
		return myImage.getBoundsInLocal().getWidth();
	}
	
}
