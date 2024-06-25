package ie.atu.sw;

import java.io.*;

public class FileIO {
	
	public BufferedReader readFile(String path) throws IOException {
		try {
			return new BufferedReader(new InputStreamReader(new FileInputStream(path)));
		} catch (IOException e) {
			throw new IOException("Word embeddings file '" +  path + "' cannot be found!");
		}
	}
	
	public void writeToFile(String path) throws IOException {
		throw new IOException("Error creating '" + path + "'!");
		
	}
}