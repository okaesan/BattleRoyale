package com.plugin.ftb.battleroyale.clubs.data;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.plugin.ftb.battleroyale.clubs.ClubManager;


public class Effects implements Listener {
	
	@EventHandler
	public void move(PlayerMoveEvent e){
		
		Player p = e.getPlayer();
		if(ClubManager.hasClub(p)){
			if(ClubManager.getClub(p) == ClubManager.getClub("BasketBall") || ClubManager.getClub(p) == ClubManager.getClub("VolleyBall")){
				p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP , 50000, 1));
			}
			else if(ClubManager.getClub(p) == ClubManager.getClub("Soccer")){
				p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP , 50000, 0));
			}
			else{
				p.removePotionEffect(PotionEffectType.JUMP);
			}
			if(ClubManager.getClub(p) == ClubManager.getClub("Karate")){
				p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE , 50000, 0));
			}
			else{
				p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
			}
			if(ClubManager.getClub(p) == ClubManager.getClub("Swimming")){
				if(p.getLocation().getBlock().isLiquid()){
					p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED , 50000, 2));
					p.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING , 50000, 0));
				}
				else{
					p.removePotionEffect(PotionEffectType.SPEED);
					p.removePotionEffect(PotionEffectType.WATER_BREATHING);
				}
			}
			else if(ClubManager.getClub(p) != ClubManager.getClub("TrackAndField")){
				p.removePotionEffect(PotionEffectType.SPEED);
				p.removePotionEffect(PotionEffectType.WATER_BREATHING);
			}
			if(ClubManager.getClub(p) == ClubManager.getClub("TrackAndField")){
				if(!p.getLocation().getBlock().isLiquid()){
					p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED , 50000, 2));
				}
				else{
					p.removePotionEffect(PotionEffectType.SPEED);
				}
			}
			else if(ClubManager.getClub(p) == ClubManager.getClub("Soccer")){
				p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED , 50000, 0));
			}
			else if(ClubManager.getClub(p) != ClubManager.getClub("Swimming") && ClubManager.getClub(p) != ClubManager.getClub("Soccer")){
				p.removePotionEffect(PotionEffectType.SPEED);
			}
		}
	}
}
