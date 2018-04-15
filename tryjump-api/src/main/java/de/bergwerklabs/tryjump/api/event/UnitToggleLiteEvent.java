package de.bergwerklabs.tryjump.api.event;

import de.bergwerklabs.tryjump.api.TryJumpPlayer;
import de.bergwerklabs.tryjump.api.Unit;

/**
 * Created by Yannic Rieger on 12.04.2018.
 * <p>
 *
 * @author Yannic Rieger
 */
public class UnitToggleLiteEvent extends TryJumpEvent {

    private Unit currentUnit;

    public UnitToggleLiteEvent(TryJumpPlayer player, Unit unit) {
        super(player);
        currentUnit = unit;
    }

    public Unit getCurrentUnit() {
        return currentUnit;
    }
}
