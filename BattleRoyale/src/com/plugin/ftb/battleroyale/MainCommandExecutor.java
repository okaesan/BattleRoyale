package com.plugin.ftb.battleroyale;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainCommandExecutor implements CommandExecutor {

	public static BattleRoyale _plugin = BattleRoyale.plugin;

	public MainCommandExecutor(BattleRoyale plugin) {
		MainCommandExecutor._plugin = plugin;
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
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (sender instanceof Player) {
			if (args.length == 0) {
				return false;
			}

			Player _player = (Player) sender;
			/*
			 * 対角線でステージの登録を行うためのL、Rの登録
			 * 引数の0と1は、LとRの識別子でMainConfigクラスで関数が増えるのを防ぐため
			 */
			switch(args[0]){
			case "stageL":
				Location _locL = _player.getLocation();

				MainConfig.makeStage(_locL,_player,0);

				return true;

			case "stageR":
				Location _locR = _player.getLocation();

				MainConfig.makeStage(_locR,_player,1);

				return true;
			default:
				_player.sendMessage(BattleRoyale.prefix+ChatColor.GRAY+"\n/battleroyale " + ChatColor.RED + "stageL\n"
						+ ChatColor.GRAY + "/battleroyale " + ChatColor.RED+"stageR");
				return true;
			}
		}

		return false;
	}
}