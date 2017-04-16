package com.plugin.ftb.battleroyale;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class MainConfig extends BattleRoyale {

	public static BattleRoyale plugin = BattleRoyale.plugin;
	public static ArrayList<Location> locations = new ArrayList<>();
	public static ArrayList<Integer> _stagelocationsL = new ArrayList<>();
	public static ArrayList<Integer> _stagelocationsR = new ArrayList<>();
	public static ArrayList<Integer> _chestlocations = new ArrayList<>();
	public static ArrayList<Integer> check = new ArrayList<>();

	public static int chestCount;

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
		_stagelocationsL = (ArrayList<Integer>) plugin.getConfig().get("stagelocationsL");
		if (_stagelocationsL == null) {
			_stagelocationsL = new ArrayList<>();
		}

		_stagelocationsR = new ArrayList<>();
		_stagelocationsR = (ArrayList<Integer>) plugin.getConfig().get("stagelocationsR");
		if (_stagelocationsR == null) {
			_stagelocationsR = new ArrayList<>();
		}

		_chestlocations = new ArrayList<>();
		_chestlocations = (ArrayList<Integer>) plugin.getConfig().get("chestlocations");
		if (_chestlocations == null) {
			_chestlocations = new ArrayList<>();
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
	@SuppressWarnings("unchecked")
	public static void makeStage(Location loc, Player player, int jud){
		loadConfig();

		switch(jud){
		case 0:
			check = (ArrayList<Integer>)plugin.getConfig().get("stagelocationsL");
			/*
			 * stageloacationsLパスに値が入力されているか確認
			 */
			if (check != null) {
				player.sendMessage(BattleRoyale.prefix + "既に設定されています");
				return;
			}

			_stagelocationsL.add(loc.getBlockX());
			_stagelocationsL.add(loc.getBlockZ());

			plugin.getConfig().set("stagelocationsL", null);
			plugin.getConfig().set("stagelocationsL", Arrays.asList(_stagelocationsL));
			plugin.saveConfig();

			player.sendMessage(BattleRoyale.prefix + ChatColor.GREEN + "stageLを設定しました");

			return;

		case 1:
			check = (ArrayList<Integer>)plugin.getConfig().get("stagelocationsR");
			/*
			 * stageloacationsRパスに値が入力されているか確認
			 */
			if (check != null) {
				player.sendMessage(BattleRoyale.prefix + "既に設定されています");
				return;
			}

			_stagelocationsR.add(loc.getBlockX());
			_stagelocationsR.add(loc.getBlockZ());

			plugin.getConfig().set("stagelocationsR", null);
			plugin.getConfig().set("stagelocationsR", Arrays.asList(_stagelocationsR));
			plugin.saveConfig();

			player.sendMessage(BattleRoyale.prefix + ChatColor.GREEN + "stageRを設定しました");

			return;
		}
	}

	public static void subChestConfig(Location loc, Player player){
		loadConfig();

		if(_chestlocations.contains(loc)){
			player.sendMessage(BattleRoyale.prefix + ChatColor.RED + "このチェストは既に追加されています");
			return;
		}

		chestCount = plugin.getConfig().getInt("chestCounter");
		chestCount++;

		_chestlocations.add(loc.getBlockX());
		_chestlocations.add(loc.getBlockY());
		_chestlocations.add(loc.getBlockZ());

		plugin.getConfig().set("chestlocations", null);

		plugin.getConfig().set("chestCounter", chestCount);
		plugin.getConfig().set("chestlocations"+chestCount+".x", _chestlocations.get(0));
		plugin.getConfig().set("chestlocations"+chestCount+".y", _chestlocations.get(1));
		plugin.getConfig().set("chestlocations"+chestCount+".z", _chestlocations.get(2));
		plugin.saveConfig();

		player.sendMessage(BattleRoyale.prefix + ChatColor.GREEN + "このチェストを追加しました");
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
	
	/*
	 * マップを保存する
	 */
	public static void setMap(Short map){
		loadConfig();
		plugin.getConfig().set("map", map);
		plugin.saveConfig();
	}

	// デバッグ用
	public static void broadcast(String message) {
		BattleRoyale.broadcast(message);
	}
}