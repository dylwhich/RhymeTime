package com.dylwhich.rhymetime;

import java.util.HashMap;
import java.util.Map;

import com.dylwhich.rhymetime.poet.WordDatabase;

public class RhymeScheme {
	public static boolean validate(WordDatabase db, String scheme, String[] sentences) {
		if (scheme.length() != sentences.length) {
			throw new IllegalArgumentException("Rhyme scheme and sentence list length must match");
		}

		Map<Character, String> lastRhymes = new HashMap<Character, String>();

		for (int i = 0; i < scheme.length(); i++) {
			if (lastRhymes.containsKey(scheme.charAt(i))) {
				String lastRhyme = lastRhymes.get(scheme.charAt(i));
				if (db.rhymes(getLastWord(sentences[i]), lastRhyme)) {
					lastRhymes.put(scheme.charAt(i), getLastWord(sentences[i]));
				} else {
					return false;
				}
			} else {
				lastRhymes.put(scheme.charAt(i), getLastWord(sentences[i]));
			}
		}

		return true;
	}

	public static String getLastWord(String sentence) {
		return sentence.substring(sentence.lastIndexOf(' ') + 1);
	}
}
