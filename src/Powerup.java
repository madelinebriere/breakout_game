/**
 * The Powerup class produces powerups that, when collected by a Paddle
 * (when they collide with a Paddle) can give the game player special abilities for 
 * a short amount of time. This class extends the GamePiece class, so it is
 * updated via its own ImageView.
 * 
 * @author maddiebriere
 */

import java.util.ArrayList;
import java.util.Random;

public class Powerup extends GamePiece {
	public static final int FALL_RATE = 1; // start with ball moving up
	public static final String[] POWERUPS = { "paddlepower.gif", "pointpower.gif", "lifepower.gif", "lifepower.gif" };

	// What does this powerup do?
	private String myType; // options in POWERUPS

	public Powerup(double x, double y, String type) {
		super(x, y, type);
		myType = type;
	}

	public static void setPowers(int level, ArrayList<Block> blocks) {
		Random randy = new Random();
		for (int i = 0; i < level; i++) {
			int randomIndex = randy.nextInt(blocks.size()); // choose a random
															// block index
			Block pBlock = blocks.get(randomIndex);// get that block
			pBlock.setMyPower(new Powerup(pBlock.getCenterX(), pBlock.getCenterY(), POWERUPS[i]));
			// set its powerup to POWERUPS[i]
		}
	}

	@Override
	public void update(int size, int level) {
		setY(getY() + FALL_RATE); // falling powerup
	}

	public String getMyType() {
		return myType;
	}

	public void setMyType(String myType) {
		this.myType = myType;
	}

}
