package com._28ftb.battleroyale.clubs.support;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Homing extends BukkitRunnable {
  private static final double MaxRotationAngle = 0.12;
  Projectile arrow;
  LivingEntity target;

  public Homing(Projectile arrow, LivingEntity target, Plugin plugin) {
    this.arrow = arrow;
    this.target = target;
    runTaskTimer(plugin, 1L, 1L);
  }

  public void run() {
    double speed = this.arrow.getVelocity().length();
    if ((this.arrow.isOnGround()) || (this.arrow.isDead()) || (this.target.isDead())) {
      cancel();
      return;
    }
    Vector toTarget = this.target.getLocation().clone().add(new Vector(0.0D, 0.5D, 0.0D))
        .subtract(this.arrow.getLocation()).toVector();

    Vector dirVelocity = this.arrow.getVelocity().clone().normalize();
    Vector dirToTarget = toTarget.clone().normalize();
    double angle = dirVelocity.angle(dirToTarget);

    double newSpeed = 0.9D * speed + 0.14D;
    Vector newVelocity;
    if (angle < 0.12D) {
      newVelocity = dirVelocity.clone().multiply(newSpeed);
    } else {
      Vector newDir = dirVelocity.clone().multiply((angle - MaxRotationAngle) / angle)
          .add(dirToTarget.clone().multiply(MaxRotationAngle / angle));
      newDir.normalize();
      newVelocity = newDir.clone().multiply(newSpeed);
    }
    this.arrow.setVelocity(newVelocity.add(new Vector(0.0D, 0.03D, 0.0D)));
  }
}
