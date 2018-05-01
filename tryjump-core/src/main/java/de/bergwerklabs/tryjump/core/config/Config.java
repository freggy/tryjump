package de.bergwerklabs.tryjump.core.config;

import com.google.gson.GsonBuilder;
import de.bergwerklabs.tryjump.core.unit.strategy.SelectionStrategy;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Yannic Rieger on 12.02.2018.
 *
 * <p>Contains all configurable settings of TryJump. This class will be created by using the {@link
 * ConfigDeserializer}. Custom instantiation should be avoided.
 *
 * @author Yannic Rieger
 */
public class Config {

  /** Gets the amount of ranking points given to a player per unit. */
  public int getRankingPointsPerUnit() {
    return rankingPointsPerUnit;
  }

  /** Gets the amount of coins given to a player per unit. */
  public int getCoinsPerUnit() {
    return coinsPerUnit;
  }

  /** The duration in seconds the jump phase will last. */
  public int getJumpPhaseDuration() {
    return jumpPhaseDuration;
  }

  /** The duration in seconds the deathmatch will last. */
  public int getDeathmatchDuration() {
    return deathmatchDuration;
  }

  /** The duration in seconds the buy phase will last. */
  public int getBuyPhaseDuration() {
    return buyPhaseDuration;
  }

  /** Gets the duration in seconds the regeneration effect will last. */
  public int getRegenerationDuration() {
    return regenerationDuration;
  }

  /** Gets the duration in seconds of the invulnerability effect. */
  public int getInvulerableDuration() {
    return invulerableDuration;
  }

  /** The amount of tokens given to a player that finishes the jump phase with 0 fails. */
  public int getZeroFailsTokenBoost() {
    return zeroFailsTokenBoost;
  }

  /**
   * The {@link SelectionStrategy} used for selecting the units that will be spawned in the jump
   * phase.
   */
  public SelectionStrategy getSelectionStrategy() {
    return selectionStrategy;
  }

  /**
   * Gets the list of messages that can be displayed when a player finishes the jump phase with zero
   * fails.
   */
  public List<String> getZeroFailsMessages() {
    return zeroFailsMessages;
  }

  /** Gets the tokens that will be given to player when he finishes a unit of type EASY. */
  public UnitTokens getEasyTokens() {
    return easy;
  }

  /** Gets the tokens that will be given to player when he finishes a unit of type MEDIUM. */
  public UnitTokens getMediumTokens() {
    return medium;
  }

  /** Gets the tokens that will be given to player when he finishes a unit of type HARD. */
  public UnitTokens getHardTokens() {
    return hard;
  }

  /** Gets the tokens that will be given to player when he finishes a unit of type EXTREME. */
  public UnitTokens getExtremeTokens() {
    return extreme;
  }

  /** Whether the running game is a session where teams are allowed. */
  public boolean isTeamSession() {
    return isTeamSession;
  }

  private int rankingPointsPerUnit;
  private int coinsPerUnit;
  private int jumpPhaseDuration;
  private int deathmatchDuration;
  private int buyPhaseDuration;
  private int zeroFailsTokenBoost;
  private int regenerationDuration; // TODO: document
  private int invulerableDuration; // TODO: document
  private SelectionStrategy selectionStrategy;
  private List<String> zeroFailsMessages;
  private UnitTokens easy;
  private UnitTokens medium;
  private UnitTokens hard;
  private UnitTokens extreme;
  private boolean isTeamSession;

  Config(
      int rankingPointsPerUnit,
      int coinsPerUnit,
      int jumpPhaseDuration,
      int deathmatchDuration,
      int buyPhaseDuration,
      int zeroFailsTokenBoost,
      int regenerationDuration,
      int invulerableDuration,
      @NotNull String selectionStrategy,
      @NotNull List<String> zeroFailsMessages,
      @NotNull UnitTokens easy,
      @NotNull UnitTokens medium,
      @NotNull UnitTokens hard,
      @NotNull UnitTokens extreme,
      boolean isTeamSession) {
    this.rankingPointsPerUnit = rankingPointsPerUnit;
    this.coinsPerUnit = coinsPerUnit;
    this.jumpPhaseDuration = jumpPhaseDuration;
    this.deathmatchDuration = deathmatchDuration;
    this.buyPhaseDuration = buyPhaseDuration;
    this.zeroFailsTokenBoost = zeroFailsTokenBoost;
    this.invulerableDuration = invulerableDuration;
    this.regenerationDuration = regenerationDuration;
    this.selectionStrategy = SelectionStrategy.valueOf(selectionStrategy);
    this.zeroFailsMessages = zeroFailsMessages;
    this.easy = easy;
    this.medium = medium;
    this.hard = hard;
    this.extreme = extreme;
    this.isTeamSession = isTeamSession;
  }

  /**
   * Reads the config from a JSON file.
   *
   * @param config {@link File} containing configuration in JSON format.
   * @return {@link Optional#empty()} if an error occurs while loading.
   */
  public static Optional<Config> read(@NotNull File config) {
    try (InputStreamReader reader =
        new InputStreamReader(new FileInputStream(config), StandardCharsets.UTF_8)) {
      return Optional.of(
          new GsonBuilder()
              .registerTypeAdapter(Config.class, new ConfigDeserializer())
              .create()
              .fromJson(reader, Config.class));
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return Optional.empty();
  }
}
