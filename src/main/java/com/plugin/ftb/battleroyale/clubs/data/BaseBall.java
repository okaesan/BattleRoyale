package com.plugin.ftb.battleroyale.clubs.data;

import org.bukkit.Material;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.plugin.ftb.battleroyale.BattleRoyale;
import com.plugin.ftb.battleroyale.clubs.ClubManager;

public class BaseBall implements Listener {
	
	public static BattleRoyale plugin = BattleRoyale.plugin;
	private static final String name = "BaseBall"; 
	private String data;

	@EventHandler
	public void Ball(PlayerInteractEvent e){
		
		if(e.getAction().equals(Action.RIGHT_CLICK_AIR) ||e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
			Player p = e.getPlayer();
			if(ClubManager.hasClub(p)){
				if(ClubManager.getClub(p) == ClubManager.getClub(name)){
					if(p.getInventory().getItemInMainHand().getType() == Material.SNOW_BALL){
						data = name;
					}
				}
			}
		}
	}
	
	@EventHandler
	public void BallLunch(ProjectileLaunchEvent e){
		if(e.getEntityType() == EntityType.SNOWBALL){
			if(name.equals(data)){
				
				Projectile p = (Projectile)e.getEntity();
			    
				p.setVelocity(p.getVelocity().multiply(4));
			    data = null;
			    p.setCustomName(name);
			}
		}
	}
	
	@EventHandler
	public void Hit(ProjectileHitEvent e){
		if(name.equals(e.getEntity().getCustomName())){
			if(e.getHitEntity() instanceof Creature){
				Creature c = (Creature)e.getHitEntity();
				c.damage(2);
			}
			else if(e.getHitEntity() instanceof Player){
				Player c = (Player)e.getHitEntity();
				c.damage(2);
			}
		}
	}
}