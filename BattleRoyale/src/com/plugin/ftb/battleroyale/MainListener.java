package com.plugin.ftb.battleroyale;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;

public class MainListener implements Listener {

	public static BattleRoyale plugin = BattleRoyale.plugin;

	static int c = 0;

	// チーム名
	public static final String TEAM_ALIVE_NAME = BattleRoyale.TEAM_ALIVE_NAME;
	public static final String TEAM_DEAD_NAME = BattleRoyale.TEAM_DEAD_NAME;

	// キル数カウント
	public static HashMap<Player, Integer> killCount = BattleRoyale.killCount;
	// ポイントカウント
	public static HashMap<Player, Integer> pointCount = BattleRoyale.pointCount;


	@SuppressWarnings("deprecation")
	@EventHandler
	public static void subChest(BlockBreakEvent e){
		Player player = (Player)e.getPlayer();
		
		if(MainCommandExecutor.judEdit == 2&&player.getInventory().getItemInHand().getType()==Material.STONE){
			MainConfig.subChestConfig(e.getBlock().getLocation(), player);
			
		}
	}

	/*
	 * 死亡者チャットを実装
	 */
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		String message = event.getMessage();
		Player player = event.getPlayer();
		Scoreboard board = plugin.getServer().getScoreboardManager().getMainScoreboard();

		// デバッグ用。
		if (message.equalsIgnoreCase("showCounts")) {
			broadcast(killCount + "");
			broadcast(pointCount + "");
		}

		// DEADチームのみに送信
		if (board.getTeam(TEAM_DEAD_NAME).hasPlayer(player)) {
			event.setCancelled(true);
			for (Player p : plugin.getServer().getOnlinePlayers()) {
				if (board.getTeam(TEAM_DEAD_NAME).hasPlayer(p)) {
					p.sendMessage(ChatColor.GRAY + "<" + player.getName() + "> " + message);
				}
			}
		}
	}

	/*
	 * キル数、残り人数をカウント
	 */
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		Player killer = player.getKiller();
		Scoreboard board = plugin.getServer().getScoreboardManager().getMainScoreboard();

		// 死亡後、DEADチームへ移行
		if (board.getTeam(TEAM_ALIVE_NAME).hasPlayer(player)) {
			board.getTeam(TEAM_ALIVE_NAME).removePlayer(player);
			board.getTeam(TEAM_DEAD_NAME).addPlayer(player);
		}

		// キル数をカウント
		if (killCount.containsKey(killer)) {
			killCount.put(killer, killCount.get(killer) + 1);
		} else {
			killCount.put(killer, 1);
		}

		// 最後の1人だった場合ポイントを加算
		if (board.getTeam(TEAM_ALIVE_NAME).getPlayers().size() == 1) {
			if (pointCount.containsKey(killer)) {
				pointCount.put(killer, pointCount.get(killer) + 1);
			} else {
				pointCount.put(killer, 1);
			}
		}
	}

	/*
	 * DEADチームは一切のダメージを受けないようにする
	 */
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			Scoreboard board = plugin.getServer().getScoreboardManager().getMainScoreboard();

			// DEADチームはダメージを受けないように
			if (board.getTeam(TEAM_DEAD_NAME).hasPlayer(player)) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onCick(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			Block block = event.getClickedBlock();
			Material material = block.getType();
			if (material.equals(Material.NOTE_BLOCK)) {
				if (StartPointCommand.adders.contains(player)) {
					MainConfig.addButton(block.getLocation(), player);
					StartPointCommand.adders.remove(player);
					return;
				}
				if (StartPointCommand.removers.contains(player)) {
					MainConfig.removeButton(block.getLocation(), player);
					StartPointCommand.removers.remove(player);
					return;
				}

				MainConfig.loadConfig();
				if (MainConfig.locations != null && MainConfig.locations.contains(block.getLocation())) {
					Random md = new Random();
					player.getEquipment().clear();
					player.getInventory().clear();
					PotionEffect p = new PotionEffect(PotionEffectType.SLOW, 10000, 10);
					player.addPotionEffect(p);
					// ItemStack h = new ItemStack(Material.MAGMA_CREAM, 64);
					// player.getInventory().addItem(h);
					int itemran = md.nextInt(4);
					if (itemran == 0) {
						ItemStack item = new ItemStack(Material.WOOD_SWORD);
						player.getInventory().addItem(item);
					} else if (itemran == 1) {
						ItemStack item = new ItemStack(Material.STONE_SWORD);
						player.getInventory().addItem(item);
					} else if (itemran == 2) {
						ItemStack item = new ItemStack(Material.GOLD_SWORD);
						player.getInventory().addItem(item);
					} else if (itemran == 3) {
						ItemStack item = new ItemStack(Material.IRON_SWORD);
						player.getInventory().addItem(item);
					} else {
						ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
						player.getInventory().addItem(item);
					}
					itemran = md.nextInt(4);
					if (itemran == 0) {
						ItemStack item = new ItemStack(Material.LEATHER_CHESTPLATE);
						player.getInventory().addItem(item);
					} else if (itemran == 1) {
						ItemStack item = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
						player.getInventory().addItem(item);
					} else if (itemran == 2) {
						ItemStack item = new ItemStack(Material.GOLD_CHESTPLATE);
						player.getInventory().addItem(item);
					} else if (itemran == 3) {
						ItemStack item = new ItemStack(Material.DIAMOND_CHESTPLATE);
						player.getInventory().addItem(item);
					} else {
						ItemStack item = new ItemStack(Material.IRON_CHESTPLATE);
						player.getInventory().addItem(item);
					}
				}
			}
		}
	}

	@EventHandler
	public void BeforeGame(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		/*
		 * if (c == 1){ itemhas = new ItemStack(Material.MAGMA_CREAM);
		 * player.getInventory().remove(itemhas); } else if(h == true){ Location
		 * l = player.getLocation(); player.teleport(l);
		 */
		Boolean t = player.hasPotionEffect(PotionEffectType.SLOW);
		if (t == true) {
			Location l = player.getLocation();
			player.teleport(l);
		}
	}

	// デバッグ用
	public void broadcast(String message) {
		BattleRoyale.broadcast(message);
	}

}
