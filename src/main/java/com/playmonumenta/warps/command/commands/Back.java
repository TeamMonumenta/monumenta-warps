package com.playmonumenta.warps.command.commands;

import java.util.Deque;

import com.playmonumenta.warps.Warp;
import com.playmonumenta.warps.command.CommandContext;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import net.sourceforge.argparse4j.inf.ArgumentParser;

public class Back extends TeleportBase {

	public Back(Plugin plugin) {
		super(
		    "back",
		    "Transfers back to location prior to teleporting, or multiple teleports prior. Saves coordinates to /forward.",
		    plugin
		);
	}

	@Override
	protected void configure(final ArgumentParser parser) {
		parser.addArgument("steps")
		.help("number of teleports back")
		.type(Integer.class)
		.nargs("?")
		.setDefault(1);
	}

	@Override
	protected boolean run(final CommandContext context) {
		//noinspection OptionalGetWithoutIsPresent - checked before being called
		final Player player = context.getPlayer().get();
		final int numSteps = context.getNamespace().get("steps");
		final Deque<Warp> backStack = getBackStack(player);
		final Deque<Warp> forwardStack = getForwardStack(player);

		if (backStack.isEmpty()) {
			sendErrorMessage(context, "No back location to teleport to");
			return false;
		}

		final Warp target = getTarget(player, numSteps, forwardStack, backStack);

		skipBackAdd(player);
		saveUpdatedStacks(player, forwardStack, backStack);

		target.warp(player);
		sendMessage(context, "Teleporting back" + (backStack.isEmpty() ? " (end of list)" : ""));

		return true;
	}
}
