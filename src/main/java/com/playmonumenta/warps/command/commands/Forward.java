package com.playmonumenta.warps.command.commands;

import com.playmonumenta.warps.command.CommandContext;

import java.util.Deque;

import net.sourceforge.argparse4j.inf.ArgumentParser;

import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

public class Forward extends TeleportBase {

	public Forward(Plugin plugin) {
		super(
		    "forward",
		    "Transfers forward to location prior to /back, or multiple teleports prior. Saves coordinates to /back.",
		    plugin
		);
	}

	@Override
	protected void configure(final ArgumentParser parser) {
		parser.addArgument("steps")
		.help("number of teleports forward")
		.type(Integer.class)
		.nargs("?")
		.setDefault(1);
	}

	@Override
	protected boolean run(final CommandContext context) {
		//noinspection OptionalGetWithoutIsPresent - checked before being called
		final Player player = context.getPlayer().get();
		final int numSteps = context.getNamespace().get("steps");
		final Deque<Location> backStack = getBackStack(player);
		final Deque<Location> forwardStack = getForwardStack(player);

		if (forwardStack.isEmpty()) {
			sendErrorMessage(context, "No forward location to teleport to");
			return false;
		}

		final Location target = getTarget(player, numSteps, backStack, forwardStack);

		skipBackAdd(player);
		saveUpdatedStacks(player, forwardStack, backStack);

		player.teleport(target);
		sendMessage(context, "Teleporting forward" + (backStack.isEmpty() ? " (end of list)" : ""));

		return true;
	}
}
