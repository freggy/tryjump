package de.bergwerklabs.tryjump.unitcreator.command;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by nexotekHD on 25.06.2016.
 */
public class ListsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender cs, Command command, String s, String[] args) {

        File dir = new File("units/");
        cs.sendMessage("Liste aller Units:");
        if(dir.isDirectory())
        {
            ArrayList<File> units = new ArrayList<File>();
            ArrayList<File> liteUnits = new ArrayList<File>();
            for(File file : dir.listFiles())
            {
                if(file.getName().contains("_lite"))
                {
                    liteUnits.add(file);
                }else
                {
                    units.add(file);
                }
            }

            for(File file : units)
            {
                if(isLite(file,liteUnits))
                {
                    cs.sendMessage(file.getName() + ChatColor.GREEN + " [inkl. Lite Unit]");
                }else
                {
                    cs.sendMessage(file.getName() + ChatColor.RED + "[kein Lite Unit gefunden]");
                }
            }

        }

        return true;
    }

    private boolean isLite(File file, ArrayList<File> liteUnits)
    {
        boolean lite = false;

        for(File f : liteUnits)
        {
            if(f.getName().split(".unit",-1)[0].equalsIgnoreCase(file.getName().split(".unit", -1)[0] + "_lite"))
            {
                lite = true;
            }
        }
        return lite;
    }
}
