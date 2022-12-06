package org.mineblock.motd.bungee.listeners;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.mineblock.motd.bungee.BungeePlugin;

import java.util.List;
import java.util.UUID;

public class ProxyPingListener implements Listener {
	private final BungeePlugin plugin;

	public ProxyPingListener(final BungeePlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = 64)
	public void onProxyPing(final ProxyPingEvent event) {
		final ServerPing response = event.getResponse();

		if (response == null) {
			return;
		}

		response.getModinfo().setType("VANILLA");
		response.getVersion().setProtocol(Math.max(event.getConnection().getVersion(), plugin.getConfig().getInt("version.minimum",1)));

		final ServerPing.Players players = response.getPlayers();

		if (plugin.getConfig().getBoolean("motd.enable")) {
			response.setDescriptionComponent(new TextComponent(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("motd.list"))));
		}

		if (plugin.getConfig().getBoolean("protocol.enable")) {
			response.getVersion().setName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("protocol.name","MineBlock")));
		}

		if (plugin.getConfig().getBoolean("sample.enable")) {
			final UUID fakeUuid = new UUID(0, 0);
			List<String> sampleList = plugin.getConfig().getStringList("sample.list");
			final ServerPing.PlayerInfo[] sample = new ServerPing.PlayerInfo[sampleList.size()];

			for (int i = 0; i < sampleList.size(); i++) {
				sample[i] = new ServerPing.PlayerInfo(ChatColor.translateAlternateColorCodes('&',
						sampleList.get(i).replace("%maxplayers%", String.valueOf(players.getMax())).replace("%onlineplayers%", String.valueOf(players.getOnline()))),
						fakeUuid
				);
			}

			players.setSample(sample);
		}

		if(plugin.getConfig().getBoolean("maintenance.enable")){
			response.getVersion().setName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("maintenance.protocol","&c维护模式")));
			players.setSample(new ServerPing.PlayerInfo[]{new ServerPing.PlayerInfo(ChatColor.translateAlternateColorCodes('&',ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("maintenance.sample","&c服务器正在进行维护！"))), new UUID(0, 0))});
			response.getVersion().setProtocol(plugin.getConfig().getInt("maintenance.version",0));
		}

		event.setResponse(response);
	}
}
