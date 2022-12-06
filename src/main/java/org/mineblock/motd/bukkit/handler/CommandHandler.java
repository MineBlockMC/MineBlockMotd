package org.mineblock.motd.bukkit.handler;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.mineblock.motd.bukkit.BukkitPlugin;

public class CommandHandler implements CommandExecutor {
	private final BukkitPlugin plugin;

	public CommandHandler(BukkitPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String s, final String[] args) {
		switch (args[0].toLowerCase()) {
			case "help":{
				sender.sendMessage(plugin.getMessage().getString("messages.usage"));
				break;
			}
			case "reload":{
				plugin.reloadConfig();
				sender.sendMessage(plugin.getMessage().getString("messages.reload"));
				break;
			}
			default:{
				sender.sendMessage("This server is running "+ plugin.getDescription().getName() +" version "+ plugin.getDescription().getVersion()+" by "+ plugin.getDescription().getAuthors().toString().replace("[","").replace("]",""));
				sender.sendMessage(plugin.getMessage().getString("messages.unknown-command"));
				break;
			}
		}
		return true;
	}
}
