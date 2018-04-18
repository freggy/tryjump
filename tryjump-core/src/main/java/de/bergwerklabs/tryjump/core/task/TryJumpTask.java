package de.bergwerklabs.tryjump.core.task;

import de.bergwerklabs.tryjump.core.TryJump;
import de.bergwerklabs.tryjump.core.TryJumpSession;

/**
 * Created by Yannic Rieger on 14.04.2018.
 *
 * <p>
 *
 * @author Yannic Rieger
 */
public abstract class TryJumpTask implements Runnable {

  protected TryJumpSession session;
  protected TryJump game;

  TryJumpTask(TryJumpSession session) {
    this.session = session;
    this.game = (TryJump) session.getGame();
  }
}
