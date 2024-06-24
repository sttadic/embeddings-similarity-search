package ie.atu.sw;

import java.io.*;
import java.util.Arrays;

public class EmbeddingsProcessor {
	private FileIO fileHandler;
	private static final int VECTOR_DIMENSION = 50;
	private static final int MAX_WORDS = 59_602;
	private String[] words;
	private double[][] embeddings;
	
	// Initialize file handler and allocate memory for words and embeddings arrays
	public EmbeddingsProcessor() {
		this.fileHandler = new FileIO();
		this.words = new String[MAX_WORDS];
		this.embeddings = new double[MAX_WORDS][VECTOR_DIMENSION];
	}
	
	// Start with processing
	public void start(String embeddingsFilePath, String outputFilePath, String distanceMetric, String textToCompare)
			throws Exception {
		
		BufferedReader bReader = fileHandler.readFile(embeddingsFilePath);
		extractWordEmbeddings(bReader);
		bReader.close();
		
		switch (distanceMetric) {
			case "Dot Product" 		  -> dotProduct(textToCompare);
			case "Euclidean Distance" -> euclideanDistance(textToCompare);
			case "Cosine Distance"	  -> cosineDistance(textToCompare);
			default 				  -> throw new Exception(
					ConsoleColour.RED + "Unsupported distance metric: " + ConsoleColour.GREEN + distanceMetric);
		}
	}
	
	// Extract elements from each line of input stream and store into relevant array
	private void extractWordEmbeddings(BufferedReader br) throws IOException {
		int i = 0;
		String line = null;
		
		while ((line = br.readLine()) != null) {
			String[] parts = line.trim().split(",");
			words[i] = parts[0];
			for (int j = 1; j <= 50; j++) {
				embeddings[i][j-1] = Double.parseDouble(parts[j]);
			}
			i++;
		}
	}
	
	// Calculate Dot Product
	private void dotProduct(String text) throws Exception {
		int indexOfText = embVectorOfInput(text);
		if (indexOfText == -1) {
			throw new Exception(ConsoleColour.RED + "No matching word in embeddings. Please try another word");
		}

		
		double[] topScores = new double[10];
		Arrays.fill(topScores, Double.NEGATIVE_INFINITY);
		String[] topWords = new String[10];
		
		// Iterate over whole embeddings array (same number of elements as MAX_WORDS constant = 59602)
		for (int i = 0; i < MAX_WORDS; i++) {
			// Skip the vector of user inputted word (it is to be compared with all other 59601 words)
			if (indexOfText == i) {
				continue;
			}
			double similarityScore = 0;
			// Calculate similarity score
			for (int j = 0; j < VECTOR_DIMENSION; j++) {
				similarityScore += embeddings[indexOfText][j] * embeddings[i][j];
			}
			
			// If similarity score is larger than the smallest element of arrScores (always first element)
			if (similarityScore > topScores[0]) {
				// Insert score and new word into a proper place of topScores and topWords arrays
				insertIntoArray(topScores, topWords, similarityScore, words[i]);
			}
		}
		// Print top 10 most similar words
		for (String n: topWords) {
			System.out.println(n);
		}
	}
	
	// Insert score into the arrays and keep them sorted so words would point to their respective scores
	private void insertIntoArray(double[] arrScores, String[] arrWords, double newScore, String newWord) {
		
		int i;
		for (i = 0; i < arrScores.length - 1 && arrScores[i + 1] < newScore; i++) {
			arrScores[i] = arrScores[i + 1];
			arrWords[i] = arrWords[i + 1];
			
		}
		arrScores[i] = newScore;
		arrWords[i] = newWord;
	}
	
	// Calculate Euclidean Distance
	private double euclideanDistance(String text) {
		return 0;
	}
	
	// Calculate Cosine Distance
	private double cosineDistance(String text) {
		return 0;
	}
	
	// Returns index of words array that holds embeddings of a user inputted word
	private int embVectorOfInput(String t) {
		for (int i = 0; i < words.length; i++) {
			if (t.equals(words[i])) {
				return i;
			}
		}
		return -1;
	}
}
