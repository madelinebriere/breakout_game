/**
 * Extension of Block class, produces normal blocks that are destroyed
 * in one hit and drop a normal number of points. 
 * 
 * @author maddiebriere
 *
 */

public class NormalBlock extends Block {

	public static final String NORMAL_PIC = "normal.gif";

	public NormalBlock(double x, double y) {
		super(x, y, NORMAL_PIC);
		setMyHits(1); // destroyed in one hit
		setMyPoints(BLOCK_POINTS); // Normal number of points
	}

	@Override
	public void takeHit() {
		setMyHits(getMyHits()-1);
	}

}
