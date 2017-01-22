import java.util.Random;

public class Cat extends GamePiece{

	public static final int FALL_RATE = 1;
	public static final String NYAN_CAT = "nyancat.gif";
	public static final int DEFAULT_TIME=2000;
	
	private int mySoloTime; //timer until this cat's partner enters the scene
	
	public Cat(double x, double y, String type) {
		super(x, y, type);
		mySoloTime=0;
	}

	public static Cat buildCat(int size){
		Random randy = new Random();
		int location = randy.nextInt(size);
		Cat toRet = new Cat(location, 0, NYAN_CAT);
		return toRet;
	}
	
	public boolean timeToAttack(int level){
		return mySoloTime*level >= DEFAULT_TIME;
	}
	
	@Override
	public void update(int size, int level) {
		setY(getY()+FALL_RATE); //falling cat
		mySoloTime++;
		
	}
	
	public int getMySoloTime() {
		return mySoloTime;
	}

	public void setMySoloTime(int mySoloTime) {
		this.mySoloTime = mySoloTime;
	}

}
