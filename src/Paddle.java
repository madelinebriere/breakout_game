import java.util.ArrayList;

public class Paddle extends GamePiece{

	/**
	 * Largely the same as GamePiece, included for flexibility and consistency
	 */

    public static final String RIGHT_PIC = "shortr.gif";
    public static final String LEFT_PIC = "shortl.gif";
    public static final String LONG_RIGHT_PIC = "longr.gif";
    public static final String LONG_LEFT_PIC = "longl.gif";
    public static final String LONG_PADDLE_PIC="longpaddle.gif";
    
    public static final int LONG_PADDLE_BUFFER=35;
    private boolean movingLeft; // set default to left to avoid errors
	private boolean movingRight;//used to point paddle in the correct direction


	//has the paddle been reset with a popwerup?
    //needed to avoid concurrent modification exception
    private boolean reset;
	
	public Paddle(double x, double y, String type) {
		super(x, y, type);
		movingLeft = true;
		movingRight = false;
		reset=false;
	}

	public static Paddle buildStartPaddle(int size)
	{
		return new Paddle(size/2, size-80, LEFT_PIC);
	}

	public static Paddle buildLongLeft(Paddle p){
		Paddle toRet = new Paddle(0,0,LONG_LEFT_PIC);
		toRet.copyPrevSettings(p);
		return toRet;
	}
	
	public static Paddle buildLongRight(Paddle p){
		Paddle toRet = new Paddle(0,0,LONG_RIGHT_PIC);
		toRet.copyPrevSettings(p);
		return toRet;
	}
	
	public static Paddle buildLeft(Paddle p){
		Paddle toRet = new Paddle(0, 0,LEFT_PIC);
		toRet.copyPrevSettings(p);
		return toRet;
	}
	
	public static Paddle buildRight(Paddle p){
		Paddle toRet = new Paddle(0,0,RIGHT_PIC);
		toRet.copyPrevSettings(p);
		return toRet;
	}
	
	public void copyPrevSettings(Paddle p){
		this.centerPaddle(p);
		this.setMovingLeft(p.isMovingLeft());
		this.setMovingRight(p.isMovingRight());
	}
	
	public void centerPaddle(Paddle prev){
		setCenterX(prev.getCenterX());//center on x
		setCenterY(prev.getCenterY());//center on y
	}
	
	 public boolean isMovingLeft() {
		return movingLeft;
	 }

	public void setMovingLeft(boolean movingLeft) {
		this.movingLeft = movingLeft;
	}

	public boolean isMovingRight() {
		return movingRight;
	}

	public void setMovingRight(boolean movingRight) {
		this.movingRight = movingRight;
	}
	
	public boolean isReset() {
		return reset;
	}

	public void setReset(boolean reset) {
		this.reset = reset;
	}

	
	@Override
	public void update() {
		//No automatic updates for paddle as of current version
	}

}
