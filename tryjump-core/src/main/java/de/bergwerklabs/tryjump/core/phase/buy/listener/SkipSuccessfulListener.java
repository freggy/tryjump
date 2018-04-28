package de.bergwerklabs.tryjump.core.phase.buy.listener;

import de.bergwerklabs.tryjump.api.event.skip.SkipSuccessfulEvent;
import de.bergwerklabs.tryjump.core.TryJumpSession;
import de.bergwerklabs.tryjump.core.command.SkipCommand;
import de.bergwerklabs.tryjump.core.phase.buy.BuyPhase;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;

/**
 * Created by Yannic Rieger on 20.04.2018.
 *
 * <p>
 *
 * @author Yannic Rieger
 */
public class SkipSuccessfulListener extends BuyListener {

  public SkipSuccessfulListener(BuyPhase phase, TryJumpSession session) {
    super(phase, session);
  }

  @EventHandler
  private void onSuccessfulSkip(SkipSuccessfulEvent event) {
    // TODO: stop displayShopTimeTask
    // TODO: teleport players to deathmatch arena
    SkipCommand.unregister();
    Bukkit.broadcastMessage("VORBEI");
  }
}
