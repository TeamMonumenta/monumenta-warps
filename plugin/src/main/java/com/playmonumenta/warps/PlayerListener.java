package com.playmonumenta.warps;

import com.playmonumenta.warps.Constants;
import com.playmonumenta.warps.Plugin;

import java.util.List;
import java.util.Stack;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.Location;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

public class PlayerListener implements Listener {
	Plugin mPlugin = null;

	public PlayerListener(Plugin plugin) {
		mPlugin = plugin;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	@SuppressWarnings("unchecked")
	public void PlayerTeleportEvent(PlayerTeleportEvent event) {
		if (event.isCancelled()) {
			return;
		}

		Player player = event.getPlayer();

		// Only add the location to the back stack if the player didn't just use /back or /forward
		if (player.hasMetadata(Constants.PLAYER_SKIP_BACK_ADD_METAKEY)) {
			player.removeMetadata(Constants.PLAYER_SKIP_BACK_ADD_METAKEY, mPlugin);
		} else if (event.getFrom().distance(event.getTo()) > 2) {
			// Only add locations to /back if they were more than two blocks away

			// Get the stack of previous teleport locations
			Stack<Location> backStack = null;
			if (player.hasMetadata(Constants.PLAYER_BACK_STACK_METAKEY)) {
				List<MetadataValue> val = player.getMetadata(Constants.PLAYER_BACK_STACK_METAKEY);
				if (val != null && !val.isEmpty()) {
					backStack = (Stack<Location>)val.get(0).value();
				}
			}

			if (backStack == null) {
				backStack = new Stack<Location>();
			}

			backStack.push(event.getFrom());
			player.setMetadata(Constants.PLAYER_BACK_STACK_METAKEY, new FixedMetadataValue(mPlugin, backStack));
		}
	}
}
