package de.bergwerklabs.tryjump.core.phase.buy.listener;

import de.bergwerklabs.tryjump.core.TryJumpSession;
import de.bergwerklabs.tryjump.core.phase.buy.BuyPhase;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Created by Yannic Rieger on 11.05.2018.
 *
 * <p>
 *
 * @author Yannic Rieger
 */
public class PlayerInteractListener extends BuyListener {

  public PlayerInteractListener(BuyPhase phase, TryJumpSession session) {
    super(phase, session);
  }

  @EventHandler
  private void onPlayerInteract(PlayerInteractEvent event) {
    final Action action = event.getAction();
    event.setCancelled(true);

    if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) return;
    if (event.getItem().getType() != Material.CHEST) return;

    event.getPlayer().openInventory(this.session.getMenus().get("categories").getInventory());
  }
}
