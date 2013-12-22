package com.dylwhich.rhymetime.poet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WordAssembler {
	private class Solution implements Comparable<Solution> {
		int length;
		int score;
		Word[] words;

		@Override
		public int compareTo(Solution second) {

			if (score > second.score) {
				return -1;
			} else if (score < second.score) {
				return 1;
			} else {
				return 0;
			}
		}
	}

	private List<Word>[][] table;
	private List<Solution> solutions;
	private List<Word> concepts;

	public WordAssembler() {
	}

	private void enumerateSolutions(int depth, int[] pos, int length, int numSyllables, Word[] phrase, String rhyme,
			String stresses) {
	}

	private void addSolution(Word[] array) {
	}

	public void run(int[] pos, int length, int numSyllables, String rhymesWith, String stresses, Word[] concepts) {
	}

	public void addSolution(List<Word> phrase) {
		Solution solution = new Solution();
		solution.length = phrase.size();
		solution.words = new Word[solution.length];
		solution.words = phrase.toArray(solution.words);
		solution.score = 0;

		if (WordFrequencyAnalyzer.instance != null) {
			Word previous = null;
			for (int i = 0; i < solution.length; i++) {
				Word cur = solution.words[i];
				if (previous != null) {
					solution.score += WordFrequencyAnalyzer.instance.lookup(previous, cur);
				}
				previous = cur;
			}
		}

		if (concepts != null) {
			for (int j = 0; j < solution.length; j++) {
				for (int i = 0; i < concepts.size(); i++) {
					if (concepts.get(i) == solution.words[j]) {
						solution.score += 32;
					}
				}
			}
		}

		if (solution.score == 0) {
			return;
		}

		if (false) {
			System.out.printf("%2d ", solution.score);
			for (int k = solution.length - 1; k >= 0; k--) {
				System.out.printf("%s ", solution.words[k].text);
			}

			System.out.printf(" [");
			for (int k = solution.length - 1; k >= 0; k--) {
				solution.words[k].printPron();
			}

			System.out.printf("]\n");
		}

		if ((solutions.size() % 10000) == 0) {
			System.out.printf("Found %d solutions....\r", solutions.size());
		}

		solutions.add(solution);
	}

	private void enumerateSolutions(int depth, int POS[], int length, int numSyllables, List<Word> phrase,
			String rhyme, char[] stresses) {
		int stressLen = 0;
		int syllablesUsed = 0;
		if (stresses != null) {
			stressLen = stresses.length;

			for (int i = 0; i < phrase.size(); i++) {
				syllablesUsed += phrase.get(i).numSyllables;
			}
		}

		for (int i = numSyllables - 1; i >= 0; i--) {
			List<Word> rhymes;
			List<Word> choices = table[length - depth - 1][i];
			if (rhyme != null) {
				rhymes = WordDatabase.instance.findRhymes(rhyme, null, choices);
				choices = rhymes;
			}

			for (int j = 0; j < choices.size(); j++) {

				if (stresses != null) {
					int stressesStart = numSyllables - syllablesUsed - choices.get(j).numSyllables;
					int len = stresses.length - stressesStart;
					char[] tmpStresses = new char[len];
					System.arraycopy(stresses, stressesStart, tmpStresses, 0, len);
					if (!choices.get(j).matchesStress(tmpStresses)) {
						continue;
					}
				}

				phrase.add(choices.get(j));

				// if it is not possible to recurse then just output the phrase.
				if (length - depth - 1 == 0 && numSyllables - syllablesUsed == choices.get(j).numSyllables) {
					addSolution(phrase);
				} else if (length - depth - 1 > 0 && numSyllables - syllablesUsed - choices.get(j).numSyllables > 0) {
					enumerateSolutions(depth + 1, POS, length, numSyllables, phrase, null, stresses);
				}
			}

		}
	}

	public void run(int POS[], int length, int numSyllables, String rhymesWith, char[] stresses, List<Word> concepts) {
		// make a 2-D table of results that is length x numSyllables large.
		table = new List[length][];

		for (int i = 0; i < length; i++) {
			table[i] = new List[numSyllables];
		}

		WordFilter filter = new WordFilter();
		List<Word> results = new ArrayList<Word>();

		this.concepts = concepts;

		// for each part of speech given,
		for (int i = 0; i < length; i++) {
			// find all words that are between 1 and numSyllables long.
			filter.maxSyllables = numSyllables;
			filter.partOfSpeech = POS[i];

			results = WordDatabase.instance.filter(filter);

			// separate them and store into their correct location in the table.
			for (int j = 0; j < results.size(); j++) {
				table[i][results.get(j).numSyllables - 1].add(results.get(j));
			}

		}

		if (false) {
			for (int i = 0; i < length; i++) {
				for (int j = 0; j < numSyllables; j++) {
					System.out.printf("-- POS: %d Length: %d\n", POS[i], j + 1);
					for (int k = 0; k < table[i][j].size(); k++) {
						table[i][j].get(k).print();
					}
				}
			}
		}

		List<Word> phrase = new ArrayList<Word>();

		// okay now we can enumerate solutions.
		enumerateSolutions(0, POS, length, numSyllables, phrase, rhymesWith, stresses);

		// destroy the table of results.
		for (int i = 0; i < length; i++) {
			table[i] = null;
		}

		table = null;

		System.out.printf("Sorting...\n");

		if (solutions != null && solutions.size() > 0) {
			Collections.sort(solutions);
		}

		// output the phrase.
		for (int i = 0; i < solutions.size(); i++) {

			System.out.printf("%2d ", solutions.get(i).score);
			for (int k = solutions.get(i).length - 1; k >= 0; k--) {
				System.out.printf("%s ", solutions.get(i).words[k].text);
			}

			System.out.printf(" [");
			for (int k = solutions.get(i).length - 1; k >= 0; k--) {
				solutions.get(i).words[k].printPron();
			}

			System.out.printf("]\n");
		}
	}
}