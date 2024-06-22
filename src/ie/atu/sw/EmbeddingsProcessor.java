package ie.atu.sw;

import java.io.*;

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
	private double dotProduct(String text) {
		return 0;
	}
	
	// Calculate Euclidean Distance
	private double euclideanDistance(String text) {
		return 0;
	}
	
	// Calculate Cosine Distance
	private double cosineDistance(String text) {
		return 0;
	}
}
