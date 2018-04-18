package de.bergwerklabs.tryjump.api.event;

import de.bergwerklabs.tryjump.api.TryJumpPlayer;

/**
 * Created by Yannic Rieger on 17.04.2018.
 *
 * <p>
 *
 * @author Yannic Rieger
 */
public class LastUnitReachedEvent extends TryJumpEvent {

  public LastUnitReachedEvent(TryJumpPlayer player) {
    super(player);
  }
}
