package de.bergwerklabs.tryjump.core;

import de.bergwerklabs.framework.commons.math.SQRT;
import de.bergwerklabs.framework.commons.spigot.title.ActionbarTitle;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Yannic Rieger on 12.04.2018.
 * <p>
 *
 * @author Yannic Rieger
 */
public class UpdatePlayerInfoTask implements Runnable {
    private Plugin plugin;
    private TryJump tryJump;

    public UpdatePlayerInfoTask(Plugin plugin, TryJump tryJump) {
        this.plugin = plugin;
        this.tryJump = tryJump;
    }

    @Override
    public void run() {
        final Collection<Jumper> jumpers = this.tryJump.getPlayerRegistry().getPlayerCollection();
        jumpers.forEach(jumper -> {
            final Player spigotPlayer = jumper.getPlayer();
            ActionbarTitle.send(spigotPlayer, jumper.buildActionbarText());
        });
        this.updateJumpProgress(jumpers);
    }

    private void updateJumpProgress(Collection<Jumper> jumpers) {
        final List<Jumper> sorted = jumpers.stream()
                                           .peek(this::calculateProgress)
                                           .sorted(Comparator.comparingLong(Jumper::getJumpProgress))
                                           .collect(Collectors.toList());

        jumpers.forEach(jumper ->  jumper.updateScoreboardProgress(sorted));
    }

    private void calculateProgress(Jumper jumper) {
        double currentDistance = this.calculateDistanceFast(jumper.getStartSpawn(), jumper.getPlayer().getLocation());
        double totalDistance = TryJumpSession.getInstance().getPlacer().getParkourLength();
        int i = new Long(Math.round((currentDistance / totalDistance) * 100)).intValue();
        jumper.setJumpProgress(i);
    }

    private double calculateDistanceFast(Location location1, Location location2) {
        return SQRT.fast(
                (location1.getX() - location2.getX()) +
                (location1.getY() - location2.getY()) +
                (location1.getZ() - location2.getZ())
        );
    }
}
