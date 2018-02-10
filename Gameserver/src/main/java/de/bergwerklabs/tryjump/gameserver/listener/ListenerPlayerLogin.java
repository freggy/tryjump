package de.bergwerklabs.tryjump.gameserver.listener;

import de.bergwerklabs.atlantis.client.bukkit.GamestateManager;
import de.bergwerklabs.atlantis.columbia.packages.gameserver.spigot.gamestate.Gamestate;
import de.bergwerklabs.tryjump.gameserver.TryJump;
import de.bergwerklabs.tryjump.gameserver.util.AtlantisStatsWrapper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

/**
 * Created by nexotekHD on 16.04.2016.
 */
public class ListenerPlayerLogin implements Listener {

    @EventHandler()
    public void onPlayerLogin(PlayerLoginEvent e)
    {
        Player p = e.getPlayer();        // handle spec
        AtlantisStatsWrapper.load(p.getUniqueId());

        // check 5 min ban
        if(!p.hasPermission("bergwerklabs.full-join"))
        {
            String value = AtlantisStatsWrapper.getLastBan(p.getUniqueId());

            if(!value.equalsIgnoreCase("0"))
            {
                Long lastban = Long.valueOf(value);
                if(lastban + 300000 > System.currentTimeMillis())
                {
                    Long milliseconds = lastban + 300000 - System.currentTimeMillis();
                    int seconds = (int) (milliseconds / 1000) % 60 ;
                    int minutes = (int) ((milliseconds / (1000*60)) % 60);
                    e.disallow(PlayerLoginEvent.Result.KICK_BANNED,"Du musst noch " + minutes+ " Minuten und "+seconds+" Sekunden warten, bevor du eine weitere Runde spielen kannst, da du die letzte Runde vorzeitig verlassen hast! Kaufe jetzt Premium, um diese Sperre zu deaktivieren: shop.bergwerklabs.de");
                }
            }
        }


        if(GamestateManager.getCurrentState() != Gamestate.WAITING)
        {
            if(p.hasPermission("bergwerklabs.team"))
                e.allow();
            System.out.println("2");
            TryJump.getInstance().getGameSession().addSpectator(p);
        }

    }
}
