package de.bergwerklabs.tryjump.core;

import de.bergwerklabs.framework.bedrock.api.LabsPlayer;
import de.bergwerklabs.tryjump.api.TryJumpPlayer;
import de.bergwerklabs.tryjump.api.Unit;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Created by Yannic Rieger on 11.02.2018.
 * <p>
 *
 * @author Yannic Rieger
 */
public class Jumper extends LabsPlayer implements TryJumpPlayer {

    private Queue<TryJumpUnit> unitsAhead;
    private Queue<TryJumpUnit> completed;
    private Unit current;
    private int currentFails;

    public Jumper(Player player) {
        super(player);
    }

    @Override
    public int getWins() {
        return 0;
    }

    @Override
    public int getTokens() {
        return 0;
    }

    @Override
    public int getKills() {
        return 0;
    }

    @Override
    public int getLosses() {
        return 0;
    }

    @Override
    public Unit getCurrentUnit() {
        return this.current;
    }

    // TODO: find better solutions

    @Override
    public Set<Unit> getCompletedUnits() {
        return new HashSet<>(completed);
    }

    @Override
    public Queue<Unit> getUnitsAhead() {
        return new LinkedList<>(this.unitsAhead);
    }

    @Override
    public int getCurrentFails() {
        return currentFails;
    }

    public void setUnits(Queue<TryJumpUnit> units) {
        this.unitsAhead = units;
    }

    public Optional<TryJumpUnit> getNextUnit() {
        TryJumpUnit unit = this.unitsAhead.poll();
        if (unit == null) return Optional.empty();
        return Optional.of(unit);
    }

    public void setCurrentUnit(Unit unit) {
        this.current = unit;
    }

    public void setCurrentFails(int currentFails) {
        this.currentFails = currentFails;
    }
}
