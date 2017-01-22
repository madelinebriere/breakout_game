

public class NormalBlock extends Block{
	
	public static final String NORMAL_PIC = "normal.gif";
	
	public NormalBlock(double x, double y)
	{
		super(x, y, NORMAL_PIC);
		myHits=1; //destroyed in one hit
		myPoints=BLOCK_POINTS; //Normal number of points
	}

	@Override
	public boolean isDestroyed() {
		myHits--;
		return true;
	}

}
