package de.bergwerklabs.tryjump.unitcreator.metadata;

import com.flowpowered.nbt.CompoundMap;
import com.flowpowered.nbt.CompoundTag;
import com.flowpowered.nbt.DoubleTag;
import com.flowpowered.nbt.IntTag;
import com.flowpowered.nbt.LongTag;
import com.flowpowered.nbt.StringTag;
import de.bergwerklabs.framework.schematicservice.NbtUtil;
import de.bergwerklabs.framework.schematicservice.metadata.MetadataSerializer;
import de.bergwerklabs.tryjump.api.TryjumpUnitMetadata;
import org.bukkit.util.Vector;

/**
 * Created by Yannic Rieger on 23.09.2017.
 *
 * <p>Deserializes module metadata.
 *
 * @author Yannic Rieger
 */
public class UnitSerializer implements MetadataSerializer<TryjumpUnitMetadata> {

  @Override
  public CompoundTag serialize(TryjumpUnitMetadata tryjumpModuleMetadata) {
    final CompoundMap map = new CompoundMap();
    final Vector vector = tryjumpModuleMetadata.getEndVector();
    final CompoundMap compoundMap =
        NbtUtil.vectorToNbt("DistanceVector", vector.getX(), vector.getY(), vector.getZ())
            .getValue();

    map.put(new StringTag("Name", tryjumpModuleMetadata.getName()));
    map.put(new IntTag("Difficulty", tryjumpModuleMetadata.getDifficulty()));
    map.put(new StringTag("IsLite", String.valueOf(tryjumpModuleMetadata.isLite())));
    map.put(new DoubleTag("DistanceToEnd", tryjumpModuleMetadata.getDistanceToEnd()));
    map.put(new CompoundTag("DistanceVector", compoundMap));
    map.put(new LongTag("Created", tryjumpModuleMetadata.getCreationTime()));

    return new CompoundTag("Metadata", map);
  }
}
