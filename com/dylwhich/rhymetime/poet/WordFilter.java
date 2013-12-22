package com.dylwhich.rhymetime.poet;

public class WordFilter {
	public static final int MAX_SYLLABLES_IN_WORD = 32;
	public static final int MAX_PHENOMES_IN_WORD = 32;

	public int partOfSpeech;
	public int minSyllables;
	public int maxSyllables;
	public char[] stresses;

	public WordFilter() {
		partOfSpeech = 0xffff;
		minSyllables = 1;
		maxSyllables = 0xffff;
		stresses = new char[MAX_SYLLABLES_IN_WORD + 1];
	}
}
