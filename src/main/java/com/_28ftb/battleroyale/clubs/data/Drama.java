package com._28ftb.battleroyale.clubs.data;

import com._28ftb.battleroyale.BattleRoyale;
import com._28ftb.battleroyale.clubs.ClubManager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class Drama implements Listener {

  public static BattleRoyale plugin = BattleRoyale.plugin;
  private static final String name = "Drama";

  @EventHandler
  public void zoom(PlayerMoveEvent e) {
    Player p = e.getPlayer();
    if (ClubManager.hasClub(p)) {
      if (ClubManager.getClub(p) == ClubManager.getClub(name)) {
        if (e.getPlayer().isSneaking()) {
          p.setGravity(false);
          p.setVelocity(new Vector(0, -1, 0));
          p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 50000, 4));
          p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 50000, 4));
          p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 50000, 0));
        } else {
          p.removePotionEffect(PotionEffectType.INVISIBILITY);
          p.removePotionEffect(PotionEffectType.SLOW);
          p.removePotionEffect(PotionEffectType.BLINDNESS);
          p.setGravity(true);
        }
      }
    }
  }

}
