package de.bergwerklabs.tryjump.unitcreator;

import com.boydti.fawe.object.schematic.Schematic;
import com.flowpowered.nbt.CompoundTag;
import com.sk89q.worldedit.*;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.regions.Region;
import de.bergwerklabs.framework.schematicservice.LabsSchematic;
import de.bergwerklabs.framework.schematicservice.NbtUtil;
import de.bergwerklabs.framework.schematicservice.SchematicService;
import de.bergwerklabs.framework.schematicservice.SchematicServiceBuilder;
import de.bergwerklabs.tryjump.api.Difficulty;
import de.bergwerklabs.tryjump.api.TryjumpModuleMetadata;
import de.bergwerklabs.tryjump.unitcreator.metadata.ModuleSerializer;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Arrays;

/**
 * Created by Yannic Rieger on 14.05.2017.
 * <p>
 * Class representing a session in which the player can create schematics representing TryJump units.
 *
 * @author Yannic Rieger
 */
public class CreatorSession {

    private SchematicService<TryjumpModuleMetadata> service = new SchematicServiceBuilder<TryjumpModuleMetadata>().setSerializer(new ModuleSerializer()).build();

    /**
     * Sets the vector representing the start of the unit
     *
     * @param start Vector which contains the x, y and z coordinate of the start point.
     */
    void setStart(Vector start) {
        player.sendMessage(Main.CHAT_PREFIX + "Startpunkt gesetzt bei §b" + start.getX() + ", " + start.getY() + ", " + start.getZ());
        this.start = start;
    }

    /**
     * Sets the vector representing the end of the unit
     *
     * @param end Vector which contains the x, y and z coordinate of the end point.
     */
    void setEnd(Vector end) {
        player.sendMessage(Main.CHAT_PREFIX + "Endpunkt gesetzt bei §b" + end.getX() + ", " + end.getY() + ", " + end.getZ());
        this.end = end;
    }

    private final String FOLDER = Main.getInstance().getDataFolder().getAbsolutePath();
    private String schemName;
    private Vector end, start;
    private Player player;

    /**
     * @param p Player belonging to the session.
     */
    CreatorSession(Player p) {
        this.player = p;
    }

    /**
     * Creates a schematic representing a unit in TryJump.
     *
     * @param difficulty Difficulty of the TryJump unit
     * @param isLite     Value indicating whether or not the unit is lite.
     */
    void createSchematic(String name, int difficulty, boolean isLite, Region region) {
        if (start == null || end == null) {
            this.player.sendMessage(Main.CHAT_PREFIX + ChatColor.RED + "Start- oder Endpunkt fehlt.");
            return;
        }

        if (difficulty > 4 || difficulty <= 0) {
            this.player.sendMessage(Main.CHAT_PREFIX + ChatColor.RED + "Die Schwierigkeit muss zwischen 1 und 4 liegen.");
            Arrays.stream(Difficulty.values())
                  .forEach(diff -> player.sendMessage(Main.CHAT_PREFIX + diff.toString()));
            return;
        }

        Difficulty diff = Difficulty.getByValue(difficulty);
        Schematic schematic = new Schematic(region);
        File schemFile = new File(this.FOLDER + "/" + this.schemName + diff.name() + (isLite ? "_lite" : "") + ".schematic");

        try {
            schematic.save(schemFile, ClipboardFormat.SCHEMATIC);
            CompoundTag schematicTag = NbtUtil.readCompoundTag(schemFile);
            Vector origin = new Vector(Integer.valueOf(schematicTag.getValue().get("WEOriginX").getValue().toString()),
                                       Integer.valueOf(schematicTag.getValue().get("WEOriginY").getValue().toString()),
                                       Integer.valueOf(schematicTag.getValue().get("WEOriginZ").getValue().toString()));

            // To set the offset properly we need to subtract the origin from the start point.
            // Now when pasting the schematic will be put exactly where specified.
            NbtUtil.writeDistance(this.worldEditVectorToBukkitVector(origin.subtract(start)), schematicTag, "WEOffset");

            TryjumpModuleMetadata metadata = new TryjumpModuleMetadata(name, this.worldEditVectorToBukkitVector(start.subtract(end)), isLite, difficulty, System.currentTimeMillis());
            service.saveSchematic(schematicTag, schemFile, metadata);

            player.sendMessage(Main.CHAT_PREFIX + "Unit §b" + schemFile.getName() + "§7 wurde erfolgreich erstellt.");
            this.deselect();
        }
        catch (Exception e) {
            player.sendMessage(Main.CHAT_PREFIX + "§cEs ist ein Fehler während des Erstellens aufgetreten");
            e.printStackTrace();
            this.deselect();
        }
    }

    /**
     * Loads the unit.
     */
    void loadOld(Location loc, String schemName) {
        File file = new File(FOLDER + "/" + schemName + ".schematic");
        LabsSchematic schematic = new LabsSchematic(file);
        schematic.pasteAsync(loc.getWorld().getName(), loc.toVector());
    }

    /**
     * Converts a {@link Vector} to an {@link org.bukkit.util.Vector}.
     *
     * @param vector {@link Vector} to convert.
     * @return       a {@link org.bukkit.util.Vector}
     */
    private org.bukkit.util.Vector worldEditVectorToBukkitVector(Vector vector) {
        return new org.bukkit.util.Vector(vector.getX(), vector.getY(), vector.getZ());
    }

    /**
     * Deselects the current selection.
     */
    private void deselect() {
        this.start = null;
        this.end = null;
        this.schemName = null;
    }
}
