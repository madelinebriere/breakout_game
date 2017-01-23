
public class ConcreteBlock extends Block {

	public static final String CONCRETE_PIC = "concrete.gif";

	public ConcreteBlock(double row, double col) {
		super(row, col, CONCRETE_PIC);
		setMyHits(1);
		setMyPoints(0); // Never destroyed, no points gained
	}

	@Override
	public boolean isDestroyed() {
		return false;
	}

	@Override
	public void takeHit() {
	} // no action taken for indestructible block

}
