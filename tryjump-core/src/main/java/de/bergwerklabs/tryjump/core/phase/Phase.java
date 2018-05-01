package de.bergwerklabs.tryjump.core.phase;

import de.bergwerklabs.tryjump.core.Jumper;
import de.bergwerklabs.tryjump.core.TryJump;
import de.bergwerklabs.tryjump.core.TryJumpSession;
import java.util.Collection;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Yannic Rieger on 22.04.2018.
 *
 * <p>
 *
 * @author Yannic Rieger
 */
public abstract class Phase {

  protected TryJumpSession session;
  protected TryJump tryJump;
  protected Collection<Jumper> jumpers;

  /** @param session {@link TryJumpSession} instance */
  public Phase(@NotNull TryJumpSession session) {
    this.session = session;
    this.tryJump = (TryJump) session.getGame();
    this.jumpers = this.tryJump.getPlayerRegistry().getPlayerCollection();
  }

  /** Starts the phase. */
  public abstract void start();

  /** Stops the phase. */
  public abstract void stop();
}
