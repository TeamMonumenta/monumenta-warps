package com.playmonumenta.warps.command.commands;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import com.playmonumenta.warps.Constants;
import com.playmonumenta.warps.Warp;
import com.playmonumenta.warps.command.AbstractPlayerCommand;

import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public abstract class TeleportBase extends AbstractPlayerCommand {

	public TeleportBase(String name, String description, Plugin plugin) {
		super(name, description, plugin);
	}

	/**
	 * Get the stack of previous teleport locations
	 *
	 * @param player
	 * @return
	 */
	protected Deque<Warp> getForwardStack(final Player player) {
		return getStack(player, Constants.PLAYER_FORWARD_STACK_METAKEY);
	}

	/**
	 * Get the stack of previous /forward locations and push the target location to it
	 *
	 * @param player
	 * @return
	 */
	protected Deque<Warp> getBackStack(final Player player) {
		return getStack(player, Constants.PLAYER_BACK_STACK_METAKEY);
	}

	@SuppressWarnings("unchecked")
	private Deque<Warp> getStack(final Player player, final String metadataKey) {
		final List<MetadataValue> metadata = player.getMetadata(metadataKey);
		return metadata.isEmpty() ? new ArrayDeque<>(Constants.MAX_STACK_SIZE) : (Deque<Warp>) metadata.get(0).value();
	}

	/**
	 * Pop items off the stack, adding popped elements to the opposite stack
	 *
	 * @param player
	 * @param numSteps
	 * @param pushTo
	 * @param popFrom
	 * @return
	 */
	protected Warp getTarget(final Player player, final int numSteps, final Deque<Warp> pushTo, final Deque<Warp> popFrom) {
		Warp target = new Warp("backforward", player.getLocation());

		for (int i = 0; i < numSteps; i++) {
			if (pushTo.size() >= Constants.MAX_STACK_SIZE) {
				pushTo.removeLast();
			}
			pushTo.addFirst(target);
			target = popFrom.removeFirst();

			if (popFrom.isEmpty()) {
				break;
			}
		}

		return target;
	}

	/**
	 * Set the status to indicate that the next teleport shouldn't be added to the list
	 *
	 * @param player
	 */
	protected void skipBackAdd(final Player player) {
		player.setMetadata(Constants.PLAYER_SKIP_BACK_ADD_METAKEY, new FixedMetadataValue(mPlugin, true));
	}

	/**
	 * Save updated stacks
	 *
	 * @param player
	 * @param forwardStack
	 * @param backStack
	 */
	protected void saveUpdatedStacks(final Player player, final Deque<Warp> forwardStack, final Deque<Warp> backStack) {
		player.setMetadata(Constants.PLAYER_FORWARD_STACK_METAKEY, new FixedMetadataValue(mPlugin, forwardStack));
		player.setMetadata(Constants.PLAYER_BACK_STACK_METAKEY, new FixedMetadataValue(mPlugin, backStack));
	}
}
