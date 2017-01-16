import java.util.*;
 
/**
 * Sprite manager is responsible for holding all sprite objects, and cleaning up
 * sprite objects to be removed. All collections are used by the JavaFX
 * application thread. During each cycle (animation frame) sprite management
 * occurs. This assists the user of the API to not have to create lists to
 * later be garbage collected. Should provide some performance gain.
 * @author cdea
 */
public class GameManager {
    /** All the sprite objects currently in play */
    private final static List<GamePiece> GAME_ACTORS = new ArrayList<>();
    /** A global single threaded list used to check collision against other
     * sprite objects.
     */
    private final static List<GamePiece> CHECK_COLLISION_LIST = new ArrayList<>();
 
    /** A global single threaded set used to cleanup or remove sprite objects
     * in play.
     */
    private final static Set <GamePiece>CLEAN_UP_PIECES = new HashSet<>();
 
    /** */
    public List <GamePiece>getAllPieces() {
        return GAME_ACTORS;
    }
    
    /** */
    public void removeAllPieces() {
        GAME_ACTORS.clear();
    }
 
    /**
     * VarArgs of sprite objects to be added to the game.
     * @param gamePieces
     */
    public void addPieces(GamePiece... gamePieces) {
        GAME_ACTORS.addAll(Arrays.asList(gamePieces));
    }
 
    /**
     * VarArgs of sprite objects to be removed from the game.
     * @param sprites
     */
    public void removePieces(GamePiece... gamePieces) {
        GAME_ACTORS.removeAll(Arrays.asList(gamePieces));
    }
 
    /** Returns a set of sprite objects to be removed from the GAME_ACTORS.
     * @return CLEAN_UP_SPRITES
     */
    public Set<GamePiece> getPiecesToBeRemoved() {
        return CLEAN_UP_PIECES;
    }
 
 /**
     * Adds sprite objects to be removed
     * @param sprites varargs of sprite objects.
     */
    public void addPiecesToBeRemoved(GamePiece... gamePieces) {
        if (gamePieces.length > 1) {
            CLEAN_UP_PIECES.addAll(Arrays.asList((GamePiece[]) gamePieces));
        } else {
            CLEAN_UP_PIECES.add(gamePieces[0]);
        }
    }
 
    /**
     * Returns a list of sprite objects to assist in collision checks.
     * This is a temporary and is a copy of all current sprite objects
     * (copy of GAME_ACTORS).
     * @return CHECK_COLLISION_LIST
     */
    public List<GamePiece> getCollisionsToCheck() {
        return CHECK_COLLISION_LIST;
    }
 
    /**
     * Clears the list of sprite objects in the collision check collection
     * (CHECK_COLLISION_LIST).
     */
    public void resetCollisionsToCheck() {
        CHECK_COLLISION_LIST.clear();
        CHECK_COLLISION_LIST.addAll(GAME_ACTORS);
    }
 
    /**
     * Removes sprite objects and nodes from all
     * temporary collections such as:
     * CLEAN_UP_SPRITES.
     * The sprite to be removed will also be removed from the
     * list of all sprite objects called (GAME_ACTORS).
     */
    public void cleanupPieces() {
 
        // remove from actors list
        GAME_ACTORS.removeAll(CLEAN_UP_PIECES);
 
        // reset the clean up sprites
        CLEAN_UP_PIECES.clear();
    }
}