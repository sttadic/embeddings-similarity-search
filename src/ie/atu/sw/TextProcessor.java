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
	
	public double[] processText() {
		double[] vector = new double[EmbeddingsProcessor.VECTOR_DIMENSION];
		
		String[] parts = text.split(" ");
		
		return null;
	}
}