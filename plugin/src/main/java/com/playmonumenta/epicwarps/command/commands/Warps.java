package com.playmonumenta.epicwarps.command.commands;

import com.playmonumenta.epicwarps.command.AbstractPlayerCommand;
import com.playmonumenta.epicwarps.command.CommandContext;
import com.playmonumenta.epicwarps.WarpManager;

import net.sourceforge.argparse4j.inf.ArgumentParser;

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
		parser.addArgument("page")
		.help("Warp page")
		.type(Integer.class)
		.nargs("?")
		.setDefault(1);
	}

	@Override
	protected boolean run(final CommandContext context) throws Exception {
		final Player player = context.getPlayer().get();
		final int page = context.getNamespace().get("page");

		WarpManager.getWarpManager().listWarps(player, page);

		return true;
	}
}
