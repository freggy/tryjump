package de.bergwerklabs.tryjump.gameserver.util;

import java.util.ArrayList;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;

public class Stoplag implements Listener
{

    public static ArrayList<String> ACTIVE_WORLDS = new ArrayList<>();

    public static void setActive(boolean b, World w)
    {
        if (b)
        {
            if (!ACTIVE_WORLDS.contains(w.getName()))
            {
                ACTIVE_WORLDS.add(w.getName());
            }
        } else if (ACTIVE_WORLDS.contains(w.getName()))
        {
            ACTIVE_WORLDS.remove(w.getName());
        }
    }

    public static boolean isActive(World w)
    {
        return ACTIVE_WORLDS.contains(w.getName());
    }

    @EventHandler
    public void handle(BlockFormEvent e)
    {
        if (ACTIVE_WORLDS.contains(e.getBlock().getLocation().getWorld().getName()))
        {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void handle(BlockIgniteEvent e)
    {
        if (ACTIVE_WORLDS.contains(e.getBlock().getLocation().getWorld().getName()))
        {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void handle(BlockBurnEvent e)
    {
        if (ACTIVE_WORLDS.contains(e.getBlock().getLocation().getWorld().getName()))
        {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void handle(BlockPhysicsEvent e)
    {
        if (ACTIVE_WORLDS.contains(e.getBlock().getLocation().getWorld().getName()))
        {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void handle(LeavesDecayEvent e)
    {
        if (ACTIVE_WORLDS.contains(e.getBlock().getLocation().getWorld().getName()))
        {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void handle(EntityBlockFormEvent e)
    {
        if (ACTIVE_WORLDS.contains(e.getBlock().getLocation().getWorld().getName()))
        {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void handle(BlockSpreadEvent e)
    {
        if (ACTIVE_WORLDS.contains(e.getBlock().getLocation().getWorld().getName()))
        {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void handle(EntityExplodeEvent e)
    {
        if (ACTIVE_WORLDS.contains(e.getLocation().getWorld().getName()))
        {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void handle(ExplosionPrimeEvent e)
    {
        if (ACTIVE_WORLDS.contains(e.getEntity().getWorld().getName()))
        {
            e.setCancelled(true);
        }
    }
}