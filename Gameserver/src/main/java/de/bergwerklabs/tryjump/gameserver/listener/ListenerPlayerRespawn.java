package de.bergwerklabs.tryjump.gameserver.listener;

import de.bergwerklabs.tryjump.gameserver.TryJump;
import de.bergwerklabs.util.GameState;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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
            final Location loc = TryJump.getInstance().getDmSession().getRandomSpawn();
            e.setRespawnLocation(loc);
            Bukkit.getScheduler().scheduleSyncDelayedTask(TryJump.getInstance(), new Runnable() {
                @Override
                public void run() {
                    p.teleport(loc);
                }
            },2L);
            Bukkit.getScheduler().scheduleSyncDelayedTask(TryJump.getInstance(), new Runnable() {
                @Override
                public void run() {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 60, 1));
                    p.sendMessage(TryJump.getInstance().getChatPrefix() + "Du hast 3 Sekunden Spawn-Schutz!");
                }
            }, 5L);
            Bukkit.getScheduler().scheduleSyncDelayedTask(TryJump.getInstance(), new Runnable() {
                @Override
                public void run() {
                    p.teleport(loc);
                }
            },1L);

        }
    }

}
