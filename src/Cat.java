// This entire file is part of my masterpiece.
// Maddie Briere

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
/**
 * A class that defines a "Cat" object, that updates by "falling" down
 * the screen. This class has narrow applications in that most of the implementation
 * is very specific to this particular version of Breakout.
 * 
 * New Objects of this class can be made using the constructor, or the buildCat()
 * method, which generates cats at random.
 * 
 * @author maddiebriere
 *
 */


public class Cat extends GamePiece {
	public static final int DEFAULT_TIME = 2000;

	public static final int FALL_RATE = 1;
	public static final String NYAN_CAT = "nyancat.gif";
	
	private int mySoloTime; // timer until this cat's partner enters the scene
	
	public Cat(double x, double y, String type) {
		super(x, y, type);
	}

	/**
	 * Randomized placement of new Cat at the top of the screen
	 * (i.e., x-location is randomized)
	 * 
	 * @param size,
	 *            used to decide where to place object
	 * @return new Cat
	 */
	public static Cat buildCat(int size) {
		int location = new Random().nextInt(size);
		return new Cat(location, 0, NYAN_CAT);
	}


	@Override
	public void update(int size, int level) {
		setY(getY() + FALL_RATE); // falling cat

	}
	
	/**
	 * Determines whether or not it is time to add a new cat
	 * 
	 * @param level
	 *            used in calculations in such a way that the time between
	 *            attacks decreases with increasing level, to increase the
	 *            difficulty of the game
	 * @return true if it is time to add a new cat, false otherwise
	 */
	public boolean timeToAttack(int level) {
		return mySoloTime * level >= DEFAULT_TIME;
	}
	
	//getters/ setters if needed
	public int getMySoloTime() {
		return mySoloTime;
	}

	public void setMySoloTime(int mySoloTime) {
		this.mySoloTime = mySoloTime;
	}
}


