package ie.atu.sw;

import java.io.*;

public class FileIO {
	// Handle file input using instance of BufferedReader
	public BufferedReader readFile(String path) throws IOException {
		try {
			return new BufferedReader(new InputStreamReader(new FileInputStream(path)));
		} catch (IOException e) {
			throw new IOException("Word embeddings file '" +  path + "' cannot be found!");
		}
	}
	// Writing to the output stream
	public FileWriter writeToFile(String path) throws IOException {
		try {
			return new FileWriter(path);
		} catch (IOException e) {
			throw new IOException("Error creating '" + path + "'!");
		}
	}
}