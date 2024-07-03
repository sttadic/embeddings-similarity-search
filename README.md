# Java Application - Similarity Search with Word Embeddings
Final project for the Object Oriented Software Development module - Higher Diploma in Software Development. <br>
Author: Stjepan Tadic

<br>

## Description
This Java application performs similarity search between words. Users can input a word or a short sentence, and the application will calculate similarity between the corresponding vectors based on the selected metric (vector comparison algorithm). The program returns the closest matches from a predefined set of word embeddings (GloVe embeddigs reduced to 59602 words).

<br>

## Usage
- Clone the repository:
```bash
git clone https://github.com/sttadic/embeddings-similarity-search
```

- Run the compiled Runner class:
```bash
java ie.atu.sw.Runner
```
<br>

## Application Setup

1. Specify paths for word embeddings and output results file, or use the defaults
2. Provide a word or a short sentence to compare against the embeddings
3. Choose from three comparison algorithms: Dot Product, Euclidean Distance, and Cosine Similarity (default), or use all of them
4. Indicate the number of top matches to be outputted (default is 10)
5. Start the processing

<br>

## Features

- **User Configuration** <br>
Allows comprehensive customization where users can define input, output, vector comparison algorithms and number of top matches.

- **Intuitive UI** <br>
Features a clear and intuitive user interface with distinct colours for menu options, specified parameters, and error messages. This design ensures users can easily see what is currently selected, what configurations are set, and what parts are missing.

- **Text Pre-processing** <br>
Removes words not found in the embeddings from further processing and computes average vector for multi-word inputs.

- **Robust Error Handling** <br> 
Provides user-friendly error messages for invalid inputs, missing files, incomplete program configuration, unsupported format of embeddings file, and more.

- **Neat and Informative Output** <br>
Displays results in a well-organized table format, showing words and their respective similarity scores in an ordered fashion, along with the metric used, original input text, and the processed text.