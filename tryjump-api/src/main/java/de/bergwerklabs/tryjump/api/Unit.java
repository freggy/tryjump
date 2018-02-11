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

    public Unit(String name, Difficulty difficulty) {
        this.name = name;
        this.difficulty = difficulty;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public String getName() {
        return name;
    }
}
