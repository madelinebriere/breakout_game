import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class is solely used for reading in the Block grids for each level
 * from corresponding text files. It assigned each row and column of 
 * Blocks via the .gif file written (e.g., concrete.gif). Refer to the 
 * given blocks.txt files for examples.
 * 
 * @author maddiebriere
 *
 */

public class BlockReader {

	/**
	 * Reads in blocks from a text file holding the blocks for that level
	 * 
	 * @return ArrayList<Block> corresponding to the blocks listed in the text
	 *         file
	 */
	public static ArrayList<Block> readBlocks(int level) {
		Scanner scan = getScanner(level);
		ArrayList<Block> toRet = new ArrayList<Block>();
		int line = 0;
		while (scan.hasNextLine()) {
			String next = scan.nextLine();
			String[] row = next.split(" ");
			for (int i = 0; i < row.length; i++) {
				if (row[i].equals("normal.gif")) {
					toRet.add(new NormalBlock(line + 1, i + 1));
				}
				if (row[i].equals("concrete.gif")) {
					toRet.add(new ConcreteBlock(line + 1, i + 1));
				}
				if (row[i].equals("strong.gif")) {
					toRet.add(new StrongBlock(line + 1, i + 1));
				}
				if (row[i].equals("double.gif")) {
					toRet.add(new DoubleBlock(line + 1, i + 1));
				}
			}
			line++;
		}
		scan.close();
		return toRet;
	}

	/**
	 * Sets up the appropriate scanner for this level
	 * 
	 * @param level:
	 *            game level
	 * @return Scanner of level file
	 */
	private static Scanner getScanner(int level) {
		File file = new File("");

		if (level == 1) {
			file = new File("src/blocks1.txt");
		}
		if (level == 2) {
			file = new File("src/blocks2.txt");
		}
		if (level == 3) {
			file = new File("src/blocks3.txt");
		}
		if (level == 4) {
			file = new File("src/blocks4.txt");
		}

		Scanner scan = new Scanner("");
		try {
			scan = new Scanner(file);
		} catch (FileNotFoundException e) {
			System.out.println("Scanner file not found");
		}
		return scan;
	}

}
