package de.bergwerklabs.tryjump.core.phase.jump.listener;

import de.bergwerklabs.tryjump.core.Jumper;
import de.bergwerklabs.tryjump.core.TryJumpSession;
import de.bergwerklabs.tryjump.core.phase.jump.JumpPhase;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * Created by Yannic Rieger on 14.04.2018.
 *
 * <p>
 *
 * @author Yannic Rieger
 */
class PlayerDamageListener extends JumpPhaseListener {

  PlayerDamageListener(JumpPhase phase, TryJumpSession session) {
    super(phase, session);
  }

  @EventHandler
  private void onPlayerDamage(EntityDamageEvent event) {
    final Entity entity = event.getEntity();
    if (!(entity instanceof Player)) return;
    final Player player = (Player) entity;

    if (event.getCause().equals(EntityDamageEvent.DamageCause.VOID)) {
      final Jumper jumper = this.tryJump.getPlayerRegistry().getPlayer(player.getUniqueId());
      if (jumper == null) return;
      jumper.resetToSpawn();
    }
    event.setCancelled(true);
  }
}
