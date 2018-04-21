package de.bergwerklabs.tryjump.core.task;

import de.bergwerklabs.framework.commons.spigot.general.timer.LabsTimer;
import de.bergwerklabs.framework.commons.spigot.general.timer.LabsTimerStopCause;
import de.bergwerklabs.framework.commons.spigot.title.ActionbarTitle;
import de.bergwerklabs.tryjump.core.TryJumpSession;
import de.bergwerklabs.tryjump.core.command.SkipCommand;
import org.bukkit.Bukkit;

/**
 * Created by Yannic Rieger on 20.04.2018.
 *
 * <p>
 *
 * @author Yannic Rieger
 */
public class SkipDisplayTask extends TryJumpTask {

  public LabsTimer getTimer() {
    return timer;
  }

  private LabsTimer timer;

  public SkipDisplayTask(TryJumpSession session) {
    super(session);
    this.timer =
        new LabsTimer(
            10,
            timeLeft -> {
              String timeString = String.format("§b%02d:%02d", timeLeft / 60, timeLeft % 60);
              this.game
                  .getPlayerRegistry()
                  .getPlayerCollection()
                  .forEach(
                      jumper -> {
                        ActionbarTitle.send(jumper.getPlayer(), "§7Verbleibend: " + timeString);
                      });
            });
  }

  @Override
  public void run() {

    if (!timer.isRunning()) timer.start();

    timer.addStopListener(
        event -> {
          if (event.getCause() != LabsTimerStopCause.TIMES_UP) return;
          // TOOD: teleport to deathmatch arena.
          SkipCommand.unregister();
          Bukkit.broadcastMessage("VORBEI");
        });
  }
}
