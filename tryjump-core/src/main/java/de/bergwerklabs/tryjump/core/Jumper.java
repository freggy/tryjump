package de.bergwerklabs.tryjump.core;

import com.google.common.base.Preconditions;
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
    private Set<TryJumpUnit> completed = new HashSet<>();
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

    /**
     * Sets the units for the player.
     *
     * @param units {@link Queue} containing the units.
     */
    public void setUnits(Queue<TryJumpUnit> units) {
        Preconditions.checkNotNull(units);
        this.unitsAhead = units;
    }

    /**
     * Gets the next unit contained in {@link TryJumpPlayer#getUnitsAhead()}.
     *
     * @return {@link Optional} containing the next unit. {@link Optional#empty()} when there is no unit left.
     */
    public Optional<TryJumpUnit> getNextUnit() {
        TryJumpUnit unit = this.unitsAhead.poll();
        if (unit == null) return Optional.empty();
        return Optional.of(unit);
    }

    /**
     * Sets the current unit for the player.
     *
     * @param unit unit to be set.
     */
    public void setCurrentUnit(Unit unit) {
        Preconditions.checkNotNull(unit);
        this.current = unit;
    }

    /**
     * Sets the current fails for the module.
     *
     * @param currentFails number of fails for this module.
     */
    public void setCurrentFails(int currentFails) {
        this.currentFails = currentFails;
    }

    public void addCompletedUnit(TryJumpUnit unit) {
        Preconditions.checkNotNull(unit);
        this.completed.add(unit);
    }
}
