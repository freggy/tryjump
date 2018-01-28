package de.bergwerklabs.tryjump.unitcreator.command;

import com.google.gson.Gson;
import de.bergwerklabs.tryjump.unitcreator.JSONBlock;
import de.bergwerklabs.tryjump.unitcreator.JSONUnit;
import de.bergwerklabs.tryjump.unitcreator.UnitCreator;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by Yannic Rieger on 03.01.2018.
 * <p>
 *
 * @author Yannic Rieger
 */
public class PrepareCommand implements CommandExecutor {

    HashSet<String> placed = new HashSet<>();


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player && commandSender.getName().equalsIgnoreCase("freggyy")) {
            File file = new File(UnitCreator.getInstance().getDataFolder().getParent() + "/units");
            Iterator<File> fileIterator = Arrays.asList(file.listFiles()).iterator();
            Location location = ((Player) commandSender).getLocation().clone().add(10, 0, 0);
            Bukkit.getScheduler().runTaskTimer(UnitCreator.getInstance(), () -> {
                if (fileIterator.hasNext()) {
                    File module  = fileIterator.next();
                    if (!placed.contains(module.getName())) {
                        File sibling = getSibling(module);
                        if (module.getName().contains("_lite")) {
                            this.build(sibling, location);
                            this.build(module, location);
                        }
                        else {
                            this.build(module, location);
                            this.build(sibling, location);
                        }
                        location.add(25, 0, 0);
                        this.placed.add(module.getName());
                        this.placed.add(sibling.getName());
                    }
                }
            }, 0, 20 * 6);
        }
        return false;
    }



    private File getSibling(File file) {
        String name = file.getName();
        if (name.contains("_lite")) {
            return new File(UnitCreator.getInstance().getDataFolder().getParent() + "/units/" + name.replace("_lite", ""));
        }
        else return new File(UnitCreator.getInstance().getDataFolder().getParent() + "/units/" + name.replace(".unit", "") + "_lite.unit");
    }

    private void build(File module, Location location) {
        this.prepare(module, location);
        location.add(25, 0, 0);
        Bukkit.broadcastMessage("Placed " + module.getName());
    }



    private void prepare(File file, Location location) {

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
            Location loc = location.clone().add(block.getX(), block.getY() -1, block.getZ());
            loc.getBlock().setType(block.getMaterial());
            loc.getBlock().setData(block.getData());
        }
    }
}
