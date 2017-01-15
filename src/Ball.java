import java.util.ArrayList;

public class Ball extends GamePiece{

	  //Ball variables
    public static final int BALL_START_X=BreakoutMain.SIZE/2;
    public static final int BALL_START_Y=BreakoutMain.SIZE/2;
    public static final int START_X_MOV=0;
    public static final int START_Y_MOV=1; //start with ball moving up
    public static final int START_SPEED=110;
    public static final String BALL_PIC = "dogball.gif";
	
	private int mySpeed;
	private int myXMov; //-1 left, 0 none, 1 right
	private int myYMov; //-1 down, 0 none, 1 up

	/*
	 * Type of ball can be:
	 * "ball.gif"
	 */
	
	public Ball(double x, double y, String type, int speed, int xMov, int yMov)
	{
		super(x,y,type);
		mySpeed = speed;
		myXMov=xMov;
		myYMov=yMov;
	}
	
    public static ArrayList<Ball> buildBalls()
    {
    	Ball startBall = new Ball(BALL_START_X, BALL_START_Y,  
    			BALL_PIC, START_SPEED, START_X_MOV, START_Y_MOV);
        ArrayList <Ball> balls = new ArrayList<Ball>();
        balls.add(startBall);
        return balls;
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

	public int getMySpeed() {
		return mySpeed;
	}

	public void setMySpeed(int mySpeed) {
		this.mySpeed = mySpeed;
	}
	

	
}
