package de.bergwerklabs.tryjump.api;

/**
 * Created by Yannic Rieger on 11.02.2018.
 * <p>
 *
 * @author Yannic Rieger
 */
public abstract class Unit {

    private String name;
    private Difficulty difficulty;
    private long timeOfCreation;


    public Unit(TryjumpModuleMetadata metadata) {
        this.name = metadata.getName();
        this.difficulty = difficulty;
        this.timeOfCreation = metadata.getCreationTime();
        this.difficulty =
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public String getName() {
        return name;
    }
}
