/**
 * Paddle class produces an instance of GamePiece that is able to move from side 
 * to side on the bottom of the screen, deflecting Ball objects. It can have 
 * different images depending on its function, and keeps track of its movement
 * via doubles for motion in the left and right directions.
 * 
 * @author maddiebriere
 *
 */
public class Paddle extends GamePiece {

	public static final String RIGHT_PIC = "shortr.gif";
	public static final String LEFT_PIC = "shortl.gif";
	public static final String LONG_RIGHT_PIC = "longr.gif";
	public static final String LONG_LEFT_PIC = "longl.gif";
	public static final String LONG_PADDLE_PIC = "longpaddle.gif";

	// Paddle power levels
	public static final int PORTAL_LEVEL = 2;// level where paddle is able to
												// transport from one side to
												// other
	public static final int SPEED_LEVEL = 3; // level where paddle is able to
												// speed up as it moves in one
												// direction
	public static final int PADDLE_STEP = 5;// step made by paddle each time
											// left or right key is hit
	public static final double LITTLE_PADDLE_STEP = .2; // used for momentum
	public static final int Y_OFFSET = 80; // distance of paddle from bottom

	public static final int LONG_PADDLE_BUFFER = 35;
	// used to make changing picture from short --> long
	// less noticeable

	private double leftMove;
	private double rightMove;

	private boolean reset; // true if the paddle has been reset by a powerup

	public Paddle(double x, double y, String type) {
		super(x, y, type);
		reset = false;
		leftMove = 0;
		rightMove = 0;
	}

	public void moveRight(int size, int level) {
		if (getX() + getWidth() < size - rightMove)
			setX(getX() + rightMove);
		else if (level >= PORTAL_LEVEL)
			setX(0);
		if (level >= SPEED_LEVEL)
			rightMove += LITTLE_PADDLE_STEP;
		// Otherwise paddle cannot move
	}

	public void moveLeft(int size, int level) {
		if (getX() > leftMove)
			setX(getX() - leftMove);
		else if (level >= PORTAL_LEVEL)
			setX(size - getWidth());
		if (level >= SPEED_LEVEL)
			leftMove += LITTLE_PADDLE_STEP;
		// Otherwise paddle cannot move

	}

	public static Paddle buildStartPaddle(int size) {
		return new Paddle(size / 2, size - Y_OFFSET, LEFT_PIC);
	}

	public static Paddle buildLongLeft(Paddle p) {
		return buildPaddle(p, LONG_LEFT_PIC);
	}

	public static Paddle buildLongRight(Paddle p) {
		return buildPaddle(p, LONG_RIGHT_PIC);
	}

	public static Paddle buildLeft(Paddle p) {
		return buildPaddle(p, LEFT_PIC);
	}

	public static Paddle buildRight(Paddle p) {
		return buildPaddle(p, RIGHT_PIC);
	}

	public static Paddle buildPaddle(Paddle p, String pic) {
		Paddle toRet = new Paddle(0, 0, pic);
		toRet.copyPrevSettings(p);
		return toRet;
	}

	public void copyPrevSettings(Paddle p) {
		this.centerPaddle(p);
		this.setLeftMove(p.getLeftMove());
		this.setRightMove(p.getRightMove());
		this.setReset(p.isReset());
	}

	public void centerPaddle(Paddle prev) {
		setCenterX(prev.getCenterX());// center on x
		setCenterY(prev.getCenterY());// center on y
	}

	public Paddle resetImage() {
		Paddle toRet;
		if (isReset()) { // if paddlePower picked up
			if (rightMove > 0) {
				toRet = Paddle.buildLongRight(this);
			} else {
				toRet = Paddle.buildLongLeft(this);
			}
		} else { // reset image to correct direction
			if (leftMove > 0) {
				toRet = Paddle.buildLeft(this);
			} else {
				toRet = Paddle.buildRight(this);
			}
		}
		return toRet;
	}

	@Override
	public void update(int size, int level) {
		if (leftMove > 0) {
			moveLeft(size, level);
		}
		if (rightMove > 0) {
			moveRight(size, level);
		}
	}

	public double getLeftMove() {
		return leftMove;
	}

	public void stopLeftMove() {
		this.leftMove = 0;
	}

	public void startLeftMove() {
		this.leftMove = PADDLE_STEP;
	}

	public void setLeftMove(double leftMove) {
		this.leftMove = leftMove;
	}

	public double getRightMove() {
		return rightMove;
	}

	public void stopRightMove() {
		this.rightMove = 0;
	}

	public void startRightMove() {
		this.rightMove = PADDLE_STEP;
	}

	public void setRightMove(double rightMove) {
		this.rightMove = rightMove;
	}

	public boolean isReset() {
		return reset;
	}

	public void setReset(boolean reset) {
		this.reset = reset;
	}

}
