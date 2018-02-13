package de.bergwerklabs.tryjump.api;

/**
 * Created by Yannic Rieger on 11.02.2018.
 * <p>
 * This class represents a Unit that a player has to complete.
 *
 * @author Yannic Rieger
 */
public abstract class Unit {

    private String name;
    private Difficulty difficulty;
    private long timeOfCreation;
    private int position = -1;

    /**
     * @param metadata metadata of the unit.
     */
    public Unit(TryjumpModuleMetadata metadata) {
        this.name = metadata.getName();
        this.difficulty = Difficulty.getByValue(metadata.getDifficulty());
        this.timeOfCreation = metadata.getCreationTime();
    }

    /**
     * Gets the {@link Difficulty} of this unit.
     */
    public Difficulty getDifficulty() {
        return difficulty;
    }

    /**
     * Gets the name of this unit.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the time in milliseconds when this unit was created.
     */
    public long getTimeOfCreation() {
        return timeOfCreation;
    }

}
