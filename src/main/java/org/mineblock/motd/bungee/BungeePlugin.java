package org.mineblock.motd.bungee;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import org.mineblock.motd.bungee.handler.CommandHandler;
import org.mineblock.motd.bungee.listeners.ProxyPingListener;
import org.mineblock.motd.bungee.utils.ConfigurationUtil;
import org.mineblock.motd.bungee.variables.Messages;
import org.mineblock.motd.bungee.variables.Variables;

import java.io.File;

public class BungeePlugin extends Plugin {
	public static BungeePlugin INSTANCE;

	public void onEnable() {
		INSTANCE = this;

		final ConfigurationUtil configurationUtil = new ConfigurationUtil(this);

		configurationUtil.createConfiguration(new File(getDataFolder(), "config.yml"));
		configurationUtil.createConfiguration(new File(getDataFolder(), "messages.yml"));

		final ProxyServer proxy = getProxy();
		final Variables variables = new Variables(configurationUtil);
		final Messages messages = new Messages(configurationUtil);
		final PluginManager pluginManager = proxy.getPluginManager();

		pluginManager.registerListener(this, new ProxyPingListener(variables));
		pluginManager.registerCommand(this, new CommandHandler("motd", variables, messages, this));
	}
}