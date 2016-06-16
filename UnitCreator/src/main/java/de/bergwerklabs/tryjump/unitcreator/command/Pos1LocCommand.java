package de.bergwerklabs.tryjump.unitcreator.command;

import de.bergwerklabs.tryjump.unitcreator.UnitCreator;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by nexotekHD on 03.04.2016.
 */
public class Pos1LocCommand implements CommandExecutor {


    public boolean onCommand(CommandSender cs, Command command, String label, String[] args) {

        if(!(cs instanceof Player))
        {
            return true;
        }

        Player p = (Player)cs;
        Location loc = p.getLocation().clone();
        UnitCreator.getInstance().pos1.put(p.getUniqueId(),loc);
        p.sendMessage(UnitCreator.PREFIX + "Pos1 gesetzt auf " + loc.toString());

        return true;
    }
}
