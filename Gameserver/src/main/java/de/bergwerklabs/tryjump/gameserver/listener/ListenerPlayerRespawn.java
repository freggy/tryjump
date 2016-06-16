package de.bergwerklabs.tryjump.gameserver.listener;

import de.bergwerklabs.tryjump.gameserver.TryJump;
import de.bergwerklabs.util.GameState;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.Random;

/**
 * Created by nexotekHD on 04.05.2016.
 */
public class ListenerPlayerRespawn implements Listener {

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e)
    {
        Player p = e.getPlayer();
        if(TryJump.getInstance().getGameStateManager().getState() == GameState.RUNNING_DEATHMATCH)
        {
            Location loc = TryJump.getInstance().getDmSession().getRandomSpawn();
            e.setRespawnLocation(loc);
        }
    }

}
