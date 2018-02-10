package de.bergwerklabs.tryjump.gameserver.listener;

import de.bergwerklabs.atlantis.client.bukkit.GamestateManager;
import de.bergwerklabs.atlantis.columbia.packages.gameserver.spigot.gamestate.Gamestate;
import de.bergwerklabs.tryjump.gameserver.TryJump;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by nexotekHD on 13.04.2016.
 */
public class ListenerPlayerJoin implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        Player p = e.getPlayer();
        p.getInventory().clear();
        if(GamestateManager.getCurrentState() == Gamestate.WAITING)
        {
            ItemStack achievements = new ItemStack(Material.NETHER_STAR);
            ItemMeta im = achievements.getItemMeta();
            im.setDisplayName(ChatColor.AQUA + "Errungenschaften");
            achievements.setItemMeta(im);
            p.getInventory().setItem(4, achievements);
            p.setGameMode(GameMode.ADVENTURE);
        }

        for(int i = 0; i < 20;i++) p.sendMessage("");

        if (p.hasPermission("bergwerklabs.nick"))
        {
            e.setJoinMessage("");
            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    if(GamestateManager.getCurrentState() == Gamestate.WAITING)
                    {
                        TryJump.getInstance().getServer().broadcastMessage(TryJump.getInstance().getChatPrefix() + p.getDisplayName() + ChatColor.GRAY + " ist dem Spiel beigetreten!"); //TODO
                    }

                }
            }.runTaskLater(TryJump.getInstance(), 2L);
        } else
        {
            if(GamestateManager.getCurrentState() == Gamestate.WAITING)
            {
                e.setJoinMessage(TryJump.getInstance().getChatPrefix() + p.getDisplayName() + ChatColor.GRAY + " ist dem Spiel beigetreten!");
            }else
            {
                e.setJoinMessage("");
            }

        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(TryJump.getInstance(), new Runnable() {
            @Override
            public void run() {
                for(Player pl : Bukkit.getOnlinePlayers())
                {
                    pl.showPlayer(p);
                }
            }
        },40L);

        p.setFoodLevel(20);
        p.setHealth(20.0);


        // handle spec
        if(GamestateManager.getCurrentState() != Gamestate.WAITING)
        {
            System.out.println("2");
            TryJump.getInstance().getGameSession().addSpectator(p);
        }
    }
}
