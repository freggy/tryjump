package de.bergwerklabs.tryjump.gameserver.listener;

import de.bergwerklabs.tryjump.gameserver.TryJump;
import de.bergwerklabs.util.GameState;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by nexotekHD on 16.04.2016.
 */
public class ListenerPlayerQuit implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e)
    {
        if(TryJump.getInstance().getCurrentState() == GameState.RUNNING)
        {
            TryJump.getInstance().getGameSession().onPlayerQuit(e.getPlayer());
        }
        if(TryJump.getInstance().getCurrentState() == GameState.RUNNING_DEATHMATCH)
        {
            TryJump.getInstance().getGameSession().onPlayerQuit(e.getPlayer());
        }
        if(Bukkit.getOnlinePlayers().size() <= 1 && TryJump.getInstance().getCurrentState() != GameState.WAITING)
        {
            TryJump.getInstance().end();
            Bukkit.shutdown();
        }
        e.setQuitMessage("");
    }

}
