package ie.atu.sw;

import java.io.*;
import java.util.Arrays;

public class EmbeddingsProcessor {
	// Constants defining size of word embeddings arrays
	public static final int VECTOR_DIMENSION = 50;
	public static final int MAX_WORDS = 59_602;
	// Arrays to hold words and their respective embeddings
	private String[] words;
	private double[][] embeddings;
	// I/O handling instance variables
	private FileIO fileHandler;
	private FileWriter out;
	// Text and distance measure used for similarity search
	private String text;
	private String measure;
	
	// Initialize file handler, text, measure and allocate memory for 'words' and 'embeddings' arrays
	public EmbeddingsProcessor(String text, String measure) {
		this.fileHandler = new FileIO();
		this.text = text;
		this.measure = measure;
		this.words = new String[MAX_WORDS];
		this.embeddings = new double[MAX_WORDS][VECTOR_DIMENSION];
	}
	
	// Start with processing
	public void start(String embeddingsFilePath, String outputFilePath, int numOfMatches)
			throws Exception {
		
		// Open a BufferedReader to read the embeddings file, extract word embeddings and close the input stream
		BufferedReader bReader = fileHandler.readFile(embeddingsFilePath);
		extractWordEmbeddings(bReader);
		bReader.close();
		
		// Pre-process the text and return index and a vector representing word
		// For multiple words (after pre-processing) vector is an average of those, and indexOfWord is set to -1
		TextProcessor tp = new TextProcessor(text, words, embeddings);
		VectorIndexPair pair = tp.processText();
		double[] vector = pair.vector();
		int indexOfWord = pair.index();
		
		// Set the output stream file path
		out = fileHandler.writeToFile(outputFilePath);
		
		// Invoke particular method based on measure parameter. Throw exception in case of unsupported one
		switch (measure) {
			case "Dot Product" 		  -> dotProduct(vector, numOfMatches, indexOfWord);
			case "Euclidean Distance" -> euclideanDistance(vector, numOfMatches, indexOfWord);
			case "Cosine Similarity"  -> cosineSimilarity(vector, numOfMatches, indexOfWord);
			default 				  -> throw new Exception("Unsupported method: " + measure);
		}
		// Close output stream
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
	
	// Print to console and write results to the file
	private void printAndWrite(String s) throws IOException {
		System.out.print(s);
		out.write(s);
	}
	
	// Process and format results
	private void processResults(String[] topWords, double[] topScores) throws IOException {
		System.out.println("\n");
		printAndWrite("--------------------------------------------------\n");
		printAndWrite("Similarity calculated using " + ConsoleColour.GREEN + measure + ConsoleColour.WHITE
				+ "\nInput text: " + ConsoleColour.GREEN + text + ConsoleColour.WHITE + "\n");
		printAndWrite("--------------------------------------------------\n");
		printAndWrite("   Top Matching Words    |    Similarity Scores\n");
		printAndWrite("-------------------------|------------------------\n");
		// Format with printf(). Explanation found at: https://www.baeldung.com/java-printstream-printf
		for (int i = topWords.length - 1; i >= 0; i--) {
			String s = String.format("%3s%-22s%-5s%s%n", "", topWords[i], "|", topScores[i]);
			printAndWrite(s);
		}
	}
	
	// Insert similarity scores and words into new arrays in sorted order
	private void insertIntoArray(double[] arrScores, String[] arrWords, double newScore, String newWord) {
		int i;
		// If 'i' less than arrays length-1, and arrScores next element smaller than new similarity score
		for (i = 0; i < arrScores.length - 1 && arrScores[i + 1] < newScore; i++) {
			// Overwrite current element with the next one - get rid of the smallest (first) element
			// since there is a larger one (newScore)
			arrScores[i] = arrScores[i + 1];
			arrWords[i] = arrWords[i + 1];
		}
		// Store new score and new word at the current index of arrays
		arrScores[i] = newScore;
		arrWords[i] = newWord;
	}
	
	// Dot Product of vectors
	private void dotProduct(double[] vector, int numOfMatches, int indexOfWord) throws Exception {
		// Initialize arrays to store top matching scores and related words
		String[] topWords = new String[numOfMatches];
		double[] topScores = new double[numOfMatches];
		// Populate 'topScores' with -infinity so it can initially be filled with 'numOfMatches' larger elements
		Arrays.fill(topScores, Double.NEGATIVE_INFINITY);
		
		// Iterate over an 'embeddings' array of arrays
		for (int i = 0; i < MAX_WORDS; i++) {
			// Skip the specific vector related to user inputted word - not to be compared with itself
			// If indexOfWord is -1, which indicates an average vector, compare against all vectors
			if (indexOfWord == i) {
				continue;
			}
			// Calculate dot product of a vector at the current index of 'embeddings' array and input vector
			double dotProduct = dotProductScore(i, vector);
			// If dot product is larger than the smallest element (first element) of the 'topScores' array
			if (dotProduct > topScores[0]) {
				// Insert dotProduct and a related word into a proper place in 'topScores' and 'topWords' arrays
				insertIntoArray(topScores, topWords, dotProduct, words[i]);
			}
		}
		processResults(topWords, topScores);
	}
	
	// Compute Dot Product
	private double dotProductScore(int index, double[] vector) {
		double score = 0.0;
		// Iterate over embeddings dimensions and calculate similarity score
		for (int i = 0; i < VECTOR_DIMENSION; i++) {
			score += vector[i] * embeddings[index][i];
		}
		return score;
	}
	
	// Euclidean Distance of vectors
	private double euclideanDistance(double[] vector, int numOfMatches, int indexOfWord) {
		return 0;
	}
	
	// Cosine Similarity of vectors
	private double cosineSimilarity(double[] vector, int numOfMatches, int indexOfWord) {
		return 0;
	}
}
