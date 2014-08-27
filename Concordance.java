import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeMap;

public class Concordance {
	// class for storing the word count and position for each word
	public static class WordCount_Positions {
		private int wordCount = 0;
		private ArrayList<Integer> positions = new ArrayList<Integer>();

		public WordCount_Positions(int startingPosition) {
			wordCount++;
			positions.add(startingPosition);
		}

		public void addToPositions(int newPosition) {
			this.wordCount++;
			this.positions.add(newPosition);
			
		}

		@Override
		public String toString() {
			String output = "{" + Integer.toString(this.wordCount) + ": ";
			for (int eachPosition : this.positions) {
				// adding positions and commas
				output = output + Integer.toString(eachPosition) + ",";
			}
			// removing last comma
			output = output.substring(0, output.length() - 1);
			output = output + "}";
			return output;
		}
	}

	public static void main(String[] args) throws IOException {
		TreeMap<String, WordCount_Positions> concordance = new TreeMap<String, WordCount_Positions>();
		// reading from input file
		Scanner in = new Scanner(System.in);
		System.out.println("What file are you reading from?");
		String fileName = in.next();
		FileReader foo = new FileReader(fileName);
		Scanner inFile = new Scanner(foo);
		boolean sentenceNumberChange = false;
		int sentenceNumber = 0;
		while (inFile.hasNext()) {
			String nextWord = inFile.next();
			if (nextWord.charAt(nextWord.length() - 1) == '.') {
				sentenceNumber++;
				sentenceNumberChange = true;
			}
			// removing symbols and punctuation from text
			nextWord = nextWord.replaceAll("[^a-zA-Z0-9.\\-\\s]", "");
			nextWord = nextWord.trim();
			if (nextWord.contains("-")) {
				String[] multipleWords = nextWord.split("-");
				for (String eachWord : multipleWords) {
					if (sentenceNumberChange) {
						eachWord = eachWord.replace(".", "");
						add(eachWord.toLowerCase(), sentenceNumber - 1,
								concordance);
					} else {
						add(eachWord.toLowerCase(), sentenceNumber, concordance);
					}
				}
			}
			if (!nextWord.contains("-")) {
				if (sentenceNumberChange) {
					nextWord = nextWord.replace(".", "");
					add(nextWord.toLowerCase(), sentenceNumber - 1, concordance);
				} else {
					add(nextWord.toLowerCase(), sentenceNumber, concordance);
				}
			}
			sentenceNumberChange = false;
		}
		System.out.println("The concordance from " + fileName
				+ " is listed below...");
		displayConcordance(concordance);
	}

	// adding words to concordance
	public static void add(String word, int sentenceNumber,
			TreeMap<String, WordCount_Positions> concordance) {
		if (concordance.containsKey(word)) {
			for (Entry<String, WordCount_Positions> entry : concordance
					.entrySet()) {
				String key = entry.getKey();
				if (key.equalsIgnoreCase(word)) {
					WordCount_Positions wordRepeated = entry.getValue();
					wordRepeated.addToPositions(sentenceNumber);
					entry.setValue(wordRepeated);
					return;
				}
			}

		}
		if (!concordance.containsKey(word)) {
			WordCount_Positions value = new WordCount_Positions(sentenceNumber);
			concordance.put(word, value);
		}

	}

	// displaying Concordance
	public static void displayConcordance(
			TreeMap<String, WordCount_Positions> concordance) {
		// printing out all values
		for (Entry<String, WordCount_Positions> entry : concordance.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue().toString();
			System.out.printf("%s  %s\n", key, value);
		}
	}

}
