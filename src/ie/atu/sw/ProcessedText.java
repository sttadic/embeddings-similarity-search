package ie.atu.sw;

// Record holding vector of a word(s), its index, and resulting string after stop words are removed
public record ProcessedText(double[] vector, int index, String postProcText) {}