package de.bergwerklabs.tryjump.core;

import de.bergwerklabs.framework.bedrock.api.LabsGame;
import de.bergwerklabs.framework.bedrock.api.event.session.SessionDonePreparationEvent;
import de.bergwerklabs.framework.bedrock.api.session.MinigameSession;
import de.bergwerklabs.tryjump.core.listener.PlayerJoinListener;
import de.bergwerklabs.tryjump.core.unit.UnitPlacer;
import de.bergwerklabs.tryjump.core.unit.UnitSelectionStrategy;
import de.bergwerklabs.tryjump.core.unit.strategy.SelectionStrategy;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

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
        return this.tryJump;
    }

    private TryJump tryJump = new TryJump();

    @Override
    public void prepare() {
        instance = this;
        // Folder structure is:
        //  - /${datafolder}/units/${difficulty}/lite/
        //  - /${datafolder}/units/${difficulty}/default/
        //
        // Example:
        //  - /${datafolder}/units/hard/lite/myUnit_hard_lite.schematic
        //  - /${datafolder}/units/hard/default/myUnit_hard.schematic

        // ONLY FOR TEST PURPOSES [START]
        try {
            FileUtils.deleteDirectory(new File("/development/gameserver/tryjump_rework/jump"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        // ONLY FOR TEST PURPOSES [END]

        final String basePath = this.getDataFolder().getAbsolutePath() + "/units/";

        this.placer = new UnitPlacer(
                new File(basePath + "easy"),
                new File(basePath + "medium"),
                new File(basePath + "hard"),
                new File(basePath + "extreme"),
                new File(basePath + "start.schematic"),
                SelectionStrategy.RANDOM // TODO: make configurable
        );

        this.getServer().createWorld(new WorldCreator("jump").type(WorldType.FLAT).generatorSettings("2;0").generateStructures(false));
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Bukkit.getPluginManager().callEvent(new SessionDonePreparationEvent(this));
    }
}
