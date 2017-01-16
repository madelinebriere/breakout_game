
public class ConcreteBlock extends Block{
	
	public static final String CONCRETE_PIC = "concrete.gif";
	
	public ConcreteBlock(double x, double y)
	{
		super(x, y, CONCRETE_PIC);
		setMyHits(1);
		setMyPoints(0); // Never destroyed, no points gained
	}

	@Override
	public boolean handleCollision() {
		//No change to hits
		return false;
	}

}
