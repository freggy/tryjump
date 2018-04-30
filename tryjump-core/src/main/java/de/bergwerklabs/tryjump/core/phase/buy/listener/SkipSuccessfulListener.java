package de.bergwerklabs.tryjump.core.phase.buy.listener;

import de.bergwerklabs.tryjump.api.event.skip.SkipSuccessfulEvent;
import de.bergwerklabs.tryjump.core.TryJumpSession;
import de.bergwerklabs.tryjump.core.command.SkipCommand;
import de.bergwerklabs.tryjump.core.phase.buy.BuyPhase;
import org.bukkit.event.EventHandler;

/**
 * Created by Yannic Rieger on 20.04.2018.
 *
 * <p>
 *
 * @author Yannic Rieger
 */
class SkipSuccessfulListener extends BuyListener {

  SkipSuccessfulListener(BuyPhase phase, TryJumpSession session) {
    super(phase, session);
  }

  @EventHandler
  private void onSuccessfulSkip(SkipSuccessfulEvent event) {
    SkipCommand.unregister();
    this.tryJump.getBuyPhase().stop();
  }
}
