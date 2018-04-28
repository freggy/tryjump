package de.bergwerklabs.tryjump.core.phase.jump.listener;

import de.bergwerklabs.tryjump.core.TryJumpSession;
import de.bergwerklabs.tryjump.core.phase.jump.JumpPhase;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.FoodLevelChangeEvent;

/**
 * Created by Yannic Rieger on 14.04.2018.
 *
 * <p>
 *
 * @author Yannic Rieger
 */
class FoodLevelChangeListener extends JumpPhaseListener {

  FoodLevelChangeListener(JumpPhase phase, TryJumpSession session) {
    super(phase, session);
  }

  @EventHandler
  private void onFoodLevelChange(FoodLevelChangeEvent event) {
    event.setCancelled(true);
  }
}
