package de.bergwerklabs.tryjump.core.phase.deathmatch;

import de.bergwerklabs.framework.commons.misc.Tuple;
import de.bergwerklabs.framework.commons.spigot.scoreboard.LabsScoreboard;
import de.bergwerklabs.framework.commons.spigot.scoreboard.Row;
import de.bergwerklabs.tryjump.core.TryJumpSession;
import de.bergwerklabs.tryjump.core.phase.Phase;
import de.bergwerklabs.tryjump.core.phase.deathmatch.listener.DeathmachtListener;
import de.bergwerklabs.tryjump.core.util.Scoreboards;
import java.util.Iterator;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Created by Yannic Rieger on 22.04.2018.
 *
 * <p>
 *
 * @author Yannic Rieger
 */
public class DeathmatchPhase extends Phase {

  public DeathmatchPhase(TryJumpSession session) {
    super(session);
  }

  @Override
  public void start() {
    final Iterator<Location> iterator = this.tryJump.getArena().getSpawns().iterator();
    DeathmachtListener.registerListeners(this.session, this);
    this.jumpers.forEach(
        jumper -> {
          final Player player = jumper.getPlayer();
          player.getInventory().clear();
          if (iterator.hasNext()) {
            player.teleport(iterator.next());
          }
          jumper.setScoreboard(
              this.createDeathmatchScoreboard(
                  this.session.getTryJumpConfig().getDeathmatchDuration()));
        });
  }

  @Override
  public void stop() {
    DeathmachtListener.unregisterListeners();
    // TODO: stop game after x seconds.
  }

  private LabsScoreboard createDeathmatchScoreboard(int duration) {
    final String timeString = String.format("§b%02d:%02d", duration / 60, duration % 60);
    final String prefix = this.tryJump.getMessenger().getPrefix();
    final int size = this.jumpers.size();
    final Tuple<LabsScoreboard, Integer> result =
        Scoreboards.withFooter(prefix + timeString, "deathmatch", size, size + 2);

    final int start = result.getValue2();

    final LabsScoreboard scoreboard = result.getValue1();
    scoreboard.addRow(size + start + 1, new Row(scoreboard, "§eKills: §b0"));
    scoreboard.addRow(size + start, new Row(scoreboard, "§e§e§e"));

    final int[] count = {start};

    this.jumpers.forEach(
        jumper -> {
          final Player spigotPlayer = jumper.getPlayer();
          scoreboard.addPlayerSpecificRow(
              count[0]++,
              spigotPlayer,
              new Row(
                  scoreboard,
                  "§7" + spigotPlayer.getDisplayName() + ": §b" + jumper.getLivesLeft()));
        });

    return scoreboard;
  }
}
