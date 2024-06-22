package ie.atu.sw;

import java.io.*;

public class FileIO {
	
	public BufferedReader readFile(String path) throws IOException {
		try {
			return new BufferedReader(new InputStreamReader(new FileInputStream(path)));
		} catch (IOException e) {
			throw new IOException(ConsoleColour.RED + "Word embeddings file " + ConsoleColour.GREEN + path
					+ ConsoleColour.RED + " could not be found");
		}
	}
	
	public void writeToFile(String path) throws IOException {
		throw new IOException(ConsoleColour.RED + "Error creating " + ConsoleColour.GREEN + path);
		
	}
}