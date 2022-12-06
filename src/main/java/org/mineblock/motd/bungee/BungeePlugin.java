package org.mineblock.motd.bungee;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.mineblock.motd.bungee.handler.CommandHandler;
import org.mineblock.motd.bungee.listeners.ProxyPingListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class BungeePlugin extends Plugin {
	private Configuration config;
	private Configuration message;

	@Override
	public void onLoad() {
		saveDefaultConfig();
		reloadConfig();
		reloadMessage();
	}

	@Override
	public void onEnable() {
		getProxy().getPluginManager().registerListener(this, new ProxyPingListener(this));
		getProxy().getPluginManager().registerCommand(this, new CommandHandler("motd", "mineblock.command.motd", this));
	}

	private void saveResource(String fileName) {
		if (!getDataFolder().exists()) {
			getDataFolder().mkdir();
		}

		File file = new File(getDataFolder(), fileName);

		if (!file.exists()) {
			try (InputStream in = getResourceAsStream(fileName)) {
				Files.copy(in, file.toPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void saveDefaultConfig() {
		saveResource("config.yml");
	}

	public void reloadConfig() {
		try {
			config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
		} catch (IOException ex) {
			getLogger().severe("Failed to save config file!");
		}
	}

	public void reloadMessage()  {
		saveResource("messages.yml");
		try {
			message = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "messages.yml"));
		} catch (IOException ex) {
			getLogger().severe("Failed to save message file!");
		}
	}

	public Configuration getMessage(){
		return message;
	}

	public Configuration getConfig(){
		return config;
	}
}