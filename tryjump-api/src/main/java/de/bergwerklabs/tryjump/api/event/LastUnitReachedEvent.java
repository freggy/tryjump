package de.bergwerklabs.tryjump.api.event;

import de.bergwerklabs.tryjump.api.TryJumpPlayer;

/**
 * Created by Yannic Rieger on 17.04.2018.
 *
 * <p>Event gets fired when a player reaches the last checkpoint of the jump phase.
 *
 * @author Yannic Rieger
 */
public class LastUnitReachedEvent extends TryJumpEvent {

  /**
   * Token boost the player receives when completing the jump phase with 0 fails. Zero tokens when
   * finished with >= 1 fails.
   */
  public int getTokenBoost() {
    return this.tokenBoost;
  }

  private int tokenBoost;

  /**
   * @param player the {@link TryJumpPlayer} who finished the jump phase.
   * @param tokenBoost token boost the player receives when completing the jump phase with 0 fails.
   *     Zero tokens when finished with >= 1 fails.
   */
  public LastUnitReachedEvent(TryJumpPlayer player, int tokenBoost) {
    super(player);
    this.tokenBoost = tokenBoost;
  }
}
