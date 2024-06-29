package ie.atu.sw;

import static java.lang.System.out;
import java.util.Scanner;

public class Menu {
	private Scanner scan;
	private boolean keepRunning = true;
	// App configuration instance variables with default values
	private String embeddingsFilePath = "../word-embeddings.txt";
	private String outputFilePath = "../output.txt";
	private String metric = "Cosine Similarity";
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
			// Display an error message and reprompt for invalid input
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
				case 4  -> setMetric();
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
			// Prevent empty input string
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
			// Prevent empty input string
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
			out.print("Please enter a word or a short sentence to compare against word "
					+ "embeddings (max 50 characters) > ");
			userInput = scan.nextLine().trim().toLowerCase();
			// Reprompt for empty of large string
			if (userInput.isEmpty()) {
				out.println(ConsoleColour.RED + "Invalid input! Please try again.");
				continue;
			}
			if (userInput.length() > 50) {
				out.println(ConsoleColour.RED + "Text cannot be more than 50 characters long!");
				continue;
			}
			break;
		}
		textToCompare = userInput;
	}
	
	// Method that defines metric to be used for similarity search
	private void setMetric() {
		clearScreen();
		out.println(ConsoleColour.WHITE_BOLD);
		out.println("Select a metric you would like to use to find the closest matches to your input");
		out.print("*******************************************************************************");
		out.println(ConsoleColour.WHITE);
		out.println("(1) Dot Product");
		out.println("(2) Euclidean Distance");
		out.println("(3) Cosine Similarity");
		out.println();
		// Initialize metricChoice variable and handle user input
		int metricChoice = 0;
		while (true) {
			out.print(ConsoleColour.WHITE_BOLD + "Select Option (1-3) > ");
			try {
				metricChoice = Integer.parseInt(scan.nextLine());
				if (metricChoice >= 1 && metricChoice <= 3) break;
				out.println(ConsoleColour.RED + "Invalid input! Please try again.");
			} catch (Exception e) {
				out.println(ConsoleColour.RED + "Invalid input! Please try again.");
			}
		}
		// Set the distance metric based on user input (default to Cosine Similarity)
		switch (metricChoice) {
			case 1  -> metric = "Dot Product";
			case 2  -> metric = "Euclidean Distance";
			default -> metric = "Cosine Similarity";
		}
	}
	
	// Set number of top matches
	private void setMatches() {
		clearScreen();
		int userInput;
		// Handle user input - reprompt until value between 1 and 99
		while (true) {
			out.println(ConsoleColour.WHITE);
			out.print("Specify the number of top matches to be displayed (1 - 99) > ");
			try {
				userInput = Integer.parseInt(scan.nextLine());
				if (userInput >= 1 && userInput <= 99) break;
				out.println(ConsoleColour.RED + "Invalid value. Please try again");
			} catch (Exception e) {
				out.println(ConsoleColour.RED + "Invalid value. Please try again");
			}
		}
		numOfMatches = userInput;
	}
	
	// Start similarity search based on specified parameters
	private void runSimilaritySearch() {
		// If no word or text, stop execution and display an error message
		if (textToCompare == null) {
			errorMsg = "Text is not defined!";
			return;
		}
		// Create an instance of 'EmbeddingsProcessor'
		EmbeddingsProcessor processor = new EmbeddingsProcessor(textToCompare, metric);
		try {
			// Pass in the rest of configuration variables to start processing
			processor.start(embeddingsFilePath, outputFilePath, numOfMatches);
			// After similarity search finishes, close the application
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
			out.println("(3) Enter a Word or Text    ----> Text to analyse: " 
					+ ConsoleColour.GREEN + textToCompare + ConsoleColour.WHITE);
		}
		// Display metric used for comparison
		out.println("(4) Select Distance Metric  ----> Currently selected: " + ConsoleColour.GREEN + metric
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