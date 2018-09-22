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
