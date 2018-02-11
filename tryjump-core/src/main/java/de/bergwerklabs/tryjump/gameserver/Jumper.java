package de.bergwerklabs.tryjump.gameserver;

import de.bergwerklabs.framework.bedrock.api.LabsPlayer;
import de.bergwerklabs.tryjump.api.TryJumpPlayer;
import de.bergwerklabs.tryjump.api.Unit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 * Created by Yannic Rieger on 11.02.2018.
 * <p>
 *
 * @author Yannic Rieger
 */
public class Jumper extends LabsPlayer implements TryJumpPlayer {

    private Queue<TryJumpUnit> unitsAhead;
    private Queue<TryJumpUnit> completed;


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
        return null;
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

    public void setUnits(Queue<TryJumpUnit> units) {
        this.unitsAhead = units;
    }
}
