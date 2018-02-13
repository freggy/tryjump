package de.bergwerklabs.tryjump.core;

import de.bergwerklabs.framework.bedrock.api.LabsGame;

import java.util.LinkedList;

/**
 * Created by Yannic Rieger on 11.02.2018.
 * <p>
 *
 * @author Yannic Rieger
 */
public class TryJumpGame extends LabsGame<Jumper> {

    public TryJumpGame() {
        super("TryJump");
    }

    @Override
    public void start() {
        this.getPlayerRegistry()
            .getPlayers()
            .values()
            .forEach(jumper -> jumper.setUnits(new LinkedList<>(TryJumpSession.getInstance().getPlacer().getSelectedUnits())));
        // TODO: place start points
        // TODO: tp them to their start points
        // TODO: do countdown
        // TODO: place first module.
    }

    @Override
    public void stop() {
        // TODO: save stats
    }
}
