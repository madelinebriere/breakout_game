public class DoubleBlock extends Block {

	public static final String DOUBLE_PIC = "double.gif";

	public DoubleBlock(double row, double col) {
		super(row, col, DOUBLE_PIC);
		myHits = 1; // destroyed in one hit
		myPoints = 2 * BLOCK_POINTS; // Normal number of points
	}

	@Override
	public void takeHit() {
		myHits--;
	}

}
