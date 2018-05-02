package de.bergwerklabs.tryjump.core;

import de.bergwerklabs.framework.bedrock.api.LabsGame;
import de.bergwerklabs.framework.bedrock.api.PlayerRegistry;
import de.bergwerklabs.framework.bedrock.api.event.game.GameStopEvent;
import de.bergwerklabs.tryjump.api.DeathmatchArena;
import de.bergwerklabs.tryjump.core.phase.buy.BuyPhase;
import de.bergwerklabs.tryjump.core.phase.deathmatch.DeathmatchPhase;
import de.bergwerklabs.tryjump.core.phase.jump.JumpPhase;
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

  private DeathmatchArena arena;
  private DeathmatchPhase deathmatchPhase;
  private BuyPhase buyPhase;
  private JumpPhase jumpPhase;

  public TryJump() {
    super("TryJump");
  }

  public DeathmatchArena getArena() {
    return arena;
  }

  public void setArena(DeathmatchArena arena) {
    this.arena = arena;
  }

  public JumpPhase getJumpPhase() {
    return jumpPhase;
  }

  public BuyPhase getBuyPhase() {
    return buyPhase;
  }

  public DeathmatchPhase getDeathmatchPhase() {
    return deathmatchPhase;
  }

  @Override
  public void start(PlayerRegistry<Jumper> registry) {
    this.playerRegistry = registry;
    final Collection<Jumper> players = registry.getPlayerCollection();

    final TryJumpSession session = TryJumpSession.getInstance();
    final Location spawn = new Location(Bukkit.getWorld("jump"), 0, 100, 0);
    final double[] count = {35};

    this.jumpPhase = new JumpPhase(session);
    this.buyPhase = new BuyPhase(session);
    this.deathmatchPhase = new DeathmatchPhase(session);

    JumpPhaseListener.registerListeners(session, this.getJumpPhase());

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

    this.getJumpPhase().start();
  }

  @Override
  public void stop() {
    Bukkit.getPluginManager().callEvent(new GameStopEvent<>(this));
  }
}
