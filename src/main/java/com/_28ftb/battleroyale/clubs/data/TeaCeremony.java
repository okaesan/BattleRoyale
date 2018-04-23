package com._28ftb.battleroyale.clubs.data;

import com._28ftb.battleroyale.BattleRoyale;
import com._28ftb.battleroyale.clubs.ClubManager;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;

public class TeaCeremony implements Listener {

  public static BattleRoyale plugin = BattleRoyale.plugin;
  private static final String name = "TeaCeremony";

  @EventHandler
  public void Heal(EntityRegainHealthEvent e) {
    if (e.getEntity().getType() == EntityType.PLAYER) {
      Player p = (Player) e.getEntity();
      if (ClubManager.hasClub(p)) {
        if (ClubManager.getClub(p) == ClubManager.getClub(name)) {
          e.setAmount(e.getAmount() + 2);
        }
      }
    }
  }
}
