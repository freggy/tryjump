package de.bergwerklabs.tryjump.unitcreator.command;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.bergwerklabs.tryjump.unitcreator.JSONBlock;
import de.bergwerklabs.tryjump.unitcreator.JSONUnit;
import de.bergwerklabs.tryjump.unitcreator.UnitCreator;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by nexotekHD on 13.05.2016.
 */
public class SubmitCommand2 implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender cs, Command command, String s, String[] args) {

        if(!(cs instanceof Player))
        {
            return true;
        }

        // arg check
        if(args.length < 2)
        {
            cs.sendMessage("Usage: /submit <Name> <1 = easy | 2 = medium | 3 = hard | 4 = extreme>");
            return true;
        }


        Player p = (Player)cs;
        // null check
        UnitCreator i = UnitCreator.getInstance();
        if(i.startLoc == null || i.endLoc == null || i.pos1 == null || i.pos2 == null)
        {
            p.sendMessage(ChatColor.RED + "Du hast nicht alle Parameter (Pos1, Pos2, StartLoc,EndLoc) gesetzt!");
            return true;
        }

        int difficulty = 1;
        try
        {
            difficulty = Integer.parseInt(args[1]);
        }catch(Exception e)
        {
            p.sendMessage(ChatColor.RED + "Du musst die Difficulty mit einer Zahl angeben:");
            p.sendMessage(ChatColor.GOLD  + "1 = EASY");
            p.sendMessage(ChatColor.GOLD  + "2 = MEDIUM");
            p.sendMessage(ChatColor.GOLD  + "3 = HARD");
            p.sendMessage(ChatColor.GOLD  + "4 = EXTREME");
            return true;
        }

        // gogo

        Location startLoc = UnitCreator.getInstance().startLoc.get(p.getUniqueId());
        Location endLoc = UnitCreator.getInstance().endLoc.get(p.getUniqueId());

        Location pos1 = UnitCreator.getInstance().pos1.get(p.getUniqueId());
        Location pos2 = UnitCreator.getInstance().pos2.get(p.getUniqueId());

        int minX = Math.min(pos1.getBlockX(), pos2.getBlockX());
        int maxX = Math.max(pos1.getBlockX(), pos2.getBlockX());

        int minY = Math.min(pos1.getBlockY(), pos2.getBlockY());
        int maxY = Math.max(pos1.getBlockY(), pos2.getBlockY());

        int minZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
        int maxZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JSONUnit unit = new JSONUnit();
        unit.setDifficulty(difficulty);

        unit.setStartLocX(0);
        unit.setStartLocY(0);
        unit.setStartLocZ(0);

        unit.setEndLocX(endLoc.getBlockX() - startLoc.getBlockX());
        unit.setEndLocY(endLoc.getBlockY() - startLoc.getBlockY());
        unit.setEndLocZ(endLoc.getBlockZ() - startLoc.getBlockZ());

        // alle Blöcke durchgehen
        ArrayList<JSONBlock> blocklist = new ArrayList<JSONBlock>();

        for(int z = minZ; z <= maxZ;z++)
        {
            for(int y = minY; y <= maxY; y++)
            {
                for(int x = minX; x <= maxX; x++)
                {
                    Location loc = new Location(startLoc.getWorld(), x,y,z);
                    Block b = loc.getBlock();
                    if(b.getType() != Material.AIR)
                    {
                        JSONBlock block = new JSONBlock();
                        block.setData(b.getData());
                        block.setMaterial(b.getType());
                        block.setX(x - startLoc.getBlockX());
                        block.setY(y - startLoc.getBlockY());
                        block.setZ(z - startLoc.getBlockZ());
                        blocklist.add(block);

                    }
                }
            }
        }

        unit.setBlocklist(blocklist);

        String name = args[0];
        String content = gson.toJson(unit,JSONUnit.class);
        File file = new File("units/" + name + ".unit");
        if(!file.exists())
        {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        BufferedWriter writer = null;
        try
        {
            writer = new BufferedWriter( new FileWriter("units/" + name + ".unit"));
            writer.write(content);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        p.sendMessage(i.PREFIX + name + " wurde gespeichert!");

        return true;
    }
}
