package ie.atu.sw;

public class TextProcessor {
	private String text;
	private String[] words;
	private double[][] embeddings;
	private String postProcText;
	
	// Initialize instance variables
	public TextProcessor(String text, String[] words, double[][] embeddings) {
		this.text = text;
		this.words = words;
		this.embeddings = embeddings;
	}
	
	// Process text and return a record holding a vector and its index within word embeddings
	public ProcessedText processText() throws Exception {
		// Initialize array to hold calculated vector and variable to store its index
		double[] vector = new double[EmbeddingsProcessor.VECTOR_DIMENSION];
		int indexOfWord = -1;
		
		// Pre-process the text to remove stop words
		String[] processedParts = preProcessor();
		
		// Input is a single words or single word left after pre-processing
		if (processedParts.length == 1) {
			// Store words index from 'words' array
			indexOfWord = getIndex(processedParts[0]);
			// Throw exception and display it to the user if word(s) not found within embeddings array
			if (indexOfWord == -2) {
				throw new Exception("The word(s) '" + text
						+ "' could not be found in embeddings! Please try another word(s) or check your spelling");
			}
			// Store vector representing the word from 2D array 'embeddings' based on indexOfWord
			vector = embeddings[indexOfWord];
		// Multiple words left after pre-processing
		} else {
			// Store average vector
			vector = averageVector(processedParts);
		}
		// Return vector, indexOfWord which value set to -1 if average vector is calculated, and text left for analysis
		return new ProcessedText(vector, indexOfWord, postProcText);
	}
	
	// Remove stop words. To simplify things, all words with no matches in embeddings array are removed
	private String[] preProcessor() {
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
		// Convert StringBuilder back to string, trim any trailing space and assign it to 'postProcText'
		postProcText = sbNoStopWords.toString().trim();
		// Split the accumulated string into an array of words and return it
		// If 'postProcText' is empty, splitting it will still return an array with 1 empty string element
		return postProcText.split(" ");
	}
	
	// Return index of a word within embeddings array. Return -2 if no match found in 'words' array
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