package com.plugin.ftb.battleroyale;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.map.MapView.Scale;

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
	public static void makeStage(Location loc, Player player, int jud){
		loadConfig();

		switch(jud){
		case 0:

			_stagelocationsL.add(loc.getBlockX());

			_stagelocationsL.add(loc.getBlockZ());

			plugin.getConfig().set("stagelocationsL", null);
			plugin.getConfig().set("stagelocationsL", _stagelocationsL);
			plugin.saveConfig();

			player.sendMessage(BattleRoyale.prefix + ChatColor.GREEN + "stageLを設定しました");

			return;

		case 1:

			plugin.getConfig().set("stagelocationsR", null);

			_stagelocationsR.add(loc.getBlockX());

			_stagelocationsR.add(loc.getBlockZ());

			plugin.getConfig().set("stagelocationsR", null);
			plugin.getConfig().set("stagelocationsR", _stagelocationsR);
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
	@SuppressWarnings("deprecation")
	public static void setMap(ItemStack map){
		loadConfig();
		MapView mapView = Bukkit.getServer().getMap(map.getDurability());
		plugin.getConfig().set("mapWorld", mapView.getWorld().getName());
		plugin.getConfig().set("mapNum", map.getDurability());
		plugin.getConfig().set("mapX", mapView.getCenterX());
		plugin.getConfig().set("mapZ", mapView.getCenterZ());
		plugin.getConfig().set("mapScale", mapView.getScale().name());
		plugin.getConfig().set("mapScale", mapView.getRenderers());
		MapCanvas mapCanvas = null;
		broadcast("" + mapCanvas);
		plugin.saveConfig();
	}
	
	/*
	 * マップを配布する
	 */
	@SuppressWarnings("deprecation")
	public static void giveMap(){
		loadConfig();
		//新しい地図データを作る
        MapView view = Bukkit.getServer().createMap(plugin.getServer().getWorld(plugin.getConfig().getString("mapWorld")));

        //座標と縮尺を設定
        view.setCenterX(plugin.getConfig().getInt("mapX"));
        view.setCenterZ(plugin.getConfig().getInt("mapZ"));
        
		Scale scale = Scale.FARTHEST;
		String scaleString = plugin.getConfig().getString("mapScale");
		if(scaleString.equalsIgnoreCase("CLOSEST")){
			scale = Scale.CLOSEST;
		}if(scaleString.equalsIgnoreCase("CLOSE")){
			scale = Scale.CLOSE;
		}if(scaleString.equalsIgnoreCase("NORMAL")){
			scale = Scale.NORMAL;
		}if(scaleString.equalsIgnoreCase("FAR")){
			scale = Scale.FAR;
		}if(scaleString.equalsIgnoreCase("FARTHEST")){
			scale = Scale.FARTHEST;
		}
        view.setScale(scale);
        
        for(MapRenderer ren : view.getRenderers()){
        	view.removeRenderer(ren);
        }
        for(MapRenderer ren : Bukkit.getServer().getMap((short)plugin.getConfig().getInt("mapNum")).getRenderers()){
        	view.addRenderer(ren);
        }
        view.addRenderer(new CustomMap());
        
        ItemStack item = new ItemStack(Material.MAP, 1, view.getId());
		for(Player player : Bukkit.getServer().getOnlinePlayers()){
			player.getInventory().addItem(item);
			player.updateInventory();
		}
	}

	// デバッグ用
	public static void broadcast(String message) {
		BattleRoyale.broadcast(message);
	}
}