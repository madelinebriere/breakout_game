
public class StrongBlock extends Block {

	public static final String STRONG_PIC = "strong.gif";

	public StrongBlock(double row, double col) {
		super(row, col, STRONG_PIC);
		myHits = 2; // destroyed in TWO hits
		myPoints = BLOCK_POINTS; // Normal number of points
	}

	@Override
	public void takeHit() {
		myHits--;
	}

}
