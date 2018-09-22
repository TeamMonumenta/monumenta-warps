package com.playmonumenta.epicwarps.command.commands;

import com.playmonumenta.epicwarps.command.AbstractPlayerCommand;
import com.playmonumenta.epicwarps.command.CommandContext;
import com.playmonumenta.epicwarps.WarpManager;

import net.sourceforge.argparse4j.inf.ArgumentParser;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class AddWarp extends AbstractPlayerCommand {

	public AddWarp(Plugin plugin) {
		super(
		    "addwarp",
		    "Creates a new warp at the current location.",
		    plugin
		);
	}

	@Override
	protected void configure(final ArgumentParser parser) {
		parser.addArgument("name")
		.help("Warp name to add")
		.type(String.class);
	}

	@Override
	protected boolean run(final CommandContext context) {
		final Player player = context.getPlayer().get();
		final String name = context.getNamespace().get("name");

		try {
			WarpManager.getWarpManager().addWarp(name, player.getLocation());
		} catch (Exception e) {
			player.sendMessage(ChatColor.RED + "" + e.getMessage());
			return false;
		}

		player.sendMessage(ChatColor.GOLD + "Warp '" + name + "' added.");

		return true;
	}
}
