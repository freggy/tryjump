package de.bergwerklabs.tryjump.core.phase.deathmatch.listener;

import de.bergwerklabs.tryjump.core.Jumper;
import de.bergwerklabs.tryjump.core.TryJumpSession;
import de.bergwerklabs.tryjump.core.phase.deathmatch.DeathmatchPhase;
import java.util.concurrent.TimeUnit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * Created by Yannic Rieger on 17.04.2018.
 *
 * <p>
 *
 * @author Yannic Rieger
 */
class PlayerDamageListener extends DeathmachtListener {

  PlayerDamageListener(DeathmatchPhase phase, TryJumpSession session) {
    super(phase, session);
  }

  @EventHandler
  private void onPlayerDamage(EntityDamageEvent event) {
    final Entity entity = event.getEntity();
    if (!(entity instanceof Player)) return;

    final Player player = (Player) entity;
    final Jumper jumper = this.tryJump.getPlayerRegistry().getPlayer(player.getUniqueId());
    final long timeSinceRespawn = System.currentTimeMillis() - jumper.getLastRespawn();

    // TODO: make configurable
    if (TimeUnit.MILLISECONDS.toSeconds(timeSinceRespawn)
        <= this.session.getTryJumpConfig().getInvulerableDuration()) {
      event.setCancelled(true);
    }
  }
}
