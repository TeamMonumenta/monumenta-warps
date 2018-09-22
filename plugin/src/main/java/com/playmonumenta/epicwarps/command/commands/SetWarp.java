package com.playmonumenta.epicwarps.command.commands;

import com.playmonumenta.epicwarps.command.AbstractPlayerCommand;
import com.playmonumenta.epicwarps.command.CommandContext;
import com.playmonumenta.epicwarps.WarpManager;

import net.sourceforge.argparse4j.inf.ArgumentParser;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class SetWarp extends AbstractPlayerCommand {

	public SetWarp(Plugin plugin) {
		super(
		    "setwarp",
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
	protected boolean run(final CommandContext context) throws Exception {
		final Player player = context.getPlayer().get();
		final String name = context.getNamespace().get("name");

		WarpManager.getWarpManager().addWarp(name, player.getLocation());

		return true;
	}
}
