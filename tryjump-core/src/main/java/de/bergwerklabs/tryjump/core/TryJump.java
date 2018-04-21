package de.bergwerklabs.tryjump.core;

import de.bergwerklabs.framework.bedrock.api.LabsGame;
import de.bergwerklabs.framework.bedrock.api.PlayerRegistry;
import de.bergwerklabs.tryjump.api.DeathmatchArena;
import de.bergwerklabs.tryjump.core.phase.deathmatch.listener.DeathmachtListener;
import de.bergwerklabs.tryjump.core.phase.jump.listener.JumpPhaseListener;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by Yannic Rieger on 11.02.2018.
 *
 * <p>
 *
 * @author Yannic Rieger
 */
public class TryJump extends LabsGame<Jumper> {

  public TryJump() {
    super("TryJump");
  }

  public DeathmatchArena getArena() {
    return arena;
  }

  public void setArena(DeathmatchArena arena) {
    this.arena = arena;
  }

  private DeathmatchArena arena;

  @Override
  public void start(PlayerRegistry<Jumper> registry) {
    this.playerRegistry = registry;
    final Collection<Jumper> players = registry.getPlayerCollection();

    final TryJumpSession session = TryJumpSession.getInstance();
    final Location spawn = new Location(Bukkit.getWorld("jump"), 0, 100, 0);
    final double[] count = {35};

    JumpPhaseListener.registerListeners(session);

    players.forEach(
        jumper -> {

          // TODO: remove this nonsense
          Queue<TryJumpUnit> units = new LinkedList<>();

          TryJumpSession.getInstance()
              .getPlacer()
              .getSelectedUnits()
              .forEach(
                  blub -> {
                    units.add(blub.clone());
                  });

          jumper.setUnits(units);
          final Location playerSpawn = spawn.clone().add(count[0] + 0.5, 0, 0.5);
          session.getPlacer().getStart().pasteAsync("jump", playerSpawn.toVector());
          jumper.setUnitSpawn(playerSpawn);
          jumper.setStartSpawn(playerSpawn);
          count[0] += 35;
        });

    players.forEach(
        jumper -> {
          final Player player = jumper.getPlayer();
          new PotionEffect(PotionEffectType.BLINDNESS, 35, 10, false, false)
              .apply(player.getPlayer());
          jumper.freeze();
          player.teleport(jumper.getUnitSpawn());
        });

    // TODO: start jump phase
  }

  @Override
  public void stop() {
    // TODO: save stats
    DeathmachtListener.unregisterListeners();
  }
}
