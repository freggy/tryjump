package de.bergwerklabs.tryjump.core.phase.jump.listener;

import de.bergwerklabs.tryjump.core.TryJumpSession;
import de.bergwerklabs.tryjump.core.phase.jump.JumpPhase;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerDropItemEvent;

/**
 * Created by Yannic Rieger on 15.04.2018.
 *
 * <p>
 *
 * @author Yannic Rieger
 */
class DropItemListener extends JumpPhaseListener {

  DropItemListener(JumpPhase phase, TryJumpSession session) {
    super(phase, session);
  }

  @EventHandler
  private void onItemDrop(PlayerDropItemEvent event) {
    event.setCancelled(true);
  }
}
