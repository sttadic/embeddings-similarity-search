package ie.atu.sw;
// Static import
import static java.lang.System.out;
import java.util.Scanner;

public class Menu {
	private Scanner scan = new Scanner(System.in);
	private FileIO f = new FileIO();
	private boolean keepRunning = true;
	
	public void init() {
		while (keepRunning) {
			showOptions();
			
			int choice = Integer.parseInt(scan.next());
			switch (choice) {
				case 1 	-> embeddingFile();
				case 2 	-> outputFile();
				case 3 	-> wordOrText();
				case 4 	-> configure();
				case 7 	-> keepRunning = false;
				default -> out.print("Invalid Selection! Please try again.");
			}
		}
		out.println("Bye!");
		
	}
	
	private void embeddingFile() {
		String file = "./word-embeddings.txt";
		out.println("Please specify the path to the word embeddings file (default ./word-embeddings.txt): ");
		if (scan.nextLine().length() >= 1) file = scan.nextLine();
		
		
	}
	
	private void outputFile() {
		
	}

	private void wordOrText() {
	
	}

	private void configure() {
	
	}
	
	public void showOptions() {
		out.println("************************************************************");
		out.println("*     ATU - Dept. of Computer Science & Applied Physics    *");
		out.println("*                                                          *");
		out.println("*          Similarity Search with Word Embeddings          *");
		out.println("*                                                          *");
		out.println("************************************************************");
		out.println("(1) Specify Embedding File");
		out.println("(2) Specify an Output File (default: ./out.txt)");
		out.println("(3) Enter a Word or Text");
		out.println("(4) Configure Options");
		out.println("(?) Optional Extras...");
		out.println("(?) Quit");
		
		out.println("");
		out.println("Select Option (1-?) >");
	}
	
	
}
