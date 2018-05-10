package de.bergwerklabs.tryjump.core.phase.buy;

import de.bergwerklabs.framework.commons.misc.Tuple;
import de.bergwerklabs.framework.commons.spigot.general.timer.LabsTimer;
import de.bergwerklabs.framework.commons.spigot.general.timer.LabsTimerStopCause;
import de.bergwerklabs.framework.commons.spigot.scoreboard.LabsScoreboard;
import de.bergwerklabs.framework.commons.spigot.scoreboard.Row;
import de.bergwerklabs.tryjump.core.Jumper;
import de.bergwerklabs.tryjump.core.TryJumpSession;
import de.bergwerklabs.tryjump.core.phase.Phase;
import de.bergwerklabs.tryjump.core.phase.buy.listener.BuyListener;
import de.bergwerklabs.tryjump.core.util.Scoreboards;
import java.util.Collection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Yannic Rieger on 22.04.2018.
 *
 * <p>
 *
 * @author Yannic Rieger
 */
public class BuyPhase extends Phase {

  public BuyPhase(TryJumpSession session) {
    super(session);
  }

  private LabsTimer timer;

  @Override
  public void start() {
    final int duration = this.session.getTryJumpConfig().getBuyPhaseDuration();

    // TODO: find better solution
    final Location location = new Location(Bukkit.getWorld("spawn"), -29.5, 108.5, -21.5);
    location.setYaw(0);
    location.setYaw(-50);

    jumpers.forEach(
        jumper -> {
          final Player player = jumper.getPlayer();
          player.teleport(location);
          player.getInventory().setItem(4, new ItemStack(Material.CHEST));
          jumper.setScoreboard(this.createTokenScoreboard(this.jumpers, duration));
        });

    this.timer =
        new LabsTimer(
            duration,
            timeLeft -> {
              String prefix = this.tryJump.getMessenger().getPrefix();
              String timeString =
                  prefix + String.format("§b%02d:%02d", timeLeft / 60, timeLeft % 60);
              this.jumpers.forEach(jumper -> jumper.getScoreboard().setTitle(timeString));
            });

    timer.addStopListener(
        event -> {
          if (event.getCause() != LabsTimerStopCause.TIMES_UP) return;
          BuyListener.unregisterListeners();
          this.stop();
        });

    timer.start();
    BuyListener.registerListeners(session, this);
  }

  @Override
  public void stop() {
    this.timer.stop();
    this.tryJump.getDeathmatchPhase().start();
  }

  /**
   *
   * @param jumpers
   * @param duration
   * @return
   */
  private LabsScoreboard createTokenScoreboard(Collection<Jumper> jumpers, int duration) {
    final String timeString = String.format("§b%02d:%02d", duration / 60, duration % 60);
    final String prefix = this.tryJump.getMessenger().getPrefix();
    final int size = jumpers.size();

    final Tuple<LabsScoreboard, Integer> result =
        Scoreboards.withFooter(prefix + timeString, "distance", size, size);

    final int start = result.getValue2();
    final LabsScoreboard scoreboard = result.getValue1();
    final int[] count = {start};

    jumpers.forEach(
        jumper -> {
          final Player spigotPlayer = jumper.getPlayer();
          scoreboard.addRow(
              count[0]++,
              new Row(
                  scoreboard, "§7" + spigotPlayer.getDisplayName() + ": §b" + jumper.getTokens()));
        });
    return scoreboard;
  }
}
