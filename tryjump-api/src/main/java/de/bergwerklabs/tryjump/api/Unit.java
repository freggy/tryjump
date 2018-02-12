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
    private int position = -1;


    public Unit(TryjumpModuleMetadata metadata) {
        this.name = metadata.getName();
        this.difficulty = Difficulty.getByValue(metadata.getDifficulty());
        this.timeOfCreation = metadata.getCreationTime();
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public String getName() {
        return name;
    }

    public long getTimeOfCreation() {
        return timeOfCreation;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition()
}
