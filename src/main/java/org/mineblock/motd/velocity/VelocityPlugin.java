package org.mineblock.motd.velocity;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.event.EventTask;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.ServerPing;
import com.velocitypowered.api.util.Favicon;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.mineblock.motd.Config;
import org.mineblock.motd.Utils;
import org.slf4j.Logger;

import java.io.*;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Plugin(
        id = "mineblockmotd",
        name = "MineBlock Motd",
        version = "1.0",
        url = "https://www.mineblock.cc",
        description = "MineBlock 服务器标语",
        authors = {"MineBlock Team"}
)
public class VelocityPlugin {
    private final ProxyServer server;
    private final Logger logger;
    private final Path dataDirectory;

    private Config config = new Config();
    private final Gson gson = new Gson();

    @Inject
    public VelocityPlugin(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        reloadConfig();
        CommandManager commandManager = server.getCommandManager();
        CommandMeta commandMeta = commandManager.metaBuilder("motd").plugin(this).build();
        SimpleCommand motdCommand = new MotdCommand(this);
        commandManager.register(commandMeta, motdCommand);
    }

    @Subscribe
    public EventTask onProxyPing(final ProxyPingEvent event) {
        return EventTask.async(() -> {
            final ServerPing.Builder pong = event.getPing().asBuilder();

            pong.clearSamplePlayers();
            try {
                pong.favicon(Favicon.create(new File(dataDirectory.toFile(), "server-icon.png").toPath()));
            } catch (IOException ignored) {}

            if(config.motd.enable){
                pong.description(Component.text()
                        .append(MiniMessage.miniMessage().deserialize(Utils.makeCenter(config.motd.line1)))
                        .appendNewline()
                        .append(MiniMessage.miniMessage().deserialize(Utils.makeCenter(config.motd.line2)))
                        .build()
                );
            }

            if(config.sample.enable){
                List<ServerPing.SamplePlayer> samplePlayerList = config.sample.list.stream().map(s -> new ServerPing.SamplePlayer(s, UUID.randomUUID())).collect(Collectors.toList());
                pong.samplePlayers(samplePlayerList.toArray(new ServerPing.SamplePlayer[0]));
            }

            if(config.protocol.enable){
                pong.version(new ServerPing.Version(Math.max(event.getConnection().getProtocolVersion().getProtocol(), config.protocol.minimum), config.protocol.name));
            }

            if(config.maintenance.enable){
                pong.version(new ServerPing.Version(config.maintenance.version, config.maintenance.protocol))
                        .samplePlayers(new ServerPing.SamplePlayer(config.maintenance.sample, UUID.randomUUID()));
            }

            event.setPing(pong.build());
        });
    }

    public void reloadConfig(){
        logger.info("Loading configuration.");
        if (!dataDirectory.toFile().exists() && !dataDirectory.toFile().mkdir()) logger.warn("Failed to create data directory.");
        File configFile = new File(dataDirectory.toFile(), "config.json");

        if (!configFile.exists()) {
            try {
                gson.toJson(config, Config.class, new FileWriter(configFile));
            } catch (IOException e) {
                logger.error("Failed to save configuration file, reason: " + e);
            }
        }
        try {
            config = new Gson().fromJson(new FileReader(configFile), Config.class);
        } catch (FileNotFoundException e) {
            logger.error("Failed to load configuration file, using default configuration instead, reason: " + e);
        }
    }
}
