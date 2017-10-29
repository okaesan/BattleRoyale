package com.plugin.ftb.battleroyale.clubs.support;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class ManyArrows extends BukkitRunnable{
	
	Vector v;
	Location l;
	Player pl;
	int a = 0;
	Plugin plugin;
	
	public ManyArrows(Vector v,Location l,Player p,Plugin plugin)
	{
		this.v = v;
		this.l = l;
		pl = p;
		this.plugin = plugin;
		
		runTaskTimer(plugin, 1L, 1L);
	}

	public void run(){
		Random r = new Random();
		Location loc = new Location(l.getWorld(),l.getX() + r.nextDouble() + r.nextInt(2) ,l.getY() + r.nextDouble() + r.nextInt(2),l.getZ() + r.nextDouble()+ r.nextInt(2));
		
		Projectile ar = loc.getWorld().spawnArrow(loc, v, 0.6F, 20);
		ar.setGravity(false);
		ar.setCustomName("メリー");
		LivingEntity le = PSSystem.getTargetNearbyEntity(pl);
		
		if(le != null){
			new Homing((org.bukkit.entity.Arrow) ar, (LivingEntity)le, plugin);
		}
		
		a = a + 1;
		if(a == 9*9*9){
			a = 0;
			cancel();
		}
		
	}

}
