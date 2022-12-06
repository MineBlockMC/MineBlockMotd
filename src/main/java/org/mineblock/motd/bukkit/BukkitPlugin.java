package org.mineblock.motd.bukkit;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.mineblock.motd.bukkit.handler.CommandHandler;
import org.mineblock.motd.bukkit.listeners.ServerInfoListener;
import org.mineblock.motd.bukkit.listeners.ServerListPingListener;

import java.io.File;

public class BukkitPlugin extends JavaPlugin {
	private YamlConfiguration message;

	@Override
	public void onLoad() {
		saveDefaultConfig();
		reloadMessage();
	}

	@Override
	public void onEnable() {
		getCommand("motd").setExecutor(new CommandHandler(this));

		if (getServer().getPluginManager().isPluginEnabled("ProtocolLib")) {
			new ServerInfoListener(this).register();
		} else {
			getServer().getPluginManager().registerEvents(new ServerListPingListener(this), this);
		}
	}

	public YamlConfiguration getMessage(){
		return message;
	}

	@Override
	public void reloadConfig() {
		super.reloadConfig();
		reloadMessage();
	}

	public void reloadMessage(){
		saveResource("message.yml", false);
		message = YamlConfiguration.loadConfiguration(new File(getDataFolder(),"message.yml"));
	}
}