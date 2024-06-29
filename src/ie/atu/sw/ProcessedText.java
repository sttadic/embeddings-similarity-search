package ie.atu.sw;

// Record representing processed text data: vector of word(s), its index (-1 for multiple words), processed text
public record ProcessedText(double[] vector, int index, String procText) {}