/**
 * StrongBlock class extends the Block class, and creates blocks
 * that require more than one hit to be destroyed.
 * @author maddiebriere
 *
 */


public class StrongBlock extends Block {

	public static final String STRONG_PIC = "strong.gif";

	public StrongBlock(double row, double col) {
		super(row, col, STRONG_PIC);
		setMyHits(2); // destroyed in TWO hits
		setMyPoints(BLOCK_POINTS); // Normal number of points
	}

	@Override
	public void takeHit() {
		setMyHits(getMyHits()-1);
	}

}
