package ie.atu.sw;

import static java.lang.System.out;
import java.util.Scanner;

public class Menu {
	private Scanner scan;
	private boolean keepRunning = true;
	// Instance variables for program configuration with default values
	private String embeddingsFilePath = "../word-embeddings.txt";
	private String outputFilePath = "../output.txt";
	private String measure = "Cosine Similarity";
	private int numOfMatches = 10;
	private String textToCompare;
	// Error message displayed within the options menu
	private String errorMsg;

	public Menu() {
		// Instantiate Scanner object
		this.scan = new Scanner(System.in);
	}
	
	// Start the application
	public void startApplication() {
		while (keepRunning) {
			int choice;
			// Handle invalid input (reprompt)
			while (true) {
				showOptions();
				try {
					choice = Integer.parseInt(scan.nextLine());
					if (choice >= 1 && choice <= 7) {
						break;
					}
					errorMsg = "Invalid Selection! Please use one of the options above";
				} catch (Exception e) {
					errorMsg = "Invalid Selection! Please use one of the options above";
				}
			}
			// Execute a statement based on user choice
			switch (choice) {
				case 1  -> setEmbeddingsPath();
				case 2  -> setOutputPath();
				case 3  -> wordOrText();
				case 4  -> setMeasure();
				case 5  -> setMatches();
				case 6  -> runSimilaritySearch();
				case 7  -> keepRunning = false;
				// Default should not be reached since input is handled within the try-catch block above
				default -> errorMsg = "Unexpected value: " + choice;
			}
		}
		// Application closed - display goodbye message
		out.println("Thank you for using Similarity Search with Word Embeddings!");
		// Close instance of Scanner
		scan.close();
	}
	
	// Prompt for a file path to the word embeddings file
	private void setEmbeddingsPath() {
		clearScreen();
		String userInput;
		while (true) {
			out.println(ConsoleColour.WHITE);
			out.print("Please specify the path and the name of the word embeddings file > ");
			userInput = scan.nextLine().trim();
			// Prevent empty inputs
			if (userInput.isEmpty()) {
				out.println(ConsoleColour.RED + "Invalid input! Please try again.");
				continue;
			}
			break;
		}
		embeddingsFilePath = userInput;
	}
	
	// Prompt for an output file path that stores results of similarity search
	private void setOutputPath() {
		clearScreen();
		String userInput;
		while (true) {
			out.println(ConsoleColour.WHITE);
			out.print("Please specify the path and the name of a file where results will be stored > ");
			userInput = scan.nextLine().trim();
			// Prevent empty inputs
			if (userInput.isEmpty()) {
				out.println(ConsoleColour.RED + "Invalid input! Please try again.");
				continue;
			}
			break;
		}
		outputFilePath = userInput;
	}
	
	// Method that prompts for, and sets a word or short text to be used in similarity search
	private void wordOrText() {
		clearScreen();
		String userInput;
		while (true) {
			out.println(ConsoleColour.WHITE);
			out.print("Please enter a word or a short sentence to compare against word embeddings > ");
			userInput = scan.nextLine().trim().toLowerCase();
			// Prevent empty inputs
			if (userInput.isEmpty()) {
				out.println(ConsoleColour.RED + "Invalid input! Please try again.");
				continue;
			}
			break;
		}
		textToCompare = userInput;
	}
	
	// Method that defines measure to be used for similarity search
	private void setMeasure() {
		clearScreen();
		out.println(ConsoleColour.WHITE_BOLD);
		out.println("Select a method you would like to use to find the closest matches to your input");
		out.print("*******************************************************************************");
		out.println(ConsoleColour.WHITE);
		out.println("(1) Dot Product");
		out.println("(2) Euclidean Distance");
		out.println("(3) Cosine Similarity");
		out.println();
		// Initialize measureChoice variable and handle user input
		int measureChoice = 0;
		while (true) {
			out.print(ConsoleColour.WHITE_BOLD + "Select Option (1-3) > ");
			try {
				measureChoice = Integer.parseInt(scan.nextLine());
				if (measureChoice >= 1 && measureChoice <= 3) break;
				out.println(ConsoleColour.RED + "Invalid input! Please try again.");
			} catch (Exception e) {
				out.println(ConsoleColour.RED + "Invalid input! Please try again.");
			}
		}
		// Set the measure based on user input
		switch (measureChoice) {
			case 1  -> measure = "Dot Product";
			case 2  -> measure = "Euclidean Distance";
			default -> measure = "Cosine Similarity";
		}
	}
	
	// Set number of top matches
	private void setMatches() {
		clearScreen();
		int userInput;
		// Reprompt until value entered is between 1 and 100
		while (true) {
			out.println(ConsoleColour.WHITE);
			out.print("Specify the number of top mathes to be displayed (1 - 100) > ");
			try {
				userInput = Integer.parseInt(scan.nextLine());
				if (userInput >= 1 && userInput <= 100) break;
				out.println(ConsoleColour.RED + "Invalid value. Please try again");
			} catch (Exception e) {
				out.println(ConsoleColour.RED + "Invalid value. Please try again");
			}
		}
		numOfMatches = userInput;
	}
	
	// Start similarity search based on specified parameters
	private void runSimilaritySearch() {
		// If text not specified, stop execution and display an error message
		if (textToCompare == null) {
			errorMsg = "Text is not specified";
			return;
		}
		// Create an instance of 'EmbeddingsProcessor'
		EmbeddingsProcessor processor = new EmbeddingsProcessor();
		try {
			// Pass in all configuration variables to start processing
			processor.start(embeddingsFilePath, outputFilePath, measure, textToCompare, numOfMatches);
			// Stop the application
			out.println();
			keepRunning = false;
		// Assign content of the exception to the 'errorMsg' variable displayed within the options menu
		} catch (Exception e) {
			errorMsg = e.getMessage();
		}	
	}

	// Menu options
	private void showOptions() {
		clearScreen();
		out.println(ConsoleColour.WHITE);
		out.println("************************************************************");
		out.println("*     ATU - Dept. of Computer Science & Applied Physics    *");
		out.println("*                                                          *");
		out.println("*          Similarity Search with Word Embeddings          *");
		out.println("*                                                          *");
		out.println("************************************************************");
		// Display input file
		out.println("(1) Specify Embedding File  ----> Currently set to: " + ConsoleColour.GREEN + embeddingsFilePath
				+ ConsoleColour.WHITE);
		// Display output file
		out.println("(2) Specify an Output File  ----> Currently set to: " + ConsoleColour.GREEN + outputFilePath
				+ ConsoleColour.WHITE);
		// If set, show text used for similarity search
		if (textToCompare == null) {
			out.println("(3) Enter a Word or Text");
		} else {
			out.println("(3) Enter a Word or Text    ----> Text used for similarity search: "
					+ ConsoleColour.GREEN + textToCompare + ConsoleColour.WHITE);
		}
		// Display measure used for comparison
		out.println("(4) Select Distance Measure ----> Currently selected: " + ConsoleColour.GREEN + measure
				+ ConsoleColour.WHITE);
		// Number of matches option
		out.println("(5) Number of Top Matches   ----> Currently set to: " + ConsoleColour.GREEN + numOfMatches
				+ ConsoleColour.WHITE);
		// Option to start similarity search (coloured red if text not set yet)
		if (textToCompare == null) {
			out.println(ConsoleColour.RED
					+ "(6) START SIMILARITY SEARCH (please ensure all parameters are set before proceeding)"
					+ ConsoleColour.WHITE);
		} else {
			out.println("(6) START SIMILARITY SEARCH");
		}
		// Quit
		out.println("(7) Quit");
		// Option selection
		out.println(ConsoleColour.WHITE_BOLD);
		out.println("");
		out.println("Select Option (1-7) > ");
		// If error, display message and reassign 'null' to prevent error from reappearing
		if (errorMsg != null) {
			out.println(ConsoleColour.RED + errorMsg + ConsoleColour.WHITE);
			errorMsg = null;
		}
	}

	/*
	 * Source: https://intellipaat.com/community/294/java-clear-the-console
	 * Clears terminal window (doesn't work for IDE console)
	 */
	private void clearScreen() {
		out.print("\033[H\033[2J");
		out.flush();
	}
}