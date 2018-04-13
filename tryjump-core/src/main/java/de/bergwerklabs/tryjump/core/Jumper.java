package de.bergwerklabs.tryjump.core;

import com.google.common.base.Preconditions;
import de.bergwerklabs.framework.bedrock.api.LabsPlayer;
import de.bergwerklabs.framework.schematicservice.LabsSchematic;
import de.bergwerklabs.tryjump.api.TryJumpPlayer;
import de.bergwerklabs.tryjump.api.TryjumpUnitMetadata;
import de.bergwerklabs.tryjump.api.Unit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Created by Yannic Rieger on 11.02.2018.
 * <p>
 *
 * @author Yannic Rieger
 */
public class Jumper extends LabsPlayer implements TryJumpPlayer {

    private Queue<TryJumpUnit> unitsAhead;
    private Set<TryJumpUnit> completed = new HashSet<>();
    private LabsSchematic<TryjumpUnitMetadata> start;
    private Location unitSpawn;
    private Unit current;
    private int currentFails;
    private int totalFails;

    Jumper(Player player) {
        super(player.getUniqueId());
    }

    @Override
    public int getWins() {
        return 0;
    }

    @Override
    public int getTokens() {
        return 0;
    }

    @Override
    public int getKills() {
        return 0;
    }

    @Override
    public int getLosses() {
        return 0;
    }

    @Override
    public Unit getCurrentUnit() {
        return this.current;
    }

    @Override
    public Set<Unit> getCompletedUnits() {
        return new HashSet<>(completed);
    }

    @Override
    public Queue<Unit> getUnitsAhead() {
        return new LinkedList<>(this.unitsAhead);
    }

    @Override
    public int getCurrentFails() {
        return currentFails;
    }

    /**
     * Sets the units for the player.
     *
     * @param units {@link Queue} containing the units.
     */
    public void setUnits(Queue<TryJumpUnit> units) {
        Preconditions.checkNotNull(units);
        this.unitsAhead = units;
    }

    /**
     * Gets the next unit contained in {@link TryJumpPlayer#getUnitsAhead()}.
     *
     * @return {@link Optional} containing the next unit. {@link Optional#empty()} when there is no unit left.
     */
    public Optional<TryJumpUnit> getNextUnit() {
        TryJumpUnit unit = this.unitsAhead.poll();
        if (unit == null) return Optional.empty();
        return Optional.of(unit);
    }

    /**
     * Sets the current unit for the player.
     *
     * @param unit unit to be set.
     */
    public void setCurrentUnit(@NotNull Unit unit) {
        Preconditions.checkNotNull(unit);
        this.current = unit;
    }

    /**
     * Sets the current fails for the module.
     *
     * @param currentFails number of fails for this module.
     */
    public void setCurrentFails(int currentFails) {
        this.currentFails = currentFails;
    }

    public void addCompletedUnit(@NotNull TryJumpUnit unit) {
        Preconditions.checkNotNull(unit);
        this.completed.add(unit);
    }

    public void updateScoreboardProgress(Location location) {

    }

    public LabsSchematic<TryjumpUnitMetadata> getStart() {
        return start;
    }

    public void setStart(@NotNull LabsSchematic<TryjumpUnitMetadata> start) {
        this.start = start;
    }

    public String buildActionbarText() {
        final StringBuilder builder = new StringBuilder();
        builder.append("§6§l>> ");
        builder.append("§7§lUnit ");
        builder.append(this.completed.size() + 1);
        builder.append(": §b");
        builder.append(this.current.getName());
        builder.append(" §6§l❘ ");
        builder.append(this.current.getDifficulty().getDisplayName());
        builder.append(" §6§l<<");
        return builder.toString();
    }

    public void resetToSpawn() {
        this.currentFails++;
        this.totalFails++;
        new PotionEffect(PotionEffectType.BLINDNESS, 22, 10, false, false).apply(this.getPlayer());
        this.getPlayer().teleport(this.unitSpawn);
    }

    @Override
    public int getTotalFails() {
        return totalFails;
    }

    @Override
    public boolean isLite() {
        return this.currentFails >= 3;
    }

    public Location getUnitSpawn() {
        return unitSpawn;
    }

    public void setUnitSpawn(Location unitSpawn) {
        this.unitSpawn = unitSpawn;
    }
}
