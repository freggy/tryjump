package de.bergwerklabs.tryjump.gameserver.listener;

import de.bergwerklabs.chat.IPlayerSpecificChatHandler;
import de.bergwerklabs.tryjump.gameserver.TryJump;
import de.bergwerklabs.util.GameState;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

/**
 * Created by nexotekHD on 18.05.2016.
 */
public class TryJumpChatHandler implements IPlayerSpecificChatHandler {

    private Player p;
    private TryJump pluginInstance;

    public TryJumpChatHandler(TryJump pluginInstance, Player p)
    {
        this.pluginInstance = pluginInstance;
        this.p = p;
    }


    @Override
    public Player getPlayer() {
        return p;
    }

    @Override
    public void processEvent(AsyncPlayerChatEvent e, String userPrefix) {
        String prefix = TryJump.getInstance().getColor(p);
        e.setFormat(prefix + "%1$s" + ChatColor.GRAY + ": " + ChatColor.WHITE + "%2$s");
        if(pluginInstance.getCurrentState() == GameState.RUNNING)
        {
            if(!TryJump.getInstance().getGameSession().getIngame_players().contains(p.getUniqueId()))
            {
                for(UUID uuid : TryJump.getInstance().getGameSession().getIngame_players())
                {
                    Player pl = Bukkit.getPlayer(uuid);
                    e.getRecipients().remove(pl);

                }
                e.setFormat(ChatColor.GRAY + "[SPEC] " + e.getFormat());
            }
        }
    }
}
