package ie.atu.sw;

public class TextProcessor {
	private String text;
	private String[] words;
	private double[][] embeddings;
	
	public TextProcessor(String text, String[] words, double[][] embeddings) {
		this.text = text;
		this.words = words;
		this.embeddings = embeddings;
	}
	
	// Process text and return a record holding a vector and its index within word embeddings
	// If text consists of multiple words, return average vector and -1 (indexOfWord) as indicator
	public VectorIndexPair processText() throws Exception {
		// Initialize vector and index
		double[] vector = new double[EmbeddingsProcessor.VECTOR_DIMENSION];
		int indexOfWord = -1;
		// Split the text using " " (space) as delimiter
		String[] parts = text.split(" ");
		
		// Process a word/multiple words accordingly
		if (parts.length <= 1) {
			indexOfWord = getIndex();
			// Throw exception to be displayed as a message to the user if word not found within embeddings array
			if (indexOfWord == -2) {
				throw new Exception("The word '" + text
						+ "' could not be found in embeddings! Please try another word or check your spelling");
			}
			vector = embeddings[indexOfWord];
		} else {
			vector = averageVector();
		}
		
		return new VectorIndexPair(vector, indexOfWord);
	}
	
	
	// Returns index of a word within embeddings array, returns -2 if word not in embeddings
	private int getIndex() {
		for (int i = 0; i < words.length; i++) {
			if (text.equals(words[i])) {
				return i;
			}
		}
		return -2;
	}
	
	private double[] averageVector() {
		return null;
	}
}