package de.bergwerklabs.tryjump.core.listener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created by Yannic Rieger on 14.04.2018.
 *
 * <p>
 *
 * @author Yannic Rieger
 */
public class PlayerJoinListener implements Listener {

  @EventHandler
  private void onPlayerJoin(PlayerJoinEvent event) {
    final Location location = new Location(Bukkit.getWorld("spawn"), -29.5, 108.5, -21.5);
    location.setYaw(0);
    location.setYaw(-50);
    event.getPlayer().teleport(location);
  }
}
