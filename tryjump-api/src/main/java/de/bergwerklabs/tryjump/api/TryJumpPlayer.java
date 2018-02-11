package de.bergwerklabs.tryjump.api;

import java.util.Queue;
import java.util.Set;

/**
 * Created by Yannic Rieger on 11.02.2018.
 * <p>
 *
 * @author Yannic Rieger
 */
public interface TryJumpPlayer {

    int getWins();

    int getTokens();

    int getKills();

    int getLosses();

    Unit getCurrentUnit();

    Set<Unit> getCompletedUnits();

    Queue<Unit> getUnitsAhead();
}
