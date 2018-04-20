package de.bergwerklabs.tryjump.api.event.skip;

import de.bergwerklabs.framework.commons.spigot.general.LabsEvent;
import java.util.Set;
import org.bukkit.entity.Player;

/**
 * Created by Yannic Rieger on 20.04.2018.
 *
 * <p>
 *
 * @author Yannic Rieger
 */
public class SkipSuccessfulEvent extends LabsEvent {

  public int getPlayersNeeded() {
    return playersNeeded;
  }

  public Set<Player> getPlayersSkiped() {
    return playersSkiped;
  }

  private int playersNeeded;
  private Set<Player> playersSkiped;

  public SkipSuccessfulEvent(int playersNeeded, Set<Player> playersSkiped) {
    this.playersNeeded = playersNeeded;
    this.playersSkiped = playersSkiped;
  }
}
