import javafx.geometry.Bounds;
import javafx.scene.shape.Rectangle;
/**
 * Ball class generates a "Ball" object that can bounce off
 * of walls and into other objects. It holds information about velocity,
 * position, photo, etc. The Ball class is an extension of the 
 * GamePiece class, which gives it a specific ImageView.
 * 
 * Objects of this class are created and then updated via their
 * own ImageViews. In this manner, they can be animated and interact
 * with other objects (walls, blocks, paddles).
 * 
 * One point of inflexibility in this class is the fact that the speed of the ball
 * is just extrapolated from each component in any situation -- it is challenging
 * to actually set any direction/speed without messing up the code.
 * 
 * @author maddiebriere
 *
 */
public class Ball extends GamePiece {
	public static final int BALL_START_X = 300;
	public static final int BALL_START_Y = 300;
	public static final int START_X_MOV = 0;
	public static final int START_Y_MOV = 3; // start with ball moving down
	public static final String BALL_PIC = "dogball.gif";
	public static final int BLOCK_EDGE_BUFFER = 1; // buffer when detecting ball
													// on edges of block

	private int myXMov; // X velocity
	private int myYMov; // Y velocity

	public Ball(double x, double y, int xMov, int yMov) {
		super(x, y, BALL_PIC);
		myXMov = xMov;
		myYMov = yMov;
	}

	/**
	 * Generate the initial Ball object
	 * 
	 * @param size:
	 *            The size of the scene
	 * @param level:
	 *            The game level (impacts speed)
	 * @return a Ball at the center of the screen, moving at the correct x and y
	 *         velocity
	 */
	public static Ball buildStartBall(int size, int level) {
		return new Ball(size / 2, size / 2, START_X_MOV, START_Y_MOV + level - 1);
	}

	public boolean offScreen(int size) {
		return this.getCenterY() > size;
	}

	public boolean ballBelowPaddle(Paddle paddle) {
		return this.getCenterY() > paddle.getY() + paddle.getHeight() / 4;
	}

	/**
	 * Set the x and y velocity of the ball according to where the ball has hit
	 * the paddle 
	 * No impact (0) : Do nothing 
	 * Left side (1) : 45 degree angle to the left 
	 * Middle (2) : Straight up 
	 * Right (3) : 45 degree angle to the right
	 * 
	 * @param p:
	 *            Paddle that the Ball (this) has hit
	 */
	public void bounceOffPaddle(Paddle p) {
		switch (paddleHitLocation(p)) {
		case 1:
			setMyXMov(-getSpeed());// left motion
			break;
		case 2:
			setMyXMov(0);
			break;
		case 3:
			setMyXMov(getSpeed()); // right motion
			break;
		}
	}

	/**
	 * Determines if and where ball has hit paddle
	 * 
	 * @param paddle,
	 *            paddle that the ball has hit
	 * @return int representing the location of the impact Returns: 0 - not hit,
	 *         1 - left hit, 2 - middle hit, 3 - right hit
	 */
	public int paddleHitLocation(Paddle paddle) {

		if (collide(paddle) && !ballBelowPaddle(paddle)) {
			setUpMov();//same no matter where the ball hits
			if (hitsLeft(paddle)) {
				return 1;
			} else if (hitsMiddle(paddle)) {
				return 2;
			} else {
				return 3;
			} // ball hits right side
		}
		return 0; // default meaning no impact

	}

	public boolean hitsLeft(Paddle paddle) {
		return getCenterX() < (paddle.getX() + paddle.getWidth() / 3);
	}

	public boolean hitsMiddle(Paddle paddle) {
		return getCenterX() < (paddle.getX() + (2 * paddle.getWidth() / 3));
	}

	/**
	 * Alters the movement of the ball if it is moving off of the screen -->
	 * bounce-like motion
	 * 
	 * @param size:
	 *            Size of the scene
	 */
	public void wallCheck(double size) {
		if (this.getX() < 0) {
			setRightMov();
		} // if hits left wall, bounce back
		if (this.getX() + this.getWidth() > size) {
			setLeftMov();
		} // if hits right wall, bounce back
		if (this.getY() < 0) {
			setDownMov();
		} // if hits top, bounce back down
	}

	/**
	 * Response to collision between ball and block Alters the movement of the
	 * ball based on the edge it has hit to create natural motion
	 * 
	 * @param b:
	 *            The block that has been hit
	 */
	public void bounceOffBlock(Block b) {
		if (overlapsBottom(b)) {
			setDownMov();// y-motion down
		} else if (overlapsTop(b)) {
			setUpMov();// y-motion up
		} else if (overlapsLeft(b)) {
			setLeftMov();// x-motion left
		} else if (overlapsRight(b)) {
			setRightMov();// x-motion right
		}

	}

	/**
	 * Checks if the ball has overlapped with an edge, used in bouncing
	 * decision-making
	 * 
	 * @param b:
	 *            Block that has been hit
	 * @param edge:
	 *            String representing the edge being checked
	 * @return boolean, true if the ball overlaps with the specific edge
	 */
	public boolean overlapsEdge(Block b, String edge) {
		Rectangle rect = new Rectangle();
		if (edge.equals("bottom")) {
			rect = new Rectangle(b.getX() - BLOCK_EDGE_BUFFER, b.getY() + b.getHeight() - BLOCK_EDGE_BUFFER,
					b.getWidth() + 2 * BLOCK_EDGE_BUFFER, 2 * BLOCK_EDGE_BUFFER);
		}
		if (edge.equals("top")) {
			rect = new Rectangle(b.getX() - BLOCK_EDGE_BUFFER, b.getY() - BLOCK_EDGE_BUFFER,
					b.getWidth() + 2 * BLOCK_EDGE_BUFFER, 2 * BLOCK_EDGE_BUFFER);
		}
		if (edge.equals("left")) {
			rect = new Rectangle(b.getX() - BLOCK_EDGE_BUFFER, b.getY() - BLOCK_EDGE_BUFFER, 2 * BLOCK_EDGE_BUFFER,
					b.getHeight() + 2 * BLOCK_EDGE_BUFFER);
		}
		if (edge.equals("right")) {
			rect = new Rectangle(b.getX() + b.getWidth() - BLOCK_EDGE_BUFFER, b.getY() - BLOCK_EDGE_BUFFER,
					2 * BLOCK_EDGE_BUFFER, b.getHeight() + 2 * BLOCK_EDGE_BUFFER);
		}
		Bounds bounds = rect.getBoundsInLocal();
		return bounds.intersects(this.getMyImage().getBoundsInLocal());
	}

	public boolean overlapsBottom(Block b) {
		return overlapsEdge(b, "bottom");
	}

	public boolean overlapsTop(Block b) {
		return overlapsEdge(b, "top");
	}

	public boolean overlapsLeft(Block b) {
		return overlapsEdge(b, "left");
	}

	public boolean overlapsRight(Block b) {
		return overlapsEdge(b, "right");
	}

	/**
	 * Automatically inherited from GamePiece, updated with other GamePieces
	 */
	@Override
	public void update(int size, int level) {

		setX(getX() + getMyXMov());
		setY(getY() + getMyYMov());
		wallCheck(size);
	}

	public int getSpeed() {
		if (myXMov != 0) {
			return Math.abs(myXMov);
		}

		else {
			return Math.abs(myYMov);
		}
	}

	public void setRightMov() {
		myXMov = Math.abs(myXMov);
	}

	public void setDownMov() {
		myYMov = Math.abs(myYMov);
	}

	public void setLeftMov() {
		myXMov = -Math.abs(myXMov);
	}

	public void setUpMov() {
		myYMov = -Math.abs(myYMov);
	}

	// Getters and setters
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
