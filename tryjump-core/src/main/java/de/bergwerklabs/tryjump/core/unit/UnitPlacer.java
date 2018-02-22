package de.bergwerklabs.tryjump.core.unit;

import com.google.common.base.Preconditions;
import de.bergwerklabs.framework.schematicservice.LabsSchematic;
import de.bergwerklabs.framework.schematicservice.SchematicService;
import de.bergwerklabs.framework.schematicservice.SchematicServiceBuilder;
import de.bergwerklabs.tryjump.api.Difficulty;
import de.bergwerklabs.tryjump.api.TryjumpUnitMetadata;
import de.bergwerklabs.tryjump.core.TryJumpUnit;
import org.bukkit.Location;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Yannic Rieger on 11.02.2018.
 * <p>
 *
 * @author Yannic Rieger
 */
public class UnitPlacer {

    private Queue<TryJumpUnit> selectedUnits;
    private final SchematicService<TryjumpUnitMetadata> service = new SchematicServiceBuilder<TryjumpUnitMetadata>()
            .setDeserializer(new UnitDeserializer())
            .build();


    /**
     * @param easyFolder    folder containing easy modules of type DEFAULT not LITE.
     * @param mediumFolder  folder containing medium modules of type DEFAULT not LITE.
     * @param hardFolder    folder containing hard modules of type DEFAULT not LITE.
     * @param extremeFolder folder containing extreme modules of type DEFAULT not LITE.
     */
    public UnitPlacer(File easyFolder, File mediumFolder, File hardFolder, File extremeFolder) {
        Preconditions.checkArgument(easyFolder == null || easyFolder.isFile());
        Preconditions.checkArgument(mediumFolder == null || mediumFolder.isFile());
        Preconditions.checkArgument(hardFolder == null || hardFolder.isFile());
        Preconditions.checkArgument(extremeFolder == null || extremeFolder.isFile());

        List<LabsSchematic<TryjumpUnitMetadata>> schematics = new ArrayList<>();

        schematics.addAll(this.fromDirectory(easyFolder));
        schematics.addAll(this.fromDirectory(mediumFolder));
        schematics.addAll(this.fromDirectory(hardFolder));
        schematics.addAll(this.fromDirectory(extremeFolder));

        schematics = schematics.stream().sorted(Comparator.comparingLong(t -> t.getMetadata().getCreationTime())).collect(Collectors.toList());

        ModuleTypeSummary easy    = new ModuleTypeSummary(this.getUnitsByDifficulty(schematics, Difficulty.EASY), Difficulty.EASY);
        ModuleTypeSummary medium  = new ModuleTypeSummary(this.getUnitsByDifficulty(schematics, Difficulty.MEDIUM), Difficulty.MEDIUM);
        ModuleTypeSummary hard    = new ModuleTypeSummary(this.getUnitsByDifficulty(schematics, Difficulty.HARD), Difficulty.HARD);
        ModuleTypeSummary extreme = new ModuleTypeSummary(this.getUnitsByDifficulty(schematics, Difficulty.EXTREME), Difficulty.EXTREME);

        selectedUnits = new LinkedList<>();
        selectedUnits.addAll(easy.getModlues(3));
        selectedUnits.addAll(medium.getModlues(3));
        selectedUnits.addAll(hard.getModlues(3));
        selectedUnits.addAll(extreme.getModlues(1));
    }

    public void placeUnit(Location location, TryJumpUnit unit, boolean toggleLite) {
        if (toggleLite) unit.getLiteVersion().pasteAsync("jump", location.toVector());
        else unit.getNormalVersion().pasteAsync("jump", location.toVector());
    }

    public Queue<TryJumpUnit> getSelectedUnits() {
        return selectedUnits;
    }


    private List<LabsSchematic<TryjumpUnitMetadata>> fromDirectory(File dir) {
        return Arrays.stream(dir.listFiles())
                     .map(file -> this.service.createSchematic(file))
                     .collect(Collectors.toList());
    }

    private List<LabsSchematic<TryjumpUnitMetadata>> getUnitsByDifficulty(
            List<LabsSchematic<TryjumpUnitMetadata>> schematics,
            Difficulty difficulty
    ) {
        return schematics
                .stream()
                .filter(schematic -> schematic.getMetadata().getDifficulty() == difficulty.getDifValue())
                .collect(Collectors.toList());
    }

}
