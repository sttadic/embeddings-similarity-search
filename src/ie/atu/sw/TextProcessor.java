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
		// Initialize vector and index variables
		double[] vector = new double[EmbeddingsProcessor.VECTOR_DIMENSION];
		int indexOfWord = -1;
		
		// Pre-process the text to remove stop words
		String[] textParts = preProcessor(text);
		
		// Process single word/multiple words accordingly
		if (textParts.length <= 1) {
			indexOfWord = getIndex(textParts[0]);
			// Throw exception to be displayed as a message to the user if word not found within embeddings array
			if (indexOfWord == -2) {
				throw new Exception("The word(s) '" + text
						+ "' could not be found in embeddings! Please try another word(s) or check your spelling");
			}
			// Store the vector of a word from embeddings 2D array based on indexOfWord
			vector = embeddings[indexOfWord];
		} else {
			// Calculate average vector
			vector = averageVector();
		}
		return new VectorIndexPair(vector, indexOfWord);
	}
	
	// Remove stop words. To simplify things, all words with no matches in embeddings array are removed
	private String[] preProcessor(String text) {
		// Split the text using " " (space) as delimiter and store tokens in 'parts' array
		String[] parts = text.split(" ");
		// Return array if only one element in parts
		if (parts.length == 1) return parts;
		// Create an instance of StringBuilder
		StringBuilder sbNoStopWords = new StringBuilder();
		// Iterate over the parts array and compare each word with elements from words array
		for (int i = 0; i < parts.length; i++) {
			for (int j = 0; j < words.length; j++) {
				// If match found, append to 'sbParts' and break out of the inner loop
				if (parts[i].equals(words[j])) {
					sbNoStopWords.append(parts[i] + " ");
					break;
				}
			}
		}
		// Convert StringBuilder back to string
		String strNoStopWords = sbNoStopWords.toString();
		// Return array without stop words
		return strNoStopWords.split(" ");
	}
	
	// Returns index of a word within embeddings array, returns -2 if word not in embeddings
	private int getIndex(String word) {
		for (int i = 0; i < words.length; i++) {
			if (word.equals(words[i])) {
				return i;
			}
		}
		return -2;
	}
	
	// Calculates average vector if text variable contains multiple words
	private double[] averageVector() {
		return null;
	}
}