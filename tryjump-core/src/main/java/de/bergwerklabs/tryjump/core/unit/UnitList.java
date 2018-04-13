package de.bergwerklabs.tryjump.core.unit;

import de.bergwerklabs.framework.schematicservice.LabsSchematic;
import de.bergwerklabs.tryjump.api.TryjumpUnitMetadata;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Created by Yannic Rieger on 12.04.2018.
 * <p>
 *
 * @author Yannic Rieger
 */
public class UnitList {

    private Map<String, LabsSchematic<TryjumpUnitMetadata>> liteUnits;
    private Map<String, LabsSchematic<TryjumpUnitMetadata>> defaultUnits;

    public UnitList(
            @NotNull Map<String, LabsSchematic<TryjumpUnitMetadata>> liteUnits,
            @NotNull Map<String, LabsSchematic<TryjumpUnitMetadata>> defaultUnits
    ) {
        this.liteUnits = liteUnits;
        this.defaultUnits = defaultUnits;
    }

    /**
     *
     */
    public Map<String, LabsSchematic<TryjumpUnitMetadata>> getDefaultUnits() {
        return defaultUnits;
    }

    /**
     *
     */
    public Map<String, LabsSchematic<TryjumpUnitMetadata>> getLiteUnits() {
        return liteUnits;
    }
}
