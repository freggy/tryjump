package de.bergwerklabs.tryjump.api.event;

import de.bergwerklabs.framework.commons.spigot.general.LabsEvent;
import de.bergwerklabs.tryjump.api.TryJumpPlayer;

/**
 * Created by Yannic Rieger on 14.04.2018.
 * <p>
 *
 * @author Yannic Rieger
 */
public abstract class TryJumpEvent extends LabsEvent {

    public TryJumpPlayer getPlayer() {
        return this.player;
    }

    protected TryJumpPlayer player;

    TryJumpEvent(TryJumpPlayer player) {
        this.player = player;
    }
}
