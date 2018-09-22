package com.playmonumenta.epicwarps.command;

import com.playmonumenta.epicwarps.command.commands.*;

import org.bukkit.plugin.java.JavaPlugin;

public class CommandFactory {

	/**
	 * Create and register all command.
	 *
	 * @param plugin        the plugin object
	 */
	public static void createCommands(JavaPlugin plugin) {
		createCommand(plugin, new Back(plugin));
		createCommand(plugin, new Forward(plugin));
		createCommand(plugin, new Warp(plugin));
		createCommand(plugin, new Warps(plugin));
		createCommand(plugin, new AddWarp(plugin));
		createCommand(plugin, new DelWarp(plugin));

		// Alias for addwarp
		plugin.getCommand("setwarp").setExecutor(new AddWarp(plugin));
	}

	/**
	 * Create and register a single command.
	 *
	 * @param plugin  the plugin object
	 * @param command the new command object
	 */
	private static void createCommand(JavaPlugin plugin, AbstractCommand command) {
		plugin.getCommand(command.getName()).setExecutor(command);
	}
}
