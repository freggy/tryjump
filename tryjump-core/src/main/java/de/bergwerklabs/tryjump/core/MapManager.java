package de.bergwerklabs.tryjump.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.bergwerklabs.tryjump.api.DeathmatchArena;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Optional;
import org.apache.commons.io.FileUtils;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.WorldCreator;

/**
 * Created by Yannic Rieger on 19.04.2018.
 *
 * <p>Manages all {@link DeathmatchArena} related things and provides methods to retrieve arenas.
 *
 * @author Yannic Rieger
 */
class MapManager {

  private File[] arenas;
  private File arenaDir;
  private Gson gson =
      new GsonBuilder()
          .registerTypeAdapter(DeathmatchArena.class, new ArenaDeserializer())
          .create();

  /** @param arenaDir directory where the deathmatch arenas are located. */
  MapManager(File arenaDir) {
    this.arenaDir = arenaDir;
    this.arenas = arenaDir.listFiles(File::isDirectory);
  }

  /**
   * Chooses a random deathmatch arena using {@link SecureRandom} and loads it in.
   *
   * @return a randomly selected arena. {@link Optional#empty()} when no arena could be found.
   */
  public Optional<DeathmatchArena> chooseArenaRandomlyAndLoad() {
    final SecureRandom random = new SecureRandom();
    final File arena = this.arenas[random.nextInt(this.arenas.length)];

    try {
      final File destination = new File(Paths.get("").toAbsolutePath().toString());
      FileUtils.copyDirectory(this.arenaDir, destination);
      this.prepareAndCreateArenaWorld(arena.getName());
      return this.readMapConfig(new File(arena.getAbsolutePath() + "/config.json"));
    } catch (Exception ex) {
      ex.printStackTrace();
      return Optional.empty();
    }
  }

  private void prepareAndCreateArenaWorld(String mapName) {
    World world = new WorldCreator(mapName).createWorld();
    world.setAutoSave(false);
    world.setDifficulty(Difficulty.NORMAL);
    world.setGameRuleValue("doDaylightCycle", "false");
    world.setGameRuleValue("mobGriefing", "false");
    world.setGameRuleValue("doMobSpawning", "false");
    world.setGameRuleValue("doFireTick", "false");
    world.setGameRuleValue("keepInventory", "true");
    world.setGameRuleValue("commandBlockOutput", "false");
    world.setTime(3000);
    world.setSpawnFlags(false, false);
  }

  private Optional<DeathmatchArena> readMapConfig(File arena) {
    try (InputStreamReader reader =
        new InputStreamReader(new FileInputStream(arena), StandardCharsets.UTF_8)) {

      return Optional.of(this.gson.fromJson(reader, DeathmatchArena.class));
    } catch (Exception ex) {
      ex.printStackTrace();
      return Optional.empty();
    }
  }
}
