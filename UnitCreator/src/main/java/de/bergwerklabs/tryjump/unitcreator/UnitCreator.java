package de.bergwerklabs.tryjump.unitcreator;

import de.bergwerklabs.tryjump.unitcreator.command.*;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.defaults.ListCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by nexotekHD on 03.04.2016.
 */
public class UnitCreator extends JavaPlugin implements Listener {

    private static UnitCreator instance;

    public static final String PREFIX = ChatColor.GOLD + "[" + ChatColor.AQUA + "UnitCreator" + ChatColor.GOLD + "] ";


    public HashMap<UUID, Location> startLoc = new HashMap<UUID, Location>();
    public HashMap<UUID, Location> endLoc = new HashMap<UUID, Location>();
    public HashMap<UUID, Location> pos1 = new HashMap<UUID, Location>();
    public HashMap<UUID, Location> pos2 = new HashMap<UUID, Location>();

    public void onEnable()
    {
        instance = this;
        this.getCommand("startloc").setExecutor(new StartLocCommand());
        this.getCommand("endloc").setExecutor(new EndLocCommand());
        this.getCommand("pos1").setExecutor(new Pos1LocCommand());
        this.getCommand("pos2").setExecutor(new Pos2LocCommand());
        this.getCommand("submit").setExecutor(new SubmitCommand2());
        this.getCommand("load").setExecutor(new LoadCommand());
        this.getCommand("list").setExecutor(new ListsCommand());
        this.getCommand("delete").setExecutor(new DeleteCommand());

        getServer().getPluginManager().registerEvents(this,this);

    }

    public static UnitCreator getInstance()
    {
        return instance;
    }


    public void onDisable()
    {

    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e)
    {
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK)
        {
            Player p = e.getPlayer();
            String displayName = null;
            try
            {
                displayName = p.getItemInHand().getItemMeta().getDisplayName().toLowerCase();
            }catch(Exception ex)
            {

            }
            if(displayName != null && displayName.contains("startloc"))
            {
                Location loc = e.getClickedBlock().getLocation();
                startLoc.put(p.getUniqueId(),loc);
                p.sendMessage(UnitCreator.PREFIX + "StartLoc gesetzt auf " + loc.toString());
            }else if(displayName != null &&  displayName.contains("endloc"))
            {
                Location loc = e.getClickedBlock().getLocation();
                endLoc.put(p.getUniqueId(), loc);
                p.sendMessage(UnitCreator.PREFIX + "EndLoc gesetzt auf " + loc.toString());
            }
        }
    }
}
