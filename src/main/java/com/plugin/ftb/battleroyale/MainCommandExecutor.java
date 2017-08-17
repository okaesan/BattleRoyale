package com.plugin.ftb.battleroyale;

import java.util.ArrayList;

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

	public static ArrayList<Player> setChestPlayer = new ArrayList<>();
	public static ArrayList<Player> setProtectionPlayer = new ArrayList<>();


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
	@SuppressWarnings({ "deprecation" })
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (sender instanceof Player) {
			if (args.length == 0) {
				return false;
			}

			Player _player = (Player) sender;

			/*
			 * 対角線でステージの登録を行うためのL、Rの登録
			 * 引数の0と1は、LとRの識別子
			 */
			switch(args[0]){
			case "setStageL":
				if(args.length == 1){
					Location _locL = _player.getLocation();
					MainConfig.makeStage(_locL,_player,0);
				}
				return true;

			case "setStageR":
				if(args.length == 1){
					if(_plugin.getConfig().get("stagelocationsL")!=null){
						Location _locR = _player.getLocation();
						MainConfig.makeStage(_locR,_player,1);
					}else{
						_player.sendMessage(BattleRoyale.prefix + ChatColor.GRAY + "stageLから設定してください");
					}
				}
				return true;

			case "setJoinGameL":
				if(args.length == 1){
					Location _locR = _player.getLocation();
					MainConfig.makeJoinGame(_locR,_player,0);
				}
				return true;

			case "setJoinGameR":
				if(args.length == 1){
					Location _locR = _player.getLocation();
					MainConfig.makeJoinGame(_locR,_player,1);
				}
				return true;

			case "areaJoinGame":
				if(args.length == 1){
					AreaJoin.joinArea();
				}
				return true;

			case "setLobbypoint":
				if(args.length == 1){
					MainConfig.setLobbypoint(_player.getLocation(), _player);
					_player.sendMessage(BattleRoyale.prefix + ChatColor.GREEN + "Lobbypointを設定しました");
				}
				return true;

			case "setStartpoint":
				if(args.length == 1){
					MainConfig.setStartpoint(_player.getLocation(), _player);
					_player.sendMessage(BattleRoyale.prefix + ChatColor.GREEN + "Startpointを追加しました");
				}
				return true;

			case "setStartpointRandom":
				if(args.length == 1){
					MainConfig.setStartpointRandom(_player);
				}
				return true;

			case "setDeathpoint":
				if(args.length == 1){
					MainConfig.setDeathpoint(_player.getLocation(), _player);
				_player.sendMessage(BattleRoyale.prefix + ChatColor.GREEN + "Deathpointを設定しました");
				}
				return true;

			case "setMap":
				ItemStack item = _player.getItemInHand();
				if(args.length == 1){
					if(item.getType().equals(Material.MAP)){
						MainConfig.setMap(item);
						_player.sendMessage(BattleRoyale.prefix + ChatColor.GRAY + "マップを設定しました。");
					}else{
						_player.sendMessage(BattleRoyale.prefix + ChatColor.GRAY + "マップを持ってコマンドを実行してください。");
					}
				}
				return true;

			case "setTimer":
				//argsがnullの場合、args[num]でもえらーがでるのでlengthでチェックしたほうがいいです。。
				if(args.length == 3){
					MainConfig.setTimer(Integer.parseInt(args[1]),Integer.parseInt(args[2]));
					_player.sendMessage(BattleRoyale.prefix + ChatColor.GRAY + "時間を設定しました");
				}else{
					_player.sendMessage(BattleRoyale.prefix + "/battleroyale setTimer" + ChatColor.RED + " t1 t2\n"
							+ ChatColor.GRAY + "t1には最初の禁止区域が追加されるまでの時間\n"
									+ "t2にはそれ以降何秒おきに禁止区域を追加するかの時間\n"
									+ "例:/battleroyale setTimer 1500 1000");
				}
				return true;

			case "setNATimer":
				if(args.length == 2){
					MainConfig.setNATimer(Integer.parseInt(args[1]));
					_player.sendMessage(BattleRoyale.prefix + ChatColor.GRAY + "攻撃不可の時間を設定しました");
				}else{
					_player.sendMessage(BattleRoyale.prefix + "/battleroyale setNATimer" + ChatColor.RED + "t1\n"
							+ ChatColor.GRAY + "t1にはゲームを開始してから攻撃を不可能にする時間");
				}
				return true;

			case "setChest":
				if(args.length == 1){
					_player.sendMessage(BattleRoyale.prefix + ChatColor.AQUA + "骨を持って左クリックでチェストの位置を編集してください");
					setChestPlayer.add(_player);
				}
				return true;

			case "comChest":
				if(args.length == 1){
					_player.sendMessage(BattleRoyale.prefix + ChatColor.AQUA + "チェストの位置を確定しました");
					if(setChestPlayer.contains(_player)){
					setChestPlayer.remove(_player);
					}
				}
				return true;

			case "startgame":
				if(args.length == 1){
					StartCommand.startGame();
				}
				return true;

			case "setProtection":
				//破壊できないガラスを設定
				if(args.length == 1) {
					if(setProtectionPlayer.contains(_player)) {
						_player.sendMessage(BattleRoyale.prefix + ChatColor.WHITE + "設定を終わりました。");
						setProtectionPlayer.remove(_player);
						return true;
					}else {
						_player.getInventory().addItem(new ItemStack(Material.STICK));
						_player.sendMessage(BattleRoyale.prefix + ChatColor.WHITE + "棒を持って、破壊されたくないブロックを左クリックしてください。");
						setProtectionPlayer.add(_player);
						return true;
					}
				}

			case "checkConf":
				if(args.length == 1){
					_player.sendMessage("" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "BattleRoyale" + ChatColor.GRAY + " 設定一覧\n"
							+ ChatColor.DARK_GREEN + ChatColor.BOLD + "- ステージ範囲 -\n" + ChatColor.RESET + ChatColor.GRAY + _plugin.getConfig().getString("stagelocationsL") + "\n" + _plugin.getConfig().getString("stagelocationsR")
							+ "\n\n" + ChatColor.DARK_GREEN + ChatColor.BOLD + "- ロビー -\n" + ChatColor.RESET + ChatColor.GRAY + _plugin.getConfig().getString("Lobbypoint")
							+ "\n\n" + ChatColor.DARK_GREEN + ChatColor.BOLD + "- スタート地点 -\n" + ChatColor.RESET + ChatColor.GRAY + _plugin.getConfig().getString("Startpoints")
							+ "\n\n" + ChatColor.DARK_GREEN + ChatColor.BOLD + "- 死者の集い -\n" + ChatColor.RESET + ChatColor.GRAY + _plugin.getConfig().getString("Deathpoint")
							+ "\n\n" + ChatColor.DARK_GREEN + ChatColor.BOLD + "- 禁止区域追加までの時間 -\n" + ChatColor.RESET + ChatColor.GRAY + _plugin.getConfig().getString("Timer")
							+ "\n\n" + ChatColor.DARK_GREEN + ChatColor.BOLD + "- 攻撃不可能時間 -\n" + ChatColor.RESET + ChatColor.GRAY + _plugin.getConfig().getString("NATimer")
							+ "\n\n" + ChatColor.DARK_GREEN + ChatColor.BOLD + "- 設定されたチェストの数 -\n" + ChatColor.RESET + ChatColor.GRAY + _plugin.getConfig().getString("chestCounter"));
				}
				return true;
			case "reload":
				//スコアボードをリロード
				if(args.length == 1) {
					ScoreBoard.reloadScoreboard();
					_player.sendMessage(BattleRoyale.prefix + ChatColor.GRAY + "スコアボードをリロードしました。");
					return true;
				}
			default:
				_player.sendMessage(BattleRoyale.prefix + ChatColor.GRAY + "\n/battleroyale " + ChatColor.RED + "setStageL\n"
						+ ChatColor.GRAY + "/battleroyale " + ChatColor.RED + "setStageR\n"
						+ ChatColor.GRAY + "/battleroyale " + ChatColor.RED + "setLobbypoint\n"
						+ ChatColor.GRAY + "/battleroyale " + ChatColor.RED + "setStartpoint\n"
						+ ChatColor.GRAY + "/battleroyale " + ChatColor.RED + "setStartpointRandom\n"
						+ ChatColor.GRAY + "/battleroyale " + ChatColor.RED + "setDeathpoint\n"
						+ ChatColor.GRAY + "/battleroyale " + ChatColor.RED + "setMap\n"
						+ ChatColor.GRAY + "/battleroyale " + ChatColor.RED + "setTimer t1 t2\n"
						+ ChatColor.GRAY + "/battleroyale " + ChatColor.RED + "setNATimer t1\n"
						+ ChatColor.GRAY + "/battleroyale " + ChatColor.RED + "setJoinGameL\n"
						+ ChatColor.GRAY + "/battleroyale " + ChatColor.RED + "setJoinGameR\n"
						+ ChatColor.GRAY + "/battleroyale " + ChatColor.RED + "areaJoinGame\n"
						+ ChatColor.GRAY + "/battleroyale " + ChatColor.RED + "setChest\n"
						+ ChatColor.GRAY + "/battleroyale " + ChatColor.RED + "comChest\n"
						+ ChatColor.GRAY + "/battleroyale " + ChatColor.RED + "setProtection\n"
						+ ChatColor.GRAY + "/battleroyale " + ChatColor.RED + "reload");
				return true;
			}
		}
		return false;
	}
}