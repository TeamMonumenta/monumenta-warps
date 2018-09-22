package com.playmonumenta.epicwarps;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.util.StringUtil;

public class WarpManager {
	// This is ugly - only one warp mananger can exist at a time
	private static WarpManager WARP_MANAGER = null;

	private Plugin mPlugin;

	// TODO: Case insensitive warping
	private SortedMap<String, Warp> mWarps = new TreeMap<String, Warp>();

	public WarpManager(Plugin plugin, YamlConfiguration config) {
		WARP_MANAGER = this;
		mPlugin = plugin;

		// Load the warps configuration section
		if (!config.isConfigurationSection("warps")) {
			plugin.getLogger().log(Level.INFO, "No warps defined");
			return;
		}
		ConfigurationSection warpsSection = config.getConfigurationSection("warps");

		Set<String> keys = warpsSection.getKeys(false);

		// Iterate over all the warps (shallow list at this level)
		for (String key : keys) {
			if (!warpsSection.isConfigurationSection(key)) {
				plugin.getLogger().log(Level.WARNING,
				                       "warps entry '" + key + "' is not a configuration section!");
				continue;
			}

			try {
				mWarps.put(key, Warp.fromConfig(key, warpsSection.getConfigurationSection(key)));
			} catch (Exception e) {
				plugin.getLogger().log(Level.WARNING, "Failed to load warp '" + key + "': ", e);
				continue;
			}
		}
	}

	public static WarpManager getWarpManager() {
		return WARP_MANAGER;
	}

	public void addWarp(String name, Location loc) throws Exception {
		if (mWarps.containsKey(name)) {
			throw new Exception("Warp '" + name + "' already exists");
		}

		mWarps.put(name, new Warp(name, loc));
		mPlugin.saveConfig();
	}

	public void removeWarp(String name) throws Exception {
		if (!mWarps.containsKey(name)) {
			throw new Exception("Warp '" + name + "' does not exist");
		}

		mWarps.remove(name);
		mPlugin.saveConfig();
	}

	public void listWarps(CommandSender sender, int page) {
		final int ITEMS_PER_PAGE = 10;
		final int numPages = ((mWarps.size() - 1) / ITEMS_PER_PAGE) + 1;

		if (mWarps.isEmpty()) {
			sender.sendMessage("No warps defined");
			return;
		}

		sender.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Warp List (Page " + Integer.toString(page) +
		                   " of " + Integer.toString(numPages) + ")");
		boolean empty = true;
		int skipped = 0;
		int printed = 0;
		for (Map.Entry<String, Warp> entry : mWarps.entrySet()) {
			if (skipped < (page - 1) * ITEMS_PER_PAGE) {
				skipped++;
			} else {
				printed++;
				sender.sendMessage(ChatColor.GREEN + entry.getKey());

				if (printed >= ITEMS_PER_PAGE) {
					return;
				}
			}
		}
	}

	public List<String> tabComplete(String partial) {
        final List<String> completions = new ArrayList<String>(mWarps.size());

		StringUtil.copyPartialMatches(partial, mWarps.keySet(), completions);
		Collections.sort(completions);
		return completions;
	}

	public void warp(Player player, String name) throws Exception {
		Warp warp = mWarps.get(name);
		if (warp == null) {
			throw new Exception("Warp '" + name + "' not found!");
		}
		warp.warp(player);
	}

	public YamlConfiguration getConfig() {
		// Create the top-level config to return
		YamlConfiguration config = new YamlConfiguration();

		// Create the container for warps and iterate over them
		ConfigurationSection warpsConfig = config.createSection("warps");
		for (Map.Entry<String, Warp> entry : mWarps.entrySet()) {
			// Create the container for this warp's data and load it with values
			warpsConfig.createSection(entry.getKey(), entry.getValue().getConfig());
		}

		return config;
	}
}
