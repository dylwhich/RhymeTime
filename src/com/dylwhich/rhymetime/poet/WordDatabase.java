package com.dylwhich.rhymetime.poet;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;

public class WordDatabase {

	public enum PhoneType {
		a_in_dab,
		a_in_air,
		a_in_far,
		a_in_day,
		a_in_ado,
		ir_in_tire,
		b_in_nab,
		ch_in_ouch,
		d_in_pod,
		e_in_red,
		e_in_see,
		f_in_elf,
		g_in_fig,
		h_in_had,
		w_in_white,
		i_in_hid,
		i_in_ice,
		g_in_vegetably,
		c_in_act,
		l_in_ail,
		m_in_aim,
		ng_in_bang,
		n_in_and,
		oi_in_oil,
		o_in_bob,
		ow_in_how,
		o_in_dog,
		o_in_boat,
		oo_in_too,
		oo_in_book,
		p_in_imp,
		r_in_ire,
		sh_in_she,
		s_in_sip,
		th_in_bath,
		th_in_the,
		t_in_tap,
		u_in_cup,
		u_in_burn,
		v_in_average,
		w_in_win,
		y_in_you,
		s_in_vision,
		z_in_zoo,
		a_in_ami,
		n_in_francoise,
		r_in_der,
		ch_in_bach,
		eu_in_bleu,
		u_in_duboise,
		wa_in_noire;
	};

	private static class StateMachine {
		public enum State {
			start,
			got_lparen,
			got_lparen_at,
			got_a,
			got_at,
			got_A,
			got_O,
			got_d,
			got_e,
			got_h,
			got_o,
			got_t,
			got_W,
			end
		}

		public static final StateMachine[] STATE_MACHINE = new StateMachine[] {
				new StateMachine(State.start, '&', PhoneType.a_in_dab, State.start),
				new StateMachine(State.start, '(', -1, State.got_lparen),
				new StateMachine(State.start, '[', -1, State.got_lparen),
				new StateMachine(State.start, '@', PhoneType.a_in_air, State.start),
				new StateMachine(State.got_lparen, '@', -1, State.got_lparen_at),
				new StateMachine(State.got_lparen_at, ')', PhoneType.a_in_air, State.start),
				new StateMachine(State.got_lparen_at, ']', PhoneType.a_in_air, State.start),
				new StateMachine(State.start, '-', PhoneType.ir_in_tire, State.start),
				new StateMachine(State.start, '@', -1, State.got_at),
				new StateMachine(State.got_at, 'r', PhoneType.u_in_burn, State.start),
				new StateMachine(State.got_at, '?', PhoneType.a_in_ado, State.start),
				new StateMachine(State.start, 'A', -1, State.got_A),
				new StateMachine(State.got_A, 'U', PhoneType.ow_in_how, State.start),
				new StateMachine(State.got_A, '?', PhoneType.a_in_far, State.start),
				new StateMachine(State.start, 'D', PhoneType.th_in_the, State.start),
				new StateMachine(State.start, 'E', PhoneType.e_in_red, State.start),
				new StateMachine(State.start, 'I', PhoneType.i_in_hid, State.start),
				new StateMachine(State.start, 'N', PhoneType.ng_in_bang, State.start),
				new StateMachine(State.start, 'O', -1, State.got_O),
				new StateMachine(State.got_O, 'i', PhoneType.oi_in_oil, State.start),
				new StateMachine(State.got_O, '?', PhoneType.o_in_dog, State.start),
				new StateMachine(State.start, 'R', PhoneType.r_in_der, State.start),
				new StateMachine(State.start, 'S', PhoneType.sh_in_she, State.start),
				new StateMachine(State.start, 'T', PhoneType.th_in_bath, State.start),
				new StateMachine(State.start, 'U', PhoneType.oo_in_book, State.start),
				new StateMachine(State.start, 'Y', PhoneType.u_in_duboise, State.start),
				new StateMachine(State.start, 'V', PhoneType.v_in_average, State.start), // french?
				// not
				// in
				// readme,
				new StateMachine(State.start, 'W', -1, State.got_W), // not in
				// readme
				new StateMachine(State.got_W, 'A', PhoneType.wa_in_noire, State.start),
				new StateMachine(State.start, 'Z', PhoneType.s_in_vision, State.start),
				new StateMachine(State.start, 'b', PhoneType.b_in_nab, State.start),
				new StateMachine(State.start, 'd', -1, State.got_d),
				new StateMachine(State.got_d, 'Z', PhoneType.g_in_vegetably, State.start),
				new StateMachine(State.got_d, '?', PhoneType.d_in_pod, State.start),
				new StateMachine(State.start, 'a', -1, State.got_a),
				new StateMachine(State.got_a, 'I', PhoneType.i_in_ice, State.start),
				new StateMachine(State.got_a, '?', PhoneType.a_in_dab, State.start), // due
				// to
				// errors
				// in
				// mobydict.
				new StateMachine(State.start, 'e', -1, State.got_e),
				new StateMachine(State.got_e, 'I', PhoneType.a_in_day, State.start),
				new StateMachine(State.start, 'f', PhoneType.f_in_elf, State.start),
				new StateMachine(State.start, 'g', PhoneType.g_in_fig, State.start),
				new StateMachine(State.start, 'h', -1, State.got_h),
				new StateMachine(State.got_h, 'w', PhoneType.w_in_white, State.start),
				new StateMachine(State.got_h, '?', PhoneType.h_in_had, State.start),
				new StateMachine(State.start, 'i', PhoneType.e_in_see, State.start),
				new StateMachine(State.start, 'j', PhoneType.y_in_you, State.start),
				new StateMachine(State.start, 'k', PhoneType.c_in_act, State.start),
				new StateMachine(State.start, 'l', PhoneType.l_in_ail, State.start),
				new StateMachine(State.start, 'm', PhoneType.m_in_aim, State.start),
				new StateMachine(State.start, 'n', PhoneType.n_in_and, State.start),
				new StateMachine(State.start, 'o', -1, State.got_o),
				new StateMachine(State.got_o, 'U', PhoneType.o_in_boat, State.start),
				new StateMachine(State.start, 'p', PhoneType.p_in_imp, State.start),
				new StateMachine(State.start, 'r', PhoneType.r_in_ire, State.start),
				new StateMachine(State.start, 's', PhoneType.s_in_sip, State.start),
				new StateMachine(State.start, 't', -1, State.got_t),
				new StateMachine(State.got_t, 'S', PhoneType.ch_in_ouch, State.start),
				new StateMachine(State.got_t, '?', PhoneType.t_in_tap, State.start),
				new StateMachine(State.start, 'u', PhoneType.oo_in_too, State.start),
				new StateMachine(State.start, 'v', PhoneType.v_in_average, State.start),
				new StateMachine(State.start, 'w', PhoneType.w_in_win, State.start),
				new StateMachine(State.start, 'x', PhoneType.ch_in_bach, State.start),
				new StateMachine(State.start, 'y', PhoneType.eu_in_bleu, State.start),
				new StateMachine(State.start, 'z', PhoneType.z_in_zoo, State.start),
				new StateMachine(State.start, '/', -1, State.start),
				new StateMachine(State.start, '_', -1, State.start),
				new StateMachine(State.start, '\'', -2, State.start),
				new StateMachine(State.start, ',', -3, State.start),
				new StateMachine(State.start, (char) 0, -1, State.end) };

		public StateMachine(State s, char c, int a, State n) {
			state = s;
			ch = c;
			action = a;
			nextState = n;
		}

		public StateMachine(State s, char c, PhoneType t, State n) {
			state = s;
			ch = c;
			action = t.ordinal();
			nextState = n;
		}

		public State state;
		public char ch;
		public int action;
		public State nextState;
	}

	private final Map<String, Word> wordMap;
	private final List<Word> wordArray;

	public WordDatabase() {
		wordMap = new HashMap<String, Word>(100000);
		wordArray = new ArrayList<Word>();
	}

	public boolean load(String filename) throws FileNotFoundException {
		File f = new File(filename);
		int max = 0;
		Scanner s = new Scanner(f);

		while (s.hasNextLine()) {
			String line = s.nextLine();

			if (line == null) {
				continue;
			}

			StringTokenizer tok = new StringTokenizer(line, " \n\r");
			String name = tok.nextToken();

			if (name == null) {
				continue;
			}

			String pos = tok.nextToken();

			if (pos == null) {
				continue;
			}

			String pron = tok.nextToken();

			if (pron == null) {
				continue;
			}

			char posType = (char) MobyToPartOfSpeech(pos.charAt(0));
			if (posType == Word.POS_Unknown) {
				continue;
			}

			short[] pronBuffer = new short[32];
			short[] sylBuffer = new short[1];
			int numPron = generateSyllables(pron, pronBuffer, pronBuffer.length - 1, sylBuffer);
			int numSyllables = sylBuffer[0];

			if (numPron == 0) {
				continue;
			}

			if (numPron > max) {
				max = numPron;
				continue;
			}

			pronBuffer[numPron++] = (short) -1;

			Word word = new Word();
			word.text = name;
			word.partOfSpeech = posType;
			word.pron = pronBuffer.clone();
			word.numSyllables = numSyllables;

			// word.print();

			wordMap.put(word.text, word);
			wordArray.add(word);
		}

		// assert(max <= MAX_PHENOMES_IN_WORD)

		s.close();
		return true;
	}

	public static int MobyToPartOfSpeech(char ch) {
		switch (ch) {
		case 'N':
			return Word.POS_Noun;
		case 'p':
			return Word.POS_Plural;
		case 'h':
			return Word.POS_NounPhrase;
		case 'V':
			return Word.POS_VerbParticiple;
		case 't':
			return Word.POS_VerbTransitive;
		case 'i':
			return Word.POS_VerbIntransitive;
		case 'A':
			return Word.POS_Adjective;
		case 'v':
			return Word.POS_Adverb;
		case 'C':
			return Word.POS_Conjunction;
		case 'P':
			return Word.POS_Preposition;
		case '!':
			return Word.POS_Interjection;
		case 'r':
			return Word.POS_Pronoun;
		case 'D':
			return Word.POS_DefiniteArticle;
		case 'I':
			return Word.POS_IndefiniteArticle;
		case 'o':
			return Word.POS_Nominative;
		default:
			return Word.POS_Unknown;
		}
	}

	public static int generateSyllables(String mobyText, short[] pron, int maxSize, short[] pnumSyllables) {
		StateMachine.State state = StateMachine.State.start;
		int pos = 0;
		int numPhones = 0;
		short numSyllables = 0;
		short syllableType = 1; // normal stress

		while (state != StateMachine.State.end) {
			if (pos >= mobyText.length()) {
				break;
			}
			char ch = mobyText.charAt(pos);
			boolean found = false;

			if (numPhones == maxSize) {
				throw new IllegalStateException("Invalid number of phones");
			}

			for (int i = 0; i < StateMachine.STATE_MACHINE.length; i++) {
				if (StateMachine.STATE_MACHINE[i].state == state
						&& (StateMachine.STATE_MACHINE[i].ch == ch || StateMachine.STATE_MACHINE[i].ch == '?')) {

					state = StateMachine.STATE_MACHINE[i].nextState;

					if (StateMachine.STATE_MACHINE[i].ch == '?') {
						pron[numPhones++] = (short) StateMachine.STATE_MACHINE[i].action;

						if (PhoneSet.PHONE_SET[StateMachine.STATE_MACHINE[i].action].isSyllable) {
							pron[numPhones - 1] |= syllableType << 14;
							syllableType = 1;
							numSyllables++;
						}
					} else {
						if (StateMachine.STATE_MACHINE[i].action >= 0) {
							pron[numPhones++] = (short) StateMachine.STATE_MACHINE[i].action;

							if (PhoneSet.PHONE_SET[StateMachine.STATE_MACHINE[i].action].isSyllable) {
								pron[numPhones - 1] |= syllableType << 14;
								syllableType = 1;
								numSyllables++;
							}
							pos++;
						} else if (StateMachine.STATE_MACHINE[i].action == -1) {
							pos++;
						} else if (StateMachine.STATE_MACHINE[i].action == -2) {
							syllableType = 2;
							pos++;
						} else if (StateMachine.STATE_MACHINE[i].action == -3) {
							syllableType = 3;
							pos++;
						}
					}
					found = true;
					break;
				}
			}

			if (!found) {
				return 0;
			}
		}

		pnumSyllables[0] = numSyllables;

		return numPhones;
	}

	public void addSynonym(Word base, Word synonym) {
		base.synonyms.add(synonym);
	}

	public static int getCommonSuffixLength(Word word1, Word word2) {
		int pos1 = 0;
		int pos2 = 0;
		int count = 0;

		while (word1.pron[pos1] != -1) {
			pos1++;
		}

		while (word2.pron[pos2] != -1) {
			pos2++;
		}

		while (pos1 > 0 && pos2 > 0 && word1.pron[pos1] == word2.pron[pos2]) {
			count++;
			pos1--;
			pos2--;
		}

		return count;
	}

	public static Word rootWord = null;

	public static int suffixCompare(Word first, Word second) {
		int count1 = getCommonSuffixLength(rootWord, first);
		int count2 = getCommonSuffixLength(rootWord, second);
		if (count1 > count2) {
			return -1;
		} else if (count1 < count2) {
			return 1;
		} else {
			return 0;
		}
	}

	static int MinSuffixContainingSyllable(Word word) {
		int pos = 0;
		int lastSyllable = 0;
		while (word.pron[pos] != -1) {
			if ((word.pron[pos] >> 14) != 0) {
				lastSyllable = pos;
			}
			pos++;
		}

		return pos - lastSyllable;
	}

	static int CountPhenomes(Word word) {
		int pos = 0;
		while (word.pron[pos] != -1) {
			pos++;
		}

		return pos;
	}

	public List<Word> findRhymes(String whichword, WordFilter filter, List<Word> wordList) {
		List<Word> results = new ArrayList<Word>();

		// look up word.
		Word word = wordMap.get(RemovePunctuation(whichword));

		if (word == null) {
			return null;
		}

		word.print();

		if (wordList == null) {
			wordList = wordArray;
		}

		int minLength = MinSuffixContainingSyllable(word);

		// for each other word that is not equal to the word,
		for (int i = 0; i < wordList.size(); i++) {
			if (word.equals(wordList.get(i))) {
				continue;
			}

			if (filter != null && !(wordList.get(i)).match(filter)) {
				continue;
			}

			// if they share any suffix,
			int count = getCommonSuffixLength(word, wordList.get(i));
			if (count > minLength) {
				// add it to the results.
				results.add(wordList.get(i));
			}
		}

		rootWord = word;
		if (results.size() > 0) {
			Collections.sort(results, new Comparator<Word>() {
				@Override
				public int compare(Word a, Word b) {
					return suffixCompare(a, b);
				}
			});
		}

		return results;
	}

	public boolean rhymes(String word1, String word2) {
		List<Word> results = findRhymes(word1, null, null);
		List<Word> results2 = findRhymes(word2, null, null);
		Word w1 = lookup(word1);
		Word w2 = lookup(word2);
		return w1 != null && w2 != null
				&& ((results != null && results.contains(w2)) || (results2 != null && results2.contains(w1)));
	}

	public Word lookup(String whichword) {
		Word found = wordMap.get(whichword);
		if (found == null) {
			found = wordMap.get(whichword.toLowerCase());
		}

		return found;
	}

	public List<Word> filter(WordFilter filter) {
		List<Word> results = new ArrayList<Word>();

		for (Word w : wordArray) {
			if (w.match(filter)) {
				results.add(w);
			}
		}

		return results;
	}

	public static String RemovePunctuation(String text) {
		String result = "";
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (Character.isAlphabetic(c) || c == '\'' || c == ' ') {
				result += c;
			}
		}

		return result;
	}

	public List<Word> makeWords(String text) {
		List<Word> result = new ArrayList<Word>();
		text = RemovePunctuation(text);
		int failedWords = 0;
		StringTokenizer tok = new StringTokenizer(text, " \t\r\n");

		while (tok.hasMoreTokens()) {
			String token = tok.nextToken();
			Word word = lookup(token);
			if (word != null) {
				result.add(word);
			} else {
				System.out.printf("    Couldn't find word for %s\n", token);
				failedWords++;
			}
		}
		return result;
	}

	public List<Word> getSynonyms(String wordtext) {
		List<Word> results = new ArrayList<Word>();
		Word word = lookup(wordtext);
		if (word == null) {
			return null;
		}

		results.addAll(word.synonyms);

		return results;
	}
}
