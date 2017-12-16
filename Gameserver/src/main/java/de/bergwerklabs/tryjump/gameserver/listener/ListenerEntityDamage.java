package de.bergwerklabs.tryjump.gameserver.listener;

import de.bergwerklabs.atlantis.client.bukkit.GamestateManager;
import de.bergwerklabs.atlantis.columbia.packages.gameserver.spigot.gamestate.Gamestate;
import de.bergwerklabs.tryjump.gameserver.TryJump;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * Created by nexotekHD on 14.04.2016.
 */
public class ListenerEntityDamage implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e)
    {
        if(e.getEntityType() == EntityType.PLAYER)
        {
            Player p = (Player)e.getEntity();

            if(GamestateManager.getCurrentState() == Gamestate.RUNNING)
            {
                if(e.getCause() == EntityDamageEvent.DamageCause.VOID)
                {
                    e.setCancelled(true);
                    TryJump.getInstance().getGameSession().onJumpFail(p);
                }
                if(e.getCause() == EntityDamageEvent.DamageCause.FALL)
                {
                    e.setCancelled(true);
                }
                e.setCancelled(true);
            }
            if(GamestateManager.getCurrentState() == Gamestate.WAITING)
            {
                e.setCancelled(true);
            }
            if(TryJump.getInstance().getGameSession().hasFinished())
            {
                e.setCancelled(true);
            }
        }
    }

}
