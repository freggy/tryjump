package de.bergwerklabs.tryjump.core.phase.deathmatch;

import de.bergwerklabs.framework.commons.misc.Tuple;
import de.bergwerklabs.framework.commons.spigot.scoreboard.LabsScoreboard;
import de.bergwerklabs.framework.commons.spigot.scoreboard.Row;
import de.bergwerklabs.tryjump.core.TryJumpSession;
import de.bergwerklabs.tryjump.core.phase.Phase;
import de.bergwerklabs.tryjump.core.util.Scoreboards;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

/**
 * Created by Yannic Rieger on 22.04.2018.
 *
 * <p>
 *
 * @author Yannic Rieger
 */
public class DeathmatchPhase extends Phase {

  private static final Set<Listener> LISTENERS = new HashSet<>();

  public DeathmatchPhase(TryJumpSession session) {
    super(session);
  }

  @Override
  public void start() {
    final Iterator<Location> iterator = this.tryJump.getArena().getSpawns().iterator();

    this.jumpers.forEach(
        jumper -> {
          if (iterator.hasNext()) {
            final Location location = iterator.next();
            System.out.println(location);
            System.out.println(jumper);
            System.out.println(jumper.getPlayer());
            jumper.getPlayer().teleport(location);
          }
          jumper.setScoreboard(
              this.createDeathmatchScoreboard(
                  this.session.getTryJumpConfig().getDeathmatchDuration()));
        });
  }

  @Override
  public void stop() {}

  private LabsScoreboard createDeathmatchScoreboard(int duration) {
    String timeString = String.format("§b%02d:%02d", duration / 60, duration % 60);
    Tuple<LabsScoreboard, Integer> result =
        Scoreboards.withFooter(
            "§6>> §eTryJump §6❘ " + timeString,
            "deathmatch",
            this.jumpers.size(),
            this.jumpers.size() + 2);
    LabsScoreboard scoreboard = result.getValue1();

    scoreboard.addRow(
        this.jumpers.size() + result.getValue2() + 1, new Row(scoreboard, "§eKills: §b0"));
    scoreboard.addRow(this.jumpers.size() + result.getValue2(), new Row(scoreboard, "§e§e§e"));

    final int[] count = {result.getValue2()};

    this.jumpers.forEach(
        jumper -> {
          count[0]++;
          final Player spigotPlayer = jumper.getPlayer();
          scoreboard.addRow(count[0], new Row(scoreboard, "§7" + spigotPlayer.getDisplayName()));
        });

    return scoreboard;
  }
}
