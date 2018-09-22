package com.playmonumenta.epicwarps;

import com.playmonumenta.epicwarps.command.CommandFactory;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;

public class Plugin extends JavaPlugin {
	@Override
	public void onEnable() {
		PluginManager manager = getServer().getPluginManager();

		manager.registerEvents(new PlayerListener(this), this);

		CommandFactory.createCommands(this);
	}

	@Override
	public void onDisable() {
		getServer().getScheduler().cancelTasks(this);

		MetadataUtils.removeAllMetadata(this);
	}
}
