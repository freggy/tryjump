package de.bergwerklabs.tryjump.core;

import de.bergwerklabs.framework.schematicservice.LabsSchematic;
import de.bergwerklabs.tryjump.api.TryjumpUnitMetadata;
import de.bergwerklabs.tryjump.api.Unit;

/**
 * Created by Yannic Rieger on 11.02.2018.
 * <p>
 *
 * @author Yannic Rieger
 */
public class TryJumpUnit extends Unit {

    private LabsSchematic<TryjumpUnitMetadata> normalVersion;
    private LabsSchematic<TryjumpUnitMetadata> liteVersion;

    public TryJumpUnit(LabsSchematic<TryjumpUnitMetadata> normal, LabsSchematic<TryjumpUnitMetadata> lite) {
        super(normal.getMetadata());
        this.normalVersion = normal;
        this.liteVersion = lite;
    }

    public LabsSchematic<TryjumpUnitMetadata> getNormalVersion() {
        return normalVersion;
    }

    public LabsSchematic<TryjumpUnitMetadata> getLiteVersion() {
        return liteVersion;
    }
}
