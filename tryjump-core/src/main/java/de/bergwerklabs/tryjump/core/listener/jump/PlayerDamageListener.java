package de.bergwerklabs.tryjump.core.listener.jump;

import de.bergwerklabs.tryjump.core.Jumper;
import de.bergwerklabs.tryjump.core.TryJump;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * Created by Yannic Rieger on 14.04.2018.
 * <p>
 *
 * @author Yannic Rieger
 */
public class PlayerDamageListener extends JumpPhaseListener {

    public PlayerDamageListener(TryJump tryJump) {
        super(tryJump);
    }

    private void onPlayerDamage(EntityDamageEvent event) {
        final Entity entity = event.getEntity();
        if (!(entity instanceof Player)) return;

        System.out.println(event.getCause());

        if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {
            final Player player = (Player)entity;
            final Jumper jumper = this.tryJump.getPlayerRegistry().getPlayer(player.getUniqueId());
            jumper.resetToSpawn();
        }
        event.setCancelled(true);
    }
}
