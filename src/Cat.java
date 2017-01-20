import java.util.Random;

public class Cat extends GamePiece{

	public static final int FALL_RATE = 1;
	public static final String NYAN_CAT = "nyancat.gif";
	
	public Cat(double x, double y, String type) {
		super(x, y, type);
	}

	public static Cat buildCat(int size){
		Random randy = new Random();
		int location = randy.nextInt(size);
		Cat toRet = new Cat(location, 0, NYAN_CAT);
		return toRet;
	}
	
	@Override
	public void update() {
		setY(getY()+FALL_RATE); //falling cat
		
	}

}
