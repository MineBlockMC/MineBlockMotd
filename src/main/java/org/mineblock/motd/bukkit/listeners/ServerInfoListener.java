package org.mineblock.motd.bukkit.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerOptions;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedServerPing;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class ServerInfoListener extends PacketAdapter {
    public ServerInfoListener(final Plugin plugin) {
        super(plugin, ListenerPriority.HIGH, Collections.singletonList(PacketType.Status.Server.OUT_SERVER_INFO), ListenerOptions.ASYNC);
    }

    public void register() {
        ProtocolLibrary.getProtocolManager().addPacketListener(this);
    }

    @Override
    public void onPacketSending(final PacketEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final WrappedServerPing ping = event.getPacket().getServerPings().read(0);

        if (plugin.getConfig().getBoolean("motd.enable")) {
            ping.setMotD(plugin.getConfig().getString("motd.list"));
        }

        if (plugin.getConfig().getBoolean("sample.enable")) {
            final UUID fakeUuid = new UUID(0, 0);
            List<String> sampleList = plugin.getConfig().getStringList("sample.list");
            final List<WrappedGameProfile> sample = new ArrayList<>();

            for (final String s : sampleList) {
                sample.add(new WrappedGameProfile(fakeUuid,
                        ChatColor.translateAlternateColorCodes('&',s.replace("%maxplayers%", String.valueOf(ping.getPlayersMaximum())).replace("%onlineplayers%", String.valueOf(ping.getPlayersOnline())))));
            }

            ping.setPlayers(sample);
        }

        if (plugin.getConfig().getBoolean("protocol.enable")) {
            ping.setVersionName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("protocol.name","MineBlock")));
        }

        if(plugin.getConfig().getBoolean("maintenance.enable")){
            ping.setVersionName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("maintenance.protocol","&c维护模式")));

            List<WrappedGameProfile> sample = new ArrayList<>();
            sample.add(new WrappedGameProfile(new UUID(0, 0), ChatColor.translateAlternateColorCodes('&',ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("maintenance.sample","&c服务器正在进行维护！")))));
            ping.setPlayers(sample);
        }
    }
}
