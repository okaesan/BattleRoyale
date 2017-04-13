package com.plugin.ftb.battleroyale;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class MainConfig extends BattleRoyale {

	public static BattleRoyale plugin = BattleRoyale.plugin;
	public static ArrayList<Location> locations = new ArrayList<>();
	public static ArrayList<Location> _stagelocationsL = new ArrayList<>();
	public static ArrayList<Location> _stagelocationsR = new ArrayList<>();

	/*
	 * configからlocationsを取得
	 */
	@SuppressWarnings("unchecked")
	public static void loadConfig() {
		plugin.reloadConfig();
		locations = new ArrayList<>();
		locations = (ArrayList<Location>) plugin.getConfig().get("locations");
		if (locations == null) {
			locations = new ArrayList<>();
		}

		_stagelocationsL = new ArrayList<>();
		_stagelocationsL = (ArrayList<Location>) plugin.getConfig().get("stagelocationsL");
		if (_stagelocationsL == null) {
			_stagelocationsL = new ArrayList<>();
		}

		_stagelocationsR = new ArrayList<>();
		_stagelocationsR = (ArrayList<Location>) plugin.getConfig().get("stagelocationsR");
		if (_stagelocationsR == null) {
			_stagelocationsR = new ArrayList<>();
		}
	}

	/*
	 * ステージの対角線の座標
	 *
	 * @param loc コマンドを打った人の現在地の座標
	 *
	 * @param player コマンド発信者
	 *
	 * @param jud stageLとstageRのどちらがコマンドとして入力されたかの識別子
	 */
	public static void makeStage(Location loc, Player player, int jud){
		loadConfig();

		switch(jud){
		case 0:
			_stagelocationsL.add(loc);

			/*
			 * stageloacationsLパスに値が入力されているか確認
			 */
			if (plugin.getConfig().get("stagelocationsL") != null) {
				player.sendMessage(BattleRoyale.prefix + "既に設定されています");
				return;
			}

			plugin.getConfig().set("stagelocationsL", null);
			plugin.getConfig().set("stagelocationsL", _stagelocationsL);
			plugin.saveConfig();

			player.sendMessage(BattleRoyale.prefix + ChatColor.GREEN + "stageLを設定しました");

			return;

		case 1:
			_stagelocationsR.add(loc);

			/*
			 * stageloacationsRパスに値が入力されているか確認
			 */
			if (plugin.getConfig().get("stagelocationsR") != null) {
				player.sendMessage(BattleRoyale.prefix + "既に設定されています");
				return;
			}

			plugin.getConfig().set("stagelocationsR", null);
			plugin.getConfig().set("stagelocationsR", _stagelocationsR);
			plugin.saveConfig();

			player.sendMessage(BattleRoyale.prefix + ChatColor.GREEN + "stageRを設定しました");

			return;
		}

	}

	/*
	 * 新しい音符ブロックをconfigに保存
	 *
	 * @param loc 音符ブロックのロケーション
	 *
	 * @param player コマンド発信者
	 */
	public static void addButton(Location loc, Player player) {
		loadConfig();

		if (locations.contains(loc)) {
			player.sendMessage(BattleRoyale.prefix + ChatColor.RED + "既に追加されています");
			return;
		}

		locations.add(loc);
		plugin.getConfig().set("locations", null);
		plugin.getConfig().set("locations", locations);
		plugin.saveConfig();

		player.sendMessage(BattleRoyale.prefix + ChatColor.GREEN + "音符ブロックを設定しました。");
	}

	/*
	 * 登録していた音符ブロックを削除
	 *
	 * @param loc 音符ブロックのロケーション
	 *
	 * @param player コマンド発信者
	 */
	public static void removeButton(Location loc, Player player) {
		loadConfig();

		if (!locations.contains(loc)) {
			player.sendMessage(BattleRoyale.prefix + ChatColor.RED + "登録されていません");
			return;
		}

		locations.remove(loc);
		plugin.getConfig().set("locations", null);
		plugin.getConfig().set("locations", locations);
		plugin.saveConfig();

		player.sendMessage(BattleRoyale.prefix + ChatColor.GREEN + "音符ブロックを削除しました。");
	}

	// デバッグ用
	public static void broadcast(String message) {
		BattleRoyale.broadcast(message);
	}
}