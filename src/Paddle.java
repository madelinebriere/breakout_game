import java.util.ArrayList;

public class Paddle extends GamePiece{

	public static final int PADDLE_STEP=15;
	/**
	 * Largely the same as GamePiece, included for flexibility and consistency
	 */

    public static final String PADDLE_PIC = "paddle.gif";
    public static final String LONG_PADDLE_PIC="longpaddle.gif";
	
	public Paddle(double x, double y, String type) {
		super(x, y, type);
	}
	
	public static Paddle buildStartPaddle(int size)
	{
		return new Paddle(size/2, size-50, PADDLE_PIC);
	}
	
	
	
	public void moveRight(){
		this.setX(getX()+PADDLE_STEP);
	}
	
	public void moveLeft(){
		this.setX(getX()-PADDLE_STEP);
	}

	@Override
	public void update() {
		//No automatic updates for paddle as of current version
	}

}
