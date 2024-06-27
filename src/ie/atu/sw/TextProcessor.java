package ie.atu.sw;

public class TextProcessor {
	private String text;
	private String[] words;
	private double[][] embeddings;
	
	// Initialize instance variables
	public TextProcessor(String text, String[] words, double[][] embeddings) {
		this.text = text;
		this.words = words;
		this.embeddings = embeddings;
	}
	
	// Process text and return a record holding a vector and its index within word embeddings
	public VectorIndexPair processText() throws Exception {
		// Initialize 'vector' array and 'index' variable
		double[] vector = new double[EmbeddingsProcessor.VECTOR_DIMENSION];
		int indexOfWord = -1;
		
		// Pre-process the text to remove stop words
		String[] processedParts = preProcessor(text);

		// Process single word/multiple words accordingly
		if (processedParts.length == 1) {
			indexOfWord = getIndex(processedParts[0]);
			// Throw exception to be displayed as a message to the user if word(s) not found within embeddings array
			if (indexOfWord == -2) {
				throw new Exception("The word(s) '" + text
						+ "' could not be found in embeddings! Please try another word(s) or check your spelling");
			}
			// Store the word vector from 2D array 'embeddings' based on indexOfWord
			vector = embeddings[indexOfWord];
		} else {
			// Calculate average vector if multiple words are left after pre-processing
			vector = averageVector(processedParts);
		}
		// Return vector and indexOfWord (indexOfWord value set to -1 when average vector is calculated - multiple words)
		return new VectorIndexPair(vector, indexOfWord);
	}
	
	// Remove stop words. To simplify things, all words with no matches in embeddings array are removed
	private String[] preProcessor(String text) {
		// Split the text into words using space as a delimiter
		String[] parts = text.split(" ");
		// Return array if only one element in 'parts' array
		if (parts.length == 1) return parts;
		// Create an instance of StringBuilder to accumulate the matching words
		StringBuilder sbNoStopWords = new StringBuilder();
		// Iterate over the 'parts' array and compare each word against a word from 'words' array
		for (String part : parts) {
			for (String word : words) {
				// If match found, append to StringBuilder along with space, and break out of the inner loop
				if (part.equals(word)) {
					sbNoStopWords.append(part).append(" ");
					break;
				}
			}
		}
		// Convert StringBuilder back to string, trim any trailing space
		String strNoStopWords = sbNoStopWords.toString().trim();
		// Split the accumulated string into an array of words and return it
		// If 'strNoStopWords' is empty, splitting it will still return an array with 1 empty string element
		return strNoStopWords.split(" ");
	}
	
	// Return index of a word within embeddings array. Return -2 if word not in embeddings
	private int getIndex(String word) {
		for (int i = 0; i < words.length; i++) {
			if (word.equals(words[i])) {
				return i;
			}
		}
		return -2;
	}
	
	// Calculate average vector if 'text' variable contains multiple words
	private double[] averageVector(String[] processedParts) {
		double[] avgVector = new double[EmbeddingsProcessor.VECTOR_DIMENSION];
		int partsLength = processedParts.length;
		int[] partsIndices = new int[partsLength];
		
		// Get the index of each word in 'processedParts' array from 'words' array
		// It corresponds to the index in 2D array 'embeddings'
		for (int i = 0; i < partsLength; i++) {
			partsIndices[i] = getIndex(processedParts[i]);
		}
		
		// Iterate over avgVector length (VECTOR_DIMENSION)
		for (int i = 0; i < avgVector.length; i++) {
			// Initialize varibale to store sum of each embedding dimension
			double sumOfEmbeddings = 0.0;
			// Iterate over number of words in 'processedParts' array
			for (int j = 0; j < partsLength; j++) {
				// Sum the embeddings at current index for all words from 'processedParts' array
				sumOfEmbeddings += embeddings[partsIndices[j]][i];  
			}
			// Calculate average embedding and store it at current index of 'avgVector' array
			avgVector[i] = sumOfEmbeddings/partsLength;
		}
		return avgVector;
	}
}