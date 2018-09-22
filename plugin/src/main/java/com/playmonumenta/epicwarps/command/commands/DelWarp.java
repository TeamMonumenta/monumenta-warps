package com.playmonumenta.epicwarps.command.commands;

import com.playmonumenta.epicwarps.command.AbstractPlayerCommand;
import com.playmonumenta.epicwarps.command.CommandContext;
import com.playmonumenta.epicwarps.WarpManager;

import java.util.List;

import net.sourceforge.argparse4j.inf.ArgumentParser;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class DelWarp extends AbstractPlayerCommand {

	public DelWarp(Plugin plugin) {
		super(
		    "delwarp",
		    "Deletes an existing warp.",
		    plugin
		);
	}

	@Override
	protected void configure(final ArgumentParser parser) {
		parser.addArgument("name")
		.help("Warp name to delete")
		.type(String.class);
	}

	@Override
	protected boolean run(final CommandContext context) {
		final Player player = context.getPlayer().get();
		final String name = context.getNamespace().get("name");

		try {
			WarpManager.getWarpManager().removeWarp(name);
		} catch (Exception e) {
			player.sendMessage(ChatColor.RED + "" + e.getMessage());
		}

		// TODO: Success message

		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		try {
			if (args.length == 1) {
				return WarpManager.getWarpManager().tabComplete(args[0]);
			}
		} catch (Exception e) {
			sender.sendMessage(ChatColor.RED + "" + e.getMessage());
		}

		return null;
	}
}
