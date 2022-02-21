package com.playmonumenta.warps;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

public class PlayerListener implements Listener {
	@EventHandler(priority = EventPriority.LOWEST)
	@SuppressWarnings("unchecked")
	public void PlayerTeleportEvent(PlayerTeleportEvent event) {
		if (event.isCancelled()) {
			return;
		}

		Player player = event.getPlayer();

		// Only add the location to the back stack if the player didn't just use /back or /forward
		if (player.hasMetadata(Constants.PLAYER_SKIP_BACK_ADD_METAKEY)) {
			player.removeMetadata(Constants.PLAYER_SKIP_BACK_ADD_METAKEY, WarpsPlugin.getInstance());
		} else if (!event.getFrom().getWorld().equals(event.getTo().getWorld()) || event.getFrom().distance(event.getTo()) > 2) {
			// Only add locations to /back if they were more than two blocks away or a different world

			// Get the stack of previous teleport locations
			Deque<Warp> backStack = null;
			if (player.hasMetadata(Constants.PLAYER_BACK_STACK_METAKEY)) {
				List<MetadataValue> val = player.getMetadata(Constants.PLAYER_BACK_STACK_METAKEY);
				if (val != null && !val.isEmpty()) {
					backStack = (Deque<Warp>)val.get(0).value();
				}
			}

			if (backStack == null) {
				backStack = new ArrayDeque<Warp>(Constants.MAX_STACK_SIZE);
			}

			if (backStack.size() >= Constants.MAX_STACK_SIZE) {
				backStack.removeLast();
			}
			backStack.addFirst(new Warp("backforward", event.getFrom()));
			player.setMetadata(Constants.PLAYER_BACK_STACK_METAKEY, new FixedMetadataValue(WarpsPlugin.getInstance(), backStack));
		}
	}
}
