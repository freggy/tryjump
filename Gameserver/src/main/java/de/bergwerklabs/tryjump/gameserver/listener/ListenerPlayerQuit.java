package de.bergwerklabs.tryjump.gameserver.listener;

import de.bergwerklabs.atlantis.client.bukkit.GamestateManager;
import de.bergwerklabs.atlantis.columbia.packages.gameserver.spigot.gamestate.Gamestate;
import de.bergwerklabs.tryjump.gameserver.TryJump;
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
        if(GamestateManager.getCurrentState() == Gamestate.RUNNING)
        {
            TryJump.getInstance().getGameSession().onPlayerQuit(e.getPlayer());
        }
        if(GamestateManager.getCurrentState() == Gamestate.RUNNING_DEATHMATCH)
        {
            TryJump.getInstance().getGameSession().onPlayerQuit(e.getPlayer());
        }
        if(Bukkit.getOnlinePlayers().size() <= 1 && GamestateManager.getCurrentState() != Gamestate.WAITING)
        {
            TryJump.getInstance().end();
            Bukkit.shutdown();
        }
        e.setQuitMessage("");
    }

}
