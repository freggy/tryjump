package de.bergwerklabs.tryjump.gameserver.listener;

import de.bergwerklabs.atlantis.client.bukkit.GamestateManager;
import de.bergwerklabs.atlantis.columbia.packages.gameserver.spigot.gamestate.Gamestate;
import de.bergwerklabs.tryjump.api.Unit;
import de.bergwerklabs.tryjump.gameserver.Jumper;
import de.bergwerklabs.tryjump.gameserver.TryJumpSession;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Created by Yannic Rieger on 11.02.2018.
 * <p>
 *
 * @author Yannic Rieger
 */
public class PlayerInteractListener implements Listener {

    @EventHandler
    private void onPlayerInteract(PlayerInteractEvent event) {
        if (GamestateManager.getCurrentState() != Gamestate.RUNNING) return;
        if (event.getAction() != Action.PHYSICAL) return;
        if (event.getClickedBlock().getType() != Material.GOLD_PLATE) return;

        Player player = event.getPlayer();
        Jumper jumper = (Jumper) TryJumpSession.getInstance()
                                               .getGame()
                                               .getPlayerRegistry()
                                               .getPlayer(player.getUniqueId());

        boolean isLast = jumper.getUnitsAhead().peek() == null;

        if (isLast) {
            handleLast();
            return;
        }

        Unit next = jumper.getUnitsAhead().poll();

        // TODO: get tokens
        // TODO: display messages
    }

    private void handleLast() {

    }
}
