package de.bergwerklabs.tryjump.core.listener;

import de.bergwerklabs.atlantis.client.bukkit.GamestateManager;
import de.bergwerklabs.atlantis.columbia.packages.gameserver.spigot.gamestate.Gamestate;
import de.bergwerklabs.tryjump.core.Jumper;
import de.bergwerklabs.tryjump.core.TryJumpSession;
import de.bergwerklabs.tryjump.core.TryJumpUnit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Optional;

/**
 * Created by Yannic Rieger on 11.02.2018.
 * <p>
 * Handles event when players activate the gold pressure plate.
 *
 * @author Yannic Rieger
 */
public class PlayerInteractListener implements Listener {

    @EventHandler
    private void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.PHYSICAL) return;
        final Block clicked = event.getClickedBlock();
        if (clicked.getType() != Material.GOLD_PLATE) return;

        clicked.setType(Material.AIR);
        event.setCancelled(true);

        Player player = event.getPlayer();
        Jumper jumper = (Jumper) TryJumpSession.getInstance()
                                               .getGame()
                                               .getPlayerRegistry()
                                               .getPlayer(player.getUniqueId());

        Optional<TryJumpUnit> unitOptional = jumper.getNextUnit();
        TryJumpUnit current = (TryJumpUnit) jumper.getCurrentUnit();

        if (unitOptional.isPresent()) {
            TryJumpUnit next = unitOptional.get();
            this.handleNext(jumper, next);
            jumper.setCurrentUnit(next);
            jumper.addCompletedUnit(current);
        }
        else this.handleLast(jumper);
    }

    private void handleLast(Jumper jumper) {
        // TODO: add tokens
        // TODO: display messages
        // TODO: start deathmatch
    }

    private void handleNext(Jumper jumper, TryJumpUnit unit) {
        TryJumpSession.getInstance().getPlacer().placeUnit(jumper.getPlayer().getLocation().clone().subtract(0, 1, 0), unit, false);
        jumper.setCurrentFails(0);
        // TODO: display messages
    }
}
