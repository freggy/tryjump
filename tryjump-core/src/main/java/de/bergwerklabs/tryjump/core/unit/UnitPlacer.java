package de.bergwerklabs.tryjump.core.unit;

import de.bergwerklabs.framework.schematicservice.LabsSchematic;
import de.bergwerklabs.framework.schematicservice.SchematicService;
import de.bergwerklabs.framework.schematicservice.SchematicServiceBuilder;
import de.bergwerklabs.tryjump.api.TryjumpUnitMetadata;
import de.bergwerklabs.tryjump.core.Jumper;
import de.bergwerklabs.tryjump.core.TryJumpSession;
import de.bergwerklabs.tryjump.core.TryJumpUnit;
import de.bergwerklabs.tryjump.core.unit.strategy.RandomSelectionStrategies;
import de.bergwerklabs.tryjump.core.unit.strategy.SelectionStrategy;
import de.bergwerklabs.tryjump.core.unit.strategy.TimeBasedSelectionStrategy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by Yannic Rieger on 11.02.2018.
 * <p>
 *
 * @author Yannic Rieger
 */
public class UnitPlacer {

    /**
     *
     */
    public LabsSchematic<TryjumpUnitMetadata> getStart() {
        return start;
    }

    /**
     *
     */
    public Queue<TryJumpUnit> getSelectedUnits() {
        return selectedUnits;
    }

    private Queue<TryJumpUnit> selectedUnits;
    private LabsSchematic<TryjumpUnitMetadata> start;
    private UnitSelectionStrategy strategy;
    private double parkourLength;
    private final SchematicService<TryjumpUnitMetadata> service = new SchematicServiceBuilder<TryjumpUnitMetadata>().setDeserializer(new UnitDeserializer()).build();


    /**
     * @param easyFolder    folder containing subdirectories (../default, ../lite) of units with type EASY
     * @param mediumFolder  folder containing subdirectories (../default, ../lite) of units with type MEDIUM
     * @param hardFolder    folder containing subdirectories (../default, ../lite) of units with type HARD
     * @param extremeFolder folder containing subdirectories (../default, ../lite) of units with type EXTREME
     */
    public UnitPlacer(
            @NotNull File easyFolder,
            @NotNull File mediumFolder,
            @NotNull File hardFolder,
            @NotNull File extremeFolder,
            @NotNull File startSchematic,
            @NotNull SelectionStrategy type
    ) {
        this.start = this.service.createSchematic(startSchematic);

        final UnitList easy = this.createUnitList(
                new File(easyFolder.getAbsoluteFile() + "/default"),
                new File(easyFolder.getAbsoluteFile() + "/lite")
        );

        final UnitList medium = this.createUnitList(
                new File(mediumFolder.getAbsoluteFile() + "/default"),
                new File(mediumFolder.getAbsoluteFile() + "/lite")
        );

        final UnitList hard = this.createUnitList(
                new File(hardFolder.getAbsoluteFile() + "/default"),
                new File(hardFolder.getAbsoluteFile() + "/lite")
        );

        final UnitList extreme = this.createUnitList(
                new File(extremeFolder.getAbsoluteFile() + "/default"),
                new File(extremeFolder.getAbsoluteFile() + "/lite")
        );

        switch (type) {
            case RANDOM:
                this.strategy = new RandomSelectionStrategies(easy, medium, hard, extreme);
                break;
            case TIME_BASED:
                this.strategy = new TimeBasedSelectionStrategy(easy, medium, hard, extreme);
                break;
        }

        this.selectedUnits = this.strategy.createParkour();

        this.parkourLength = this.selectedUnits.stream()
                                               .mapToDouble(unit -> unit.getNormalVersion().getMetadata().getEndVector().length())
                                               .sum();

                /*
        schematics = schematics.stream().sorted(Comparator.comparingLong(t -> t.getMetadata().getCreationTime()))
                               .collect(Collectors.toList()); */
    }

    public void placeUnit(@NotNull Location location, @NotNull TryJumpUnit unit, boolean toggleLite) {
        LabsSchematic<TryjumpUnitMetadata> schematic;
        if (toggleLite) {
            schematic = unit.getLiteVersion();
            LabsSchematic<TryjumpUnitMetadata> normal = unit.getNormalVersion();
            normal.remove(unit.getPlaceLocation());

            Location end = location.clone().subtract(normal.getMetadata().getEndVector()).add(0, 1, 0);
            end.getBlock().setType(Material.AIR);

           // Bukkit.getScheduler().runTaskLater(TryJumpSession.getInstance(), () -> {
                this.place(unit.getPlaceLocation(), schematic);
            //}, 20 * 2);
        }
        else {
            schematic = unit.getNormalVersion();
            unit.setPlaceLocation(location);
            this.place(location, schematic);
        }
    }

    private void place(Location location, LabsSchematic<TryjumpUnitMetadata> schematic) {
        Location end = location.clone().subtract(schematic.getMetadata().getEndVector()).add(0, 1, 0);
        end.getBlock().setType(Material.GOLD_PLATE);
        schematic.pasteAsync("jump", location.toVector());
    }

    private UnitList createUnitList(@NotNull File normalFolder, @NotNull File liteFolder) {
        final Map<String, LabsSchematic<TryjumpUnitMetadata>> normal = this.fromDirectory(normalFolder);
        final Map<String, LabsSchematic<TryjumpUnitMetadata>> lite = this.fromDirectory(liteFolder);
        return new UnitList(lite, normal);
    }

    private Map<String, LabsSchematic<TryjumpUnitMetadata>> fromDirectory(@NotNull File dir) {
        return Arrays.stream(Objects.requireNonNull(dir.listFiles()))
                     .map(this.service::createSchematic)
                     .collect(Collectors.toMap(schem ->  schem.getMetadata().getName(), Function.identity(), (s1, s2) -> s1));
    }

    public double getParkourLength() {
        return parkourLength;
    }
}
