package de.bergwerklabs.tryjump.core.phase;

import de.bergwerklabs.tryjump.core.Jumper;
import de.bergwerklabs.tryjump.core.TryJump;
import de.bergwerklabs.tryjump.core.TryJumpSession;
import java.util.Collection;

/**
 * Created by Yannic Rieger on 22.04.2018.
 * <p>
 *
 * @author Yannic Rieger
 */
public abstract class Phase {

  protected TryJumpSession session;
  protected TryJump tryJump;
  protected Collection<Jumper> jumpers;

  public Phase(TryJumpSession session) {
    this.session = session;
    this.tryJump = (TryJump) session.getGame();
    this.jumpers = this.tryJump.getPlayerRegistry().getPlayerCollection();
  }

  public abstract void start();

  public abstract void stop();
}
