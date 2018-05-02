package de.bergwerklabs.tryjump.api.event;

import de.bergwerklabs.tryjump.api.TryJumpPlayer;

/**
 * Created by Yannic Rieger on 02.05.2018.
 *
 * <p> Event gets fired when a player receives ranking points. This happens when completing a
 * unit, killing a player in deathmatch or winning the game.
 *
 * @author Yannic Rieger
 */
public class RankingPointsReceiveEvent extends TryJumpEvent {

  /**
   * Gets the amount ranking points a player will receive.
   */
  public int getAmount() {
    return amount;
  }

  private int amount;

  /**
   * @param player the {@link TryJumpPlayer} who receives the points.
   * @param amount the amount ranking points a player will receive.
   */
  public RankingPointsReceiveEvent(TryJumpPlayer player, int amount) {
    super(player);
    this.amount = amount;
  }
}
