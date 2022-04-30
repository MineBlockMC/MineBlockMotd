package org.mineblock.motd.bukkit.handler;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import org.mineblock.motd.bukkit.BukkitPlugin;
import org.mineblock.motd.bukkit.variables.Messages;
import org.mineblock.motd.bukkit.variables.Variables;

import static org.mineblock.motd.bungee.variables.Messages.*;

public class CommandHandler implements CommandExecutor {
	private final Variables variables;
	private final Messages messages;
	private final BukkitPlugin plugin;

	public CommandHandler(final Variables variables, final Messages messages, BukkitPlugin plugin) {
		this.variables = variables;
		this.messages = messages;
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] args) {
		switch (args[0].toLowerCase()) {
			case "help":{
				if (commandSender.hasPermission("mineblock.command.motd")) {
					commandSender.sendMessage(USAGE);
				} else commandSender.sendMessage(NOPERMISSION);
				break;
			}
			case "reload":{
				if (commandSender.hasPermission("mineblock.command.motd")) {
					variables.reloadConfig();
					messages.reload();
					commandSender.sendMessage(RELOAD);
				} else commandSender.sendMessage(messages.getNoPermission());
				break;
			}
			default:{
				commandSender.sendMessage("This server is running "+ plugin.getDescription().getName() +" version "+ plugin.getDescription().getVersion()+" by "+ plugin.getDescription().getAuthors().toString().replace("[","").replace("]",""));
				commandSender.sendMessage(UNKNOWNCOMMAND);
				break;
			}
		}
		return true;
	}
}
