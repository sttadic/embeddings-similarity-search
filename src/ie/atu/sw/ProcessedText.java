package ie.atu.sw;

// Record holding vector of a word(s), its index (-1 for multiple words), and a string after original text was processed
public record ProcessedText(double[] vector, int index, String postProcText) {}