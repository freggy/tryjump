package de.bergwerklabs.tryjump.core;

import de.bergwerklabs.atlantis.client.bukkit.GamestateManager;
import de.bergwerklabs.atlantis.columbia.packages.gameserver.spigot.gamestate.Gamestate;
import de.bergwerklabs.framework.bedrock.api.LabsGame;
import de.bergwerklabs.framework.bedrock.api.event.session.SessionDonePreparationEvent;
import de.bergwerklabs.framework.bedrock.api.session.MinigameSession;
import de.bergwerklabs.tryjump.core.config.Config;
import de.bergwerklabs.tryjump.core.listener.PlayerJoinListener;
import de.bergwerklabs.tryjump.core.listener.PlayerQuitListener;
import de.bergwerklabs.tryjump.core.unit.UnitPlacer;
import java.io.File;
import java.util.Optional;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

/**
 * Created by Yannic Rieger on 11.02.2018.
 *
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

  public Config getTryJumpConfig() {
    return config;
  }

  private static TryJumpSession instance;
  private UnitPlacer placer;
  private Config config;

  @Override
  public LabsGame getGame() {
    return this.tryJump;
  }

  private TryJump tryJump = new TryJump();
  private final Logger logger = Bukkit.getLogger();
  private MapManager mapManager;

  @Override
  public void prepare() {
    instance = this;
    Optional<Config> optional = Config.read(new File(this.getDataFolder() + "/config.json"));

    if (optional.isPresent()) {
      this.config = optional.get();
    } else {
      logger.warning("Error while reading config, aborting...");
      GamestateManager.setGamestate(Gamestate.FAILED);
      Bukkit.getServer().shutdown();
    }

    // ONLY FOR TEST PURPOSES [START]
    try {
      FileUtils.deleteDirectory(new File("/development/gameserver/tryjump_rework/jump"));
      Thread.sleep(2000);
    } catch (Exception e) {
      e.printStackTrace();
    }
    // ONLY FOR TEST PURPOSES [END]

    // Folder structure is:
    //  - /${datafolder}/units/${difficulty}/lite/
    //  - /${datafolder}/units/${difficulty}/default/
    //
    // Example:
    //  - /${datafolder}/units/hard/lite/myUnit_hard_lite.schematic
    //  - /${datafolder}/units/hard/default/myUnit_hard.schematic

    final String basePath = this.getDataFolder().getAbsolutePath() + "/units/";


    this.mapManager = new MapManager(new File(this.getDataFolder().getAbsolutePath() + "/arenas"));

    this.placer =
        new UnitPlacer(
            new File(basePath + "easy"),
            new File(basePath + "medium"),
            new File(basePath + "hard"),
            new File(basePath + "extreme"),
            new File(basePath + "start.schematic"),
            this.config.getSelectionStrategy());

    this.createAndPrepareWorld();

    Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
    Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(), this);
    Bukkit.getPluginManager().callEvent(new SessionDonePreparationEvent(this));
  }

  private void createAndPrepareWorld() {
    World world =
        new WorldCreator("jump")
            .type(WorldType.FLAT)
            .generatorSettings("2;0")
            .generateStructures(false)
            .environment(World.Environment.NORMAL)
            .createWorld();
    world.setGameRuleValue("keepInventory", "true");
    world.setGameRuleValue("doTileDrops", "false");
    world.setGameRuleValue("showDeathMessages", "false");
  }
}
