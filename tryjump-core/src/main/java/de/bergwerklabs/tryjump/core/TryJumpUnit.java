package de.bergwerklabs.tryjump.core;

import de.bergwerklabs.framework.schematicservice.LabsSchematic;
import de.bergwerklabs.tryjump.api.TryjumpUnitMetadata;
import de.bergwerklabs.tryjump.api.Unit;
import org.bukkit.Location;

/**
 * Created by Yannic Rieger on 11.02.2018.
 * <p>
 *
 * @author Yannic Rieger
 */
public class TryJumpUnit extends Unit implements Cloneable {

    private LabsSchematic<TryjumpUnitMetadata> normalVersion;
    private LabsSchematic<TryjumpUnitMetadata> liteVersion;
    private Location placeLocation;

    public TryJumpUnit(LabsSchematic<TryjumpUnitMetadata> normal, LabsSchematic<TryjumpUnitMetadata> lite) {
        super(normal.getMetadata());
        this.normalVersion = normal.clone();
        this.liteVersion = lite.clone();
    }

    public LabsSchematic<TryjumpUnitMetadata> getNormalVersion() {
        return normalVersion;
    }

    public LabsSchematic<TryjumpUnitMetadata> getLiteVersion() {
        return liteVersion;
    }

    public Location getPlaceLocation() {
        return placeLocation;
    }

    public void setPlaceLocation(Location placeLocation) {
        this.placeLocation = placeLocation;
    }

    @Override
    public TryJumpUnit clone() {
        try {
            return (TryJumpUnit)super.clone();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
