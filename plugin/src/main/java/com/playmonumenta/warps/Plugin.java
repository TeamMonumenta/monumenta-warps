package com.playmonumenta.warps;

import com.playmonumenta.warps.command.CommandFactory;

import java.io.File;
import java.io.IOException;

import java.util.logging.Level;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.PluginManager;

public class Plugin extends JavaPlugin {
	private YamlConfiguration mConfig;
	public WarpManager mWarpManager = null;
	private File mConfigFile;

	@Override
	public void onEnable() {
		PluginManager manager = getServer().getPluginManager();

		manager.registerEvents(new PlayerListener(this), this);

		CommandFactory.createCommands(this);

		reloadConfig();
	}

	@Override
	public void onDisable() {
		// Save current warps
		saveConfig();

		getServer().getScheduler().cancelTasks(this);

		MetadataUtils.removeAllMetadata(this);
	}

	public void reloadConfig() {
		// Do not save first

		if (mConfigFile == null) {
			mConfigFile = new File(getDataFolder(), "warps.yml");
		}

		if (!mConfigFile.exists()) {
			try {
				// Create parent directories if they do not exist
				mConfigFile.getParentFile().mkdirs();

				// Create the file if it does not exist
				mConfigFile.createNewFile();
			} catch (IOException ex) {
				getLogger().log(Level.SEVERE, "Failed to create non-existent configuration file");
			}
		}

		mConfig = YamlConfiguration.loadConfiguration(mConfigFile);

		mWarpManager = new WarpManager(this, mConfig);
	}

	public void saveConfig() {
		if (mWarpManager != null) {
			try {
				mConfig = mWarpManager.getConfig();
				mConfig.save(mConfigFile);
			} catch (IOException ex) {
				getLogger().log(Level.SEVERE, "Could not save config to " + mConfigFile, ex);
			}
		}
	}
}
