package de.bergwerklabs.tryjump.core;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.bergwerklabs.framework.commons.spigot.json.JsonUtil;
import de.bergwerklabs.framework.commons.spigot.location.LocationUtil;
import de.bergwerklabs.tryjump.api.DeathmatchArena;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.bukkit.Location;

/**
 * Created by Yannic Rieger on 19.04.2018.
 *
 * <p>Deserializes deathmatch arena information from JSON.
 *
 * @author Yannic Rieger
 */
public class ArenaDeserializer implements JsonDeserializer<DeathmatchArena> {

  @Override
  public DeathmatchArena deserialize(
      JsonElement json, Type typeOfT, JsonDeserializationContext context) {
    JsonObject object = json.getAsJsonObject();
    List<String> creators = JsonUtil.jsonArrayToStringList(object.get("creators").getAsJsonArray());
    String name = object.get("name").getAsString();
    Set<Location> spawns =
        JsonUtil.jsonArrayToJsonObjectList(object.get("spawns").getAsJsonArray())
            .stream()
            .map(element -> LocationUtil.locationFromJson(element.getAsJsonObject()))
            .collect(Collectors.toSet());
    return new DeathmatchArena(creators.toArray(new String[creators.size() - 1]), name, spawns);
  }
}
