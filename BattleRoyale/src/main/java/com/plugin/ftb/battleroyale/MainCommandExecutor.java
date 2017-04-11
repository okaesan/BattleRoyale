package com.plugin.ftb.battleroyale;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainCommandExecutor implements CommandExecutor {

	public static BattleRoyale plugin = BattleRoyale.plugin;

	public MainCommandExecutor(BattleRoyale plugin) {
		MainCommandExecutor.plugin = plugin;
	}

	public static void loadConfig() {
		plugin.reloadConfig();
	}

	/*
	 * onEnable()メソッドで定義したコマンドが実行されるとこのメソッドを通る。
	 * 
	 * @param sender コマンド実行者
	 * 
	 * @param cmd 実行されたコマンド
	 * 
	 * @param label コマンドエイリアス
	 * 
	 * @param args コマンドの引数
	 */

	// 制作途中です
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if (args.length == 0) {
				return false;
			}

			Player player = (Player) sender;
			if (args[0].equalsIgnoreCase("stage1")) {
				Location loc = player.getLocation();

				plugin.getConfig().set("stage1", loc);

				sender.sendMessage("yeaaaaaah");
				return true;
			}
		}

		return false;
	}
}