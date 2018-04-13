package de.bergwerklabs.tryjump.core;

import de.bergwerklabs.framework.commons.spigot.title.ActionbarTitle;
import de.bergwerklabs.tryjump.api.Unit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Created by Yannic Rieger on 12.04.2018.
 * <p>
 *
 * @author Yannic Rieger
 */
public class UpdatePlayerInfoTask implements Runnable {

    private Plugin plugin;
    private TryJump tryJump;

    public UpdatePlayerInfoTask(Plugin plugin, TryJump tryJump) {
        this.plugin = plugin;
        this.tryJump = tryJump;
    }

    @Override
    public void run() {
        this.tryJump.getPlayerRegistry().getPlayerCollection().forEach(jumper -> {
            final Player spigotPlayer = jumper.getPlayer();
            ActionbarTitle.send(spigotPlayer, jumper.buildActionbarText());
            jumper.updateScoreboardProgress(spigotPlayer.getLocation());
        });
    }



}
