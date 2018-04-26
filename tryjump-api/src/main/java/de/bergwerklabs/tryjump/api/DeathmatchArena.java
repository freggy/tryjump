package de.bergwerklabs.tryjump.api;

import java.util.Set;
import org.bukkit.Location;

/**
 * Created by Yannic Rieger on 22.02.2018.
 *
 * <p>
 *
 * @author Yannic Rieger
 */
public class DeathmatchArena {

  private String[] creators;
  private String name;
  private Set<Location> spawns;

  public DeathmatchArena(String[] creators, String name, Set<Location> spawns) {
    this.creators = creators;
    this.name = name;
    this.spawns = spawns;
  }

  public Set<Location> getSpawns() {
    return spawns;
  }

  public String getName() {
    return name;
  }

  public String[] getCreators() {
    return creators;
  }
}
