package com.playmonumenta.warps;

import com.playmonumenta.worlds.paper.MonumentaWorldManagementAPI;

import org.bukkit.World;

public class WorldManagementIntegration {
	public static World getWorldFromIntegration(String worldName) {
		try {
			return MonumentaWorldManagementAPI.ensureWorldLoaded(worldName, false, false);
		} catch (Exception ex) {
			WarpsPlugin.getInstance().getLogger().warning("Failed to use world manager to get world '" + worldName + "': " + ex.getMessage());
			ex.printStackTrace();
		}
		return null;
	}
}
