import javafx.geometry.Bounds;
import javafx.scene.shape.Rectangle;

public class Ball extends GamePiece{
	//Default starting variables
    public static final int BALL_START_X=300;
    public static final int BALL_START_Y=300;
    public static final int START_X_MOV=0;
    public static final int START_Y_MOV=3; //start with ball moving down
    public static final String BALL_PIC = "dogball.gif";
    public static final int BLOCK_EDGE_BUFFER=1; //buffer when detecting ball on edges of block
	
	private int myXMov; //X velocity
	private int myYMov; //Y velocity

	public Ball(double x, double y, int xMov, int yMov)
	{
		super(x,y,BALL_PIC);
		myXMov=xMov;
		myYMov=yMov;
	}
	
	/**
	 * Generate the initial Ball object
	 * @param size: The size of the scene
	 * @param level: The game level (impacts speed)
	 * @return a Ball at the center of the screen, moving at the correct x and y velocity
	 */
    public static Ball buildStartBall(int size, int level){
    	return new Ball(size/2, size/2, START_X_MOV, START_Y_MOV+level-1);
    }

    public boolean offScreen(int size){
    	return this.getCenterY()>size;
    }
    
    public boolean ballBelowPaddle(Paddle paddle){
    	return this.getCenterY()>paddle.getY()+paddle.getHeight()/4;
    }
    
    /**
     * Set the x and y velocity of the ball according to where the ball has hit the paddle
     * --> Left side: 45 degree angle to the left
     * --> Middle: Straight up
     * --> Right: 45 degree angle to the right
     * @param p: Paddle that the Ball (this) has hit
     */
	public void bounceOffPaddle(Paddle p)
    {
		switch (paddleHitLocation(p)) //rebound ball if 1,2 or 3 (impact), 0 if none
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
    
    /**
     * Determines if and where ball has hit paddle
     * @param paddle, paddle that the ball has hit
     * @return int representing the location of the impact
     * Returns: 0 - not hit, 1 - left hit, 2 - middle hit, 3 - right hit
     */
    public int paddleHitLocation(Paddle paddle)
    {
    	//needed ball attributes
    	double bx = getCenterX();
    	
    	//paddle attributes
    	double px = paddle.getX();
    	double pw = paddle.getWidth();//paddle width
    	
    	if(collide(paddle) && !ballBelowPaddle(paddle)) //ball has hit paddle and is not below paddle
    	{
    		setMyYMov(-1*Math.abs(getMyYMov())); //ball moves up no matter what
    		
    		if(bx<(px+pw/3)) {return 1;} //ball hits left side
    		else if(bx<(px+(2*pw/3))){return 2;} //ball hits middle
    		else {return 3;}//ball hits right side
    	}
    	return 0; //default meaning no impact
    	
    }
 
    /**
     * Alters the movement of the ball if it is moving off of the screen --> bounce-like motion
     * @param size: Size of the scene
     */
    public void wallCheck(double size)
    {
	    	//ball attributes
	   	double bx = getX();
	   	double by = getY();
	   	double width = getWidth();
	    	
	   	//Boundary logic --> check for walls
	   	if(bx<0){setMyXMov(1*getNonZeroSpeed());}//if hits left wall, bounce back
	   	if(bx+width>size){setMyXMov(-getNonZeroSpeed());}//if hits right wall, bounce back
	   	if(by<0){setMyYMov(1*getNonZeroSpeed());} //if hits top, bounce back down
    }
    /**
     * Response to collision between ball and block
     * Alters the movement of the ball based on the edge it has hit to create natural motion 
     * @param b: The block that has been hit
     */
    public void bounceOffBlock(Block b){
    	if(overlapsBottom(b)){
    		setMyYMov(Math.abs(getMyYMov()));//y-motion down
    	}
    	else if(overlapsTop(b)){
    		setMyYMov(-Math.abs(getMyYMov()));//y-motion up
    	}
    	else if(overlapsLeft(b)){
    		setMyXMov(-Math.abs(getMyXMov()));//x-motion left
    	}
    	else if (overlapsRight(b)){
    		setMyXMov(Math.abs(getMyXMov()));//x-motion right
    	}

    }
    
    /**
     * Checks if the ball has overlapped with an edge, used in bouncing decision-making
     * @param b: Block that has been hit
     * @param edge: String representing the edge being checked
     * @return boolean,  true if the ball overlaps with the specific edge
     */
    public boolean overlapsEdge(Block b, String edge){
    	Rectangle rect = new Rectangle();
    	if(edge.equals("bottom")){
    		rect= new Rectangle(b.getX()-BLOCK_EDGE_BUFFER, b.getY()+b.getHeight()-BLOCK_EDGE_BUFFER, 
        			b.getWidth()+2*BLOCK_EDGE_BUFFER, 2*BLOCK_EDGE_BUFFER);
    	}
    	if(edge.equals("top")){
    		rect= new Rectangle(b.getX()-BLOCK_EDGE_BUFFER, b.getY()-BLOCK_EDGE_BUFFER, 
        			b.getWidth()+2*BLOCK_EDGE_BUFFER, 2*BLOCK_EDGE_BUFFER);
    	}
    	if(edge.equals("left")){
    		rect= new Rectangle(b.getX()-BLOCK_EDGE_BUFFER, b.getY()-BLOCK_EDGE_BUFFER,
        			2*BLOCK_EDGE_BUFFER, b.getHeight()+2*BLOCK_EDGE_BUFFER);
    	}
    	if(edge.equals("right")){
    		rect= new Rectangle(b.getX()+b.getWidth()-BLOCK_EDGE_BUFFER, b.getY()-BLOCK_EDGE_BUFFER, 
        			2*BLOCK_EDGE_BUFFER, b.getHeight()+2*BLOCK_EDGE_BUFFER);
    	}
    	Bounds bounds = rect.getBoundsInLocal();
    	return bounds.intersects(this.getMyImage().getBoundsInLocal());
    }
    
    public boolean overlapsBottom(Block b){return overlapsEdge(b,"bottom");}
    public boolean overlapsTop(Block b){return overlapsEdge(b,"top");}
    public boolean overlapsLeft(Block b){return overlapsEdge(b,"left");}
    public boolean overlapsRight(Block b){return overlapsEdge(b,"right");}
    
    
    /**
     * Automatically inherited from GamePiece, updated with other GamePieces
     */
	@Override
	public void update(int size, int level) {

        setX(getX() + getMyXMov());
        setY(getY() + getMyYMov());
        wallCheck(size);
	}

	public int getNonZeroSpeed()
	{
		if(myXMov!=0){return Math.abs(myXMov);}
		else{return Math.abs(myYMov);}
		
	}
	
	//Getters and setters
	public int getMyXMov() {return myXMov;}
	public void setMyXMov(int myXMov) {this.myXMov = myXMov;}
	public int getMyYMov() {return myYMov;}
	public void setMyYMov(int myYMov) {this.myYMov = myYMov;}
	
}
