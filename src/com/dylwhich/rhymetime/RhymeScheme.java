package com.dylwhich.rhymetime;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Bukkit;

import com.dylwhich.javaspeak.javaSpeak;

public class RhymeScheme {
	public static boolean validate(String scheme, String[] sentences) {
		if (scheme.length() != sentences.length) {
			throw new IllegalArgumentException("Rhyme scheme and sentence list length must match");
		}

		Map<Character, String> lastRhymes = new HashMap<Character, String>();

		for (int i = 0; i < scheme.length(); i++) {
			if (sentences[i] == null) {
				return true;
			}

			if (lastRhymes.containsKey(scheme.charAt(i))) {
				String lastRhyme = lastRhymes.get(scheme.charAt(i));

				Bukkit.getLogger().log(Level.INFO, "Checking if " + lastRhyme + " rhymes with " + sentences[i]);
				if (rhymes(sentences[i], lastRhyme)) {
					lastRhymes.put(scheme.charAt(i), sentences[i]);
				} else {
					return false;
				}
			} else {
				lastRhymes.put(scheme.charAt(i), sentences[i]);
			}
		}

		return true;
	}

	public static boolean rhymes(String aWord, String bWord) {
		String aPhon = javaSpeak.getPhonemes(aWord);
		String bPhon = javaSpeak.getPhonemes(bWord);

		return bPhon.endsWith(aPhon.substring(aPhon.length() - 2));
	}
}
