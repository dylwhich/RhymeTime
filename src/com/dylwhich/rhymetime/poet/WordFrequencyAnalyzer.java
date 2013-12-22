package com.dylwhich.rhymetime.poet;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class WordFrequencyAnalyzer {
	public static WordFrequencyAnalyzer instance;

	private Map<Word, Map<Word, Integer>> wordMap;

	public void analyzeFile(String filename) throws FileNotFoundException {
		Scanner s = new Scanner(new File(filename));
		String text;
		Word word, previousWord = null;

		while (s.hasNext()) {
			text = WordDatabase.RemovePunctuation(s.next());

			word = WordDatabase.instance.lookup(text);

			if (word != null && previousWord != null) {
				insert(previousWord, word);
			}

			previousWord = word;
		}

		s.close();
	}

	public void save(String filename) {
		// this apparently does nothing
	}

	public void print() {
		for (Word first : wordMap.keySet()) {
			Map<Word, Integer> map = wordMap.get(first);
			for (Word second : map.keySet()) {
				System.out.printf(" %10d %32s %32s\n", map.get(second), first.text, second.text);
			}
		}
	}

	public int lookup(Word word1, Word word2) {
		int score = 0;
		Map<Word, Integer> wordRes = wordMap.get(word1);
		if (wordRes != null) {
			score++;
			Integer freqRes = wordRes.get(word2);
			if (freqRes != null) {
				score += freqRes;
			}
		}
		return score;
	}

	private void insert(Word word1, Word word2) {
		Map<Word, Integer> freqMap;
		if (wordMap.containsKey(word1)) {
			freqMap = wordMap.get(word1);
		} else {
			freqMap = new HashMap<Word, Integer>();
			wordMap.put(word1, freqMap);
		}

		if (freqMap.containsKey(word2)) {
			freqMap.put(word2, freqMap.get(word2) + 1);
		} else {
			freqMap.put(word2, 1);
		}
	}
}
