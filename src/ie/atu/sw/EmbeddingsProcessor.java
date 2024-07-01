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
	// Text, metric (vector comparison algorithm), and text after processing
	private String text;
	private String metric;
	private String procText;
	
	// Initialize file handler, text, metric and allocate memory for 'words' and 'embeddings' arrays
	public EmbeddingsProcessor(String text, String metric) {
		this.fileHandler = new FileIO();
		this.text = text;
		this.metric = metric;
		this.words = new String[MAX_WORDS];
		this.embeddings = new double[MAX_WORDS][VECTOR_DIMENSION];
	}
	
	// Start with processing
	public void start(String embeddingsFilePath, String outputFilePath, int numOfMatches)
			throws Exception {
		
		// Read the embeddings file using BufferedReader, extract embeddings and close input stream
		BufferedReader bReader = fileHandler.readFile(embeddingsFilePath);
		extractWordEmbeddings(bReader);
		bReader.close();
		
		// Throw an exception in case nothing was extracted from word embeddings file
		if (words[0] == null) throw new Exception("Word embeddings file is empty!");
		
		// Create instance of TextProcessor and process the input text
		TextProcessor textProc = new TextProcessor(text, words, embeddings);
		// Instantiate record that holds vector, index of a word from 'words' array, and processed text
		ProcessedText processedText = textProc.processText();
		double[] vector = processedText.vector();
		int indexOfWord = processedText.index();
		procText = processedText.procText();
		
		// Create a file output stream
		out = fileHandler.writeToFile(outputFilePath);
		
		// Start vector comparison and compute similarity scores
		if (metric.equals("All")) {
			String[] allMetrics = {"Dot Product", "Euclidean Distance", "Cosine Similarity"};
			for (String m : allMetrics) {
				metric = m;
				computeSimScores(vector, numOfMatches, indexOfWord);
			}
		} else {
			computeSimScores(vector, numOfMatches, indexOfWord);
		}
		
		// Close output stream
		out.close();
	}
	
	// Extract elements from each line of input stream and store them into relevant arrays
	private void extractWordEmbeddings(BufferedReader br) throws Exception {
		int i = 0;
		String line = null;
		// Iterate over input stream line by line
		while ((line = br.readLine()) != null) {
			try {
				// Use comma as delimiter to split the lines of text and trim whitespace
				String[] parts = line.trim().split(",");
				// Store elements into arrays
				words[i] = parts[0];
				for (int j = 1; j <= 50; j++) {
					embeddings[i][j-1] = Double.parseDouble(parts[j]);
				}
				i++;
			} catch (Exception e) {
				br.close();
				throw new Exception("Error reading word embeddings file!");
			}
		}
	}
	
	// Print to console and write results to the file
	private void printAndWrite(String s) throws IOException {
		System.out.print(s);
		out.write(s);
	}
	
	/* 
	 * Process and format results
	 * Format strings explanation found at: https://www.developer.com/java/java-string-format-method/
	 */
	private void processResults(String[] topWords, double[] topScores) throws IOException {
		System.out.println("\n");
		printAndWrite("* Scores represent " + metric + " between vectors\n");
		printAndWrite("* Original text: - " + text + " -\n");
		printAndWrite("* Analysed text: - " + procText + " -\n");
		printAndWrite("  ==========================================\n");
		printAndWrite("   Top Matching Words |  Similarity Scores\n");
		printAndWrite("  ====================|=====================\n");
		for (int i = 0; i < topWords.length; i++) {
			String row = String.format("  %-4s%-16s|  %s%n", (i+1) + ".", topWords[i], topScores[i]);
			printAndWrite(row);
		}
		printAndWrite("  ==========================================\n\n");
	}
	
	// Insert similarity scores and corresponding words into arrays in sorted order
	private void insertIntoArr(double[] topScores, String[] topWords, double newScore, String newWord) {
		int i = 0;
		// Euclidean distance (smaller values indicate greater similarity between vectors)
		if (metric.equals("Euclidean Distance")) {
			// Return unless newScore is smaller than the largest (last) element of the 'topScores' array
			if (newScore > topScores[topScores.length-1]) return;
			// If 'i' larger than zero, and 'topScores' prior element larger than new score
			for (i = topScores.length - 1; i > 0 && topScores[i - 1] > newScore; i--) {
				// Overwrite current element with the previous one - take out largest (last) element
				topScores[i] = topScores[i - 1];
				topWords[i] = topWords[i - 1];
			} 
		// Cosine Similarity and Dot Product (larger values indicate greater similarity between vectors)
		} else {
			// Return unless newScore is larger than the smallest (last) element of the 'topScores' array
			if (newScore < topScores[topScores.length-1]) return; 
			// If 'i' larger than 0, and 'topScores' prior element smaller than new score
			for (i = topScores.length - 1; i > 0 && topScores[i - 1] < newScore; i--) {
				// Overwrite current element with the previous one - take out smallest (last) element
				topScores[i] = topScores[i - 1];
				topWords[i] = topWords[i - 1];
			}
		}
		// Store new score and new word at the current index of arrays
		topScores[i] = newScore;
		topWords[i] = newWord;
	}
	
	// Fill array with infinities so first n number of calculated scores can be added
	private double[] populateArr(int numOfMatches) {
		double[] arr = new double[numOfMatches];
		// For euclidean distance use +inifinity since smaller values indicate greater similarity
		if (metric.equals("Euclidean Distance")) {
			Arrays.fill(arr, Double.POSITIVE_INFINITY);
		} else {
			Arrays.fill(arr, Double.NEGATIVE_INFINITY);
		}
		return arr;
	}
	
	// Compare vectors and calculate scores
	private void computeSimScores(double[] vector, int numOfMatches, int indexOfWord) throws Exception {
		// Initialize arrays to store top matching scores and related words
		String[] topWords = new String[numOfMatches];
		double[] topScores = populateArr(numOfMatches);
		
		// Iterate over an 'embeddings' 2D array
		for (int i = 0; i < MAX_WORDS; i++) {
			// Skip index of a vector representing word from user input - not to be compared with itself
			if (indexOfWord == i) {
				continue;
			}
			// Initialize 'simScore' variable to store result of comparison between two vectors
			double simScore = 0.0;
			// Calculate similarity between vector at the current index of 'embeddings' and input vector
			switch (metric) {
				case "Dot Product" 		  -> {
					simScore = dotProduct(i, vector); 
					}
				case "Euclidean Distance" -> { 
					simScore = euclideanDistance(i, vector);
				}
				case "Cosine Similarity"  -> {
					simScore = cosineSimilarity(i, vector);
					// Skip current iteration of a loop if 'NaN' is returned
					if (Double.isNaN(simScore)) continue;
				}
				default					  -> {
					throw new Exception("Unsupported comparison algorithm: " + metric);
				}
			}
			// Insert simScore and a related word into a proper place in 'topScores' and 'topWords' arrays
			insertIntoArr(topScores, topWords, simScore, words[i]);
		}
		processResults(topWords, topScores);
	}
	
	// Dot Product of vectors
	private double dotProduct(int index, double[] vector) {
		double dotProd = 0.0;
		// Iterate over embeddings dimension and multiply elements of vectors
		for (int i = 0; i < VECTOR_DIMENSION; i++) {
			dotProd += vector[i] * embeddings[index][i];
		}
		// Return dot product
		return dotProd;
	}
	
	// Euclidean Distance of vectors
	private double euclideanDistance(int index, double[] vector) {
		double sum = 0.0;
		// Calculate sum of squared differences between vectors
		for (int i = 0; i < VECTOR_DIMENSION; i++) {
			sum += Math.pow(vector[i] - embeddings[index][i], 2);
		}
		// Return euclidean distance (square root of sum)
		return Math.sqrt(sum);
	}
	
	// Cosine Similarity of vectors
	private double cosineSimilarity(int index, double[] vector) {
		double dotProd = 0.0;
		double sumInputVec = 0.0;
		double sumEmbedVec = 0.0;
		// Calculate dot product and sum of squares of vector elements
		for (int i = 0; i < VECTOR_DIMENSION; i++) {
			dotProd += vector[i] * embeddings[index][i];
			sumInputVec += Math.pow(vector[i], 2);
			sumEmbedVec += Math.pow(embeddings[index][i], 2);
		}
		// Square root of product of vectors
		double sqRootOfProd = Math.sqrt(sumInputVec * sumEmbedVec);
		// Avoid possible division by zero by returning 'NaN' as a flag
		if (sqRootOfProd == 0.0) return Double.NaN;
		// Return cosine similarity (quotient of dot product and 'sqRootOfProd') 
		return dotProd / sqRootOfProd;
	}
}
