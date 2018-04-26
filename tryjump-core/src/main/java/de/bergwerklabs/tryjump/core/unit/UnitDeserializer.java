package de.bergwerklabs.tryjump.core.unit;

import com.flowpowered.nbt.CompoundMap;
import com.flowpowered.nbt.CompoundTag;
import com.flowpowered.nbt.DoubleTag;
import com.flowpowered.nbt.IntTag;
import com.flowpowered.nbt.LongTag;
import de.bergwerklabs.framework.schematicservice.NbtUtil;
import de.bergwerklabs.framework.schematicservice.metadata.MetadataDeserializer;
import de.bergwerklabs.tryjump.api.TryjumpUnitMetadata;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Yannic Rieger on 12.02.2018.
 *
 * <p>Deserializes unit metadata.
 *
 * @author Yannic Rieger
 */
public class UnitDeserializer implements MetadataDeserializer<TryjumpUnitMetadata> {

  @Override
  public TryjumpUnitMetadata deserialize(@NotNull CompoundTag compoundTag) {
    final CompoundMap map = compoundTag.getValue();
    final String name = map.get("Name").getValue().toString();
    final Boolean isLite = Boolean.valueOf(map.get("IsLite").getValue().toString());
    final Integer diff = ((IntTag) map.get("Difficulty")).getValue();
    final Long created = ((LongTag) map.get("Created")).getValue();

    Vector vector;
    double distanceToEnd = 0;
    if (compoundTag.getValue().containsKey("DistanceVector")) {
      vector = NbtUtil.vectorFromNbt(((CompoundTag) map.get("DistanceVector")));
      distanceToEnd = ((DoubleTag) map.get("DistanceToEnd")).getValue();
    } else {
      vector = NbtUtil.vectorFromNbt(((CompoundTag) map.get("DistanceToEnd")));
    }

    // final long distanceToEnd = ((LongTag)map.get("DistanceToEnd")).getValue();
    return new TryjumpUnitMetadata(name, vector, distanceToEnd, isLite, diff, created);
  }
}
