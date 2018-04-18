package de.bergwerklabs.tryjump.core.unit.strategy;

import de.bergwerklabs.framework.schematicservice.LabsSchematic;
import de.bergwerklabs.tryjump.api.TryjumpUnitMetadata;
import de.bergwerklabs.tryjump.core.TryJumpUnit;
import de.bergwerklabs.tryjump.core.unit.UnitList;
import de.bergwerklabs.tryjump.core.unit.UnitSelectionStrategy;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Yannic Rieger on 12.04.2018.
 *
 * <p>Strategy for randomly selecting {@link LabsSchematic}s. For random selection {@link
 * SecureRandom} is used.
 *
 * @author Yannic Rieger
 */
public class RandomSelectionStrategies extends UnitSelectionStrategy {

  /**
   * @param easy
   * @param medium
   * @param hard
   * @param extreme
   */
  public RandomSelectionStrategies(
      UnitList easy, UnitList medium, UnitList hard, UnitList extreme) {
    super(easy, medium, hard, extreme);
  }

  @Override
  public Queue<TryJumpUnit> createParkour() {
    final Queue<TryJumpUnit> parkour = new LinkedList<>();
    final Set<TryJumpUnit> easy = this.getRandomUnits(this.easy, 3);
    final Set<TryJumpUnit> medium = this.getRandomUnits(this.medium, 3);
    final Set<TryJumpUnit> hard = this.getRandomUnits(this.hard, 3);
    final Set<TryJumpUnit> extreme = this.getRandomUnits(this.extreme, 1);

    parkour.addAll(easy);
    parkour.addAll(medium);
    parkour.addAll(hard);
    parkour.addAll(extreme);

    return parkour;
  }

  /**
   * @param list
   * @param amount
   * @return
   */
  private Set<TryJumpUnit> getRandomUnits(@NotNull UnitList list, int amount) {
    final SecureRandom random = new SecureRandom();
    final Set<TryJumpUnit> units = new HashSet<>();
    final List<LabsSchematic<TryjumpUnitMetadata>> schematics =
        new ArrayList<>(list.getDefaultUnits().values());

    for (int i = 0; i < amount; i++) {
      int index = random.nextInt(schematics.size());
      final LabsSchematic<TryjumpUnitMetadata> normalUnit = schematics.get(index);
      final LabsSchematic<TryjumpUnitMetadata> liteUnit =
          list.getLiteUnits().get(normalUnit.getMetadata().getName());
      schematics.remove(index);
      units.add(new TryJumpUnit(normalUnit, liteUnit));
    }
    return units;
  }
}
