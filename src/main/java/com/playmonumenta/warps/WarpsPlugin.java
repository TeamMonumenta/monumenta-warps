package com.playmonumenta.warps;

import java.io.File;
import java.io.IOException;

import com.playmonumenta.warps.command.CommandFactory;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class WarpsPlugin extends JavaPlugin {
	private static WarpsPlugin INSTANCE = null;

	private YamlConfiguration mConfig;
	public WarpManager mWarpManager = null;
	private File mConfigFile;

	@Override
	public void onEnable() {
		INSTANCE = this;

		PluginManager manager = getServer().getPluginManager();

		manager.registerEvents(new PlayerListener(), this);

		CommandFactory.createCommands(this);

		reloadConfig();
	}

	@Override
	public void onDisable() {
		INSTANCE = null;

		// Save current warps
		saveConfig();

		getServer().getScheduler().cancelTasks(this);

		MetadataUtils.removeAllMetadata(this);
	}

	public static WarpsPlugin getInstance() {
		return INSTANCE;
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
				getLogger().severe("Failed to create non-existent configuration file");
			}
		}

		mConfig = YamlConfiguration.loadConfiguration(mConfigFile);

		mWarpManager = new WarpManager(mConfig);
	}

	public void saveConfig() {
		if (mWarpManager != null) {
			try {
				mConfig = mWarpManager.getConfig();
				mConfig.save(mConfigFile);
			} catch (IOException ex) {
				getLogger().severe("Could not save config to " + mConfigFile + ": " + ex.getMessage());
				ex.printStackTrace();
			}
		}
	}
}
