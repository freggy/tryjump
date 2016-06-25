package de.bergwerklabs.tryjump.gameserver.command;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by nexotekHD on 25.06.2016.
 */
public class FixCommand implements CommandExecutor {

    private static HashMap<UUID,Long> cooldownMap = new HashMap<UUID,Long>();


    @Override
    public boolean onCommand(CommandSender cs, Command command, String s, String[] args) {

        if(!(cs instanceof Player))
        {
            return true;
        }

        Player p = (Player)cs;

        if(cooldown(p.getUniqueId()))
        {
            p.sendMessage("Du darfst diesen Befehl nur 1x pro Minute ausführen!");
            return true;
        }
        p.sendMessage("Fixing ...");
        ArrayList<Chunk> toUpdate = new ArrayList<Chunk>();
        toUpdate.add(p.getLocation().getChunk());
        toUpdate.add(p.getLocation().clone().add(0, 0, 16).getChunk());
        toUpdate.add(p.getLocation().clone().add(0,0,-16).getChunk());
        toUpdate.add(p.getLocation().clone().add(-16,0,0).getChunk());
        toUpdate.add(p.getLocation().clone().add(16,0,0).getChunk());

        Location loc = p.getLocation().clone();

        for(Chunk c : toUpdate)
        {
            /*
            PacketMapChunk packet = new PacketMapChunk(c);
            packet.send(p);
            */
            p.getWorld().refreshChunk(c.getX(),c.getZ());
        }

        for(Player pl : Bukkit.getOnlinePlayers())
        {
            p.hidePlayer(pl);
        }
        for(Player pl : Bukkit.getOnlinePlayers())
        {
            p.showPlayer(pl);
        }

        p.teleport(loc);

        p.sendMessage("Done!");



        return true;
    }

    private boolean cooldown(UUID uuid)
    {
        if(cooldownMap.containsKey(uuid))
        {
            if(cooldownMap.get(uuid) + 60000 > System.currentTimeMillis())
            {
                return true;
            }
        }
        cooldownMap.put(uuid,System.currentTimeMillis());
        return false;
    }
}
