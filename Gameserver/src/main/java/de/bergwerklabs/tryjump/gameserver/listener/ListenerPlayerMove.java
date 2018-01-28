package de.bergwerklabs.tryjump.gameserver.listener;

import de.bergwerklabs.atlantis.client.bukkit.GamestateManager;
import de.bergwerklabs.atlantis.columbia.packages.gameserver.spigot.gamestate.Gamestate;
import de.bergwerklabs.tryjump.gameserver.TryJump;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Created by nexotekHD on 16.04.2016.
 */
public class ListenerPlayerMove implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e)
    {
        Player p = e.getPlayer();
        if(GamestateManager.getCurrentState() == Gamestate.RUNNING)
        {
            if(TryJump.getInstance().getGameSession().isFreezed())
            {
                if(e.getFrom().getX() != e.getTo().getX())
                {
                    Location loc = new Location(e.getFrom().getWorld(),e.getFrom().getX(),e.getFrom().getY(),e.getFrom().getZ());
                    loc.setYaw(p.getLocation().getYaw());
                    p.teleport(loc);
                }
                if(e.getFrom().getZ() != e.getTo().getZ())
                {
                    Location loc = new Location(e.getFrom().getWorld(),e.getFrom().getX(),e.getFrom().getY(),e.getFrom().getZ());
                    loc.setYaw(p.getLocation().getYaw());
                    p.teleport(loc);
                }
            }
        }
    }

}
