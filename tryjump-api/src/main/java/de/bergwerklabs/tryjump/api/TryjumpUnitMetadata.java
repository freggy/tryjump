package de.bergwerklabs.tryjump.api;

import org.bukkit.util.Vector;

/**
 * Created by Yannic Rieger on 23.09.2017.
 *
 * <p>Metadata for a TryJump unit.
 *
 * @author Yannic Rieger
 */
public class TryjumpUnitMetadata {

  /** Distance from the start point to the end point. */
  public double getDistanceToEnd() {
    return distanceToEnd;
  }

  /** Whether the unit is lite. */
  public boolean isLite() {
    return isLite;
  }

  /** Difficulty of the module. */
  public int getDifficulty() {
    return difficulty;
  }

  /** Gets the time when the module was created. */
  public long getCreationTime() {
    return creationTime;
  }

  /** Gets the name of the module. */
  public String getName() {
    return name;
  }

  public Vector getEndVector() {
    return endVector;
  }

  private long creationTime;
  private double distanceToEnd;
  private boolean isLite;
  private Vector endVector;
  private int difficulty;
  private String name;

  /**
   * @param distanceToEnd Distance from the start point to the end point.
   * @param isLite Whether the unit is lite.
   * @param difficulty Difficulty of the module.
   */
  public TryjumpUnitMetadata(
      String name,
      Vector endVector,
      double distanceToEnd,
      boolean isLite,
      int difficulty,
      long creationTime) {
    this.distanceToEnd = distanceToEnd;
    this.endVector = endVector;
    this.isLite = isLite;
    this.creationTime = creationTime;
    this.difficulty = difficulty;
    this.name = name;
  }
}
