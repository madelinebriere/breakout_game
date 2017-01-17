import java.util.ArrayList;

public class Ball extends GamePiece{

	  //Ball variables
    public static final int BALL_START_X=300;
    public static final int BALL_START_Y=300;
    public static final int START_X_MOV=0;
    public static final int START_Y_MOV=3; //start with ball moving up
    public static final String BALL_PIC = "dogball.gif";
    public static final int PADDLE_BUFFER=5;
	
	private int myXMov; //X velocity
	private int myYMov; //Y velocity

	public Ball(double x, double y, int xMov, int yMov)
	{
		super(x,y,BALL_PIC);
		myXMov=xMov;
		myYMov=yMov;
	}
	
    public static Ball buildStartBall(int level)
    {
    	return new Ball(BALL_START_X, BALL_START_Y, START_X_MOV, START_Y_MOV+level-1);
    }

    public boolean ballBelowPaddle(Paddle p){
    	return this.getY()>p.getY();
    }
    
	public void paddleCheck(Paddle p)
    {
    	 switch (paddleHitLoc(p)) //rebound ball if 1,2 or 3 (impact), 0 if none
		 {
	    	//Sideways bouncing based on y component	
		   	case 1: 
	    		setMyXMov(getMyYMov()*1);
		   		break;
		   	case 2:
		   		setMyXMov(0);
		   		break;
		   	case 3:
		   		setMyXMov(getMyYMov()*-1);
		   		break;
		  }
	    
    	
    }
    
    /*
     * Determines if and where ball has hit paddle
     * Returns: 0 - not hit, 1 - left hit, 2 - middle hit, 3 - right hit
     */
    public int paddleHitLoc(Paddle paddle)
    {
    	//needed ball attributes
    	double bx = getCenterX();
    	
    	//paddle attributes
    	double px = paddle.getX();
    	double pw = paddle.getWidth();//paddle width
    	
    	if(collide(paddle)) //ball hits paddle
    	{
    		setMyYMov(-1*Math.abs(getMyYMov())); //ball moves up no matter what
    		
    		if(bx<(px+pw/3)) {return 1;} //ball hits left side, bounce up and to left
    		else if(bx<(px+(2*pw/3))){return 2;} //ball hits middle, go straight up
    		else if(bx<(px+pw)){return 3;}//ball hits left, bounce up and to right
    		
    	}
    	return 0; //default meaning no impact
    	
    }
 
    //return true if the ball has fallen off the bottom of the screen
    public boolean wallCheck(double size)
    {
	    	//ball attributes
	   	double bx = getX();
	   	double by = getY();
	   	double width = getWidth();
	   	double height = getHeight();
	    	
	   	//Boundary logic --> check for walls
	   	if(bx<0){setMyXMov(1*getNonZeroSpeed());}//if hits left wall, bounce back
	   	if(bx+width>size){setMyXMov(-getNonZeroSpeed());}//if hits right wall, bounce back
	   	if(by<0){setMyYMov(1*getNonZeroSpeed());} //if hits top, bounce back down
	   	if(by+height>size){return true;} // if passes through bottom, this ball "is dead" and a new one must be created
    	return false;
    }
    
    /**
     * Troubleshoot
     * @param b
     */
    public void blockBounce(Block b){
    	if(bottom(b)||top(b)){setMyYMov(-getMyYMov());}
    	else if(left(b)||right(b)){setMyXMov(-getMyXMov());}
    	
/*
 *     finish logic
 */
    }
    
    //Fix logic
    public boolean bottom(Block b){
    	return verticalColumn(b) && getY()>b.getCenterY() && getY()<b.getY()+b.getHeight();
    }
    
    public boolean top(Block b){
    	return verticalColumn(b) && getY()<b.getCenterY() && getY()>b.getY();
    }
    
    public boolean left(Block b){
    	return horizontalColumn(b) && getX()<b.getCenterX() && getX()>b.getX();
    }
    
    public boolean right(Block b){
    	return horizontalColumn(b) && getX()>b.getCenterX() && getX()<b.getX()+b.getWidth();
     }
    //Does the ball hit the top or bottom edge?
    public boolean verticalColumn(Block b){
    	return getCenterX()>=b.getX() && getCenterX()<=(b.getX()+b.getWidth());
    }
    
    //Does the ball hit the left or right edge
    public boolean horizontalColumn(Block b){
    	return getCenterY()>=b.getY() && getCenterY()<=(b.getY()+b.getHeight());
    }
    
    
    //Move ball
	@Override
	public void update() {

        setX(getX() + getMyXMov());
        setY(getY() + getMyYMov());
	}

	public int getNonZeroSpeed()
	{
		if(myXMov!=0){return Math.abs(myXMov);}
		else{return Math.abs(myYMov);}
		
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
	
}
