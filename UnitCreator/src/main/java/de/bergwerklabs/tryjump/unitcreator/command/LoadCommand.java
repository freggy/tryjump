package de.bergwerklabs.tryjump.unitcreator.command;

import com.google.gson.Gson;
import de.bergwerklabs.tryjump.unitcreator.JSONBlock;
import de.bergwerklabs.tryjump.unitcreator.JSONUnit;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.*;

/**
 * Created by nexotekHD on 09.04.2016.
 */
public class LoadCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender cs, Command command, String label, String[] args) {

        if(!(cs instanceof Player))
        {
            return true;
        }

        Player p = (Player)cs;

        if(args.length < 1)
        {
            p.sendMessage(ChatColor.RED + "Usage: /load <Name>");
            return true;
        }
        File file = new File("units/" + args[0] + ".unit");
        if(!file.exists())
        {
            p.sendMessage(ChatColor.RED + "Unit nicht gefunden!");
            return true;
        }
        BufferedReader reader;
        String content = null;

        try {
            reader = new BufferedReader( new FileReader(file));
            String line;
            while((line = reader.readLine()) != null)
            {
                if(content == null)
                {
                    content = line;
                }else
                {
                    content = content + line;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



        Gson gson = new Gson();
        JSONUnit unit = gson.fromJson(content, JSONUnit.class);
        for(JSONBlock block : unit.getBlocklist())
        {
            Location loc = p.getLocation().add(block.getX(),block.getY() -1,block.getZ());
            loc.getBlock().setType(block.getMaterial());
            loc.getBlock().setData(block.getData());
        }

        return true;
    }
}
