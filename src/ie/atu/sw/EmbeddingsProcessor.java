package ie.atu.sw;

import java.io.*;

public class EmbeddingsProcessor {
	private FileIO fileHandler;
	private static final int VECTOR_DIMENSION = 50;
	private static final int MAX_WORDS = 59_602;
	private String[] words;
	private double[][] embeddings;
	
	public EmbeddingsProcessor() {
		this.fileHandler = new FileIO();
		this.words = new String[MAX_WORDS];
		this.embeddings = new double[MAX_WORDS][VECTOR_DIMENSION];
	}
	
	public void start(String embeddingsFilePath, String outputFilePath, String distanceMetric, String textToCompare)
			throws IOException {
		BufferedReader br = fileHandler.readFile(embeddingsFilePath);
		
	}
	
	private String[] extractWords(BufferedReader b) {
		return null;
	}
	
	private double[][] extractEmbeddings(BufferedReader b) {
		return null;
	}
}
