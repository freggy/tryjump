package de.bergwerklabs.tryjump.core.phase.deathmatch.listener;

import de.bergwerklabs.framework.bedrock.api.PlayerRegistry;
import de.bergwerklabs.framework.bedrock.api.event.game.SpectatorEvent;
import de.bergwerklabs.framework.commons.spigot.scoreboard.LabsScoreboard;
import de.bergwerklabs.framework.commons.spigot.scoreboard.Row;
import de.bergwerklabs.framework.commons.spigot.title.Title;
import de.bergwerklabs.tryjump.core.Jumper;
import de.bergwerklabs.tryjump.core.TryJumpSession;
import de.bergwerklabs.tryjump.core.phase.deathmatch.DeathmatchPhase;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by Yannic Rieger on 11.02.2018.
 *
 * <p>
 *
 * @author Yannic Rieger
 */
class PlayerDeathListener extends DeathmachtListener {

  PlayerDeathListener(DeathmatchPhase phase, TryJumpSession session) {
    super(phase, session);
  }

  @EventHandler
  private void onPlayerDeath(PlayerDeathEvent event) {
    final PlayerRegistry<Jumper> registry = this.tryJump.getPlayerRegistry();
    final Player killed = event.getEntity();
    final Player killer = killed.getKiller();

    final Jumper killedJumper = registry.getPlayer(killed.getUniqueId());
    final Jumper killingJumper = registry.getPlayer(killer.getUniqueId());
    final int livesLeft = killedJumper.decrementLife();

    if (livesLeft <= 0) {
      registry.unregisterPlayer(killedJumper);
      registry.registerSpectator(killedJumper);
      Bukkit.getPluginManager().callEvent(new SpectatorEvent<>(this.tryJump, killedJumper));

      // If this is false it indicates that only one player is alive so the killer has won the game.
      if (!(registry.getPlayers().size() == 1)) return;

      this.jumpers.forEach(
          jumper -> {
            new Title("§a" + killer.getDisplayName(), "§7hat das Spiel gewonnen", 20, 40, 20)
                .display(jumper.getPlayer());
          });

      // TODO: give ranking points to winner
      // TODO: give coins to winner

      this.phase.stop();
    }

    new PotionEffect(
            PotionEffectType.REGENERATION,
            20 * this.session.getTryJumpConfig().getRegenerationDuration(),
            20,
            false,
            false)
        .apply(killer);

    killingJumper.updateKills();

    this.jumpers.forEach(
        jumper -> {
          // TODO: use rank color and maybe use different messages each time a player dies.
          this.tryJump
              .getMessenger()
              .message(
                  "§a"
                      + killer.getDisplayName()
                      + " §7hat §a"
                      + killed.getDisplayName()
                      + " §7getötet.",
                  jumper.getPlayer());

          final LabsScoreboard scoreboard = jumper.getScoreboard();
          final Row row = scoreboard.getPlayerSpecificRows().get(killed.getUniqueId());
          row.setText("§7" + killed.getDisplayName() + ": §b" + livesLeft);
        });
  }
}
