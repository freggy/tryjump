package de.bergwerklabs.tryjump.unitcreator.command;

import de.bergwerklabs.tryjump.unitcreator.UnitCreator;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by nexotekHD on 03.04.2016.
 */
public class StartLocCommand implements CommandExecutor {


    public boolean onCommand(CommandSender cs, Command command, String label, String[] args) {

        if(!(cs instanceof Player))
        {
            return true;
        }

        Player p = (Player)cs;
        /*
        Location loc = p.getLocation().clone();
        UnitCreator.getInstance().startLoc = loc;
        p.sendMessage(UnitCreator.PREFIX + "StartLoc gesetzt auf " + loc.toString());

        */
        ItemStack tool = new ItemStack(Material.FLINT);
        ItemMeta im = tool.getItemMeta();
        im.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "StartLoc");
        tool.setItemMeta(im);
        p.getInventory().addItem(tool);
        return true;
    }
}
