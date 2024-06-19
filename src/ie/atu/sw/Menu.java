package ie.atu.sw;

// Static import
import static java.lang.System.out;

import java.io.IOException;
import java.util.Scanner;

public class Menu {
	private Scanner scan;
	private FileIO fileHandler;
	private boolean keepRunning = true;
	private String embeddingsFilePath = "./word-embeddings.txt";
	private String outputFilePath = "./output.txt";
	private String distanceMetric = "Cosine Distance";
	private String textToCompare;
	private String errorMsg;

	public Menu() {
		// Initialize scan and fileHandler objects
		this.scan = new Scanner(System.in);
		this.fileHandler = new FileIO();
	}
	
	// Method that starts the application
	public void startApplication() {
		while (keepRunning) {
			showOptions();
			int choice = 0;

			// Input handling. Values allowed 1 to 6, otherwise print an error message and reprompt
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
				case 5  -> runSimilaritySearch();
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
	}
	
	// Prompt user to set an output (results of similarity search) file path
	private void setOutputFile() {
		clearScreen();
		out.print("Please specify the path and the name of a file where results will be stored > ");
		outputFilePath = scan.nextLine().trim();
		out.println();
	}
	
	// Method that prompts for, and sets a word or short text to be used in similarity search
	private void wordOrText() {
		clearScreen();
		String input;
		while (true) {
			out.print("Please enter a word or a short sentence to compare against word embeddings > ");
			input = scan.nextLine().trim().toLowerCase();
			if (input.isEmpty()) {
				out.println(ConsoleColour.RED + "Invalid input. Please try again" + ConsoleColour.WHITE);
				continue;
			}
			break;
		}
		textToCompare = input;
		out.println();
	}
	
	// Method that defines distance metric for similarity search
	private void setMetric() {
		clearScreen();
		out.println(ConsoleColour.WHITE_BOLD);
		out.println("Select a Distance Metric to calculate similarity between words");
		out.print("**************************************************************");
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
	
	// Start similarity search according to specified parameters
	private void runSimilaritySearch() {
		// If text not specified, stop execution and show options with an error message
		if (textToCompare == null) {
			errorMsg = ConsoleColour.RED + "Text is not specified";
			return;
		}
		
		try {
			fileHandler.readFile(embeddingsFilePath);
		} catch (IOException e) {
			errorMsg = e.toString();
		}
		
		try {
			fileHandler.writeToFile(outputFilePath);
		} catch (IOException e) {
			errorMsg = e.toString();
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
		if (textToCompare == null) {
			out.println("(3) Enter a Word or Text");
		} else {
			out.println("(3) Enter a Word or Text   ----> Text used for similarity search: "
					+ ConsoleColour.GREEN + textToCompare + ConsoleColour.WHITE);
		}
		out.println("(4) Select Distance Metric ----> Currently selected: " + ConsoleColour.GREEN + distanceMetric
				+ ConsoleColour.WHITE);
		if (textToCompare == null) {
			out.println(ConsoleColour.RED + "(5) SIMILARITY SEARCH (please ensure all parameters are set before proceeding)"
					+ ConsoleColour.WHITE);
		} else {
			out.println("(5) SIMILARITY SEARCH");
		}
		out.println("(6) Quit");

		out.println(ConsoleColour.WHITE_BOLD);
		out.println("");
		out.println("Select Option (1-6) > ");
		// If exists, display error message
		if (errorMsg != null) {
			out.println(errorMsg);
			errorMsg = null;
		}
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
