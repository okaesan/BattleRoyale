package com._28ftb.battleroyale.clubs.data;

import com._28ftb.battleroyale.BattleRoyale;
import com._28ftb.battleroyale.clubs.ClubManager;

import net.minecraft.server.v1_11_R1.ItemStack;
import net.minecraft.server.v1_11_R1.NBTTagCompound;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class Science implements Listener {

  public static BattleRoyale plugin = BattleRoyale.plugin;
  private static final String name = "Science";
  private String data;

  @EventHandler
  public void Ball(PlayerInteractEvent e) {

    if (e.getAction().equals(Action.RIGHT_CLICK_AIR)
        || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
      Player p = e.getPlayer();
      if (ClubManager.hasClub(p)) {
        if (ClubManager.getClub(p) == ClubManager.getClub(name)) {
          if (p.getInventory().getItemInMainHand().getType() == Material.SPLASH_POTION) {
            data = name;
          }
        }
      }
    }
  }

  @EventHandler
  public void BallLunch(ProjectileLaunchEvent e) {
    if (e.getEntityType() == EntityType.SPLASH_POTION) {
      if (name.equals(data)) {
        Projectile p = (Projectile) e.getEntity();
        data = null;
        p.setCustomName(name);
      }
    }
  }

  @EventHandler
  public void Hit(PotionSplashEvent e) {
    if (name.equals(e.getEntity().getCustomName())) {
      ItemStack nmsItem = CraftItemStack.asNMSCopy(e.getPotion().getItem());
      NBTTagCompound nbttag = nmsItem.getTag();
      if ("minecraft:harming".equals(nbttag.getString("Potion"))) {
        for (LivingEntity le : e.getAffectedEntities()) {
          if (le instanceof Player) {
            Player p = (Player) le;
            p.damage(6);
          }
        }
        e.setCancelled(true);
      } else if ("minecraft:strong_harming".equals(nbttag.getString("Potion"))) {
        for (LivingEntity le : e.getAffectedEntities()) {
          if (le instanceof Player) {
            Player p = (Player) le;
            p.damage(12);
          }
        }
        e.setCancelled(true);
      }

    }
  }
}
