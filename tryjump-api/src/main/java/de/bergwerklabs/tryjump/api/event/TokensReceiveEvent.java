package de.bergwerklabs.tryjump.api.event;

import de.bergwerklabs.tryjump.api.TryJumpPlayer;

/**
 * Created by Yannic Rieger on 28.04.2018.
 *
 * <p>Event gets fired when a player receives tokens. This happens when the player completes a unit.
 *
 * @author Yannic Rieger
 */
public class TokensReceiveEvent extends TryJumpEvent {

  /** Gets the amount of tokens that have been added to a {@link TryJumpPlayer}. */
  public int getAmount() {
    return amount;
  }

  private int amount;

  public TokensReceiveEvent(TryJumpPlayer player, int amount) {
    super(player);
    this.amount = amount;
  }
}
