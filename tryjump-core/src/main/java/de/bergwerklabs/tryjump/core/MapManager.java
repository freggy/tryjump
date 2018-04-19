package de.bergwerklabs.tryjump.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.bergwerklabs.tryjump.api.DeathmatchArena;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Optional;

/**
 * Created by Yannic Rieger on 19.04.2018.
 *
 * <p>Manages all {@link DeathmatchArena} related things and provides methods to retrieve arenas.
 *
 * @author Yannic Rieger
 */
public class MapManager {

  private File[] arenas;
  private Gson gson =
      new GsonBuilder()
          .registerTypeAdapter(DeathmatchArena.class, new ArenaDeserializer())
          .create();

  /** @param arenaDir directory where the deathmatch arenas are located. */
  public MapManager(File arenaDir) {
    this.arenas = arenaDir.listFiles(File::isDirectory);
  }

  /**
   * Chooses a random deathmatch arena using {@link SecureRandom}.
   *
   * @return a randomly selected arena. {@link Optional#empty()} when no arena could be found.
   */
  public Optional<DeathmatchArena> chooseArenaRandomly() {
    final SecureRandom random = new SecureRandom();
    final File arena = this.arenas[random.nextInt(this.arenas.length)];

    try (InputStreamReader reader =
        new InputStreamReader(new FileInputStream(arena), StandardCharsets.UTF_8)) {
      return Optional.of(this.gson.fromJson(reader, DeathmatchArena.class));
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return Optional.empty();
  }
}
