package ie.atu.sw;

// Static import
import static java.lang.System.out;

import java.io.Console;
import java.util.Scanner;

public class Menu {
	private Scanner scan;
	private boolean keepRunning = true;
	private String embeddingsFilePath = "./word-embeddings.txt";
	private String outputFilePath = "./output.txt";
	private String text;
	private String distanceMetric = "Cosine Distance";
	// private FileIO fileHandler;
	// Default path to the embeddings file
	// private static final String DEFAULT_FILE_PATH = "./word-embeddings.txt";

	public Menu() {
		this.scan = new Scanner(System.in);
		// this.fileHandler = new FileIO();
		init();
	}
	
	// Method that starts the application
	public void init() {
		while (keepRunning) {
			showOptions();
			int choice = 0;

			// Input handling. Values allowed 1 to 6, otherwise print error message and reprompt
			while (true) {
				out.print(ConsoleColour.WHITE_BOLD);
				try {
					choice = Integer.parseInt(scan.nextLine());
					if (choice >= 1 && choice <= 6) {
						break;
					}
					showOptions();
					out.println(ConsoleColour.RED + "Invalid Selection! Please use one of the options above >");
					continue;
				} catch (Exception e) {
					showOptions();
					out.println(ConsoleColour.RED + "Invalid Selection! Please use one of the options above >");
					continue;
				}
			}
			// Execute a statement based on user choice
			switch (choice) {
				case 1  -> setEmbeddingsPath();
				case 2  -> setOutputFile();
				case 3  -> wordOrText();
				case 4  -> setMetric();
				case 5  -> start();
				case 6  -> keepRunning = false;
				// Default should never be reached since input is handled within the try-catch block above
				default -> out.println(ConsoleColour.RED + "Invalid Selection!");
			}
		}
		out.println("Thank you for using Similarity Search with Word Embeddings application!");
	}
	
	// Prompt user for a file path to the word embeddings file
	private void setEmbeddingsPath() {
		clearScreen();
		out.print("Please specify the path and the name of the word embeddings file > ");
		embeddingsFilePath = scan.nextLine().trim();
		out.println();

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
	
	// Prompt user to set an output (results) file path
	private void setOutputFile() {
		clearScreen();
		out.print("Please specify the path and the name of a file where results will be stored > ");
		outputFilePath = scan.nextLine().trim();
		out.println();
	}
	
	// Method that prompts and sets a word or short text for similarity search
	private void wordOrText() {
		clearScreen();
		out.print("Please enter a word or a short sentence to compare against word embeddings > ");
		text = scan.nextLine().trim().toLowerCase();
		out.println();
	}
	
	// Method that defines distance metric for similarity search
	private void setMetric() {
		clearScreen();
		out.println(ConsoleColour.WHITE_BOLD);
		out.println("Select a Distance Metric to be used: ");
		out.print("*******************************************");
		out.println(ConsoleColour.WHITE);
		out.println("(1) Dot Product");
		out.println("(2) Euclidean Distance");
		out.println("(3) Cosine Distance");
		out.println();

		int metricChoice = 0;
		while (true) {
			out.print(ConsoleColour.WHITE_BOLD + "Select Option (1-3) > ");
			try {
				metricChoice = Integer.parseInt(scan.nextLine());
				if (metricChoice >= 1 && metricChoice <= 3)
					break;
				out.println(ConsoleColour.RED + "Invalid input, please try again!");
			} catch (Exception e) {
				out.println(ConsoleColour.RED + "Invalid input, please try again!");
				continue;
			}
		}
		switch (metricChoice) {
			case 1  -> distanceMetric = "Dot Product";
			case 2  -> distanceMetric = "Euclidean Distance";
			default -> distanceMetric = "Cosine Distance";
		}
	}
	
	// Start with similarity search according to specified parameters
	private void start() {
		// If text not specified, stop execution and reprompt options
		if (text == null) {
			return;
		}
	}

	// Menu options
	public void showOptions() {
		clearScreen();
		out.println(ConsoleColour.WHITE);
		out.println("************************************************************");
		out.println("*     ATU - Dept. of Computer Science & Applied Physics    *");
		out.println("*                                                          *");
		out.println("*          Similarity Search with Word Embeddings          *");
		out.println("*                                                          *");
		out.println("************************************************************");
		out.println("(1) Specify Embedding File ----> Currently set to: " + ConsoleColour.GREEN + embeddingsFilePath
				+ ConsoleColour.WHITE);
		out.println("(2) Specify an Output File ----> Currently set to: " + ConsoleColour.GREEN + outputFilePath
				+ ConsoleColour.WHITE);
		if (text == null) {
			out.println("(3) Enter a Word or Text");
		} else {
			out.println("(3) Enter a Word or Text ------> Following text will be used for similarity search: "
					+ ConsoleColour.GREEN + text + ConsoleColour.WHITE);
		}
		out.println("(4) Specify Distance Metric ---> Currently set to: " + ConsoleColour.GREEN + distanceMetric
				+ ConsoleColour.WHITE);
		if (text == null) {
			out.println(ConsoleColour.RED + "(5) SIMILARITY SEARCH (please ensure all parameters are set before proceeding)"
					+ ConsoleColour.WHITE);
		} else {
			out.println("(5) SIMILARITY SEARCH");
		}
		out.println("(6) Quit");

		out.println(ConsoleColour.WHITE_BOLD);
		out.println("");
		out.println("Select Option (1-?) > ");
	}

	/*
	 * Method that clears terminal window (doesn't work in IDE console) 
	 * Source: https://intellipaat.com/community/294/java-clear-the-console
	 */
	private void clearScreen() {
		out.print("\033[H\033[2J");
		out.flush();
	}

}
