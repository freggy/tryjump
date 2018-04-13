package de.bergwerklabs.tryjump.core.unit;

import com.flowpowered.nbt.*;
import de.bergwerklabs.framework.schematicservice.NbtUtil;
import de.bergwerklabs.framework.schematicservice.metadata.MetadataDeserializer;
import de.bergwerklabs.tryjump.api.TryjumpUnitMetadata;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Yannic Rieger on 12.02.2018.
 * <p>
 * Deserializes unit metadata.
 *
 * @author Yannic Rieger
 */
public class UnitDeserializer implements MetadataDeserializer<TryjumpUnitMetadata> {

    @Override
    public TryjumpUnitMetadata deserialize(@NotNull CompoundTag compoundTag) {
        final CompoundMap map      = compoundTag.getValue();
        final String name          = map.get("Name").getValue().toString();
        final Boolean isLite       = Boolean.valueOf(map.get("IsLite").getValue().toString());
        final Integer diff         = ((IntTag)map.get("Difficulty")).getValue();
        final Long created         = ((LongTag)map.get("Created")).getValue();
        final Vector distanceToEnd = NbtUtil.vectorFromNbt((CompoundTag)map.get("DistanceToEnd"));
        return new TryjumpUnitMetadata(name, distanceToEnd, 0.0, isLite, diff, created);
    }
}
