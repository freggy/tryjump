package de.bergwerklabs.tryjump.api;

import java.util.Queue;
import java.util.Set;

/**
 * Created by Yannic Rieger on 11.02.2018.
 *
 * <p>Represents a player playing TryJump. This class contains useful information about the player
 * currently playing it.
 *
 * @author Yannic Rieger
 */
public interface TryJumpPlayer {

  /** Gets the total wins of this player. */
  int getWins();

  /** Gets the current tokens earned. */
  int getTokens();

  /** Gets the total amount of kills of this player. */
  int getTotalKills();

  /** Gets the current amount of kills. */
  int getCurrentKills();

  /** Gets the total amount of losses of this player. */
  int getLosses();

  /** Gets the amount of lives left. */
  int getLivesLeft();

  /**
   * Gets the current fails for the {@link Unit} the player is at. To know which unit is the current
   * use {@link TryJumpPlayer#getCurrentUnit()}.
   */
  int getCurrentFails();

  /**
   * Gets the current total fails. To know which unit is the current use {@link
   * TryJumpPlayer#getCurrentUnit()}.
   */
  int getTotalFails();

  /** Determines whether or not the player currently plays a lite unit. */
  boolean isLite();

  /** Gets the current {@link Unit} the player is currently at. */
  Unit getCurrentUnit();

  /** Gets the {@link Unit}s the player has already completed in this round. */
  Set<Unit> getCompletedUnits();

  /** Gets the {@link Unit}s that are ahead of this player in this round. */
  Queue<Unit> getUnitsAhead();
}
