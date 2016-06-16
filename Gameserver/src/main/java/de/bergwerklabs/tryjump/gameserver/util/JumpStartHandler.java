package de.bergwerklabs.tryjump.gameserver.util;

import de.bergwerklabs.tryjump.gameserver.TryJump;
import de.bergwerklabs.util.mechanic.StartTimer;
import org.bukkit.entity.Player;

/**
 * Created by nexotekHD on 16.04.2016.
 */
public class JumpStartHandler implements StartTimer.StartHandler {

    @Override
    public void handle(Player[] players) {
        for(Player p : players)
        {
            TryJump.getInstance().getGameSession().addGamePlayer(p);
        }
        TryJump.getInstance().getGameSession().start();
    }
}
