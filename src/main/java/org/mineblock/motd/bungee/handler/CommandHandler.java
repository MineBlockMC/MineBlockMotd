package org.mineblock.motd.bungee.handler;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import org.mineblock.motd.bungee.BungeePlugin;

public class CommandHandler extends Command {
	private final BungeePlugin plugin;

	public CommandHandler(final String string, String permission, BungeePlugin plugin) {
		super(string, permission);
		this.plugin = plugin;
	}

	public void execute(final CommandSender commandSender, final String[] args) {
		if (commandSender.hasPermission("mineblock.command.motd")) {
			switch (args[0].toLowerCase()) {
				case "help": {
					commandSender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', plugin.getMessage().getString("messages.usage"))));
					break;
				}
				case "reload": {
					plugin.reloadConfig();
					plugin.reloadMessage();
					commandSender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&',plugin.getMessage().getString("messages.reload"))));
					break;
				}
				default: {
					commandSender.sendMessage(new TextComponent("This server is running " + plugin.getDescription().getName() + " version " + plugin.getDescription().getVersion() + " by " + plugin.getDescription().getAuthor()));
					commandSender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&',plugin.getMessage().getString("messages.unknown-command"))));
					break;
				}
			}
		} else commandSender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&',plugin.getMessage().getString("messages.no-permission"))));
	}
}
