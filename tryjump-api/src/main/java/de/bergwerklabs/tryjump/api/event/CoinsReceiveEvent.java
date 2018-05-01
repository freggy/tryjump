package de.bergwerklabs.tryjump.api.event;

import de.bergwerklabs.tryjump.api.TryJumpPlayer;

/**
 * Created by Yannic Rieger on 01.05.2018.
 *
 * <p>
 *
 * @author Yannic Rieger
 */
public class CoinsReceiveEvent extends TryJumpEvent {

  /** Gets the amount of coins given to a player. */
  public int getAmount() {
    return amount;
  }

  private int amount;

  public CoinsReceiveEvent(TryJumpPlayer player, int amount) {
    super(player);
    this.amount = amount;
  }
}
