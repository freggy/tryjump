package de.bergwerklabs.tryjump.core.phase.deathmatch.listener;

import de.bergwerklabs.tryjump.core.TryJumpSession;
import de.bergwerklabs.tryjump.core.phase.deathmatch.DeathmatchPhase;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * Created by Yannic Rieger on 11.05.2018.
 *
 * <p>
 *
 * @author Yannic Rieger
 */
public class BlockPlaceListener extends DeathmachtListener {

  BlockPlaceListener(DeathmatchPhase phase, TryJumpSession session) {
    super(phase, session);
  }

  @EventHandler
  private void onBlockPlace(BlockPlaceEvent event) {
    final Block block = event.getBlock();
    final Player player = event.getPlayer();

    if (block.getType() == Material.CAKE_BLOCK) {
      Material belowBlock = block.getRelative(BlockFace.DOWN).getType();
      Material belowPlayer = player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType();
      if (belowPlayer == Material.CAKE_BLOCK || belowBlock == Material.CAKE_BLOCK) {
        event.setCancelled(true);
      }
    }
    else if(block.getType() == Material.TNT) {
      event.setCancelled(false);
      block.setType(Material.AIR);
      TNTPrimed tnt = block.getWorld().spawn(block.getLocation(),
          TNTPrimed.class);
      tnt.setFuseTicks(40);
    }
    else event.setCancelled(true);
  }
}
