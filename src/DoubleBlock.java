public class DoubleBlock extends Block{
	
	public static final String DOUBLE_PIC = "normal.gif";
	
	public DoubleBlock(double x, double y)
	{
		super(x, y, DOUBLE_PIC);
		myHits=1; //destroyed in one hit
		myPoints=2*BLOCK_POINTS; //Normal number of points
	}

	@Override
	public boolean handleCollision() {
		myHits--;
		return true;
	}

}
