package de.bergwerklabs.tryjump.core.config;

import com.google.gson.JsonObject;
import de.bergwerklabs.tryjump.api.Difficulty;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Yannic Rieger on 16.04.2018.
 *
 * <p>Data class that contains token information that is used for each unit difficulty.
 *
 * @author Yannic Rieger
 */
public class UnitTokens {

  /** Gets the amount of tokens for the completion of a non-lite unit. */
  public int getNormal() {
    return normal;
  }

  /** Gets the amount of tokens for the completion of a lite unit. */
  public int getLite() {
    return lite;
  }

  private int lite, normal;

  /**
   * @param lite the amount of tokens for the completion of a lite unit.
   * @param normal the amount of tokens for the completion of a non-lite unit.
   */
  private UnitTokens(int lite, int normal) {
    this.lite = lite;
    this.normal = normal;
  }

  /**
   * Gets the amount of tokens for the given {@link Difficulty}.
   *
   * @param difficulty the {@link Difficulty} of the {@link de.bergwerklabs.tryjump.api.Unit}.
   * @param config the {@link Config}.
   * @param isLite value indicating whether or not the unit was a lite unit.
   * @return the amount of tokens for the given {@link Difficulty}.
   */
  public static int getTokens(
      @NotNull Difficulty difficulty, @NotNull Config config, boolean isLite) {
    final UnitTokens tokens = fromDifficulty(difficulty, config);
    return isLite ? tokens.lite : tokens.normal;
  }

  /**
   * Returns the {@link UnitTokens} instance based on the {@link Difficulty}.
   *
   * @param difficulty difficulty of the unit.
   * @param config {@link Config} where the unit tokens are specified.
   * @return the {@link UnitTokens} instance based on the {@link Difficulty}.
   */
  public static UnitTokens fromDifficulty(Difficulty difficulty, Config config) {
    switch (difficulty) {
      case EASY:
        return config.getEasyTokens();
      case MEDIUM:
        return config.getMediumTokens();
      case HARD:
        return config.getHardTokens();
      case EXTREME:
        return config.getExtremeTokens();
      default:
        return new UnitTokens(0, 0);
    }
  }

  /** Creates an {@code UnitToken} object from a {@link JsonObject}. */
  static UnitTokens fromJson(JsonObject jsonObject) {
    return new UnitTokens(jsonObject.get("lite").getAsInt(), jsonObject.get("normal").getAsInt());
  }
}
