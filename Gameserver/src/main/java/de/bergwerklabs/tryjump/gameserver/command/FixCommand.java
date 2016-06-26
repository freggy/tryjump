package de.bergwerklabs.tryjump.gameserver.command;

import de.bergwerklabs.tryjump.gameserver.TryJump;
import de.bergwerklabs.util.GameState;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
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

        Block b = p.getLocation().getBlock().getRelative(BlockFace.DOWN);
        Material before = b.getType();
        byte data = b.getData();
        b.setType(Material.BEDROCK);

        Bukkit.getScheduler().scheduleSyncDelayedTask(TryJump.getInstance(), new Runnable() {
            @Override
            public void run() {
                b.setType(before);
                b.setData(data);
            }
        }, 2L);

        b.getChunk().load();

        if(TryJump.getInstance().getCurrentState() == GameState.RUNNING && !(TryJump.getInstance().getGameSession().isBuyphase()) && !(TryJump.getInstance().getGameSession().isDeathmatch()))
        {
            TryJump.getInstance().getGameSession().fix(p);

            Location loci = p.getLocation().clone();

            Bukkit.getScheduler().scheduleSyncDelayedTask(TryJump.getInstance(), new Runnable() {
                @Override
                public void run() {
                    p.teleport(Bukkit.getWorld("spawn").getSpawnLocation());
                }
            },4L);

            Bukkit.getScheduler().scheduleSyncDelayedTask(TryJump.getInstance(), new Runnable() {
                @Override
                public void run() {
                    p.teleport(loci);
                }
            },11L);
        }//////


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
