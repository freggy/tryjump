package de.bergwerklabs.tryjump.core.phase.buy.listener;

import de.bergwerklabs.tryjump.core.TryJumpSession;
import de.bergwerklabs.tryjump.core.phase.buy.BuyPhase;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

/**
 * Created by Yannic Rieger on 11.05.2018.
 *
 * <p>
 *
 * @author Yannic Rieger
 */
public class CancelListener extends BuyListener {

  public CancelListener(BuyPhase phase, TryJumpSession session) {
    super(phase, session);
  }

  @EventHandler
  private void onPlayerDamage(EntityDamageEvent event) {
    event.setCancelled(true);
  }

  @EventHandler
  private void onFoodLevelChange(FoodLevelChangeEvent event) {
    event.setCancelled(true);
  }
}
