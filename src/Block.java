import java.awt.Point;

public class Block {
	private String myType;
	/*
	 * Block types:
	 * purple - normal
	 * black - double hit
	 * yellow - double points
	 * POWER UPS: Drops power up when destroyed
	 * 	question - answer guess
	 * 	laser - laser beam
	 * 	paddle - larger paddle
	 */
	private Point myPos;
	
	public Block()
	{
		myType = "purple";
		myPos = new Point (0,0);
	}
	
	/**
	 * 
	 * @param type
	 * Options:
	 * "purple" "black" "yellow" question" "laser" "paddle"
	 * @param pos
	 */
	public Block (String type, Point pos)
	{
		myType = type;
		myPos = pos;
	}

	public String getMyType() {
		return myType;
	}

	public void setMyType(String myType) {
		this.myType = myType;
	}

	public Point getMyPos() {
		return myPos;
	}

	public void setMyPos(Point myPos) {
		this.myPos = myPos;
	}

}
