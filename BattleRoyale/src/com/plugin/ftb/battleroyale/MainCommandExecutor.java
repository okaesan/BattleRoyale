package com.plugin.ftb.battleroyale;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MainCommandExecutor implements CommandExecutor {

	public static BattleRoyale _plugin = BattleRoyale.plugin;
	public static int judEdit;

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
	@SuppressWarnings("deprecation")
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
			 * とか言ってるけど結局文の数は対して変わってないのでわざわざswitch文使わなくてよかったかもです><
			 * 解読する際はみにくいかと思いますが頑張ってください・・・笑
			 */
			switch(args[0]){
			case "stageL":
				Location _locL = _player.getLocation();

				MainConfig.makeStage(_locL,_player,0);

				return true;

			case "stageR":
				if(_plugin.getConfig().get("stagelocationsL")!=null){
					Location _locR = _player.getLocation();

					MainConfig.makeStage(_locR,_player,1);

					return true;

				}else{
					_player.sendMessage(BattleRoyale.prefix + ChatColor.GRAY + "stageLから設定してください");

					return true;
				}

			case "chestEdit":
				if(_plugin.getConfig().get("stagelocationsR")!=null){
					judEdit = 2;

					return true;

				}else{
					_player.sendMessage(BattleRoyale.prefix + ChatColor.GRAY + "stageRを設定してください");

					return true;
				}

			case "chestCom":
				judEdit = 0;

				return true;
				
			case "setMap":
				ItemStack item = _player.getItemInHand();
				if(item.getType().equals(Material.MAP)){
					MainConfig.setMap(item);
					_player.sendMessage(BattleRoyale.prefix + ChatColor.GRAY + "マップを設定しました。");
				}else{
					_player.sendMessage(BattleRoyale.prefix + ChatColor.GRAY + "マップを持ってコマンドを実行してください。");
				}
				
				return true;
			default:
				_player.sendMessage(BattleRoyale.prefix + ChatColor.GRAY + "\n/battleroyale " + ChatColor.RED + "stageL\n"
						+ ChatColor.GRAY + "/battleroyale " + ChatColor.RED + "stageR\n"
						+ ChatColor.GRAY + "/battleroyale " + ChatColor.RED + "chestEdit\n"
						+ ChatColor.GRAY + "/battleroyale " + ChatColor.RED + "setMap");
				return true;
			}
		}

		return false;
	}
}