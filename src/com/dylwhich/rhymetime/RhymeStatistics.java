package com.dylwhich.rhymetime;

import org.bukkit.entity.Player;

public class RhymeStatistics implements Comparable<RhymeStatistics> {
	public static String getHeaderMessage() {
		return "Player: Success / Attempts (%) Streak";
	}

	private final Player owner;

	private int successes;
	private int fails;
	private int maxStreak;
	private int streak;

	public RhymeStatistics(Player owner) {
		this.owner = owner;
	}

	public void addSuccess() {
		successes++;
		streak++;

		if (streak > maxStreak) {
			maxStreak = streak;
		}
	}

	public void addFail() {
		fails++;
		streak = 0;
	}

	public void reset() {
		successes = 0;
		fails = 0;
		streak = 0;
		maxStreak = 0;
	}

	@Override
	public String toString() {
		return String.format("%s: %d/%d (%.2f) %d", owner.getName(), successes, successes + fails,
				(fails + successes) == 0 ? 0 : 1.0 * successes / (fails + successes), maxStreak);
	}

	@Override
	public int compareTo(RhymeStatistics arg0) {
		return successes - arg0.successes;
	}
}
