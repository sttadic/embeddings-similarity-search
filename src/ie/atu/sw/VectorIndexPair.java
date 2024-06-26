package ie.atu.sw;

// Record holding vector of a word and its corresponding index within the embeddings array
public record VectorIndexPair(double[] vector, int index) {}