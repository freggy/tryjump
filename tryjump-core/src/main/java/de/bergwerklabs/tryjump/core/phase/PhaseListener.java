package de.bergwerklabs.tryjump.core.phase;

import de.bergwerklabs.tryjump.core.Jumper;
import de.bergwerklabs.tryjump.core.TryJump;
import de.bergwerklabs.tryjump.core.TryJumpSession;
import java.util.Collection;
import org.bukkit.event.Listener;

/**
 * Created by Yannic Rieger on 28.04.2018.
 *
 * <p>Base class for Phase specific listeners.
 *
 * @author Yannic Rieger
 */
public abstract class PhaseListener<T extends Phase> implements Listener {

  protected T phase;
  protected TryJumpSession session;
  protected TryJump tryJump;
  protected Collection<Jumper> jumpers;

  public PhaseListener(T phase, TryJumpSession session) {
    this.phase = phase;
    this.session = session;
    this.tryJump = (TryJump) session.getGame();
    this.jumpers = tryJump.getPlayerRegistry().getPlayerCollection();
  }

  /** Gets the {@link Jumper}s currently playing. */
  public Collection<Jumper> getJumpers() {
    return jumpers;
  }

  /** Gets the current {@link TryJumpSession} */
  public TryJumpSession getSession() {
    return this.session;
  }

  /** Gets the current Phase. */
  public T getPhase() {
    return this.phase;
  }

  /** Gets {@link TryJump} instance. */
  public TryJump getTryJump() {
    return this.tryJump;
  }
}
