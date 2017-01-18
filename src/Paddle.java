import java.util.ArrayList;

public class Paddle extends GamePiece{

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
		return new Paddle(size/2, size-80, PADDLE_PIC);
	}

	@Override
	public void update() {
		//No automatic updates for paddle as of current version
	}

}
