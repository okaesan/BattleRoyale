package com.plugin.ftb.battleroyale.clubs.data;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.plugin.ftb.battleroyale.BattleRoyale;
import com.plugin.ftb.battleroyale.clubs.ClubManager;
import com.plugin.ftb.battleroyale.clubs.support.Homing;
import com.plugin.ftb.battleroyale.clubs.support.PSSystem;

public class Archery implements Listener {
	
	public static BattleRoyale plugin = BattleRoyale.plugin;
	private static final String name = "Archery"; 
	private boolean check = false;
	private int time = 0;
	private boolean homing = false;

	
	@EventHandler
	public void zoom(PlayerMoveEvent e){
		Player p = e.getPlayer();
		if(ClubManager.hasClub(p)){
			if(ClubManager.getClub(p) == ClubManager.getClub(name)){
				if(p.getInventory().getItemInMainHand().getType() == Material.BOW){
					if(e.getPlayer().isSneaking()){
						p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,50000,4));
					}
					else{
						p.removePotionEffect(PotionEffectType.SLOW);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void Bow(PlayerInteractEvent e){
		if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
			Player p = e.getPlayer();
			if(ClubManager.hasClub(p)){
				if(ClubManager.getClub(p) == ClubManager.getClub(name)){
					if(p.getInventory().getItemInMainHand().getType() == Material.BOW){
						time = 0;
						check =true;
						new BukkitRunnable() {
				            @Override
				            public void run() {
				            	if(check){
				            		
				            		time = time +1;
				            		if(time == 4){
				            			time = 0;
				            			homing = true;
				            			p.sendMessage(ChatColor.GOLD + "自動追尾弾");
				            			cancel();
				            		}
				            	}
				            	else{
				            		cancel();
				            	}
				            }
				        }.runTaskTimer(plugin, 0, 20);
						
					}
				}
			}
		}
	}
	
	
	@EventHandler
	public void Arrow(EntityShootBowEvent e){
		
		if(e.getEntity() instanceof Player){
			Player p = (Player)e.getEntity();
			if(ClubManager.hasClub(p)){
				if(ClubManager.getClub(p) == ClubManager.getClub(name)){
					PSSystem.ChangingSpeed(p, 4);
					if(homing){
						LivingEntity le = PSSystem.getTargetNearbyEntity(p);
						e.getProjectile().setGravity(false);
						if(le != null){
							new Homing((Projectile) e.getProjectile(), (LivingEntity)le, plugin);
						}
					}
					time = 0;
					check = false;
				}
			}
		}
	}
}
