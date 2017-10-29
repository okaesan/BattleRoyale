package com.plugin.ftb.battleroyale.clubs.data;

import org.bukkit.Material;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.plugin.ftb.battleroyale.BattleRoyale;
import com.plugin.ftb.battleroyale.clubs.ClubManager;

public class Karate implements Listener {
	
	public static BattleRoyale plugin = BattleRoyale.plugin;
	private static final String name = "Karate"; 

	@EventHandler
	public void Damage(EntityDamageByEntityEvent e){
		if(e.getDamager() instanceof Player){
			Player p = (Player)e.getDamager();
			if(ClubManager.hasClub(p)){
				if(ClubManager.getClub(p) == ClubManager.getClub(name)){
					Creature c = (Creature)e.getEntity();
					if(p.getInventory().getItemInMainHand().getType() == Material.AIR){
						c.damage(e.getDamage() + 3);
					}
					else{
						c.damage(e.getDamage() + 2);
					}
					e.setCancelled(true);
				}
			}
		}
	}
}