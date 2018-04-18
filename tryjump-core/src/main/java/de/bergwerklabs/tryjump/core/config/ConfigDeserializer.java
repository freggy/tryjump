package de.bergwerklabs.tryjump.core.config;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.bergwerklabs.framework.commons.spigot.json.JsonUtil;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Yannic Rieger on 16.04.2018.
 *
 * <p>Creates an {@link Config} from a {@link JsonObject}.
 *
 * @author Yannic Rieger
 */
public class ConfigDeserializer implements JsonDeserializer<Config> {

  @Override
  public Config deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
    final JsonObject object = json.getAsJsonObject();

    final int rankingPointsPerUnit = object.get("ranking-points-per-unit").getAsInt();
    final int coinsPerUnit = object.get("coins-per-unit").getAsInt();
    final int jumpPhaseDuration = object.get("jump-phase-duration").getAsInt();
    final int deathmatchDuration = object.get("deathmach-duration").getAsInt();
    final int zeroFailsTokenBoost = object.get("zero-fails-token-boost").getAsInt();
    final String selectionStrategy = object.get("selection-strategy").getAsString();
    final List<String> messages =
        JsonUtil.jsonArrayToStringList(object.get("zero-fails-messages").getAsJsonArray());
    final UnitTokens easy = UnitTokens.fromJson(object.get("easy").getAsJsonObject());
    final UnitTokens medium = UnitTokens.fromJson(object.get("medium-tokens").getAsJsonObject());
    final UnitTokens hard = UnitTokens.fromJson(object.get("hard-tokens").getAsJsonObject());
    final UnitTokens extreme = UnitTokens.fromJson(object.get("extreme-tokens").getAsJsonObject());
    final boolean isTeamSession = object.get("team-session").getAsBoolean();

    return new Config(
        rankingPointsPerUnit,
        coinsPerUnit,
        jumpPhaseDuration,
        deathmatchDuration,
        zeroFailsTokenBoost,
        selectionStrategy,
        messages,
        easy,
        medium,
        hard,
        extreme,
        isTeamSession);
  }
}
