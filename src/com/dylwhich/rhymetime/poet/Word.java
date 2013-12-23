package com.dylwhich.rhymetime.poet;

import java.util.ArrayList;
import java.util.List;

public class Word {
	public static final int POS_Noun = 0x0001, POS_Plural = 0x0002, POS_NounPhrase = 0x0004,
			POS_VerbParticiple = 0x0008, POS_VerbTransitive = 0x0010, POS_VerbIntransitive = 0x0020,
			POS_Adjective = 0x0040, POS_Adverb = 0x0080, POS_Conjunction = 0x0100, POS_Preposition = 0x0200,
			POS_Interjection = 0x0400, POS_Pronoun = 0x0800, POS_DefiniteArticle = 0x1000,
			POS_IndefiniteArticle = 0x2000, POS_Nominative = 0x4000, POS_Nouns = POS_Noun | POS_Plural | POS_NounPhrase
					| POS_Pronoun, POS_Verbs = POS_VerbParticiple | POS_VerbTransitive | POS_VerbIntransitive,
			POS_Unknown = -1;

	public String text;
	public char partOfSpeech;
	public int numSyllables;
	public short[] pron;

	public List<Word> synonyms;

	public Word() {
		synonyms = new ArrayList<Word>();
	}

	public void print() {
		System.out
				.printf("%s %d %s ",
						text,
						numSyllables,
						partOfSpeech == POS_Noun ? "Noun"
								: partOfSpeech == POS_Plural ? "Plural"
										: partOfSpeech == POS_NounPhrase ? "NounPhrase"
												: partOfSpeech == POS_VerbParticiple ? "Verb-Participle"
														: partOfSpeech == POS_VerbTransitive ? "Verb-Trasitive"
																: partOfSpeech == POS_VerbIntransitive ? "Verb-Intransitive"
																		: partOfSpeech == POS_Adjective ? "Adjective"
																				: partOfSpeech == POS_Adverb ? "Adverb"
																						: partOfSpeech == POS_Conjunction ? "Conjuction"
																								: partOfSpeech == POS_Preposition ? "Preposition"
																										: partOfSpeech == POS_Interjection ? "Interjection"
																												: partOfSpeech == POS_Pronoun ? "Pronoun"
																														: partOfSpeech == POS_DefiniteArticle ? "DefiniteArticle"
																																: partOfSpeech == POS_IndefiniteArticle ? "IndefiniteArticle"
																																		: partOfSpeech == POS_Nominative ? "Nominative"
																																				: "unknown");

		printPron();

		System.out.println();
	}

	public void printPron() {
		int i = 0;
		while (pron[i] != -1) {
			System.out.printf("-%s", pronToText(pron[i]));
			i++;
		}
	}

	public String pronToText(short pr) {
		String result, which = "unknown";
		char syllable = (char) (pr >> 14);
		pr &= 0x3fff;

		if (pr < PhoneSet.PHONE_SET.length) {
			which = PhoneSet.PHONE_SET[pr].display;
		}

		if (syllable > 0) {
			result = "" + (syllable + '0');
		}

		result = which;
		if (syllable == 2) {
			result = result.toUpperCase();
		}

		return result;
	}

	public boolean match(WordFilter filter) {
		return (partOfSpeech & filter.partOfSpeech) != 0 && numSyllables >= filter.minSyllables
				&& numSyllables <= filter.maxSyllables && matchesStress(filter.stresses);
	}

	public boolean matchesStress(char[] stresses) {
		if (stresses[0] != 0) {
			int stress = 0;
			int pos = 0;
			for (;;) {
				if (pron[pos] == -1 || stresses[stress] == 0) {
					return true;
				}

				int syllable = pron[pos] >> 14;

				if (syllable > 0) {
					if ((syllable == 2 || syllable == 3) && stresses[stress] == '0') {
						return false;
					}
					stress++;
				}
				pos++;
			}
		}
		return true;
	}

	@Override
	public boolean equals(Object w) {
		if (!(w instanceof Word)) {
			return false;
		}
		return ((Word) w).text.equals(text);
	}
}
