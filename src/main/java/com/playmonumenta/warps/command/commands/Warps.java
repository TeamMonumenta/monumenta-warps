package com.playmonumenta.warps.command.commands;

import com.playmonumenta.warps.command.AbstractPlayerCommand;
import com.playmonumenta.warps.command.CommandContext;
import com.playmonumenta.warps.WarpManager;

import net.sourceforge.argparse4j.inf.ArgumentParser;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Warps extends AbstractPlayerCommand {

	public Warps(Plugin plugin) {
		super(
		    "warps",
		    "Lists available warps.",
		    plugin
		);
	}

	@Override
	protected void configure(final ArgumentParser parser) {
	}

	@Override
	protected boolean run(final CommandContext context) {
		final Player player = context.getPlayer().get();

		try {
			WarpManager.getWarpManager().listWarps(player);
		} catch (Exception e) {
			player.sendMessage(ChatColor.RED + "" + e.getMessage());
			return false;
		}

		return true;
	}
}
