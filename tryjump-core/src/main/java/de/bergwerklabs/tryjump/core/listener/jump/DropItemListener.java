package de.bergwerklabs.tryjump.core.listener.jump;

import de.bergwerklabs.tryjump.core.TryJump;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerDropItemEvent;

/**
 * Created by Yannic Rieger on 15.04.2018.
 *
 * <p>
 *
 * @author Yannic Rieger
 */
public class DropItemListener extends JumpPhaseListener {

  public DropItemListener(TryJump tryJump) {
    super(tryJump);
  }

  @EventHandler
  private void onItemDrop(PlayerDropItemEvent event) {
    event.setCancelled(true);
  }
}
