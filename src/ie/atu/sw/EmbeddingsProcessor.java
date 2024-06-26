package ie.atu.sw;

import java.io.*;
import java.util.Arrays;

public class EmbeddingsProcessor {
	private FileIO fileHandler;
	private FileWriter out;
	private static final int VECTOR_DIMENSION = 50;
	private static final int MAX_WORDS = 59_602;
	private String[] words;
	private double[][] embeddings;
	private int numOfMatches;
	
	// Initialize file handler and allocate memory for words and embeddings arrays
	public EmbeddingsProcessor() {
		this.fileHandler = new FileIO();
		this.words = new String[MAX_WORDS];
		this.embeddings = new double[MAX_WORDS][VECTOR_DIMENSION];
	}
	
	// Start with processing
	public void start(String embeddingsFilePath, String outputFilePath, String measure, String textToCompare, int numOfMatches)
			throws Exception {
		
		// Open a BufferedReader to read the embeddings file, extract word embeddings and close the input stream
		BufferedReader bReader = fileHandler.readFile(embeddingsFilePath);
		extractWordEmbeddings(bReader);
		bReader.close();
		
		// Set number of top matches to be stored
		this.numOfMatches = numOfMatches;
		out = new FileWriter(outputFilePath);
		// Invoke particular method based on measure parameter. Throw exception in case of unsupported one
		switch (measure) {
			case "Dot Product" 		  -> dotProduct(textToCompare);
			case "Euclidean Distance" -> euclideanDistance(textToCompare);
			case "Cosine Similarity"  -> cosineSimilarity(textToCompare);
			default 				  -> throw new Exception("Unsupported method: " + measure);
		}
		out.close();
		
	}
	
	// Extract elements from each line of input stream and store them into relevant arrays
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
	
	// Print and write results to the file
	private void printAndWrite(String s) throws IOException  {
		System.out.print(s);
		out.write(s);
	}
	
	// Process and format results
	private void processResults(String[] topWords, double[] topScores) throws IOException {
		System.out.println();
		printAndWrite("--------------------------------------------------\n");
		printAndWrite("   Top Matching Words    |    Similarity Scores\n");
		printAndWrite("-------------------------|------------------------\n");
		for (int i = topWords.length - 1; i >= 0; i--) {
			String s = String.format("%3s%-22s%-5s%s%n", "", topWords[i], "|", topScores[i]);
			printAndWrite(s);
		}
	}
	
	// Calculate Dot Product
	private void dotProduct(String text) throws Exception {
		// Word not found in 'words' array
		int indexOfText = embVectorOfInput(text);
		if (indexOfText == -1) {
			throw new Exception("No matching word in embeddings. Please try another word");
		}
		
		// Initialize arrays to store top matching scores and related words
		String[] topWords = new String[numOfMatches];
		double[] topScores = new double[numOfMatches];
		// Populate 'topScores' with -infinity so it can initially be filled with 'numOfMatches' larger elements
		Arrays.fill(topScores, Double.NEGATIVE_INFINITY);
		
		// Iterate over an 'embeddings' array
		for (int i = 0; i < MAX_WORDS; i++) {
			// Skip the vector related to user inputted word - not to be compared with itself
			if (indexOfText == i) {
				continue;
			}
			// Initialize similarityScore variable and reset it on each iteration
			double similarityScore = 0;
			// Iterate over embeddings of a particular word and calculate similarity score
			for (int j = 0; j < VECTOR_DIMENSION; j++) {
				similarityScore += embeddings[indexOfText][j] * embeddings[i][j];
			}
			
			// If similarity score is larger than the smallest element (first element) of the 'topScores' array
			if (similarityScore > topScores[0]) {
				// Insert the score and a related word into a proper place in 'topScores' and 'topWords' arrays
				insertIntoArray(topScores, topWords, similarityScore, words[i]);
			}
		}
		processResults(topWords, topScores);
	}
	
	// Insert simirlatiy scores and words into new arrays in sorted order
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
	
	// Calculate Cosine Similarity
	private double cosineSimilarity(String text) {
		return 0;
	}
	
	// Returns index of an element (user inputted word) from a 'words' array. Return -1 if word not found
	private int embVectorOfInput(String t) {
		for (int i = 0; i < words.length; i++) {
			if (t.equals(words[i])) {
				return i;
			}
		}
		return -1;
	}
}
