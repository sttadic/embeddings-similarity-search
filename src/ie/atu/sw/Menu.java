package ie.atu.sw;

// Static import
import static java.lang.System.out;

import java.io.Console;
import java.util.Scanner;

public class Menu {
	private Scanner scan;
	private boolean keepRunning = true;
	private String embeddingsFilePath = "./word-embeddings.txt";
	// private FileIO fileHandler;
	// Default path to the embeddings file
	// private static final String DEFAULT_FILE_PATH = "./word-embeddings.txt";

	public Menu() {
		this.scan = new Scanner(System.in);
		// this.fileHandler = new FileIO();
		init();
	}

	public void init() {
		while (keepRunning) {
			showOptions();
			int choice = 0;

			// Handle invalid input. Values allowed are between 1 and 7, otherwise print
			// error message and reprompt
			while (true) {
				out.print(ConsoleColour.WHITE_BOLD);
				try {
					choice = Integer.parseInt(scan.nextLine());
					if (choice >= 1 && choice <= 7) {
						break;
					}
					showOptions();
					System.out.println(ConsoleColour.RED + "Invalid Selection! Please use one of the options above >");
					continue;
				} catch (Exception e) {
					showOptions();
					System.out.println(ConsoleColour.RED + "Invalid Selection! Please use one of the options above >");
					continue;
				}
			}

			switch (choice) {
				case 1 -> setEmbeddingsPath();
				case 2 -> outputFile();
				case 3 -> textToCompare();
				case 4 -> configure();
				case 7 -> keepRunning = false;
				default -> out.println("Invalid Selection!");
			}
		}
		out.println("Bye!");

	}

	private void setEmbeddingsPath() {
		clearScreen();
		System.out.print("Please specify the path and the name of the word embeddings file > ");
		embeddingsFilePath = scan.nextLine().trim();
		System.out.println();

		/*
		 * Get the file path from the user and handle possible exceptions try {
		 * 
		 * out.println(
		 * "Please specify the path and the name of the embedding file. Leave blank for default (./word-embeddings.txt) > "
		 * ); String filePath = scan.nextLine(); // Set filePath to DEFAULT_FILE_PATH if
		 * input is empty if (filePath.trim().isEmpty()) filePath = DEFAULT_FILE_PATH;
		 * // fileHandler.readFile(filePath.trim());
		 * 
		 * } catch (Exception e) { e.printStackTrace(); }
		 */
	}

	private void outputFile() {

	}

	private void textToCompare() {

	}

	private void configure() {

	}

	public void showOptions() {
		out.println(ConsoleColour.WHITE);
		out.println("************************************************************");
		out.println("*     ATU - Dept. of Computer Science & Applied Physics    *");
		out.println("*                                                          *");
		out.println("*          Similarity Search with Word Embeddings          *");
		out.println("*                                                          *");
		out.println("************************************************************");
		out.println("(1) Specify Embedding File (Currently set to: " + ConsoleColour.GREEN + embeddingsFilePath
				+ ConsoleColour.WHITE + ")");
		out.println("(2) Specify an Output File (Currently set to: ./out.txt)");
		out.println("(3) Enter a Word or Text");
		out.println("(4) Configure Options");
		out.println("(?) Optional Extras...");
		out.println("(7) Quit Application");

		out.println(ConsoleColour.WHITE_BOLD);
		out.println("");
		out.println("Select Option (1-?) > ");
	}

	/*
	 * Method that clears terminal window (doesn't work in IDE console) Source:
	 * https://intellipaat.com/community/294/java-clear-the-console
	 */
	public static void clearScreen() {
		System.out.print("\033[H\033[2J");
		System.out.flush();
	}

}
