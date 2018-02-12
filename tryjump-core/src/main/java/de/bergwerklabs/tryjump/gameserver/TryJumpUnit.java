package de.bergwerklabs.tryjump.gameserver;

import de.bergwerklabs.framework.schematicservice.LabsSchematic;
import de.bergwerklabs.tryjump.api.Difficulty;
import de.bergwerklabs.tryjump.api.TryjumpModuleMetadata;
import de.bergwerklabs.tryjump.api.Unit;

/**
 * Created by Yannic Rieger on 11.02.2018.
 * <p>
 *
 * @author Yannic Rieger
 */
public class TryJumpUnit extends Unit {

    private LabsSchematic<TryjumpModuleMetadata> normalVersion;
    private LabsSchematic<TryjumpModuleMetadata> liteVersion;

    public TryJumpUnit(LabsSchematic<TryjumpModuleMetadata> normal, LabsSchematic<TryjumpModuleMetadata> lite) {
        super(normal.getMetadata());
        this.normalVersion = normal;
        this.liteVersion = lite;
    }

    public LabsSchematic<TryjumpModuleMetadata> getNormalVersion() {
        return normalVersion;
    }

    public LabsSchematic<TryjumpModuleMetadata> getLiteVersion() {
        return liteVersion;
    }
}
