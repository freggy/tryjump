package de.bergwerklabs.tryjump.unitcreator;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Yannic Rieger on 23.09.2017.
 * <p>
 * Main class of the upload tool.
 *
 * @author Yannic Rieger
 */
public class Main extends JavaPlugin implements Listener {

    static Main getInstance() {
        return instance;
    }

    static final String CHAT_PREFIX = "§6>> §eUnitCreator §6| §7";

    private static Main instance;
    private Map<UUID, Creator> creators = new HashMap<>();

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
        instance = this;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        creators.put(e.getPlayer().getUniqueId(), new Creator(e.getPlayer()));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        creators.remove(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Creator creator = creators.get(e.getPlayer().getUniqueId());

        if (e.getAction() == Action.RIGHT_CLICK_AIR) {
                this.toggleCreativeInv(creator);
        }
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Location blocLoc = e.getClickedBlock().getLocation();
            if (creator.getPlayer().getItemInHand().getType() == Material.EMERALD) {
                creator.getSession().setStart(new com.sk89q.worldedit.Vector(blocLoc.getX(), blocLoc.getY(), blocLoc.getZ()));
            }
            else if (creator.getPlayer().getItemInHand().getType() == Material.MAGMA_CREAM) {
                creator.getSession().setEnd(new com.sk89q.worldedit.Vector(blocLoc.getX(), blocLoc.getY(), blocLoc.getZ()));
            }
            else this.toggleCreativeInv(creator);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Creator creator = creators.get(((Player)sender).getUniqueId());

            if (label.equalsIgnoreCase("create")) {
                int difficulty;
                boolean isLite;
                String name;

                if (args.length == 0 || args.length < 3) {
                    player.sendMessage(CHAT_PREFIX + "Usage: " + ChatColor.AQUA + "/create [name] [difficulty] [isLite]");
                    return true;
                }

                try {
                    name = args[0];
                    difficulty = Integer.valueOf(args[1]);
                    isLite = Boolean.valueOf(args[2]);
                }
                catch (Exception ex) {
                    player.sendMessage(CHAT_PREFIX + ChatColor.RED + "Ein Parameter ist fehlerhaft.");
                    return true;
                }

                creator.getSession().createModule(name);
                creator.getSession().createSchematic(difficulty, isLite);
            }
            else if (label.equalsIgnoreCase("load")) {
                creator.getSession().loadOld(player.getLocation(), args[0]);
            }
            else if (label.equalsIgnoreCase("submit")) {
                // TODO: copy to cloudstorage
            }
            else if (label.equalsIgnoreCase("pos1")) {
                creator.getPlayer().performCommand("/pos1");
            }
            else if (label.equalsIgnoreCase("pos2")) {
                creator.getPlayer().performCommand("/pos2");
            }
            else if (label.equalsIgnoreCase("cm")) {
                creator.toggle();
            }
            return true;
        }
        return false;
    }

    /**
     * Sets the inventory back to its initial state.
     *
     * @param creator Player
     */
    private void toggleCreativeInv(Creator creator) {
        if (creator.getPlayer().getItemInHand().getType() == Material.NETHER_STAR) {
            creator.toggle();
        }
    }
}
