package de.bergwerklabs.tryjump.core.task;

import de.bergwerklabs.framework.commons.spigot.chat.messenger.PluginMessenger;
import de.bergwerklabs.framework.commons.spigot.general.timer.LabsTimer;
import de.bergwerklabs.framework.commons.spigot.general.timer.LabsTimerStopCause;
import de.bergwerklabs.framework.commons.spigot.title.ActionbarTitle;
import de.bergwerklabs.tryjump.core.Jumper;
import de.bergwerklabs.tryjump.core.TryJumpSession;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * Created by Yannic Rieger on 12.04.2018.
 *
 * <p>
 *
 * @author Yannic Rieger
 */
public class UpdatePlayerInfoTask extends TryJumpTask {

  private LabsTimer timer;
  private Collection<Jumper> players;
  private PluginMessenger messenger;

  public UpdatePlayerInfoTask(TryJumpSession session) {
    super(session);
    this.players = this.game.getPlayerRegistry().getPlayerCollection();
    this.messenger = this.game.getMessenger();
    this.timer =
        new LabsTimer(
            60 * 10,
            timeLeft -> {
              this.players.forEach(
                  player -> {
                    final Player spigotPlayer = player.getPlayer();
                    String timeString = String.format("§b%02d:%02d", timeLeft / 60, timeLeft % 60);
                    player.getScoreboard().setTitle("§6>> §eTryJump §6❘ " + timeString);
                    if (((float) timeLeft / 60F) % 10 == 0) {
                      this.messenger.message("Noch " + timeString + " §7Minuten.", spigotPlayer);
                      spigotPlayer.playSound(
                          spigotPlayer.getEyeLocation(), Sound.NOTE_BASS, 1.0F, 1.0F);
                    } else if (timeLeft <= 5) {
                      this.messenger.message("§b" + String.valueOf(timeLeft), spigotPlayer);
                      spigotPlayer.playSound(
                          spigotPlayer.getEyeLocation(), Sound.NOTE_BASS, 1.0F, 1.0F);
                    }
                  });
            });
    this.timer.addStopListener(event -> {
      if (event.getCause() != LabsTimerStopCause.TIMES_UP) return;
      // TODO: init buy phase
    });
  }

  @Override
  public void run() {
    final Collection<Jumper> jumpers = this.game.getPlayerRegistry().getPlayerCollection();
    if (!this.timer.isRunning()) this.timer.start();
    jumpers.forEach(
        jumper -> {
          final Player spigotPlayer = jumper.getPlayer();
          ActionbarTitle.send(spigotPlayer, jumper.buildActionbarText());
        });
    this.updateJumpProgress(jumpers);
  }

  @Override
  public void stop() {
    this.timer.stop();
    this.bukkitTask.cancel();
  }

  @Override
  public void start(long delay, long interval) {
    this.bukkitTask =
        Bukkit.getScheduler().runTaskTimerAsynchronously(this.session, this, delay, interval);
  }

  private void updateJumpProgress(Collection<Jumper> jumpers) {
    final List<Jumper> sorted =
        jumpers
            .stream()
            .peek(this::calculateProgress)
            .sorted(Comparator.comparingLong(Jumper::getJumpProgress))
            .collect(Collectors.toList());

    jumpers.forEach(jumper -> jumper.updateScoreboardProgress(sorted));
  }

  private void calculateProgress(Jumper jumper) {
    double currentDistance =
        this.calculateDistanceFast(jumper.getStartSpawn(), jumper.getPlayer().getLocation());
    double totalDistance = this.session.getPlacer().getParkourLength();
    int i = new Long(Math.round((currentDistance / totalDistance) * 100)).intValue();
    jumper.setJumpProgress(i);
  }

  private double calculateDistanceFast(Location location1, Location location2) {
    return location1.distance(location2);
  }
}
