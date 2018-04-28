package de.bergwerklabs.tryjump.core.task;

import de.bergwerklabs.tryjump.core.TryJump;
import de.bergwerklabs.tryjump.core.TryJumpSession;
import org.bukkit.scheduler.BukkitTask;

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
  protected BukkitTask bukkitTask;

  TryJumpTask(TryJumpSession session) {
    this.session = session;
    this.game = (TryJump) session.getGame();
  }

  public abstract void stop();

  public abstract void start(long delay, long interval);
}
