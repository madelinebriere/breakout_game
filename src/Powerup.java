import java.util.ArrayList;
import java.util.Random;

public class Powerup extends GamePiece{
    public static final int FALL_RATE=1; //start with ball moving up
    public static final String[] POWERUPS = {"paddlepower.gif", "pointpower.gif", "lifepower.gif", "lifepower.gif"};
    //add one more?
	
    //What does this powerup do?
    private String myType; 
    /*
     * Types:
     * paddlepower.gif
     * pointspower.gif
     * lifepower.gif
     * 
     */
    //IS THIS A BETTER OPTION THAT SEPARATE CLASSES?
    //Might reduce flexibility
    
	public Powerup(double x, double y, String type) {
		super(x, y, type);
		myType=type;
	}
	
	public static void setPowers(int level, ArrayList<Block> blocks){
		Random randy = new Random();
		for(int i=0; i< level; i++){
			int randomIndex = randy.nextInt(blocks.size());
			Block pBlock = blocks.get(randomIndex);
			pBlock.setMyPower(new Powerup(pBlock.getCenterX(), pBlock.getCenterY(), POWERUPS[i]));
		}
	}

	@Override
	public void update(int size, int level) {
		setY(getY()+FALL_RATE); //falling powerup
	}
	
	public String getMyType() {
		return myType;
	}

	public void setMyType(String myType) {
		this.myType = myType;
	}

}
