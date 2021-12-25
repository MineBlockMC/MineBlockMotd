package org.mineblock.motd.bukkit.utils;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class ConfigurationUtil {
	final private Plugin plugin;

	public ConfigurationUtil(final Plugin plugin) {
		this.plugin = plugin;
	}

	public YamlConfiguration getConfiguration(File file) {
		if (file.exists())
			return YamlConfiguration.loadConfiguration(file);
		else return new YamlConfiguration();
	}

	public void createConfiguration(File file) {
		try {
			if (!file.exists()) {
				final InputStream inputStream = plugin.getClass().getClassLoader().getResourceAsStream(file.getName());
				final File parentFile = file.getParentFile();

				if (parentFile != null) parentFile.mkdirs();

				if (inputStream != null) {
					Files.copy(inputStream, file.toPath());
				} else file.createNewFile();
			}
		} catch (final IOException e) {
			plugin.getLogger().severe(("Unable to create configuration file!"));
		}
	}
}