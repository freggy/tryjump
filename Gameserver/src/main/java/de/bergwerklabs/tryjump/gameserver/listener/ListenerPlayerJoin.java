package de.bergwerklabs.tryjump.gameserver.listener;

import de.bergwerklabs.chat.Chat;
import de.bergwerklabs.tryjump.gameserver.TryJump;
import de.bergwerklabs.util.GameState;
import net.md_5.bungee.api.ChatColor;
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
        Chat chatPlugin = TryJump.getInstance().getChat();
        chatPlugin.setChatHandler(p, new TryJumpChatHandler(TryJump.getInstance(), p));
        if(TryJump.getInstance().getCurrentState() == GameState.WAITING)
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
                    TryJump.getInstance().getServer().broadcastMessage(TryJump.getInstance().getChatPrefix() + p.getName() + ChatColor.GRAY + " ist dem Spiel beigetreten!"); //TODO
                }
            }.runTaskLater(TryJump.getInstance(), 2L);
        } else
        {
            if(TryJump.getInstance().getCurrentState() == GameState.WAITING)
            {
                e.setJoinMessage(TryJump.getInstance().getChatPrefix() + p.getName() + ChatColor.GRAY + " ist dem Spiel beigetreten!");
            }else
            {
                e.setJoinMessage("");
            }

        }

        p.setFoodLevel(20);
        p.setHealth(20.0);


        // handle spec
        if(TryJump.getInstance().getCurrentState().getRoot() != GameState.WAITING)
        {
            System.out.println("2");
            TryJump.getInstance().getGameSession().addSpectator(p);
        }

        // load achievements
        TryJump.getInstance().getAchievementManager().loadPlayerToCache(p.getUniqueId());

    }
}
