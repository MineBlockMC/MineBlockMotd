package org.mineblock.motd.bukkit.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.mineblock.motd.bukkit.BukkitPlugin;

public class ServerListPingListener implements Listener {
	private final BukkitPlugin plugin;

	public ServerListPingListener(BukkitPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onServerListPing(final ServerListPingEvent event) {
		if (plugin.getConfig().getBoolean("motd.enable")) {
			event.setMotd(plugin.getConfig().getString("motd.list"));
		}
	}
}
