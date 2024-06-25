package ie.atu.sw;

import static java.lang.System.out;
import java.util.Scanner;

public class Menu {
	private Scanner scan;
	private boolean keepRunning = true;
	private String embeddingsFilePath = "./word-embeddings.txt";
	private String outputFilePath = "./output.txt";
	private String metric = "Cosine Distance";
	private String textToCompare;
	private String errorMsg;

	public Menu() {
		// Instantiate Scanner object
		this.scan = new Scanner(System.in);
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
				default -> out.println(ConsoleColour.RED + "Unexpected value: " + choice);
			}
		}
		// Application closed. Display goodbye message
		out.println("Thank you for using Similarity Search with Word Embeddings!");
		// Close scanner object
		scan.close();
	}
	
	// Prompt for a file path to the word embeddings file
	private void setEmbeddingsPath() {
		clearScreen();
		out.print("Please specify the path and the name of the word embeddings file > ");
		embeddingsFilePath = scan.nextLine().trim();
	}
	
	// Prompt for an output file path that stores results of similarity search
	private void setOutputFile() {
		clearScreen();
		out.print("Please specify the path and the name of a file where results will be stored > ");
		outputFilePath = scan.nextLine().trim();
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
	}
	
	// Method that defines measure to be used for similarity search
	private void setMetric() {
		clearScreen();
		out.println(ConsoleColour.WHITE_BOLD);
		out.println("Select a Metric to calculate similarity between words");
		out.print("**************************************************************");
		out.println(ConsoleColour.WHITE);
		out.println("(1) Dot Product");
		out.println("(2) Euclidean Distance");
		out.println("(3) Cosine Distance");
		out.println();
		// Initialize metricChoice variable and handle user input
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
		// Set the metric based on user input
		switch (metricChoice) {
			case 1  -> metric = "Dot Product";
			case 2  -> metric = "Euclidean Distance";
			default -> metric = "Cosine Distance";
		}
	}
	
	// Start similarity search based on specified parameters
	private void runSimilaritySearch() {
		// If text not specified, stop execution and display an error message
		if (textToCompare == null) {
			errorMsg = ConsoleColour.RED + "Text is not specified";
			return;
		}
		
		// Create instance of 'EmbeddingsProcessor'
		EmbeddingsProcessor processor = new EmbeddingsProcessor();
		
		try {
			// Pass in all of the configuration variables to start processing
			processor.start(embeddingsFilePath, outputFilePath, metric, textToCompare);
			// Stop the application
			out.println();
			keepRunning = false;
		// Assign content of the exception to the 'errorMsg' variable displayed within the options menu
		} catch (Exception e) {
			errorMsg = e.getMessage();
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
		out.println("(4) Select Distance Metric ----> Currently selected: " + ConsoleColour.GREEN + metric
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
		// If error exists, display message and reassign null to the 'errorMsg' variable to prevent it from reappearing
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
