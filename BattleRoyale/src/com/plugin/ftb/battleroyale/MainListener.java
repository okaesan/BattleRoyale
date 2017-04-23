package com.plugin.ftb.battleroyale;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;
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

	public static ArrayList<Integer> loc = new ArrayList<>();
	public static ArrayList<Integer> locB = new ArrayList<>();

	@SuppressWarnings("deprecation")
	@EventHandler
	public static void subChest(BlockBreakEvent e){
		Player _player = (Player)e.getPlayer();

		if(MainCommandExecutor.judEdit == 2&&_player.getInventory().getItemInHand().getType()==Material.BONE
				&&MainCommandExecutor.setChestPlayer.contains(_player)){
			MainConfig.subChestConfig(e.getBlock().getLocation(), _player);

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

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		Player killer = player.getKiller();
		Scoreboard board = plugin.getServer().getScoreboardManager().getMainScoreboard();

		loc = (ArrayList<Integer>) plugin.getConfig().getIntegerList("Deathpoint");
		Location wor = new Location(Bukkit.getWorld("world"),loc.get(0),loc.get(1),loc.get(2));
		player.teleport(wor);

		// 死亡後、DEADチームへ移行
		if (board.getTeam(TEAM_ALIVE_NAME).hasPlayer(player)) {
			board.getTeam(TEAM_ALIVE_NAME).removePlayer(player);
			board.getTeam(TEAM_DEAD_NAME).addPlayer(player);

			//死亡後、ドロップアイテムをチェストに保管
			Block block = player.getLocation().getBlock();
			block.setType(Material.CHEST);
			Chest chest = (Chest)block.getState();
			for(ItemStack itemStack : event.getDrops()){
				chest.getInventory().addItem(itemStack);
			}
			event.getDrops().clear();

			//プレイヤーの頭を置く
			block = player.getLocation().add(0, 1, 0).getBlock();
			block.setTypeIdAndData(Material.SKULL.getId(), (byte)1, true);
			Skull skull = (Skull)block.getState();
			skull.setSkullType(SkullType.PLAYER);
			skull.setOwningPlayer(player);
			skull.update();
		}

		if(killer != null){
			// キル数をカウント
			if (killCount.containsKey(killer)) {
				killCount.put(killer, killCount.get(killer) + 1);
			} else {
				killCount.put(killer, 1);
			}
			//ポイントをカウント
			if (pointCount.containsKey(killer)) {
				pointCount.put(killer, pointCount.get(killer) + 1);
			} else {
				pointCount.put(killer, 1);
			}
		}

		// 最後の1人だった場合5ポイントを加算
		if (board.getTeam(TEAM_ALIVE_NAME).getPlayers().size() == 1) {
			if(killer != null){
				if (pointCount.containsKey(killer)) {
					pointCount.put(killer, pointCount.get(killer) + 5);
				} else {
					pointCount.put(killer, 1);
				}
			}

			/*
			 * 終了時統計を表示
			 */
			//0ポイントのプレイヤーはデータがないので追加する
			for(Player p : Bukkit.getServer().getOnlinePlayers()){
				if(!pointCount.containsKey(p)){
					pointCount.put(p, 0);
					broadcast(pointCount.size() + "");
				}
				if(!killCount.containsKey(p)){
					killCount.put(p, 0);
				}
			}
			ArrayList<Player> pointRank = MainUtils.scoreSort(pointCount);
			broadcast(ChatColor.DARK_AQUA + "------------終了------------");
			int same = 0;//同率のプレイヤー用
			for(int i=0; i<pointRank.size(); i++){
				if(i >= 5)
					break;

				//前の順位と同じポイントだった場合同じ順位にする
				if(i >= 1){
					if(pointCount.get(pointRank.get(i-1)) == pointCount.get(pointRank.get(i))){
						same -= 1;
					}else{
						same = 0;
					}
				}
				int rank = i+1 + same;

				ChatColor color = ChatColor.WHITE;
				if(rank == 1)
					color = ChatColor.GOLD;
				if(rank == 2)
					color = ChatColor.YELLOW;
				if(rank == 3)
					color = ChatColor.GREEN;

				broadcast(" " + color + String.valueOf(rank) + "位 : " + pointRank.get(i).getName());
				broadcast(" " + ChatColor.RED + pointCount.get(pointRank.get(i)) + ChatColor.GRAY + " point, " +
						ChatColor.RED + killCount.get(pointRank.get(i)) + ChatColor.GRAY + " kill");
			}
			broadcast(ChatColor.DARK_AQUA + "-----------------------------");

			/*
			 * ゲーム終了後、全員をロビーに戻す
			 * 看板の値をリセット
			 */
			locB = (ArrayList<Integer>) plugin.getConfig().getIntegerList("Lobbypoint");
			Location worB = new Location(Bukkit.getWorld("world"),loc.get(0),loc.get(1),loc.get(2));

			OfflinePlayer WinP = (OfflinePlayer) board.getTeam(TEAM_ALIVE_NAME).getPlayers();
			WinP.getPlayer().teleport(worB);
			board.getTeam(TEAM_ALIVE_NAME).removePlayer(killer);
			for(OfflinePlayer p : board.getTeam(TEAM_DEAD_NAME).getPlayers()){
				p.getPlayer().teleport(worB);
				board.getTeam(TEAM_DEAD_NAME).removePlayer(p);
			}
			int locX = plugin.getConfig().getInt("SignValue.x");
			int locY = plugin.getConfig().getInt("SignValue.y");
			int locZ = plugin.getConfig().getInt("SignValue.z");

			Block b = Bukkit.getWorld("world").getBlockAt(locX, locY, locZ);

			if (b.getType()==Material.WALL_SIGN || b.getType()==Material.SIGN_POST) {

				Sign ee = (Sign) b.getState();

	        	ee.setLine(1, ChatColor.GRAY + String.valueOf(plugin.getServer().getScoreboardManager().getMainScoreboard().getTeam(TEAM_ALIVE_NAME).getPlayers().size() + "/" + 50));

	        	ee.update();

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

	@SuppressWarnings("deprecation")
	@EventHandler(ignoreCancelled = true)
	public void onCick(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Scoreboard board = plugin.getServer().getScoreboardManager().getMainScoreboard();

		/*
		 * 音符ブロックをクリックしたとき、装備を付与し、ゲームに参加させる
		 */
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

				/*
				 * PlayerをAliveチームに登録して参加
				 */
				if (!board.getTeam(TEAM_ALIVE_NAME).hasPlayer(player)) {
					board.getTeam(TEAM_ALIVE_NAME).addPlayer(player);
				}

				/*
				 * ランダムで装備品を付与
				 */
				MainConfig.loadConfig();
				if (MainConfig.locations != null && MainConfig.locations.contains(block.getLocation())) {
					Random md = new Random();
					player.getEquipment().clear();
					player.getInventory().clear();
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

	/*
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onMap(MapInitializeEvent event){
		MapView map = event.getMap();
		map.addRenderer(new CustomMap());
		}
		*/

	// ブロードキャスト
	public void broadcast(String message) {
		BattleRoyale.broadcast(message);
	}

}
