package de.bergwerklabs.tryjump.core.phase.jump.listener;

import de.bergwerklabs.framework.commons.spigot.title.Title;
import de.bergwerklabs.tryjump.api.event.CoinsReceiveEvent;
import de.bergwerklabs.tryjump.api.event.LastUnitReachedEvent;
import de.bergwerklabs.tryjump.api.event.RankingPointsReceiveEvent;
import de.bergwerklabs.tryjump.api.event.TokensReceiveEvent;
import de.bergwerklabs.tryjump.core.Jumper;
import de.bergwerklabs.tryjump.core.TryJumpSession;
import de.bergwerklabs.tryjump.core.TryJumpUnit;
import de.bergwerklabs.tryjump.core.config.Config;
import de.bergwerklabs.tryjump.core.config.UnitTokens;
import de.bergwerklabs.tryjump.core.phase.jump.JumpPhase;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.PluginManager;

/**
 * Created by Yannic Rieger on 11.02.2018.
 *
 * <p>Handles event when players activate the gold pressure plate.
 *
 * @author Yannic Rieger
 */
public class PlayerInteractListener extends JumpPhaseListener {

  public PlayerInteractListener(JumpPhase phase, TryJumpSession session) {
    super(phase, session);
  }

  @EventHandler
  private void onPlayerInteract(PlayerInteractEvent event) {
    event.setCancelled(true);

    Player player = event.getPlayer();
    Jumper jumper = this.tryJump.getPlayerRegistry().getPlayer(player.getUniqueId());
    if (jumper == null) return;

    final Action action = event.getAction();
    final Block clicked = event.getClickedBlock();
    final Material inHand = player.getItemInHand().getType();

    if (action == Action.PHYSICAL && clicked.getType() == Material.GOLD_PLATE) {
      this.handleUnitCompletion(jumper, event.getClickedBlock());
    } else if (inHand == Material.INK_SACK
        && (action == Action.RIGHT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR)) {
      if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - jumper.getLastUse()) <= 2
          && !jumper.isLite()) {
        this.tryJump
            .getMessenger()
            .message(
                "§cBitte warte noch einen Moment, bevor du den Instant Tod ausführst!", player);
        return;
      }
      jumper.setLastUse(System.currentTimeMillis());
      jumper.resetToSpawn();
    }
  }

  private void handleUnitCompletion(Jumper jumper, Block clicked) {
    clicked.setType(Material.AIR);
    final Optional<TryJumpUnit> unitOptional = jumper.getNextUnit();
    final TryJumpUnit current = (TryJumpUnit) jumper.getCurrentUnit();
    final Config config = this.session.getTryJumpConfig();

    if (unitOptional.isPresent()) {
      TryJumpUnit next = unitOptional.get();

      final UnitTokens tokens = UnitTokens.fromDifficulty(current.getDifficulty(), config);
      final int amount = jumper.isLite() ? tokens.getLite() : tokens.getNormal();
      jumper.updateJumpPhaseTokenDisplay(amount);

      final PluginManager manager = Bukkit.getPluginManager();
      manager.callEvent(new TokensReceiveEvent(jumper, amount));
      manager.callEvent(new CoinsReceiveEvent(jumper, config.getCoinsPerUnit()));
      manager.callEvent(new RankingPointsReceiveEvent(jumper, config.getRankingPointsPerUnit()));

      this.handleNext(jumper, clicked.getLocation(), next);
      jumper.setCurrentUnit(next);
      jumper.addCompletedUnit(current);

      Location unitSpawn = clicked.getLocation().clone().add(0.5, 0, 0.5);
      unitSpawn.setYaw(0);
      unitSpawn.setYaw(0);
      jumper.setUnitSpawn(unitSpawn);
    } else {
      this.handleLast(jumper);
    }
  }

  private void handleLast(Jumper jumper) {
    final Player spigotPlayer = jumper.getPlayer();

    // TODO: add tokens
    // TODO: display messages
    // TODO: start deathmatch

    this.jumpers.forEach(
        player -> {
          final Player p = player.getPlayer();
          p.playSound(p.getEyeLocation(), Sound.WITHER_SPAWN, 10, 1);
          // TODO: use rank color
          new Title("§a" + spigotPlayer.getDisplayName(), "§7hat das Ziel erreicht", 40, 40, 40)
              .display(p);
        });

    this.phase.stop();
    Bukkit.getPluginManager().callEvent(new LastUnitReachedEvent(jumper));
  }

  private void handleNext(Jumper jumper, Location blockLoc, TryJumpUnit unit) {
    final Player spigotPlayer = jumper.getPlayer();
    spigotPlayer.playSound(spigotPlayer.getEyeLocation(), Sound.LEVEL_UP, 100, 10);
    TryJumpSession.getInstance()
        .getPlacer()
        .placeUnit(blockLoc.clone().subtract(0, 1, 0), unit, false);
    jumper.setCurrentFails(0);
    // TODO: unit finished event
    // TODO: display messages
  }
}
