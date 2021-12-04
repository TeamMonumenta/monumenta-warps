package com.playmonumenta.warps;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class Warp implements Comparable<Warp> {
	protected String mName;
	private Location mLoc;

	@Override
	public int compareTo(Warp other) {
		return mName.compareTo(other.mName);
	}

	public static Warp fromConfig(String name, ConfigurationSection config) throws Exception {
		if (!config.isString("world")) {
			throw new Exception("Invalid world");
		} else if (!config.isDouble("x")) {
			throw new Exception("Invalid x value");
		} else if (!config.isDouble("y")) {
			throw new Exception("Invalid y value");
		} else if (!config.isDouble("z")) {
			throw new Exception("Invalid z value");
		} else if (!config.isDouble("yaw")) {
			throw new Exception("Invalid yaw value");
		} else if (!config.isDouble("pitch")) {
			throw new Exception("Invalid pitch value");
		}

		return new Warp(name, new Location(Bukkit.getWorld(config.getString("world")),
		                                   config.getDouble("x"),
		                                   config.getDouble("y"),
		                                   config.getDouble("z"),
		                                   (float)config.getDouble("yaw"),
		                                   (float)config.getDouble("pitch")));
	}

	public Warp(String name, Location loc) throws Exception {
		mName = name;
		mLoc = loc;
	}

	public String getName() {
		return mName;
	}

	public void warp(Player player) {
		player.teleport(mLoc);
		//TODO: play sound only to this player
	}

	public void warpIfMatches(Player player, String target) {
		// TODO: Permission check
		if (target.equals(mName)) {
			warp(player);
		}
	}
	public Map<String, Object> getConfig() {
		Map<String, Object> configMap = new LinkedHashMap<String, Object>();

		configMap.put("world", mLoc.getWorld().getName());
		configMap.put("x", mLoc.getX());
		configMap.put("y", mLoc.getY());
		configMap.put("z", mLoc.getZ());
		configMap.put("yaw", (double)mLoc.getYaw());
		configMap.put("pitch", (double)mLoc.getPitch());

		return configMap;
	}
}
