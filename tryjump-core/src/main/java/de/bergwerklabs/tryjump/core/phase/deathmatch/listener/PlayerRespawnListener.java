package de.bergwerklabs.tryjump.core.phase.deathmatch.listener;

import com.google.common.collect.Iterators;
import de.bergwerklabs.tryjump.core.Jumper;
import de.bergwerklabs.tryjump.core.TryJumpSession;
import de.bergwerklabs.tryjump.core.phase.deathmatch.DeathmatchPhase;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by Yannic Rieger on 17.04.2018.
 *
 * <p>
 *
 * @author Yannic Rieger
 */
class PlayerRespawnListener extends DeathmachtListener {

  private Iterator<Location> spawns;

  PlayerRespawnListener(DeathmatchPhase phase, TryJumpSession session) {
    super(phase, session);
    this.spawns = Iterators.cycle(this.tryJump.getArena().getSpawns());
  }

  @EventHandler
  private void onPlayerRespawn(PlayerRespawnEvent event) {
    final Player player = event.getPlayer();
    final Jumper jumper = this.tryJump.getPlayerRegistry().getPlayer(player.getUniqueId());

    // We have to delay this by 10 ticks because otherwise the player would not be invisible.
    // This is strange Mincraft behavior, which I can't change so keep it like that.
    Bukkit.getScheduler()
        .runTaskLater(
            this.session,
            () -> {
              player.addPotionEffect(
                  new PotionEffect(
                      PotionEffectType.INVISIBILITY,
                      20 * this.session.getTryJumpConfig().getInvulnerabilityDuration(),
                      100,
                      false,
                      false));
            },
            10);

    // By using a circular iterator players should not spawn in the same spot
    // the side effect is that, if one knows all the spawn points, he could predict the spawn of the
    // next player,
    // but that is highly unlikely and does not provide him with a big advantage in my opinion.
    jumper.setLastRespawn(System.currentTimeMillis());
    event.setRespawnLocation(this.spawns.next());
  }
}
