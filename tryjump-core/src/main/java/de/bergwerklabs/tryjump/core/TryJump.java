package de.bergwerklabs.tryjump.core;

import com.google.common.util.concurrent.AtomicDouble;
import de.bergwerklabs.framework.bedrock.api.LabsGame;
import de.bergwerklabs.framework.bedrock.api.PlayerRegistry;
import de.bergwerklabs.framework.commons.spigot.general.timer.LabsTimer;
import de.bergwerklabs.tryjump.api.Unit;
import de.bergwerklabs.tryjump.core.listener.PlayerInteractListener;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Yannic Rieger on 11.02.2018.
 * <p>
 *
 * @author Yannic Rieger
 */
public class TryJump extends LabsGame<Jumper> {

    public TryJump() {
        super("TryJump");
    }

    private Collection<Jumper> players;

    @Override
    public void start(PlayerRegistry<Jumper> registry) {
        this.registerListeners();
        this.playerRegistry = registry;
        this.players = registry.getPlayers().values();

        final Location spawn = new Location(Bukkit.getWorld("jump"), 0, 100, 0);
        final List<Location> spawns  = new ArrayList<>();
        final AtomicDouble count = new AtomicDouble();

        this.players.forEach(jumper -> {
            jumper.setUnits(new LinkedList<>(TryJumpSession.getInstance().getPlacer().getSelectedUnits()));
            final Location playerSpawn = spawn.clone();
            TryJumpSession.getInstance().getPlacer().getStart().pasteAsync("jump", playerSpawn.toVector());
            playerSpawn.add(count.doubleValue() + 0.5, 0.5, 0.5);
            spawns.add(playerSpawn);
            count.addAndGet(35);

        });

        final Iterator<Location> iterator = spawns.iterator();

        this.players.forEach(jumper -> {
             while (iterator.hasNext()) {
                 final Location location = iterator.next();
                 jumper.getPlayer().teleport(location);
             }
        });

        LabsTimer countdown = new LabsTimer(5, timeLeft -> {
            this.players.forEach(jumper -> {
                final Player spigotPlayer = jumper.getPlayer();
                this.messenger.message("Noch §b" + timeLeft + " Sekunden §7bis zum Start.", spigotPlayer);
            });
        });

        countdown.addStopListener(event -> {
            this.messenger.messageAll("LOS!");
            this.players.forEach(jumper -> {
                final Vector end = TryJumpSession.getInstance().getPlacer().getStart().getMetadata().getEndVector();
                // Since its the of the round, the optional will not be empty.
                final Unit unit = jumper.getNextUnit().get();
                Location to = jumper.getPlayer().getLocation().clone().subtract(end);
                jumper.setCurrentUnit(unit);
                TryJumpSession.getInstance().getPlacer().placeUnit(to, jumper.getNextUnit().get(), false);
                // TODO: play sound
            });
        });

        countdown.start();
    }

    @Override
    public void stop() {
        // TODO: save stats
    }

    private void registerListeners() {
        final Plugin plugin = TryJumpSession.getInstance();
        final PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new PlayerInteractListener(), plugin);
    }
}
