package com.dylwhich.rhymetime.poet;

import java.io.FileNotFoundException;
import java.util.List;

public class PoetTest {

	public static void main(String[] args) {
		if (args.length != 1) {
			return;
		}

		String toRhyme = args[0];

		WordDatabase db = new WordDatabase();
		try {
			db.load("res/dict.txt");
		} catch (FileNotFoundException e) {
			System.out.println("File not found: " + e.getMessage());
		}

		List<Word> words = db.findRhymes(toRhyme, null, null);
		if (words != null) {
			for (Word w : words) {
				w.print();
			}
		} else {
			System.out.printf("%s doesn't rhyme with anything! :(\n", toRhyme);
		}
	}

}
