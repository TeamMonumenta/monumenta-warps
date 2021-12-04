package com.playmonumenta.warps;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.metadata.MetadataStoreBase;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class MetadataUtils {
	public static void removeAllMetadata(Plugin plugin) {
		_removeAllMetadataHelper("getEntityMetadata", plugin.getServer(), plugin);
		_removeAllMetadataHelper("getPlayerMetadata", plugin.getServer(), plugin);
		_removeAllMetadataHelper("getWorldMetadata", plugin.getServer(), plugin);
		for (World world : Bukkit.getWorlds()) {
			_removeAllMetadataHelper("getBlockMetadata", world, plugin);
		}
	}

	@SuppressWarnings("unchecked")
	private static <T> void _removeAllMetadataHelper(String getMetaMethodName, Object obj, Plugin plugin) {
		MetadataStoreBase<T> metaStore = null;
		Map<String, Map<Plugin, MetadataValue>> metaMap = null;

		try {
			/*
			 * Use reflection to reach into the object and grab the MetadataStoreBase object
			 */
			java.lang.reflect.Method method = obj.getClass().getMethod(getMetaMethodName);
			metaStore = (MetadataStoreBase<T>) method.invoke(obj);

			if (metaStore == null) {
				plugin.getLogger().severe("While clearing metadata, retrieved null metastore object for '" + getMetaMethodName + "'");
				return;
			}

			/*
			 * Use reflection (again) to reach into the metadata store to grab the underlying (private) map
			 */
			if (metaStore.getClass().getSuperclass() == null) {
				plugin.getLogger().severe("While clearing metadata, metastore has no superclass for '" + getMetaMethodName + "'");
				return;
			}

			java.lang.reflect.Field field = metaStore.getClass().getSuperclass().getDeclaredField("metadataMap");
			field.setAccessible(true);
			metaMap = (Map<String, Map<Plugin, MetadataValue>>) field.get(metaStore);
		} catch (SecurityException | NoSuchMethodException | NoSuchFieldException | IllegalArgumentException
			         | IllegalAccessException | InvocationTargetException e) {
			plugin.getLogger().severe("While clearing metadata, failed to retrieve CraftServer metadata map object for '" + getMetaMethodName + "'");
			e.printStackTrace();
			return;
		}

		if (metaMap == null) {
			plugin.getLogger().severe("While clearing metadata, retrieved null metadata map contents for '" + getMetaMethodName + "'");
			return;
		}

		/* Clear out the metadata map of any references to this plugin */
		Iterator<Map<Plugin, MetadataValue>> iterator = metaMap.values().iterator();
		while (iterator.hasNext()) {
			Map<Plugin, MetadataValue> values = iterator.next();
			if (values.containsKey(plugin)) {
				values.remove(plugin);
			}
			if (values.isEmpty()) {
				iterator.remove();
			}
		}
	}
}
