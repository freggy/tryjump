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

import java.io.*;
import java.util.ArrayList;

/**
 * Created by nexotekHD on 09.04.2016.
 */
public class SubmitCommand implements CommandExecutor {


    public boolean onCommand(CommandSender cs, Command command, String label, String[] args) {
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


        double lowX = 0;
        double highX = 0;

        double lowY = 0;
        double highY = 0;

        double lowZ = 0;
        double highZ = 0;

        Location pos1 = i.pos1.get(p.getUniqueId());
        Location pos2 = i.pos2.get(p.getUniqueId());
        Location startLoc = i.startLoc.get(p.getUniqueId());
        Location endLoc = i.endLoc.get(p.getUniqueId());

        if(pos1.getX() < pos2.getX())
        {
            lowX = pos1.getX();
            highX = pos2.getX();
        }else
        {
            highX = pos1.getX();
            lowX = pos2.getX();
        }

        if(pos1.getY() < pos2.getY())
        {
            lowY = pos1.getY();
            highY = pos2.getY();
        }else
        {
            highY = pos1.getY();
            lowY = pos2.getY();
        }

        if(pos1.getZ() < pos2.getZ())
        {
            lowZ = pos1.getZ();
            highZ = pos2.getZ();
        }else
        {
            highZ = pos1.getZ();
            lowZ = pos2.getZ();
        }


        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JSONUnit unit = new JSONUnit();
        unit.setDifficulty(difficulty);


        unit.setStartLocX(0);
        unit.setStartLocY(0);
        unit.setStartLocZ(0);


        unit.setEndLocX(endLoc.getX() - startLoc.getX());
        unit.setEndLocY(endLoc.getY() - startLoc.getY());
        unit.setEndLocZ(endLoc.getZ() - startLoc.getZ());

        ArrayList<JSONBlock> blocklist = new ArrayList<JSONBlock>();

        System.out.println("lowX " + lowX);
        System.out.println("highX " + highX);
        System.out.println("lowY " + lowY);
        System.out.println("highY " + highY);
        System.out.println("lowZ " + lowZ);
        System.out.println("highZ " + highZ);

        // alle Blöcke durchgehen
        p.sendMessage(i.PREFIX + "speichere ...");

        for(int z = 0; z <= (highZ - lowZ);z++)
        {
            for(int y = 0; y <= (highY - lowY);y++)
            {
                for(int x = 0; x <= (highX - lowX);x++)
                {
                    Location loc = new Location(startLoc.getWorld(),lowX +x,lowY +y,lowZ +z);
                    Block b = loc.getBlock();
                    if(b.getType() != Material.AIR)
                    {
                        System.out.println(b.getType());
                        JSONBlock block = new JSONBlock();
                        block.setData(b.getData());
                        block.setMaterial(b.getType());
                        block.setX(round (x + lowX - startLoc.getBlockX()));
                        block.setY(round(y + lowY - startLoc.getBlockY()));
                        block.setZ(round (z + lowZ - startLoc.getBlockZ()));
                        blocklist.add(block);
                        System.out.println();
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

    private int round(double toRound)
    {
        if(toRound < 0 && toRound > -1)
        {
            return -1;
        }
        return (int)toRound;
    }
}
