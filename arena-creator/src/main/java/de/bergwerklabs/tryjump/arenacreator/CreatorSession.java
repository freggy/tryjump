package de.bergwerklabs.tryjump.arenacreator;

import java.util.Stack;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Created by Yannic Rieger on 22.02.2018.
 *
 * <p>
 *
 * @author Yannic Rieger
 */
public class CreatorSession {

  public Player getPlayer() {
    return player;
  }

  private final Player player;
  private final Stack<Location> spawns;

  CreatorSession(Player player) {
    this.player = player;
    this.spawns = new Stack<>();
  }

  public void addSpawn(Location location) {
    this.spawns.push(location);
  }

  public void removeSpawn() {
    if (this.spawns.empty()) return;
    this.spawns.pop();
  }
}
