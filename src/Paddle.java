import java.util.ArrayList;

public class Paddle extends GamePiece{

	/**
	 * Largely the same as GamePiece, included for flexibility and consistency
	 */

    public static final String PADDLE_PIC = "paddle.gif";
	public static final int PADDLE_X_POS=BreakoutMain.SIZE/2;
    public static final int PADDLE_Y_POS=BreakoutMain.SIZE-50;
	
	public Paddle(double x, double y, String type) {
		super(x, y, type);
	}
	
	public static ArrayList<Paddle> buildPaddles()
	{
		Paddle startPaddle = new Paddle(PADDLE_X_POS, PADDLE_Y_POS, PADDLE_PIC);
		ArrayList <Paddle> toRet = new ArrayList<Paddle>();
		toRet.add(startPaddle);
		return toRet;
	}

}
