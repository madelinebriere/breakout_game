/**
 * Subclass of Block, creates a Block worth double points
 * 
 * @author maddiebriere
 *
 */
public class DoubleBlock extends Block {

	public static final String DOUBLE_PIC = "double.gif";

	public DoubleBlock(double row, double col) {
		super(row, col, DOUBLE_PIC);
		setMyHits(1); // destroyed in one hit
		setMyPoints(2 * BLOCK_POINTS); // Normal number of points
	}

	@Override
	public void takeHit() {
		setMyHits(getMyHits()-1);
	}

}
