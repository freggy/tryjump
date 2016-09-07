package de.bergwerklabs.tryjump.unitcreator.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.File;

/**
 * Created by nexotekHD on 25.06.2016.
 */
public class DeleteCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender cs, Command command, String s, String[] args) {

        if(args.length < 1)
        {
            cs.sendMessage("Command falsch eingegeben!");
            return true;
        }

        String name = args[0];

        File file = new File("units/" + name + ".unit");
        if(file.exists())
        {
            file.delete();
            cs.sendMessage(file.getName() + " wurde erfolgreich gelöscht!");
        }
        file = new File("units/" + name + "_lite.unit");
        if(file.exists())
        {
            file.delete();
            cs.sendMessage(file.getName() + " wurde erfolgreich gelöscht!");
        }

        return true;
    }
}
