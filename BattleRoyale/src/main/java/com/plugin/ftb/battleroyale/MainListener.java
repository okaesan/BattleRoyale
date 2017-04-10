package com.plugin.ftb.battleroyale;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.WorldBorder;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scoreboard.Scoreboard;

import net.minecraft.server.v1_10_R1.EntityPlayer;
import net.minecraft.server.v1_10_R1.PacketPlayOutNamedEntitySpawn;

public class MainListener implements Listener {
	
	public static BattleRoyale plugin = BattleRoyale.plugin;
	
	public static final String TEAM_ALIVE_NAME = BattleRoyale.TEAM_ALIVE_NAME;
    	public static final String TEAM_DEAD_NAME = BattleRoyale.TEAM_DEAD_NAME;
    
    	//キル数カウント
    	public static HashMap<Player, Integer> killCount = BattleRoyale.killCount;
	//ポイントカウント
	public static HashMap<Player, Integer> pointCount = BattleRoyale.pointCount;
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event){
		String message = event.getMessage();
		Player player = event.getPlayer();
		Scoreboard board = plugin.getServer().getScoreboardManager().getMainScoreboard();
		
		//DEADチームのみに送信
		if(board.getTeam(TEAM_DEAD_NAME).hasPlayer(player)){
			event.setCancelled(true);
			for(Player p:plugin.getServer().getOnlinePlayers()){
				if(board.getTeam(TEAM_DEAD_NAME).hasPlayer(p)){
					p.sendMessage("<" + player.getName() + "> " + message);
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onDeath(PlayerDeathEvent event){
		Player player = event.getEntity();
		Player killer = player.getKiller();
		Scoreboard board = plugin.getServer().getScoreboardManager().getMainScoreboard();
		
		//死亡後、DEADチームへ移行
		if(board.getTeam(TEAM_ALIVE_NAME).hasPlayer(player)){
			board.getTeam(TEAM_ALIVE_NAME).removePlayer(player);
			board.getTeam(TEAM_DEAD_NAME).addPlayer(player);
		}
		
		//キル数をカウント
		if(killCount.containsKey(killer)){
			killCount.put(killer, killCount.get(killer) + 1);
		}else{
			killCount.put(killer, 1);
		}
		
		//最後の1人だった場合ポイントを加算
		if(board.getTeam(TEAM_ALIVE_NAME).getPlayers().size() == 1){
			if(pointCount.containsKey(killer)){
				pointCount.put(killer, pointCount.get(killer) + 1);
			}else{
				pointCount.put(killer, 1);
			}
		}
	}

	@EventHandler
	public void onDamage(EntityDamageEvent event){
		if(event.getEntity() instanceof Player){
			Player player = (Player)event.getEntity();
			Scoreboard board = plugin.getServer().getScoreboardManager().getMainScoreboard();
			
			//DEADチームはダメージを受けないように
			if(board.getTeam(TEAM_DEAD_NAME).hasPlayer(player)){
				event.setCancelled(true);
			}
		}
	}
	//デバッグ用
	public void broadcast(String message){
		BattleRoyale.broadcast(message);
	}
}
