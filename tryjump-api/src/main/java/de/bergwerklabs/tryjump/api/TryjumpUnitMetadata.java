package de.bergwerklabs.tryjump.api;

import org.bukkit.util.Vector;

/**
 * Created by Yannic Rieger on 23.09.2017.
 * <p>
 * Metadata for a TryJump unit.
 *
 * @author Yannic Rieger
 */
public class TryjumpUnitMetadata {

    /**
     * Distance from the start point to the end point.
     */
    public Vector getDistanceToEnd() {
        return distanceToEnd;
    }

    /**
     * Whether the unit is lite.
     */
    public boolean isLite() {
        return isLite;
    }

    /**
     * Difficulty of the module.
     */
    public int getDifficulty() {
        return difficulty;
    }

    /**
     * Gets the time when the module was created.
     */
    public long getCreationTime() {
        return creationTime;
    }

    /**
     * Gets the name of the module.
     */
    public String getName() {
        return name;
    }

    private long creationTime;
    private Vector distanceToEnd;
    private boolean isLite;
    private int difficulty;
    private String name;

    /**
     * @param distanceToEnd Distance from the start point to the end point.
     * @param isLite        Whether the unit is lite.
     * @param difficulty    Difficulty of the module.
     */
    public TryjumpUnitMetadata(String name, Vector distanceToEnd, boolean isLite, int difficulty, long creationTime) {
        this.distanceToEnd = distanceToEnd;
        this.isLite = isLite;
        this.creationTime = creationTime;
        this.difficulty = difficulty;
        this.name = name;
    }
}
