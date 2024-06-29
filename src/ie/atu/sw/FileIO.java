package ie.atu.sw;

import java.io.*;

public class FileIO {
	// Open a file for reading and return instance of BufferedReader
	public BufferedReader readFile(String path) throws IOException {
		try {
			return new BufferedReader(new InputStreamReader(new FileInputStream(path)));
		} catch (IOException e) {
			throw new IOException("Word embeddings file '" +  path + "' cannot be found!");
		}
	}
	// Open a file for writing and return instance of FileWriter
	public FileWriter writeToFile(String path) throws IOException {
		try {
			return new FileWriter(path);
		} catch (IOException e) {
			throw new IOException("Error creating '" + path + "'!");
		}
	}
}