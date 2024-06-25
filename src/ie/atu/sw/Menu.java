package ie.atu.sw;

import static java.lang.System.out;
import java.util.Scanner;

public class Menu {
	private Scanner scan;
	private boolean keepRunning = true;
	private String embeddingsFilePath = "./word-embeddings.txt";
	private String outputFilePath = "./output.txt";
	private String metric = "Cosine Similarity";
	private String textToCompare;
	private int numOfMatches = 10;
	private String errorMsg;

	public Menu() {
		// Instantiate Scanner object
		this.scan = new Scanner(System.in);
	}
	
	// Method that starts the application
	public void startApplication() {
		while (keepRunning) {
			int choice;
			// Input handling. Values allowed 1 to 7, otherwise print an error message and reprompt
			while (true) {
				showOptions();
				try {
					choice = Integer.parseInt(scan.nextLine());
					if (choice >= 1 && choice <= 7) {
						break;
					}
					errorMsg = ConsoleColour.RED + "Invalid Selection! Please use one of the options above >"
							+ ConsoleColour.WHITE;
					continue;
				} catch (Exception e) {
					showOptions();
					errorMsg = ConsoleColour.RED + "Invalid Selection! Please use one of the options above >"
							+ ConsoleColour.WHITE;
					continue;
				}
			}
			// Execute a statement based on user choice
			switch (choice) {
				case 1  -> setEmbeddingsPath();
				case 2  -> setOutputFile();
				case 3  -> wordOrText();
				case 4  -> setMeasure();
				case 5  -> setMatches();
				case 6  -> runSimilaritySearch();
				case 7  -> keepRunning = false;
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
	private void setMeasure() {
		clearScreen();
		out.println(ConsoleColour.WHITE_BOLD);
		out.println("Select a method used to find the closest matches to your input (word or text)");
		out.print("**************************************************************");
		out.println(ConsoleColour.WHITE);
		out.println("(1) Dot Product");
		out.println("(2) Euclidean Distance");
		out.println("(3) Cosine Similarity");
		out.println();
		// Initialize methodChoice variable and handle user input
		int methodChoice = 0;
		while (true) {
			out.print(ConsoleColour.WHITE_BOLD + "Select Option (1-3) > ");
			try {
				methodChoice = Integer.parseInt(scan.nextLine());
				if (methodChoice >= 1 && methodChoice <= 3)
					break;
				out.println(ConsoleColour.RED + "Invalid input, please try again!");
			} catch (Exception e) {
				out.println(ConsoleColour.RED + "Invalid input, please try again!");
				continue;
			}
		}
		// Set the metric based on user input
		switch (methodChoice) {
			case 1  -> metric = "Dot Product";
			case 2  -> metric = "Euclidean Distance";
			default -> metric = "Cosine Similarity";
		}
	}
	
	// Set number of top matches
	private void setMatches() {
		clearScreen();
		int userInput;
		while (true) {
			out.print(ConsoleColour.WHITE);
			out.print("Specify the number of top mathes to be displayed (1 - 20) > ");
			try {
				userInput = Integer.parseInt(scan.nextLine());
				if (userInput >= 1 && userInput <= 20) {
					break;
				}
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
			errorMsg = ConsoleColour.RED + "Text is not specified";
			return;
		}
		
		// Create instance of 'EmbeddingsProcessor'
		EmbeddingsProcessor processor = new EmbeddingsProcessor();
		
		try {
			// Pass in all of the configuration variables to start processing
			processor.start(embeddingsFilePath, outputFilePath, metric, textToCompare, numOfMatches);
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
		out.println("(4) Select Distance Measure ----> Currently selected: " + ConsoleColour.GREEN + metric
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
		// Quit option
		out.println("(7) Quit");

		out.println(ConsoleColour.WHITE_BOLD);
		out.println("");
		out.println("Select Option (1-7) > ");
		// If error, display message and reassign null to the 'errorMsg' variable to prevent it from reappearing
		if (errorMsg != null) {
			out.println(errorMsg);
			errorMsg = null;
		}
	}

	/*
	 * Source: https://intellipaat.com/community/294/java-clear-the-console
	 * Modified to, along with clearing terminal window, resets colour to white (in case of errors) 
	 */
	private void clearScreen() {
		out.print("\033[H\033[2J");
		out.flush();
		out.print(ConsoleColour.WHITE);
	}

}
