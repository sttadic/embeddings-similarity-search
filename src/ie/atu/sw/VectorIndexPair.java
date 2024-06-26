package ie.atu.sw;

// Record holding vector of a single word and its index within the embeddings array
public record VectorIndexPair(double[] vector, int index) {}