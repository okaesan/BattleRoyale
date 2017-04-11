package com.plugin.ftb.battleroyale;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class MainConfig extends BattleRoyale {

	public static BattleRoyale plugin = BattleRoyale.plugin;
	public static ArrayList<Location> locations = new ArrayList<>();

	/*
	 * configからlocationsを取得
	 */
	public static void loadConfig() {
		plugin.reloadConfig();
		locations = new ArrayList<>();
		locations = (ArrayList<Location>) plugin.getConfig().get("locations");
		if (locations == null) {
			locations = new ArrayList<>();
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