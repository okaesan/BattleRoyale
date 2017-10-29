package com.plugin.ftb.battleroyale.clubs.support;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import com.plugin.ftb.battleroyale.BattleRoyale;

public class PSSystem extends BattleRoyale{
	
	private static List<Player> list = new ArrayList<Player>();
	private static List<Monster> listM = new ArrayList<Monster>();
	private static List<Animals> listA = new ArrayList<Animals>();
	private static List<LivingEntity> listE = new ArrayList<LivingEntity>();
	
	/**
	 * 対象のスピードを変更します。
	 */
	public static Entity ChangingSpeed(Entity entity, double multiplier){
		
		Vector v = new Vector(entity.getVelocity().getX() * multiplier ,entity.getVelocity().getY() * multiplier ,entity.getVelocity().getZ() * multiplier);
		entity.setVelocity(v);
		
		return entity;
	}
	
	public static Player getNearbyPlayer(Location location ,int distance){
		
		int counter = distance;
		
		list.clear();
		
		while( counter != 0){
			for(Player p :Bukkit.getOnlinePlayers()){
				if(location.getWorld() == p.getWorld()){
					if(p.getLocation().distanceSquared(location) <= counter){
						list.add(p);
					}
				}
			}
			if(list.size() == 1){
				return list.get(0);
			}
			else if(list.size() == 0){
				for(Player p :Bukkit.getOnlinePlayers()){
					if(location.getWorld() == p.getWorld()){
						if(p.getLocation().distanceSquared(location) <= (counter+1)){
							return p;
						}
					}
				}
			}
			
			list.clear();
			counter--;
		}
		return null;
	}
	
	public static Player getNearbyPlayer(Location location ,int distance ,Player player){
		
		int counter = distance;
		
		list.clear();
		
		while( counter != 0){
			for(Player p :Bukkit.getOnlinePlayers()){
				if(location.getWorld() == p.getWorld()){
					if(p.getLocation().distanceSquared(location) <= counter){
						list.add(p);
					}
				}
			}
			if(list.size() == 1){
				return list.get(0);
			}
			else if(list.size() == 0){
				for(Player p :Bukkit.getOnlinePlayers()){
					if(location.getWorld() == p.getWorld()){
						if(p.getLocation().distanceSquared(location) <= (counter+1)){
							return p;
						}
					}
				}
			}
			
			list.clear();
			counter--;
		}
		return null;
	}
	
	public static Monster getNearbyMonster(Location location ,int distance){
		
		int counter = distance;
		
		listM.clear();
		
		while( counter != 0){
			for(Entity e :location.getWorld().getEntities()){
				if(e instanceof Monster){
					Monster p = (Monster)e;
					if(p.getLocation().distanceSquared(location) <= counter){
						listM.add(p);
					}
				}
			}
			if(listM.size() == 1){
				return listM.get(0);
			}
			else if(listM.size() == 0){
				for(Entity e :location.getWorld().getEntities()){
					if(e instanceof Monster){
						Monster p = (Monster)e;
						if(p.getLocation().distanceSquared(location) <= (counter+1)){
							listM.add(p);
						}
					}
				}
			}
			
			listM.clear();
			counter--;
		}
		return null;
	}
	
	public static Animals getNearbyAnimal(Location location ,int distance){
		
		int counter = distance;
		
		listA.clear();
		
		while( counter != 0){
			for(Entity e :location.getWorld().getEntities()){
				if(e instanceof Animals){
					Animals p = (Animals)e;
					if(p.getLocation().distanceSquared(location) <= counter){
						listA.add(p);
					}
				}
			}
			if(listA.size() == 1){
				return listA.get(0);
			}
			else if(listA.size() == 0){
				for(Entity e :location.getWorld().getEntities()){
					if(e instanceof Animals){
						Animals p = (Animals)e;
						if(p.getLocation().distanceSquared(location) <= (counter+1)){
							listA.add(p);
						}
					}
				}
			}
			
			listA.clear();
			counter--;
		}
		return null;
	}
	
	public static LivingEntity getNearbyEntity(Location location ,int distance ,Player player){
		
		int counter = distance;
		
		listE.clear();
		
		while( counter != 0){
			for(Entity e :location.getWorld().getEntities()){
				if(e.getLocation().distanceSquared(location) <= counter){
					if(e instanceof Player){
						if(e.getUniqueId() != player.getUniqueId()){
							listE.add((LivingEntity)e);
						}
					}
					else if(e instanceof LivingEntity && !(e instanceof Projectile)){
						listE.add((LivingEntity)e);
					}
				}
			}
			if(listE.size() == 1){
				return listE.get(0);
			}
			else if(listE.size() == 0){
				for(Entity e :location.getWorld().getEntities()){
					if(e.getLocation().distanceSquared(location) <= (counter+1)){
						if(e instanceof Player){
							if(e.getUniqueId() != player.getUniqueId()){
								listE.add((LivingEntity)e);
							}
						}
						else if(e instanceof LivingEntity && !(e instanceof Projectile)){
							listE.add((LivingEntity)e);
						}
					}
				}
			}
			listE.clear();
			counter--;
		}
		return null;
	}
	public static List<LivingEntity> getNearbyEntityAll(Location location ,int distance ,Player player){
		
		int counter = distance;
		
		listE.clear();
		
		for(Entity e :location.getWorld().getEntities()){
			if(e.getLocation().distanceSquared(location) <= counter){
				if(e instanceof Player){
					if((Player)e != player){
						listE.add((LivingEntity)e);
					}
				}
				if(e instanceof LivingEntity){
					listE.add((LivingEntity)e);
				}
			}
		}
		return listE;
	}
	
	public static LivingEntity getTargetNearbyEntity(Player player) {
		 
	    BlockIterator it = new BlockIterator(player, 100);
	 
	    while ( it.hasNext() ) {
	 
	        Block block = it.next();
	 
	        if ( block.getType() == Material.AIR ) {
	        	LivingEntity e = PSSystem.getNearbyEntity(block.getLocation(), 10, player);
	        	if(e != null){
	        		return e;
	        	}
	        }
	    }
		return null;
	}
}
