package com.dylwhich.rhymetime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

import com.dylwhich.javaspeak.javaSpeak;

public class RhymeTime extends JavaPlugin implements Listener {
	private static final Permission PERM_CONTROL = new Permission("rhymetime.control");
	private static final Permission PERM_OVERRIDE = new Permission("rhymetime.override");
	private static final Permission PERM_OVERRIDE_DEFAULT = new Permission("rhymetime.override.default");
	private static final Permission PERM_PARTICIPATE = new Permission("rhymetime.participate");
	private static final Permission PERM_STATS = new Permission("rhymetime.stats");

	private Map<Player, String[]> playerWords;
	private Set<Player> overrides;
	private Map<Player, RhymeStatistics> statistics;

	private String rhymeScheme;
	private boolean enabled;

	static {
		System.loadLibrary("javaSpeak");
	}

	@Override
	public void onEnable() {
		javaSpeak.initialize();

		System.loadLibrary("javaSpeak");

		playerWords = new HashMap<Player, String[]>();
		overrides = new HashSet<Player>();
		statistics = new HashMap<Player, RhymeStatistics>();

		rhymeScheme = "AA";
		enabled = false;

		for (Player p : getServer().getOnlinePlayers()) {
			playerWords.put(p, new String[rhymeScheme.length()]);
			statistics.put(p, new RhymeStatistics(p));

			if (p.hasPermission(PERM_OVERRIDE_DEFAULT)) {
				overrides.add(p);
			}
		}

		getServer().getPluginManager().registerEvents(this, this);
	}

	@Override
	public void onDisable() {
		enabled = false;
		playerWords = null;
		overrides = null;
		statistics = null;
		rhymeScheme = "AA";
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.equals(getCommand("rtenable"))) {
			if (sender.hasPermission(PERM_CONTROL)) {
				if (args.length > 0) {
					setScheme(args[0]);
				}
				enabled = true;
				sender.sendMessage("RhymeTime has been enabled.");
				return true;
			} else {
				return false;
			}
		} else if (cmd.equals(getCommand("rtdisable"))) {
			if (sender.hasPermission(PERM_CONTROL)) {
				enabled = false;
				sender.sendMessage("RhymeTime has been disabled.");
				return true;
			} else {
				return false;
			}
		} else if (cmd.equals(getCommand("rttoggle"))) {
			if (sender.hasPermission(PERM_CONTROL)) {
				enabled = !enabled;
				sender.sendMessage("RhymeTime has been " + (enabled ? "enabled" : "disabled") + ".");
				return true;
			} else {
				return false;
			}
		} else if (cmd.equals(getCommand("rtscheme"))) {
			if (sender.hasPermission(PERM_CONTROL)) {
				if (args.length > 0) {
					setScheme(args[0]);
					sender.sendMessage("Rhyme Scheme changed to " + rhymeScheme);
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else if (cmd.equals(getCommand("rtstats"))) {
			if (sender.hasPermission(PERM_STATS)) {
				if (args.length > 0) {
					Player target = getServer().getPlayer(args[0]);
					if (target != null) {
						sender.sendMessage("Stats for player " + target.getName());
						sender.sendMessage(statistics.get(target).toString());
						return true;
					} else {
						sender.sendMessage("Player " + args[0] + " not found.");
						return false;
					}
				} else {
					if (sender instanceof Player) {
						sender.sendMessage("Your stats:");
						sender.sendMessage(statistics.get(sender).toString());
						return true;
					} else {
						sender.sendMessage("You must enter a player's name.");
						return false;
					}
				}
			} else {
				return false;
			}
		} else if (cmd.equals(getCommand("rtoverride"))) {
			if (sender.hasPermission(PERM_OVERRIDE)) {
				if (args.length > 0) {
					Player target = getServer().getPlayer(args[0]);
					if (target != null) {
						sender.sendMessage("Player " + target.getName() + " is now overriding RhymeTime.");
						overrides.add(target);
						return true;
					} else {
						sender.sendMessage("Player " + args[0] + " not found.");
						return false;
					}
				} else if (sender instanceof Player) {
					overrides.add((Player) sender);
					sender.sendMessage("You are now overriding RhymeTime.");
					return true;
				} else {
					sender.sendMessage("You must enter a player's name.");
					return false;
				}
			} else {
				return false;
			}
		} else if (cmd.equals(getCommand("rtscores"))) {
			if (sender.hasPermission(PERM_STATS)) {
				sender.sendMessage(getLeaderboardText());
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	@EventHandler
	public void onChatMessage(AsyncPlayerChatEvent e) {
		getLogger().log(Level.INFO, e.getPlayer().getName() + " said " + e.getMessage());
		Player p = e.getPlayer();
		String msg = e.getMessage();

		if (p.hasPermission(PERM_PARTICIPATE) && !overrides.contains(p)) {

			String[] words = playerWords.get(p);

			for (int i = 0; i < words.length; i++) {
				if (words[i] == null) {
					getLogger().log(Level.INFO, "Putting new word into slot " + i);
					words[i] = msg;

					if (RhymeScheme.validate(rhymeScheme, words)) {
						if (i == words.length - 1) {
							statistics.get(p).addSuccess();
						}
					} else {
						statistics.get(p).addFail();
						// punish!
						p.getWorld().strikeLightning(p.getLocation());

						p.sendMessage("That does not rhyme!");
						playerWords.put(p, new String[words.length]);
					}

					if (i == words.length - 1) {
						playerWords.put(p, new String[words.length]);
					}

					break;
				} else {
					getLogger().log(Level.INFO, "" + i + ": " + words[i]);
				}
			}
		} else {
			getLogger().log(Level.INFO, "User did not have participate permission or was overriden.");
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		if (!playerWords.containsKey(e.getPlayer())) {
			playerWords.put(e.getPlayer(), new String[rhymeScheme.length()]);
		}

		if (e.getPlayer().hasPermission(PERM_OVERRIDE_DEFAULT)) {
			overrides.add(e.getPlayer());
		}

		if (!statistics.containsKey(e.getPlayer())) {
			statistics.put(e.getPlayer(), new RhymeStatistics(e.getPlayer()));
		}

		if (enabled && !overrides.contains(e.getPlayer())) {
			e.getPlayer().sendMessage("It's RhymeTime! The scheme is " + rhymeScheme);
		}
	}

	private void setScheme(String scheme) {
		rhymeScheme = scheme.toUpperCase();
		for (Player p : playerWords.keySet()) {
			playerWords.put(p, new String[rhymeScheme.length()]);
		}
	}

	private String[] getLeaderboardText() {
		List<RhymeStatistics> stats = new ArrayList<RhymeStatistics>(statistics.values());
		Collections.sort(stats);

		String[] results = new String[stats.size() + 1];

		results[0] = RhymeStatistics.getHeaderMessage();

		int rank = 1;
		for (RhymeStatistics entry : stats) {
			results[rank] = String.format("%d. %s", rank++, entry.toString());
		}

		return results;
	}
}
