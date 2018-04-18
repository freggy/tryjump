package de.bergwerklabs.tryjump.core.listener.jump;

import de.bergwerklabs.tryjump.core.TryJump;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.FoodLevelChangeEvent;

/**
 * Created by Yannic Rieger on 14.04.2018.
 *
 * <p>
 *
 * @author Yannic Rieger
 */
public class FoodLevelChangeListener extends JumpPhaseListener {

  public FoodLevelChangeListener(TryJump tryJump) {
    super(tryJump);
  }

  @EventHandler
  private void onFoodLevelChange(FoodLevelChangeEvent event) {
    event.setCancelled(true);
  }
}
