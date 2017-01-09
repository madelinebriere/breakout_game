import java.awt.Point;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Class defining ball in game
 * @author Maddie Briere
 */


public class Ball 
{
	private String myImage;
	private int mySpeed;
	private double mySize;
	private ImageView myBouncer;
	private int myXMov; // xMov = motion in x --> 0=none, -1=left, 1=right
	private int myYMov; //yMov = motion in y --> 0=none, -1=down, 1=up
	
	public Ball()
	{
		myImage = "ball.gif"; //default = average ball
		mySpeed = 40; //default speed = medium
		mySize = 1.2; //default size = medium
		myYMov=1;
		myXMov=1; //Default motion = straight up
		Image im = new Image(getClass().getClassLoader().getResourceAsStream("ball.gif"));
        myBouncer = new ImageView(im);
        myBouncer.setScaleX(mySize);
        myBouncer.setScaleY(mySize);
        myBouncer.setX(100);
        myBouncer.setY(100);//default spot near upper left corner
	}
	
	/**
	 * @param image = image address, must be non-empty and defined image
	 * @param speed = speed ball travels, must be positive
	 * @param size = width and height of ball, must be positive
	 * @param x = x position of ball, must be positive
	 * @param y = y position of ball, must be positive
	 * @param xMov = motion in x direction (0, 1, -1)
	 * @param yMov = motion in y direction (0, 1, -1)
	 */
	
	public Ball(String image, int speed, int size, int x, int y, int xMov, int yMov)
	{
		myImage = image;
		mySpeed = speed;
		mySize = size;
		myXMov=xMov;
		myYMov=yMov;
		Image im = new Image(getClass().getClassLoader().getResourceAsStream(image));
        myBouncer = new ImageView(im);
        myBouncer.setX(x);
        myBouncer.setY(y);
	}

	public int getMyXMov() {
		return myXMov;
	}

	public void setMyXMov(int myXMov) {
		this.myXMov = myXMov;
	}

	public int getMyYMov() {
		return myYMov;
	}

	public void setMyYMov(int myYMov) {
		this.myYMov = myYMov;
	}


	public ImageView getMyBouncer() {
		return myBouncer;
	}

	public void setMyBouncer(ImageView myBouncer) {
		this.myBouncer = myBouncer;
	}

	public String getMyImage() {
		return myImage;
	}

	public void setMyImage(String myImage) {
		this.myImage = myImage;
	}

	public int getMySpeed() {
		return mySpeed;
	}

	public void setMySpeed(int mySpeed) {
		this.mySpeed = mySpeed;
	}

	public double getMySize() {
		return mySize;
	}

	public void setMySize(double mySize) throws Exception {
		this.mySize = mySize;
	}
	
	
}
