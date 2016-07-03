package de.bergwerklabs.tryjump.gameserver.listener;

import de.bergwerklabs.tryjump.gameserver.TryJump;
import de.bergwerklabs.util.GameState;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by nexotekHD on 03.07.2016.
 */
public class ListenerEntityDamageByEntity implements Listener {

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e)
    {
        if(e.getEntity().getType() != EntityType.PLAYER)
        {
            return;
        }
        Player p = (Player)e.getEntity();
        if(TryJump.getInstance().getCurrentState() == GameState.RUNNING_DEATHMATCH)
        {
            if(p.hasPotionEffect(PotionEffectType.INVISIBILITY))
            {
                e.setCancelled(true);
            }
            if(e.getDamager().getType() == EntityType.PLAYER)
            {
                Player damager = (Player)e.getDamager();
                if(damager.hasPotionEffect(PotionEffectType.INVISIBILITY))
                {
                    damager.removePotionEffect(PotionEffectType.INVISIBILITY);
                }
            }
        }
    }

}
