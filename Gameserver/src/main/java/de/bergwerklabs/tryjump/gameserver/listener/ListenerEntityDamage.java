package de.bergwerklabs.tryjump.gameserver.listener;

import de.bergwerklabs.tryjump.gameserver.TryJump;
import de.bergwerklabs.util.GameState;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffectType;

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

            if(TryJump.getInstance().getCurrentState() == GameState.RUNNING)
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
            if(TryJump.getInstance().getCurrentState() == GameState.WAITING)
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
