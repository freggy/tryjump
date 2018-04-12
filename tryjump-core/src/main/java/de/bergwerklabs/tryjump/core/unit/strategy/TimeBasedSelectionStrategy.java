package de.bergwerklabs.tryjump.core.unit.strategy;

import de.bergwerklabs.tryjump.core.TryJumpUnit;
import de.bergwerklabs.tryjump.core.unit.UnitList;
import de.bergwerklabs.tryjump.core.unit.UnitSelectionStrategy;

import java.util.Queue;

/**
 * Created by Yannic Rieger on 12.04.2018.
 * <p>
 *
 * @author Yannic Rieger
 */
public class TimeBasedSelectionStrategy extends UnitSelectionStrategy {

    public TimeBasedSelectionStrategy(
            UnitList easy,
            UnitList medium,
            UnitList hard,
            UnitList extreme
    ) {
        super(easy, medium, hard, extreme);
    }

    @Override
    public Queue<TryJumpUnit> createParkour() {
        return null;
    }
}
