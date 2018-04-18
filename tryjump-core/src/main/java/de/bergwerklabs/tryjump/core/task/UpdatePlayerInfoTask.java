package de.bergwerklabs.tryjump.core.task;

import de.bergwerklabs.framework.commons.spigot.title.ActionbarTitle;
import de.bergwerklabs.tryjump.core.Jumper;
import de.bergwerklabs.tryjump.core.TryJumpSession;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Created by Yannic Rieger on 12.04.2018.
 *
 * <p>
 *
 * @author Yannic Rieger
 */
public class UpdatePlayerInfoTask extends TryJumpTask {

  public UpdatePlayerInfoTask(TryJumpSession session) {
    super(session);
  }

  @Override
  public void run() {
    final Collection<Jumper> jumpers = this.game.getPlayerRegistry().getPlayerCollection();
    jumpers.forEach(
        jumper -> {
          final Player spigotPlayer = jumper.getPlayer();
          ActionbarTitle.send(spigotPlayer, jumper.buildActionbarText());
        });
    this.updateJumpProgress(jumpers);
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
