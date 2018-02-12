package de.bergwerklabs.tryjump.gameserver.unit;

import com.google.common.base.Preconditions;
import de.bergwerklabs.framework.commons.spigot.general.WeightedLootTable;
import de.bergwerklabs.framework.commons.spigot.general.timer.LabsTimerStartCause;
import de.bergwerklabs.framework.schematicservice.LabsSchematic;
import de.bergwerklabs.framework.schematicservice.SchematicService;
import de.bergwerklabs.framework.schematicservice.SchematicServiceBuilder;
import de.bergwerklabs.tryjump.api.Difficulty;
import de.bergwerklabs.tryjump.api.TryjumpModuleMetadata;
import de.bergwerklabs.tryjump.api.Unit;
import de.bergwerklabs.tryjump.gameserver.Jumper;
import de.bergwerklabs.tryjump.gameserver.TryJumpUnit;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.io.File;
import java.util.*;
import java.util.prefs.PreferenceChangeEvent;
import java.util.stream.Collectors;

/**
 * Created by Yannic Rieger on 11.02.2018.
 * <p>
 *
 * @author Yannic Rieger
 */
public class UnitPlacer {

    private Queue<TryJumpUnit> selectedUnits;
    private SchematicService<TryjumpModuleMetadata> service = new SchematicServiceBuilder<TryjumpModuleMetadata>()
            .setDeserializer(new UnitDeserializer())
            .build();


    public UnitPlacer(File easyFolder, File mediumFolder, File hardFolder, File extremeFolder) {
        Preconditions.checkArgument(easyFolder == null || easyFolder.isFile());
        Preconditions.checkArgument(mediumFolder == null || mediumFolder.isFile());
        Preconditions.checkArgument(hardFolder == null || hardFolder.isFile());
        Preconditions.checkArgument(extremeFolder == null || extremeFolder.isFile());

        List<LabsSchematic<TryjumpModuleMetadata>> schematics = new ArrayList<>();

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


    private List<LabsSchematic<TryjumpModuleMetadata>> fromDirectory(File dir) {
        return Arrays.stream(dir.listFiles())
                     .map(file -> this.service.createSchematic(file))
                     .collect(Collectors.toList());
    }

    private List<LabsSchematic<TryjumpModuleMetadata>> getUnitsByDifficulty(
            List<LabsSchematic<TryjumpModuleMetadata>> schematics,
            Difficulty difficulty
    ) {
        return schematics
                .stream()
                .filter(schematic -> schematic.getMetadata().getDifficulty() == difficulty.getDifValue())
                .collect(Collectors.toList());
    }

}
