package de.bergwerklabs.tryjump.gameserver.listener;

import de.bergwerklabs.tryjump.gameserver.TryJump;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Created by nexotekHD on 16.04.2016.
 */
public class ListenerInventoryClick implements Listener {

    @EventHandler
    public void onInventroyClick(InventoryClickEvent e)
    {
        if(e.getInventory().getTitle() != null)
        {
            if(e.getInventory().getTitle().toLowerCase().contains("shop"))
            {
                if(e.getSlot() < 0)
                {
                    return;
                }
                if(e.getSlot() >= e.getInventory().getSize())
                {
                    return;
                }
                if(e.getInventory().getItem(e.getSlot()) == null)
                {
                    return;
                }
                e.setCancelled(true);
                TryJump.getInstance().getGameSession().getItemShop().onInventoryClick(e);
            }else if(e.getInventory().getTitle().toLowerCase().contains("errungenschaften"))
            {
                e.setCancelled(true);
            }
        }

    }


}
