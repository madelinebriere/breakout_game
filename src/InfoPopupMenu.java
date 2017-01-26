/**
 * This class is an extension of PopupMenu that is invoked
 * after the starting popup to give info. It searches for the files:
 * src/___pieces.txt and src/___info.txt, where the 
 * blanks are filled in by the chosen prefix of the user 
 * (e.g., for Fido's Breakout, the prefix is fido)
 * Following this convention, these files must actually exist in the
 * src folder, with the pieces file holding a line for each item to be
 * displayed formatted as follows:
 * "imageName.gif Description of object/piece/character"
 * The info file can be in any format -- the scanner will read
 * until it can no longer find another line.
 * 
 * @author maddiebriere
 */

import java.util.ArrayList;
import javafx.scene.Node;

public class InfoPopupMenu extends PopupMenu {
	public InfoPopupMenu(String prefix, String title) {
		super(prefix, title);
	}

	@Override
	protected ArrayList<Node> generateContent() {
		ArrayList<Node> toRet = new ArrayList<Node>();
		toRet.addAll(generateLabels(getPieces()));
		toRet.add(generateText(getInfo()));
		toRet.add(generatePageButton("Let's Play!"));
		return toRet;
	}

	private String[] getPieces() {
		return readFileByLine("src/" + getMyPrefix() + "pieces.txt");
	}

	private String getInfo() {
		return readFileInFull("src/" + getMyPrefix() + "info.txt");
	}
}
