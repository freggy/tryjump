package de.bergwerklabs.tryjump.core.phase.buy;

import de.bergwerklabs.tryjump.core.TryJumpSession;
import de.bergwerklabs.tryjump.core.phase.Phase;
import de.bergwerklabs.tryjump.core.phase.buy.listener.BuyListener;

/**
 * Created by Yannic Rieger on 22.04.2018.
 *
 * <p>
 *
 * @author Yannic Rieger
 */
public class BuyPhase extends Phase {

  public BuyPhase(TryJumpSession session) {
    super(session);
  }

  @Override
  public void start() {
    BuyListener.registerListeners(session, this);
  }

  @Override
  public void stop() {}
}
