package ie.atu.sw;

import java.io.*;

public class EmbeddingsProcessor {
	private String embeddingsFilePath;
	private String outputFilePath;
	private String distanceMetric;
	private String textToCompare;
	private FileIO fileHandler;
	
	public EmbeddingsProcessor(String embPath, String outPath, String disMetric, String text) {
		this.embeddingsFilePath = embPath;
		this.outputFilePath = outPath;
		this.distanceMetric = disMetric;
		this.textToCompare = text;
		this.fileHandler = new FileIO();
	}
	
	public void start() throws IOException {
		
	}
	
	
}
