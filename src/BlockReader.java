import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class BlockReader {

	public static ArrayList<Block> readBlocks(int level)
    {
    	File file= new File("");
    	ArrayList<Block> toRet = new ArrayList<Block>();
    	
    	if(level==1){file = new File("src/blocks3.txt");}
    	if(level==2){file = new File("src/blocks2.txt");}
    	if(level==3){file = new File("src/blocks3.txt");}
    	if(level==4){file = new File("src/blocks4.txt");}
    	
    	Scanner scan = new Scanner("");
		try {scan = new Scanner(file);} 
		catch (FileNotFoundException e) {System.out.println("Scanner file not found");}
    	
    	int line=0;
    	while(scan.hasNextLine())
    	{
    		String next = scan.nextLine();
    		String [] row = next.split(" ");
    		for(int i=0; i<row.length; i++)
    		{
    			if(row[i].equals("normal.gif")) {toRet.add(new NormalBlock(line+1, i+1));}
    			if(row[i].equals("concrete.gif")) {toRet.add(new ConcreteBlock(line+1, i+1));}
    			if(row[i].equals("strong.gif")) {toRet.add(new StrongBlock(line+1, i+1));}
    		}
    		line++;
    	}
    	return toRet;
    }
    
}
