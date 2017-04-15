package com.plugin.ftb.battleroyale;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ResetCommand implements CommandExecutor {

	public static BattleRoyale plugin = BattleRoyale.plugin;
	public static String prefix = BattleRoyale.prefix;

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		MainListener.c = 0;
		sender.sendMessage(prefix + ChatColor.AQUA +"リセットしました");
		return true;
	}
}
