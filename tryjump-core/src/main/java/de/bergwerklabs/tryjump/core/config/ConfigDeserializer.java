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

    final int rankingPointsPerUnit = object.get("rankingPointsPerUnit").getAsInt();
    final int coinsPerUnit = object.get("coinsPerUnit").getAsInt();
    final int coinsForWinning = object.get("coinsForWinning").getAsInt();
    final int rankingPointsForWinning = object.get("rankingPointsForWinning").getAsInt();
    final int zeroFailsTokenBoost = object.get("zeroFailsTokenBoost").getAsInt();

    final int jumpPhaseDuration = object.get("jumpPhaseDuration").getAsInt();
    final int deathmatchDuration = object.get("deathmachDuration").getAsInt();
    final int buyPhaseDuration = object.get("buyPhaseDuration").getAsInt();
    final int regenerationDuration = object.get("regenerationDuration").getAsInt();
    final int invulnerableDuration = object.get("invulnerableDuration").getAsInt();
    final int stopAfter = object.get("stopAfter").getAsInt();

    final String selectionStrategy = object.get("selectionStrategy").getAsString();

    final List<String> messages =
        JsonUtil.jsonArrayToStringList(object.get("zeroFailsMessages").getAsJsonArray());

    final UnitTokens easy = UnitTokens.fromJson(object.get("easyTokens").getAsJsonObject());
    final UnitTokens medium = UnitTokens.fromJson(object.get("mediumTokens").getAsJsonObject());
    final UnitTokens hard = UnitTokens.fromJson(object.get("hardTokens").getAsJsonObject());
    final UnitTokens extreme = UnitTokens.fromJson(object.get("extremeTokens").getAsJsonObject());

    final boolean isTeamSession = object.get("teamSession").getAsBoolean();

    return new Config(
        rankingPointsPerUnit,
        coinsPerUnit,
        jumpPhaseDuration,
        deathmatchDuration,
        buyPhaseDuration,
        zeroFailsTokenBoost,
        regenerationDuration,
        invulnerableDuration,
        coinsForWinning,
        rankingPointsForWinning,
        stopAfter,
        selectionStrategy,
        messages,
        easy,
        medium,
        hard,
        extreme,
        isTeamSession);
  }
}
