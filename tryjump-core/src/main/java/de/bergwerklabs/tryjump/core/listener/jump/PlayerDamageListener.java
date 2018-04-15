package de.bergwerklabs.tryjump.core.listener.jump;

import de.bergwerklabs.tryjump.core.Jumper;
import de.bergwerklabs.tryjump.core.TryJump;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

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

    @EventHandler
    private void onPlayerDamage(PlayerDeathEvent event) {
        final Player player = event.getEntity();
        final Jumper jumper = this.tryJump.getPlayerRegistry().getPlayer(player.getUniqueId());
        jumper.resetToSpawn();
    }
}
