package de.bergwerklabs.tryjump.core.listener.deathmatch;

import de.bergwerklabs.tryjump.core.TryJump;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Created by Yannic Rieger on 11.02.2018.
 *
 * <p>
 *
 * @author Yannic Rieger
 */
public class EntityDamageByEntityEventListener extends DeathmachtListener {

  public EntityDamageByEntityEventListener(TryJump tryJump) {
    super(tryJump);
  }

  @EventHandler
  private void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
    final Entity killer = event.getDamager();
    final Entity killed = event.getEntity();
    if (!(killed instanceof Player) || !(killer instanceof Player)) return;

    final Player killingPlayer = (Player) killer;
    final Player killedPlayer = (Player) killed;

    if (killedPlayer.getHealth() > 0) return;

    this.tryJump
        .getPlayerRegistry()
        .getPlayerCollection()
        .forEach(
            jumper -> {
              // TODO: use rank color and maybe use different messages each time a player dies.
              this.tryJump
                  .getMessenger()
                  .message(
                      "§a"
                          + killingPlayer.getDisplayName()
                          + " §7hat§a"
                          + killedPlayer.getDisplayName()
                          + " getötet.",
                      jumper.getPlayer());
            });
  }
}
