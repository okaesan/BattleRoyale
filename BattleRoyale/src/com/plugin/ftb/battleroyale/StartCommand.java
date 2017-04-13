package com.plugin.ftb.battleroyale;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class StartCommand implements CommandExecutor {

	public static BattleRoyale plugin = BattleRoyale.plugin;
	public static String prefix = BattleRoyale.prefix;

	/*
	 * ゲーム開始コマンド
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		MainListener.c = 1;
		//SLOWエフェクトを削除
		for (Player p : plugin.getServer().getOnlinePlayers()) {
			p.removePotionEffect(PotionEffectType.SLOW);
		}
		Bukkit.broadcastMessage(prefix + ChatColor.GOLD + "ゲームスタート");
		return true;
	}
}
