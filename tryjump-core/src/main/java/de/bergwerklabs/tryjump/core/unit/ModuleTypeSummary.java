package de.bergwerklabs.tryjump.core.unit;

import com.google.common.base.Preconditions;
import de.bergwerklabs.framework.commons.spigot.general.WeightedLootTable;
import de.bergwerklabs.framework.schematicservice.LabsSchematic;
import de.bergwerklabs.tryjump.api.Difficulty;
import de.bergwerklabs.tryjump.api.TryjumpModuleMetadata;
import de.bergwerklabs.tryjump.core.TryJumpSession;
import de.bergwerklabs.tryjump.core.TryJumpUnit;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yannic Rieger on 12.02.2018.
 * <p>
 * TODO: add documentation
 *
 * @author Yannic Rieger
 */
public class ModuleTypeSummary {

    private WeightedLootTable<LabsSchematic<TryjumpModuleMetadata>> lootTable = new WeightedLootTable<>();

    private File liteFolder;
    private String diffString;

    ModuleTypeSummary(List<LabsSchematic<TryjumpModuleMetadata>> schematics, Difficulty difficulty) {
        Preconditions.checkNotNull(difficulty);
        Preconditions.checkNotNull(schematics);

        // Folder structure is: /${datafolder}/${difficulty}/lite/
        // Example: /${datafolder}/easy/lite/myUnit_lite.schematic
        this.diffString = difficulty.name().toLowerCase();
        this.liteFolder = new File(TryJumpSession.getInstance().getDataFolder().getAbsolutePath()
                                           + "/" + this.diffString + "/lite/");

        double[] weights = schematics.stream()
                                     .mapToDouble(schematic -> schematic.getMetadata().getCreationTime())
                                     .toArray();

        this.normalize(weights);

        for (int i = 0; i < weights.length; i++) {
            LabsSchematic<TryjumpModuleMetadata> schematic = schematics.get(i);
            double weight = weights[i];
            this.lootTable.add(weight, schematic);
        }
    }

    /**
     * Preferably returns the units that have been created recently.
     *
     * @param count amount of units to be returned.
     * @return returns the units that have been created recently.
     */
    public List<TryJumpUnit> getModlues(int count) {
        List<TryJumpUnit> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            LabsSchematic<TryjumpModuleMetadata> schematic = this.lootTable.next();
            File lite = new File(this.liteFolder + schematic.getMetadata().getName() + diffString + "_lite.schematic");
            LabsSchematic<TryjumpModuleMetadata> counterPart = new LabsSchematic<>(lite);
            list.add(new TryJumpUnit(this.lootTable.next(), counterPart));
        }
        return list;
    }

    /**
     * Changes the values in the given array so they add up to one.
     * Copy pasta from https://gist.github.com/Alrecenk/7221196
     * because java.util.Vector doesn't have such a method :(
     *
     * @param array array containing values to be normalized.
     */
    private void normalize(double array[]){
        double scale = 0;
        for (double anA : array) {
            scale += anA * anA;
        }

        scale = 1 / Math.sqrt(scale);

        for (int k = 0; k < array.length; k++){
            array[k] *= scale;
        }
    }
}
