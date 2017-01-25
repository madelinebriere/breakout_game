import java.util.*;

/**
 * The GameManager keeps track of all of the GamePieces in play.
 * It has a List to hold all GamePieces in action, a collision list 
 * for checking collision and a clean-up list for removing items from the 
 * scene. Creating a GameManager object allows access to all of these capabilities.
 * 
 * A single instance of the GameManager class is used in BreakoutWorld to keep
 * track of the GamePieces for the entire game -- all changes are made via the 
 * GameManager's method.
 * 
 * @author Carl Dea (cdea), slightly modified by maddiebriere
 */
public class GameManager {

	/** All the GamePiece objects currently in play */
	private final static List<GamePiece> GAME_ACTORS = new ArrayList<>();
	/**
	 * A global single threaded list used to check collision against other
	 * GamePiece objects.
	 */
	private final static List<GamePiece> CHECK_COLLISION_LIST = new ArrayList<>();

	/**
	 * A global single threaded set used to cleanup or remove GamePiece objects
	 * in play.
	 */
	private final static Set<GamePiece> CLEAN_UP_PIECES = new HashSet<>();

	public List<GamePiece> getAllPieces() {
		return GAME_ACTORS;
	}

	public void removeAllPieces() {
		GAME_ACTORS.clear();
	}

	public void addPieces(GamePiece... gamePieces) {
		GAME_ACTORS.addAll(Arrays.asList(gamePieces));
	}

	public void removePieces(GamePiece... gamePieces) {
		GAME_ACTORS.removeAll(Arrays.asList(gamePieces));
	}

	public Set<GamePiece> getPiecesToBeRemoved() {
		return CLEAN_UP_PIECES;
	}

	public void addPiecesToBeRemoved(GamePiece... gamePieces) {
		if (gamePieces.length > 1) {
			CLEAN_UP_PIECES.addAll(Arrays.asList(gamePieces));
		} else {
			CLEAN_UP_PIECES.add(gamePieces[0]);
		}
	}

	public List<GamePiece> getCollisionsToCheck() {
		return CHECK_COLLISION_LIST;
	}

	public void resetCollisionsToCheck() {
		CHECK_COLLISION_LIST.clear();
		CHECK_COLLISION_LIST.addAll(GAME_ACTORS);
	}

	public void cleanupPieces() {

		// remove from actors list
		GAME_ACTORS.removeAll(CLEAN_UP_PIECES);

		// reset the clean up sprites
		CLEAN_UP_PIECES.clear();
	}

}