package ie.atu.sw;

import java.io.*;

public class FileIO {
	
	public void readFile(String path) throws IOException {
		throw new IOException(ConsoleColour.RED + "Word embeddings file " + ConsoleColour.GREEN + path
				+ ConsoleColour.RED + " does not exist");
	}
	
	public void writeToFile(String path) throws IOException {
		throw new IOException(ConsoleColour.RED + "Error creating " + ConsoleColour.GREEN + path);
		
	}
}