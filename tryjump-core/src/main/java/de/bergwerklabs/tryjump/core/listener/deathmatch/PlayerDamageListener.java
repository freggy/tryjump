package de.bergwerklabs.tryjump.core.listener.deathmatch;

import de.bergwerklabs.tryjump.core.Jumper;
import de.bergwerklabs.tryjump.core.TryJump;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.concurrent.TimeUnit;

/**
 * Created by Yannic Rieger on 17.04.2018.
 * <p>
 *
 * @author Yannic Rieger
 */
public class PlayerDamageListener extends DeathmachtListener {

    public PlayerDamageListener(TryJump tryJump) {
        super(tryJump);
    }

    @EventHandler
    private void onPlayerDamage(EntityDamageEvent event) {
        final Entity entity = event.getEntity();
        if (!(entity instanceof Player)) return;

        final Player player = (Player) entity;
        final Jumper jumper = this.tryJump.getPlayerRegistry().getPlayer(player.getUniqueId());

        // TODO: make configurable
        if (TimeUnit.MILLISECONDS.toSeconds(jumper.getLastRespawn()) <= 3) {
            event.setCancelled(true);
        }
    }
}
