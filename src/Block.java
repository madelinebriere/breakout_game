/**
 * The Block class is the superclass for all of the subclass __Block classes
 * 
 * It gives each Block a certain number of hits and points, a power-up and its
 * position by row and column. The Block class is an extension of the GamePiece 
 * class, meaning that it is updated via its ImageView variable.
 * 
 * All blocks are able to "take a hit", that is, react uniquely to being hit,
 * and to know if they have been destroyed or not.
 * 
 * This class assumes all Blocks are represented in certain rows and columns (a grid),
 * and that each Block has a specific image associated with it.
 * It also assumes that each block requires updating (via the update method).
 * 
 * @author maddiebriere
 */
public abstract class Block extends GamePiece {

	public static final int BLOCK_POINTS = 10; // standard # points
	// Brick grid variables
	public static final double BRICK_ROW_INC = 25; // distance between rows
	public static final double BRICK_COL_INC = 70; // distance between cols
	/**
	 * These variables have been declared as protected because 
	 * a public declaration would give unwarranted control to 
	 * outside classes and a private declaration would hide
	 * these variables from child classes -- the latter is 
	 * extremely non-viable because these variables have
	 * specific functions for each child class --> it makes
	 * more sense for them to have protected access to these
	 * variables
	 */
	protected int myHits; // how many hits the block can receive
	protected int myPoints;// how many points the block is worth
	protected Powerup myPower; // the powerup held by this block
	private double myRow;
	private double myCol;

	public Block(double row, double col, String type) {
		super(col * BRICK_COL_INC, row * BRICK_ROW_INC, type);
		myPower = null;
		myRow = row;
		myCol = col;
	}

	/**
	 * @return boolean, true if the block is destroyed
	 */
	public boolean isDestroyed() {
		return myHits <= 0;
	}

	/**
	 * Alters the block based on it's type (Each type of block must react to a
	 * collision in a unique way)
	 */
	public abstract void takeHit();

	@Override
	public void update(int size, int level) {
		// No movement necessary for blocks in this version of the game
	}

	// getters and setters
	public int getMyHits() {
		return myHits;
	}

	public void setMyHits(int myHits) {
		this.myHits = myHits;
	}

	public int getMyPoints() {
		return myPoints;
	}

	public void setMyPoints(int myPoints) {
		this.myPoints = myPoints;
	}

	public Powerup getMyPower() {
		return myPower;
	}

	public void setMyPower(Powerup myPower) {
		this.myPower = myPower;
	}

	public double getMyRow() {
		return myRow;
	}

	public void setMyRow(double myRow) {
		this.myRow = myRow;
	}

	public double getMyCol() {
		return myCol;
	}

	public void setMyCol(double myCol) {
		this.myCol = myCol;
	}

}
