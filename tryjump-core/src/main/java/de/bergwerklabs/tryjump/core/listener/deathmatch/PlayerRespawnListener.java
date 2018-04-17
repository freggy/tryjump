package de.bergwerklabs.tryjump.core.listener.deathmatch;

import com.google.common.collect.Iterators;
import de.bergwerklabs.tryjump.core.Jumper;
import de.bergwerklabs.tryjump.core.TryJump;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Iterator;

/**
 * Created by Yannic Rieger on 17.04.2018.
 * <p>
 *
 * @author Yannic Rieger
 */
public class PlayerRespawnListener extends DeathmachtListener {

    private Iterator<Location> spawns;

    public PlayerRespawnListener(TryJump tryJump) {
        super(tryJump);
        this.spawns = Iterators.cycle(this.tryJump.getArena().getSpawns());
    }

    @EventHandler
    private void onPlayerRespawn(PlayerRespawnEvent event) {
        final Player player = event.getPlayer();
        final Jumper jumper = this.tryJump.getPlayerRegistry().getPlayer(player.getUniqueId());
        new PotionEffect(PotionEffectType.INVISIBILITY, 20, 20, false, false).apply(player);
        event.setRespawnLocation(this.spawns.next());
        jumper.setLastRespawn(System.currentTimeMillis());
    }

}
