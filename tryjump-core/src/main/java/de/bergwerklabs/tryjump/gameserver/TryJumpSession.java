package de.bergwerklabs.tryjump.gameserver;

import de.bergwerklabs.framework.bedrock.api.LabsGame;
import de.bergwerklabs.framework.bedrock.api.session.MinigameSession;

/**
 * Created by Yannic Rieger on 11.02.2018.
 * <p>
 *
 * @author Yannic Rieger
 */
public class TryJumpSession extends MinigameSession {

    public static TryJumpSession getInstance() {
        return instance;
    }

    public UnitPlacer getPlacer() {
        return placer;
    }

    private static TryJumpSession instance;
    private UnitPlacer placer;



    @Override
    public LabsGame getGame() {
        return null;
    }

    @Override
    public void prepare() {
        instance = this;
        placer = new UnitPlacer()
    }
}
