package org.mineblock.motd.velocity;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Locale;

public class MotdCommand implements SimpleCommand {
    private final VelocityPlugin plugin;

    public MotdCommand(VelocityPlugin plugin){
        this.plugin = plugin;
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if(args.length == 0){
            source.sendMessage(Component.text("Usage: /" + invocation.alias() + " <reload>", NamedTextColor.RED));
        } else {
            switch (args[0].toLowerCase(Locale.ROOT)){
                case "reload":{
                    plugin.reloadConfig();
                    source.sendMessage(Component.text("MineBlockMotd 配置文件已重新加载！", NamedTextColor.GREEN));
                    break;
                }
                default:{
                    source.sendMessage(Component.text("Usage: /" + invocation.alias() + " <reload>", NamedTextColor.RED));
                    break;
                }
            }
        }
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("mineblock.command.motd");
    }
}
