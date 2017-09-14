package com.plugin.ftb.battleroyale;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
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
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;

class RunTP extends BukkitRunnable{

	public static BattleRoyale plugin = BattleRoyale.plugin;

	// チーム名
	public static final String TEAM_ALIVE_NAME = BattleRoyale.TEAM_ALIVE_NAME;
	public static final String TEAM_DEAD_NAME = BattleRoyale.TEAM_DEAD_NAME;

	//ロビーへテレポート用
	public static ArrayList<Integer> locB = new ArrayList<>();

	int locX = plugin.getConfig().getInt("SignValue.x");
	int locY = plugin.getConfig().getInt("SignValue.y");
	int locZ = plugin.getConfig().getInt("SignValue.z");

	//接頭語
	public static String prefix = BattleRoyale.prefix;

	@SuppressWarnings("deprecation")
	public void run(){
		Scoreboard board = plugin.getServer().getScoreboardManager().getMainScoreboard();

		locB = (ArrayList<Integer>) plugin.getConfig().getIntegerList("Lobbypoint");
		///////loc.get()になってました。
		Location worB = new Location(Bukkit.getWorld("world"),locB.get(0),locB.get(1),locB.get(2));

		for(Player p : Bukkit.getOnlinePlayers()){
			if(board.getTeam(TEAM_ALIVE_NAME).hasPlayer(p)){

				p.getPlayer().teleport(worB);
  				p.setHealth(20);
  				p.setFoodLevel(20);
  				for (PotionEffect effect : p.getActivePotionEffects()) {
  			        p.removePotionEffect(effect.getType());
				}
  				p.getInventory().clear();
  				board.getTeam(TEAM_ALIVE_NAME).removePlayer(p);

  			}else if(board.getTeam(TEAM_DEAD_NAME).hasPlayer(p)){

  				p.getPlayer().teleport(worB);
  				p.setHealth(20);
  				p.setFoodLevel(20);
  				for (PotionEffect effect : p.getActivePotionEffects()) {
  			        p.removePotionEffect(effect.getType());
				}
 				p.getInventory().clear();
  				board.getTeam(TEAM_DEAD_NAME).removePlayer(p);
			}
		}

		Block block = Bukkit.getWorld("world").getBlockAt(locX, locY, locZ);

		//参加用の看板の値の更新 (参加人数0に戻す)
		if (block.getType()==Material.WALL_SIGN || block.getType()==Material.SIGN_POST) {
			Sign joinSign = (Sign) block.getState();
			joinSign.setLine(1, ChatColor.BOLD + String.valueOf(plugin.getServer().getScoreboardManager().getMainScoreboard().getTeam(TEAM_ALIVE_NAME).getPlayers().size() + "/" + 50));
			joinSign.update();
		}

		//ゲーム中に破壊されたブロックの復元
		MainListener.blockReset=true;
		for(int i=0;i<MainListener.bBLOCK.size();i++){
			MainListener.bBLOCK.get(i).setType(MainListener.bMAT.get(i));
			MainListener.bBLOCK.get(i).setData(MainListener.bDATA.get(i));
		}
		MainListener.blockReset=false;

		resetVar();

		this.cancel();
	}

	//毎試合の変数の初期化
	public void resetVar(){
		PlusThreadClass.count=0;
		PlusThreadClass.countPast=0;
		PlusDeathArea.beta=0;
		StartCommand.start=0;
		StartCommand.gameTimer=0;
		countDown.attackCountDown=plugin.getConfig().getInt("NATimer");
		PlusThreadClass.loopC=plugin.getConfig().getIntegerList("Timer").get(0);
		PlusThreadClass.deathRandomCount.clear();
		PlusThreadClass.deathRandomCountPast.clear();
		PlusDeathArea.plusDeathX.clear();
		PlusDeathArea.plusDeathZ.clear();
		CustomMap.pastLoc.clear();
		CustomMap.pastLocP.clear();
		MainListener.bBLOCK.clear();
		MainListener.bDATA.clear();
		MainListener.bMAT.clear();
		MainListener.killCount.clear();
		MainListener.deathPlayer.clear();
		MainListener.deathTime.clear();

		ScoreBoard.scoreSide(false);
		ScoreBoard.scoreList(null, false);

		Bukkit.getScheduler().cancelAllTasks();

		return;
	}
}


public class MainListener implements Listener {

	public static BattleRoyale plugin = BattleRoyale.plugin;

	// チーム名
	public static final String TEAM_ALIVE_NAME = BattleRoyale.TEAM_ALIVE_NAME;
	public static final String TEAM_DEAD_NAME = BattleRoyale.TEAM_DEAD_NAME;

	// キル数カウント
	public static HashMap<UUID, Integer> killCount = BattleRoyale.killCount;

	//ランクがソートされた値を受け取る
	public static Map<Integer, Integer> rankSort = BattleRoyale.rankSort;

	//ダメージ無効かの判定用
	public static boolean Attack = true;
	//ブロックを復元している時はtrue(resetVar関数にて変更し、onDrop関数にて使用される)
	public static boolean blockReset = false;

	/*
	 * ゲーム中に破壊されたブロックの値保存用のリスト
	 * Javaでの構造体の書き方を理解できたら構造体に変更しますm(_ _)m
	 */
	public static ArrayList<Block> bBLOCK = new ArrayList<>();
	public static ArrayList<Byte> bDATA = new ArrayList<>();
	public static ArrayList<Material> bMAT = new ArrayList<>();
	//死亡後、リスポーン地点へテレポート用
	public static ArrayList<Integer> locDeath = new ArrayList<>();
	//死亡順に並べる用
	public static ArrayList<Integer> deathTime = new ArrayList<>();
	public static ArrayList<UUID> deathPlayer = new ArrayList<>();

	@SuppressWarnings("deprecation")
	@EventHandler
	public static void onBlockBreak(BlockBreakEvent e){
		Player _player = (Player)e.getPlayer();
		Scoreboard board = plugin.getServer().getScoreboardManager().getMainScoreboard();

		/*
		 * ゲーム中にアイテムが入るチェストの登録
		 * setChestPlayerはチェストを編集する人のデータが入ったリスト
		 */
		if(_player.getInventory().getItemInHand().getType()==Material.BONE && MainCommandExecutor.setChestPlayer.contains(_player)){
			MainConfig.subChestConfig(e.getBlock().getLocation(), _player);
		}

		/*
		 * 破壊できないブロックの登録
		 * 登録するコマンドを打ったプレイヤーがこのメソッドを通る
		 */
		if(_player.getItemInHand().getType().equals(Material.STICK) && MainCommandExecutor.setProtectionPlayer.contains(_player)){
			MainConfig.addProtectedBlocks(e.getBlock().getLocation(), _player);
			e.setCancelled(true);
		}


		/*
		 * ゲーム中に破壊されたブロックの情報を取得して保存
		 */
		//ゲーム中で、かつブロックを破壊した人がゲームに参加していて、生存者だった場合
		if(StartCommand.start==1 && board.getTeam(TEAM_ALIVE_NAME).hasPlayer(_player)){

			//破壊できないガラスに登録されていた場合、キャンセルする
			if(MainConfig._protectedBlocks.contains(e.getBlock().getLocation())) {
				e.setCancelled(true);
			}
			//破壊されたブロックがガラス、ガラス板、色付きガラス、色付きガラス板だった場合はその場所の値と壊されたブロックの種類、データ値を保存しておく。
			else if(e.getBlock().getType()==Material.GLASS
					|| e.getBlock().getType()==Material.STAINED_GLASS
					|| e.getBlock().getType()==Material.STAINED_GLASS_PANE
					|| e.getBlock().getType()==Material.THIN_GLASS){

				//値保存
				bBLOCK.add(Bukkit.getWorld("world").getBlockAt(e.getBlock().getX(), e.getBlock().getY(), e.getBlock().getZ()));
				bDATA.add(e.getBlock().getData());
				bMAT.add(e.getBlock().getType());

			}
			//破壊されたブロックが背の高い草花だった場合、setCancelledではデータ値が変わって違うものに置き換わるので、データ値の設定をする。
			else if(e.getBlock().getType() == Material.DOUBLE_PLANT){
				if(e.getBlock().getData() == 0 ||e.getBlock().getData() == 1||e.getBlock().getData() == 2
						||e.getBlock().getData() == 3||e.getBlock().getData() == 4||e.getBlock().getData() == 5){
					Location l = e.getBlock().getLocation().add(0,1,0);
					l.getBlock().setType(Material.DOUBLE_PLANT);
					l.getBlock().setData((byte) 10);
				}
			}
			//その他のブロックは破壊不可
			else{
				e.setCancelled(true);
			}
		}

		//死者は全ブロックを破壊不可能にする。
		if (board.getTeam(TEAM_DEAD_NAME).hasPlayer(_player)) {
			e.setCancelled(true);
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
	public void onRespawn(PlayerRespawnEvent e){
		Scoreboard board = plugin.getServer().getScoreboardManager().getMainScoreboard();
		locDeath = (ArrayList<Integer>) plugin.getConfig().getIntegerList("Deathpoint");
		Location worDeath = new Location(Bukkit.getWorld("world"),locDeath.get(0),locDeath.get(1),locDeath.get(2));

		if(board.getTeam(TEAM_DEAD_NAME).hasPlayer(e.getPlayer())||board.getTeam(TEAM_ALIVE_NAME).hasPlayer(e.getPlayer())){
			e.setRespawnLocation(worDeath);
			//e.getPlayer().getInventory().clear();
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {

		Player player = event.getEntity();
		Player killer = player.getKiller();
		Location loc = player.getLocation();
		Scoreboard board = plugin.getServer().getScoreboardManager().getMainScoreboard();

		// 死亡後、DEADチームへ移行
		if (board.getTeam(TEAM_ALIVE_NAME).hasPlayer(player)) {
			//死亡した場合、死亡時の時刻と死亡者を保存する
			deathTime.add(StartCommand.gameTimer);
			deathPlayer.add(player.getUniqueId());

			board.getTeam(TEAM_ALIVE_NAME).removePlayer(player);
			board.getTeam(TEAM_DEAD_NAME).addPlayer(player);

			//チェストとプレイヤーの頭に置き換わる前に存在していたブロックの値の保存
			bBLOCK.add(player.getLocation().getBlock());
			bDATA.add(player.getLocation().getBlock().getData());
			bMAT.add(player.getLocation().getBlock().getType());
			//頭
			loc.add(0, 1, 0);
			bBLOCK.add(loc.getBlock());
			bDATA.add(loc.getBlock().getData());
			bMAT.add(loc.getBlock().getType());
			loc.add(0, -1, 0);

			//死亡後、ドロップアイテムをチェストに保管
			Block block = loc.getBlock();
			block.setType(Material.CHEST);
			Chest chest = (Chest)block.getState();
			//ItemStack[] itemStacks = new ItemStack[event.getDrops().toArray().clone().length];
			//event.getDrops().toArray(itemStacks);
			for(ItemStack itemStack : event.getDrops()){
				chest.getInventory().addItem(itemStack);
			}
			event.getDrops().clear();

			//プレイヤーの頭を置く
			block = loc.add(0, 1, 0).getBlock();
			block.setTypeIdAndData(Material.SKULL.getId(), (byte)1, true);
			Skull skull = (Skull)block.getState();
			skull.setSkullType(SkullType.PLAYER);
			skull.setOwningPlayer(player);
			skull.update();
		}

		if(killer != null){
			// キル数をカウント
			if (killCount.containsKey(killer.getUniqueId())) {
				killCount.put(killer.getUniqueId(), killCount.get(killer.getUniqueId()) + 1);
				//スコアボードのキル数表示の変更
				ScoreBoard.scoreList(killer, true);
			}
		}

		//残り人数が10,5,3人の時に残ってる人のIDを表示する
		if(board.getTeam(TEAM_ALIVE_NAME).getPlayers().size() == 10
				|| board.getTeam(TEAM_ALIVE_NAME).getPlayers().size() == 5
				|| board.getTeam(TEAM_ALIVE_NAME).getPlayers().size() == 3){

			for(OfflinePlayer offPlayer : board.getTeam(TEAM_ALIVE_NAME).getPlayers()){
				if(offPlayer.isOnline()){
					Player onlinePlayer = (Player) offPlayer;
					onlinePlayer.sendMessage("-" + ChatColor.DARK_AQUA + "残りの生存者" + ChatColor.RESET + "-");
				}
			}
			for(OfflinePlayer offPlayer : board.getTeam(TEAM_ALIVE_NAME).getPlayers()){
				if(offPlayer.isOnline()){
					Player onlinePlayer = (Player) offPlayer;
					onlinePlayer.sendMessage("-" + ChatColor.DARK_AQUA + "残りの生存者" + ChatColor.RESET + "-");
				}
			}

			for(OfflinePlayer alivePlayer : board.getTeam(TEAM_ALIVE_NAME).getPlayers()){
				for(OfflinePlayer offPlayer : board.getTeam(TEAM_ALIVE_NAME).getPlayers()){
					if(offPlayer.isOnline()){
						Player onlinePlayer = (Player) offPlayer;
						onlinePlayer.sendMessage(alivePlayer.getName() + " - " + killCount.get(alivePlayer.getUniqueId()) + "キル");
					}
				}
				for(OfflinePlayer offPlayer : board.getTeam(TEAM_ALIVE_NAME).getPlayers()){
					if(offPlayer.isOnline()){
						Player onlinePlayer = (Player) offPlayer;
						onlinePlayer.sendMessage(alivePlayer.getName() + " - " + killCount.get(alivePlayer.getUniqueId()) + "キル");
					}
				}
			}
		}

		//最後の一人の場合はゲームを終了させる
		if (board.getTeam(TEAM_ALIVE_NAME).getPlayers().size() == 1 && StartCommand.start == 1) {

			//一位は一度も死なないため、ここでランキングを設定する
			if(killer!=null){
				//処理的に死亡者と同じ時間になるため、gameTimerに+1し、重複をなくす。
				if(!deathPlayer.contains(killer.getUniqueId())) {
					deathTime.add(StartCommand.gameTimer+1);
					deathPlayer.add(killer.getUniqueId());
				}
			}
			//禁止区域などで同時に死亡し、ゲームがフィニッシュした場合(キルした人が存在しない場合)
			else{
				for(OfflinePlayer checkPlayer : board.getTeam(TEAM_ALIVE_NAME).getPlayers()){
					if(checkPlayer.isOnline()){
						if(MainUtils.lastPlayer((Player) checkPlayer)){
							if(!deathPlayer.contains(checkPlayer.getUniqueId())) {
								//処理的に死亡者と同じ時間になるため、gameTimerに+1し、重複をなくす。
								deathTime.add(StartCommand.gameTimer+1);
								deathPlayer.add(checkPlayer.getUniqueId());
							}
						}
						else{
							if(!deathPlayer.contains(checkPlayer.getUniqueId())) {
								//死亡した場合、死亡時の時刻と死亡者を保存する
								deathTime.add(StartCommand.gameTimer);
								deathPlayer.add(checkPlayer.getUniqueId());
							}
						}
					}
				}
			}

			//ランク上位3位までを抽出
			rankSort = MainUtils.rankSort(deathTime);
			rankSort.entrySet().stream().sorted(Entry.comparingByValue());

			new BukkitRunnable() {

	            @Override
	            public void run() {
	            	/*
	    			 * 終了時統計を表示
	    			 */
	    			broadcast(ChatColor.DARK_AQUA + "------------終了------------");
	    			ArrayList<ArrayList<String>> rankStrings = new ArrayList<>();
	    			//仮の要素を挿入
	    			rankStrings.add(new ArrayList<String>());
	    			rankStrings.add(new ArrayList<String>());
	    			rankStrings.add(new ArrayList<String>());

	    			for(int i : rankSort.keySet()){
	    				ChatColor color = ChatColor.WHITE;
	    				int rank = 0;
	    				if(rankSort.get(i) == 1) {
	    					color = ChatColor.GOLD;
	    					rank = 0;
	    				}
	    				if(rankSort.get(i) == 2) {
	    					color = ChatColor.YELLOW;
	    					rank = 1;
	    				}
	    				if(rankSort.get(i) == 3) {
	    					color = ChatColor.GREEN;
	    					rank = 2;
	    				}

	    				//順位に応じた場所に入れる。
	    				ArrayList<String> value;
	    				if(rankStrings.get(rank).isEmpty()) {
	    					//同率プレイヤーがいない場合
	    					value = new ArrayList<>();
	    				}else{
	    					//同率プレイヤーがいた場合
	    					value = rankStrings.get(rank);
	    				}
	    				value.add((" " + color + String.valueOf(rankSort.get(i)) + "位 : " + Bukkit.getPlayer(deathPlayer.get(i)).getName() + "\n"
	    						+ " " + ChatColor.RED + killCount.get(deathPlayer.get(i)) + ChatColor.GRAY + " kill"));

	    				sendLog("deathPlayer(i):" + deathPlayer.get(i) + "killCount.get(deathPlayer):" + killCount.get(deathPlayer.get(i))
	    						+ "\n" + "deathPlayer:" + deathPlayer + "killCount:" + killCount);
	    				rankStrings.set(rank, value);
	    			}

	    			//表示
	    			for(ArrayList<String> values : rankStrings) {
	    				for(String value : values) {
	    					broadcast(value);
	    				}
	    			}

	    			broadcast(ChatColor.DARK_AQUA + "-----------------------------");
	            }
	        }.runTaskLater(plugin, 20);


			//エフェクト削除、op以外をadvに
			for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
				if(onlinePlayer.getPotionEffect(PotionEffectType.GLOWING) != null && onlinePlayer.getPotionEffect(PotionEffectType.GLOWING).getAmplifier() > 0) {
					//エフェクト削除
					onlinePlayer.removePotionEffect(PotionEffectType.GLOWING);
				}

				if(!onlinePlayer.hasPermission("battleroyale.op")) {
					onlinePlayer.setGameMode(GameMode.ADVENTURE);
				}
			}

			for(Player p : Bukkit.getOnlinePlayers()){
				if(board.getTeam(TEAM_ALIVE_NAME).hasPlayer(p)){
					p.sendMessage(BattleRoyale.prefix+"10秒後にロビーへ戻ります");
				}else if(board.getTeam(TEAM_DEAD_NAME).hasPlayer(p)){
					p.sendMessage(BattleRoyale.prefix+"10秒後にロビーへ戻ります");
				}
			}
			new RunTP().runTaskLater(plugin, 200);
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

			//ダメージ無効時間中はダメージを受けないようにする。
			if(Attack){
				event.setCancelled(true);
			}

			if(StartCommand.start == 0) {
				event.setCancelled(true);
			}
		}
	}

	//畑荒らし防止
	@EventHandler(ignoreCancelled=true)
	public void onEntityInteractEvent(EntityInteractEvent event){
		if (event.getBlock().getType().equals(Material.SOIL)) {
			event.setCancelled(true);
		}
	}
	@SuppressWarnings("deprecation")
	@EventHandler(ignoreCancelled=true)
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Scoreboard board = plugin.getServer().getScoreboardManager().getMainScoreboard();

		if (event.getAction().equals(Action.PHYSICAL) && event.hasBlock()
			&& event.getClickedBlock().getType().equals(Material.SOIL) && board.getTeam(TEAM_ALIVE_NAME).hasPlayer(player)) {
			event.setCancelled(true);
		}
	}

	//チェストの初期化が行われたときドロップしたアイテムを削除する
	@EventHandler
	public void onDrop(ItemSpawnEvent e){
		if(blockReset==true){
			e.getEntity().remove();
		}
	}

	//壁紙、手綱、額縁を破壊不可にする
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onBreak(HangingBreakByEntityEvent e){
		Player player = (Player) e.getRemover();
		Scoreboard board = plugin.getServer().getScoreboardManager().getMainScoreboard();

		if(board.getTeam(TEAM_ALIVE_NAME).hasPlayer(player)){
			e.setCancelled(true);
		}
	}

	//ゲーム中にログアウトしたプレイヤーはロビーに転送
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		if(StartCommand.start == 1) {
			Player player = event.getPlayer();
			Scoreboard board = plugin.getServer().getScoreboardManager().getMainScoreboard();
			if(board.getTeam(TEAM_ALIVE_NAME).hasPlayer(player)) {
				Location loc = new Location(Bukkit.getWorld("world"), MainConfig._lobbypoint.get(0) ,  MainConfig._lobbypoint.get(1),  MainConfig._lobbypoint.get(2));
				player.teleport(loc);
				board.getTeam(TEAM_ALIVE_NAME).removePlayer(player);
			}
		}
	}

	//スタート前はブロックの破壊を禁止
	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		if(StartCommand.start == 0) {
			Player player = event.getPlayer();
			if(player.getGameMode() != GameMode.CREATIVE || player.getGameMode() != GameMode.SPECTATOR) {
				event.setCancelled(true);
			}
		}
	}

	//開始前はアドベンチャーモードに
	@EventHandler
	public void onLogin(PlayerLoginEvent event) {
		Player player = event.getPlayer();
		if(!player.hasPermission("battleroyale.op")) {
			event.getPlayer().setGameMode(GameMode.ADVENTURE);
		}
	}

	/*
	//HP減ったらプレイヤーのスピードを下げる
    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if(e.isCancelled()) {
            return;
        }
        Player p = e.getPlayer();

        int hpint = (int) Math.floor(p.getHealth());

        double exchange = 1.0;

        switch (hpint) {
        case 10:
            exchange = 0.95;
        break;

        case 9:
            exchange = 0.9;
            break;

        case 8:
            exchange = 0.85;
            break;

        case 7:
            exchange = 0.8;
        break;

        case 6:
            exchange = 0.75;
            break;

        case 5:
            exchange = 0.7;
            break;

        case 4:
            exchange = 0.65;
            break;

        case 3:
            exchange = 0.6;
            break;

        case 2:
            exchange = 0.55;
        break;

        case 1:
            exchange = 0.5;
            break;

        case 0:
            exchange = 0.0;
            break;

        default:
            p.setWalkSpeed(0.2f);
            break;
        }

        if(hpint == 20) {
            p.setWalkSpeed(0.2f);
        }

        double f = 0.2 * exchange;
        float ps = p.getWalkSpeed();
        	if(!(f == ps)) {
            	p.setWalkSpeed((float) f);
        	}
    	}
*/

	// ブロードキャスト
	public void broadcast(String message) {
		BattleRoyale.broadcast(message);
	}

	//サーバーにログを残す
	public void sendLog(String message) {
		Bukkit.getLogger().info(message);
	}
}
