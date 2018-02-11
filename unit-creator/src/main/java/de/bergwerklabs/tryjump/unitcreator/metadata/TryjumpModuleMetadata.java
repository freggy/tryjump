package de.bergwerklabs.tryjump.unitcreator.metadata;

import org.bukkit.util.Vector;

/**
 * Created by Yannic Rieger on 23.09.2017.
 * <p>
 * Metadata for a TryJump unit.
 *
 * @author Yannic Rieger
 */
public class TryjumpModuleMetadata {

    /**
     * Distance from the start point to the end point.
     */
    Vector getDistanceToEnd() {
        return distanceToEnd;
    }

    /**
     * Whether the unit is lite.
     */
    boolean isLite() {
        return isLite;
    }

    /**
     * Difficulty of the module.
     */
    int getDifficulty() {
        return difficulty;
    }

    /**
     * Gets the time when the module was created.
     */
    public long getCreationTime() {
        return creationTime;
    }

    private long creationTime;
    private Vector distanceToEnd;
    private boolean isLite;
    private int difficulty;

    /**
     * @param distanceToEnd Distance from the start point to the end point.
     * @param isLite        Whether the unit is lite.
     * @param difficulty    Difficulty of the module.
     */
    public TryjumpModuleMetadata(Vector distanceToEnd, boolean isLite, int difficulty, long creationTime) {
        this.distanceToEnd = distanceToEnd;
        this.isLite = isLite;
        this.creationTime = creationTime;
        this.difficulty = difficulty;
    }
}
