package de.bergwerklabs.tryjump.core.phase.jump;

import de.bergwerklabs.framework.commons.misc.Tuple;
import de.bergwerklabs.framework.commons.spigot.chat.messenger.PluginMessenger;
import de.bergwerklabs.framework.commons.spigot.general.timer.LabsTimer;
import de.bergwerklabs.framework.commons.spigot.item.ItemStackBuilder;
import de.bergwerklabs.framework.commons.spigot.scoreboard.LabsScoreboard;
import de.bergwerklabs.framework.commons.spigot.scoreboard.Row;
import de.bergwerklabs.tryjump.api.Unit;
import de.bergwerklabs.tryjump.core.Jumper;
import de.bergwerklabs.tryjump.core.TryJumpSession;
import de.bergwerklabs.tryjump.core.TryJumpUnit;
import de.bergwerklabs.tryjump.core.command.SkipCommand;
import de.bergwerklabs.tryjump.core.phase.Phase;
import de.bergwerklabs.tryjump.core.phase.jump.listener.JumpPhaseListener;
import de.bergwerklabs.tryjump.core.phase.jump.listener.PlayerInteractListener;
import de.bergwerklabs.tryjump.core.task.TryJumpTask;
import de.bergwerklabs.tryjump.core.task.UpdatePlayerInfoTask;
import de.bergwerklabs.tryjump.core.unit.UnitPlacer;
import de.bergwerklabs.tryjump.core.util.Scoreboards;
import java.util.Collection;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

/**
 * Created by Yannic Rieger on 22.04.2018.
 *
 * <p>
 *
 * @author Yannic Rieger
 */
public class JumpPhase extends Phase {

  private TryJumpTask updatePlayerInfoTask;

  public JumpPhase(TryJumpSession session) {
    super(session);
  }

  @Override
  public void start() {
    final PluginMessenger messenger = this.tryJump.getMessenger();

    LabsTimer countdown =
        new LabsTimer(
            5,
            timeLeft -> {
              System.out.println(jumpers.size());
              this.jumpers.forEach(
                  jumper -> {
                    final Player spigotPlayer = jumper.getPlayer();
                    System.out.println(spigotPlayer.getName());
                    spigotPlayer.playSound(spigotPlayer.getEyeLocation(), Sound.CLICK, 100, 1);
                    messenger.message(
                        "Das Spiel startet in §b" + timeLeft + " Sekunden§7.", spigotPlayer);
                  });
            });

    countdown.addStopListener(
        event -> {
          this.updatePlayerInfoTask = new UpdatePlayerInfoTask(session);
          messenger.messageAll("LOS!");
          this.jumpers.forEach(
              jumper -> {
                final Player spigotPlayer = jumper.getPlayer();
                final UnitPlacer placer = session.getPlacer();
                final Vector end = placer.getStart().getMetadata().getEndVector();
                // Since its the of the round, the optional will not be empty.
                final Unit unit = jumper.getNextUnit().get();
                final ItemStack instantDeath =
                    new ItemStackBuilder(Material.INK_SACK)
                        .setName("§c§lInstant-Tod(TM)")
                        .setData((byte) 1)
                        .create();

                jumper.setScoreboard(
                    this.createScoreboard(
                        spigotPlayer,
                        this.jumpers,
                        session.getTryJumpConfig().getJumpPhaseDuration()));
                jumper.unfreeze();
                spigotPlayer.getInventory().setItem(4, instantDeath);
                Location to = jumper.getStartSpawn().clone().subtract(end);
                jumper.setCurrentUnit(unit);
                placer.placeUnit(to, (TryJumpUnit) unit, false);

                // Register at this point so players cannot use instant death.
                Bukkit.getPluginManager()
                    .registerEvents(
                        new PlayerInteractListener(this.tryJump.getJumpPhase(), this.session),
                        this.session);
              });
          this.updatePlayerInfoTask.start(0, 10);
        });

    countdown.start();
  }

  @Override
  public void stop() {
    this.updatePlayerInfoTask.stop();
    JumpPhaseListener.unregisterListeners();
    LabsTimer timer =
        new LabsTimer(
            5,
            timeLeft -> {
              jumpers.forEach(
                  jumper -> {
                    final Player player = jumper.getPlayer();
                    player.getInventory().clear();
                    this.tryJump
                        .getMessenger()
                        .message(
                            "§7Jump-Phase endet in §b" + timeLeft + " Sekunden §7neu.", player);
                  });
            });

    timer.addStopListener(
        event -> {
          // ONLY FOR TEST PURPOSES [START]
          // Bukkit.getOnlinePlayers().forEach(p -> p.kickPlayer("Restart..."));
          // try {
          // FileUtils.deleteDirectory(new File("/development/gameserver/tryjump_rework/jump"));
          // Thread.sleep(2000);
          // } catch (Exception e) {
          // e.printStackTrace();
          // }
          // Bukkit.getServer().spigot().restart();
          // ONLY FOR TEST PURPOSES [END]

          session
              .getCommand("skip")
              .setExecutor(
                  new SkipCommand(
                      jumpers.size(),
                      this.tryJump.getMessenger(),
                      jumpers.stream().map(Jumper::getPlayer).collect(Collectors.toList())));

          this.tryJump.getBuyPhase().start();
        });

    timer.start();
  }

  private LabsScoreboard createScoreboard(Player self, Collection<Jumper> players, int duration) {
    final String timeString = String.format("§b%02d:%02d", duration / 60, duration % 60);
    final int size = players.size();
    final String prefix = this.tryJump.getMessenger().getPrefix();
    final Tuple<LabsScoreboard, Integer> result =
        Scoreboards.withFooter(prefix + timeString, "distance", size, size + 2);

    final int start = result.getValue2();

    final LabsScoreboard scoreboard = result.getValue1();
    scoreboard.addRow(size + start + 1, new Row(scoreboard, "§eTokens: §b0"));
    scoreboard.addRow(size + start, new Row(scoreboard, "§e§e§e"));

    final int[] count = {start};

    players.forEach(
        jumper -> {
          final Player spigotPlayer = jumper.getPlayer();
          if (spigotPlayer.getUniqueId().equals(self.getUniqueId())) {
            scoreboard.addPlayerSpecificRow(
                count[0]++,
                spigotPlayer,
                new Row(scoreboard, "§7§n" + spigotPlayer.getDisplayName() + "§r" + " §b0%"));

          } else {
            scoreboard.addPlayerSpecificRow(
                count[0]++,
                spigotPlayer,
                new Row(scoreboard, "§7" + spigotPlayer.getDisplayName() + "§r" + " §b0%"));
          }
        });
    return scoreboard;
  }
}
