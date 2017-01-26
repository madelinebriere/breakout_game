// This entire file is part of my masterpiece.
// Madeline Briere
/**
 * This class is an extension of PopupMenu that is invoked
 * at the start of the game. It searches for the files:
 * src/___message.txt and src/___instructions.txt, where the 
 * blanks are filled in by the chosen prefix of the user 
 * (e.g., for Fido's Breakout, the prefix is fido)
 * Following this convention, these files must actually exist in the
 * src folder, with the message file holding the name of the character's
 * image file (e.g., fido.txt) and the rest of the text conveying a message.
 * The instructions file can be in any format -- the scanner will read
 * until it can no longer find another line.
 * 
 * This extension shows how the abstract class PopupMenu can
 * be extended to create a succint and purposeful class, unique
 * to the TYPE of popupmenu desired.
 * 
 * @author maddiebriere
 */
import java.util.ArrayList;
import javafx.scene.Node;

public class StartPopupMenu extends PopupMenu {

	public StartPopupMenu(String prefix, String title) {
		super(prefix, title);
	}

	@Override
	protected ArrayList<Node> generateContent() {
		ArrayList<Node> toRet = new ArrayList<Node>();
		toRet.add(generateLabel(getCharacterMessage()));
		toRet.add(generateText(getInstructions()));
		toRet.add(generatePageButton("Next page"));
		return toRet;
	}

	private String getCharacterMessage() {
		return readFileInFull("src/" + getMyPrefix() + "message.txt");
	}

	private String getInstructions() {
		return readFileInFull("src/" + getMyPrefix() + "instructions.txt");
	}

}
