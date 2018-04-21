package de.bergwerklabs.tryjump.core.listener;

import de.bergwerklabs.framework.commons.spigot.scoreboard.LabsScoreboard;
import de.bergwerklabs.framework.commons.spigot.scoreboard.Row;
import de.bergwerklabs.tryjump.core.Jumper;
import de.bergwerklabs.tryjump.core.TryJump;
import de.bergwerklabs.tryjump.core.TryJumpSession;
import java.util.Collection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by Yannic Rieger on 15.04.2018.
 *
 * <p>
 *
 * @author Yannic Rieger
 */
public class PlayerQuitListener implements Listener {

  @EventHandler(priority = EventPriority.LOWEST)
  private void onPlayerQuit(PlayerQuitEvent event) {
    final TryJumpSession session = TryJumpSession.getInstance();
    final TryJump tryJump = (TryJump) session.getGame();
    final Player player = event.getPlayer();
    final Collection<Jumper> jumpers = tryJump.getPlayerRegistry().getPlayerCollection();

    if (jumpers != null) {
      jumpers.forEach(
          jumper -> {
            final LabsScoreboard scoreboard = jumper.getScoreboard();
            final Row row = scoreboard.getPlayerSpecificRows().get(player.getUniqueId());
            if (row != null) {
              scoreboard.removeRow(row);
            }
          });
    }

    // TODO: use rank color
    event.setQuitMessage(
        "§6>> §eTryJump §e❘ §a" + player.getDisplayName() + " §7hat das Spiel verlassen.");
  }
}
