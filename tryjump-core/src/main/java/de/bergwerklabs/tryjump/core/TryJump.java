package de.bergwerklabs.tryjump.core;

import de.bergwerklabs.framework.bedrock.api.LabsGame;
import de.bergwerklabs.framework.bedrock.api.PlayerRegistry;
import de.bergwerklabs.framework.commons.spigot.general.timer.LabsTimer;
import de.bergwerklabs.framework.commons.spigot.item.ItemStackBuilder;
import de.bergwerklabs.tryjump.api.Unit;
import de.bergwerklabs.tryjump.core.listener.jump.PlayerDamageListener;
import de.bergwerklabs.tryjump.core.listener.jump.PlayerInteractListener;
import de.bergwerklabs.tryjump.core.unit.UnitPlacer;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.*;

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
    private BukkitTask updatePlayerInfoTask;

    @Override
    public void start(PlayerRegistry<Jumper> registry) {
        this.registerListeners();
        this.playerRegistry = registry;
        this.players = registry.getPlayerCollection();

        final Location spawn = new Location(Bukkit.getWorld("jump"), 0, 100, 0);
        final double[] count = {35};

        this.players.forEach(jumper -> {
            jumper.setUnits(new LinkedList<>(TryJumpSession.getInstance().getPlacer().getSelectedUnits()));
            final Location playerSpawn = spawn.clone().add(count[0] + 0.5, 0.5, 0.5);
            TryJumpSession.getInstance().getPlacer().getStart().pasteAsync("jump", playerSpawn.toVector());
            jumper.setUnitSpawn(playerSpawn);
            count[0] += 35;

        });

        this.players.forEach(jumper -> {
            final Player player = jumper.getPlayer();
            new PotionEffect(PotionEffectType.BLINDNESS, 35, 10, false, false).apply(player.getPlayer());
            player.teleport(jumper.getUnitSpawn());
        });

        LabsTimer countdown = new LabsTimer(5, timeLeft -> {
            this.players.forEach(jumper -> {
                final Player spigotPlayer = jumper.getPlayer();
                spigotPlayer.playSound(spigotPlayer.getEyeLocation(), Sound.CLICK, 100, 1);
                this.messenger.message("Das Spiel startet in §b" + timeLeft + " Sekunden§7.", spigotPlayer);
            });
        });

        countdown.addStopListener(event -> {
            this.messenger.messageAll("LOS!");
            final TryJumpSession session = TryJumpSession.getInstance();
            this.updatePlayerInfoTask = Bukkit.getScheduler().runTaskTimerAsynchronously(session, new UpdatePlayerInfoTask(session, this), 0, 10L);
            this.players.forEach(jumper -> {
                final Player spigotPlayer = jumper.getPlayer();
                final UnitPlacer placer = session.getPlacer();
                final Vector end = placer.getStart().getMetadata().getEndVector();
                // Since its the of the round, the optional will not be empty.
                final Unit unit = jumper.getNextUnit().get();
                final ItemStack instantDeath = new ItemStackBuilder(Material.INK_SACK).setName("§c§lInstant-Tod(TM)")
                                                                                      .setData((byte)1)
                                                                                      .create();

                spigotPlayer.getInventory().setItem(4, instantDeath);
                Location to = spigotPlayer.getLocation().clone().subtract(end);
                jumper.setCurrentUnit(unit);
                placer.placeUnit(to, (TryJumpUnit) unit, false);
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
        manager.registerEvents(new PlayerInteractListener(this), plugin);
        manager.registerEvents(new PlayerDamageListener(this), plugin);
    }
}
