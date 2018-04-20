package de.bergwerklabs.tryjump.api.event.skip;

import de.bergwerklabs.framework.commons.spigot.general.LabsEvent;
import org.bukkit.entity.Player;

/**
 * Created by Yannic Rieger on 20.04.2018.
 *
 * <p>
 *
 * @author Yannic Rieger
 */
public class PlayerSkipEvent extends LabsEvent {

  public Player getPlayer() {
    return player;
  }

  public int getNeeded() {
    return needed;
  }

  private Player player;
  private int needed;

  public PlayerSkipEvent(Player player, int needed) {
    this.player = player;
    this.needed = needed;
  }
}
