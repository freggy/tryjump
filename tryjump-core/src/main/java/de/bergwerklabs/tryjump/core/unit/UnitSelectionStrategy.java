package de.bergwerklabs.tryjump.core.unit;

import de.bergwerklabs.tryjump.core.TryJumpUnit;

import java.util.Queue;

/**
 * Created by Yannic Rieger on 12.04.2018.
 * <p>
 *
 * @author Yannic Rieger
 */
public abstract class UnitSelectionStrategy {

    protected UnitList easy;
    protected UnitList medium;
    protected UnitList hard;
    protected UnitList extreme;

    public UnitSelectionStrategy(UnitList easy,
                                 UnitList medium,
                                 UnitList hard,
                                 UnitList extreme
    ) {
        this.easy = easy;
        this.medium = medium;
        this.hard = hard;
        this.extreme = extreme;
    }

    /**
     *
     * @return
     */
    public abstract Queue<TryJumpUnit> createParkour();
}
