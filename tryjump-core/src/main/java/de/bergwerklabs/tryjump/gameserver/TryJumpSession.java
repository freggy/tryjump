package de.bergwerklabs.tryjump.gameserver;

import de.bergwerklabs.framework.bedrock.api.LabsGame;
import de.bergwerklabs.framework.bedrock.api.session.MinigameSession;
import de.bergwerklabs.tryjump.gameserver.unit.UnitPlacer;

import java.io.File;

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

        // Folder structure is:
        //  - /${datafolder}/units/${difficulty}/lite/
        //  - /${datafolder}/units/${difficulty}/default/
        //
        // Example:
        //  - /${datafolder}/units/easy/lite/myUnit_hard_lite.schematic
        //  - /${datafolder}/units/easy/default/myUnit_hard.schematic

        String basePath = this.getDataFolder().getAbsolutePath() + "/units/";
        this.placer = new UnitPlacer(new File(basePath + "easy/default"),
                                     new File(basePath + "medium/default"),
                                     new File(basePath + "hard/default"),
                                     new File(basePath + "extreme/default"));
    }
}
