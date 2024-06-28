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
		
		// Open BufferedReader to read the embeddings file, extract word embeddings and close the input stream
		BufferedReader bReader = fileHandler.readFile(embeddingsFilePath);
		extractWordEmbeddings(bReader);
		bReader.close();
		
		// Pre-process the text and return index of a word within 'embeddings' array and its vector
		// For multiple words (post pre-processing) average vector is calculated, and indexOfWord is -1
		TextProcessor tp = new TextProcessor(text, words, embeddings);
		VectorIndexPair pair = tp.processText();
		double[] vector = pair.vector();
		int indexOfWord = pair.index();
		
		// Set up the output stream
		out = fileHandler.writeToFile(outputFilePath);
		
		// Start vector comparison and compute similarity scores
		computeSimScores(vector, numOfMatches, indexOfWord);
		
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
		printAndWrite(" Similarity calculated using " + measure + "\n Input text: " + text + "\n");
		printAndWrite(" ------------------------------------------\n");
		printAndWrite("  Top Matching Words |  Similarity Scores\n");
		printAndWrite(" ====================|=====================\n");
		for (int i = topWords.length - 1; i >= 0; i--) {
			String s = String.format("%2s%-19s%-3s%s%n", "", topWords[i], "|", topScores[i]);
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
	
	// Pre-populate array for scores with positove/negative infinity depending on metric used
	private double[] populateArr(int numOfMatches) throws Exception {
		double[] scoresArr = new double[numOfMatches];
		/* Depending whether smaller (euclidean) or larger values represent greater similarity,
		fill the arrays with pos/neg infinity, so initial 'n' number of computed scores can be
		compared against infinities (larger or smaller values) and stored into arrays */
		if (measure.equals("Euclidean Distance")) {
			Arrays.fill(scoresArr, Double.POSITIVE_INFINITY);
		} else if (measure.equals("Dot Product") || measure.equals("Cosine Similarity")) {
			Arrays.fill(scoresArr, Double.NEGATIVE_INFINITY);
		} else {
			throw new Exception("Unsupported method: " + measure);
		}
		return scoresArr;
	}
	
	// Compare vectors and calculate scores
	private void computeSimScores(double[] vector, int numOfMatches, int indexOfWord) throws Exception {
		// Initialize arrays to store top matching scores and related words
		String[] topWords = new String[numOfMatches];
		double[] topScores = populateArr(numOfMatches);
		
		// Iterate over an 'embeddings' 2D array
		for (int i = 0; i < MAX_WORDS; i++) {
			// Skip the specific vector related to user inputted word - not to be compared with itself
			if (indexOfWord == i) {
				continue;
			}
			// Initialize 'simScore' variable that stores result of comparison between two vectors
			double simScore = 0.0;
			// Calculate similarity score of a vector at the current index of 'embeddings' and input vector
			switch (measure) {
				case "Dot Product" 		  -> {
					simScore = dotProduct(i, vector); 
					}
				case "Euclidean Distance" -> { 
					simScore = euclideanDistance(i, vector);
				}
				case "Cosine Similarity"  -> {
					simScore = cosineSimilarity(i, vector);
				}
			}
			// If simScore is larger than the smallest element (first element) of the 'topScores' array
			if (simScore > topScores[0]) {
				// Insert simScore and a related word into a proper place in 'topScores' and 'topWords' arrays
				insertIntoArray(topScores, topWords, simScore, words[i]);
			}
		}
		processResults(topWords, topScores);
	}
	
	// Dot Product of vectors
	private double dotProduct(int index, double[] vector) {
		double score = 0.0;
		// Iterate over embeddings dimension and calculate similarity score
		for (int i = 0; i < VECTOR_DIMENSION; i++) {
			score += vector[i] * embeddings[index][i];
		}
		return score;
	}
	
	// Euclidean Distance of vectors
	private double euclideanDistance(int index, double[] vector) {
		return 0;
	}
	
	// Cosine Similarity of vectors
	private double cosineSimilarity(int index, double[] vector) {
		return 0;
	}
}
