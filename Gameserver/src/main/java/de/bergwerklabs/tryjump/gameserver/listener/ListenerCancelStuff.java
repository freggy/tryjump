package de.bergwerklabs.tryjump.gameserver.listener;

import de.bergwerklabs.atlantis.client.bukkit.GamestateManager;
import de.bergwerklabs.atlantis.columbia.packages.gameserver.spigot.gamestate.Gamestate;
import de.bergwerklabs.tryjump.gameserver.TryJump;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

/**
 * Created by nexotekHD on 12.04.2016.
 */
public class ListenerCancelStuff implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e)
    {
        if (e.getPlayer().getGameMode() != GameMode.CREATIVE)
        {
            e.setCancelled(true);
        } else
        {
        }

        if(e.getBlock().getType() == Material.CAKE_BLOCK || e.getBlock().getType() == Material.CAKE)
        {
            e.setCancelled(false);
        }

    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e)
    {
        if (e.getPlayer().getGameMode() != GameMode.CREATIVE) e.setCancelled(true);
        if(GamestateManager.getCurrentState() == Gamestate.RUNNING_DEATHMATCH)
        {
            if(e.getBlock().getType() == Material.CAKE_BLOCK)
            {
                e.setCancelled(false);
            }
            if(e.getBlock().getType() == Material.TNT)
            {
                e.setCancelled(false);
                e.getBlock().setType(Material.AIR);
                TNTPrimed tnt = (TNTPrimed)e.getBlock().getWorld().spawn(e.getBlock().getLocation(), TNTPrimed.class);
                tnt.setFuseTicks(40);

            }
        }
    }

    @EventHandler
    public void onBlockFade(BlockFadeEvent e)
    {
        e.setCancelled(true);
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent e)
    {
        e.setCancelled(true);
    }

    @EventHandler
    public void onBlockFromTo(BlockFromToEvent e)
    {
        e.setCancelled(true);
    }
    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent e)
    {
        e.setCancelled(true);
    }

    @EventHandler
    public void onHangingBreakByEntity(HangingBreakByEntityEvent e)
    {
        e.setCancelled(true);
    }

    @EventHandler
    public void onLeaveDecay(LeavesDecayEvent e)
    {
        e.setCancelled(true);
    }

    @EventHandler
    public void onBlockSpread(BlockSpreadEvent e)
    {
        e.setCancelled(true);
    }


    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent e)
    {
        if(e.getEntity() instanceof Player && GamestateManager.getCurrentState() != Gamestate.RUNNING_DEATHMATCH)
        {
            Player p =  (Player)e.getEntity();
            p.updateInventory();
            e.setCancelled(true);
        }

    }


    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent e)
    {
        e.setCancelled(true);
        if(GamestateManager.getCurrentState() == Gamestate.RUNNING_DEATHMATCH)
        {
            e.setCancelled(false);
        }
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent e)
    {
        e.setCancelled(true);
    }

    @EventHandler
    public void onItemSpawn(ItemSpawnEvent e)
    {
        if(e.getEntity() != null && e.getEntity().getItemStack() != null && e.getEntity().getItemStack().getType() == Material.GOLD_PLATE)
        {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent e)
    {
        e.blockList().clear();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e)
    {
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR)
        {
            Player p = e.getPlayer();
            if(p.getItemInHand() != null)
            {
                if(p.getItemInHand().getType() == Material.POTION && GamestateManager.getCurrentState() != Gamestate.RUNNING_DEATHMATCH)
                {
                    e.setCancelled(true);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(TryJump.getInstance(), new Runnable() {
                        @Override
                        public void run() {
                            p.updateInventory();
                        }
                    },2L);
                }
            }
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e)
    {
        if(e.getInventory().getHolder() instanceof Chest || e.getInventory().getHolder() instanceof DoubleChest)
        {
            e.setCancelled(true);
        }
    }

    /*
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e)
    {
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK)
        {
            if(e.getClickedBlock().getType() == Material.WORKBENCH)
            {
                e.setCancelled(true);
            }else if(e.getClickedBlock().getType() == Material.ANVIL)
            {
                e.setCancelled(true);
            }else if(e.getClickedBlock().getType() == Material.FURNACE)
            {
                e.setCancelled(true);
            }else if(e.getClickedBlock().getType() == Material.DROPPER)
            {
                e.setCancelled(true);
            }else if(e.getClickedBlock().getType() == Material.ENCHANTMENT_TABLE)
            {
                e.setCancelled(true);
            }else if(e.getClickedBlock().getType() == Material.BREWING_STAND)
            {
                e.setCancelled(true);
            }
        }
    }

     */

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent e)
    {
        Player p = e.getPlayer();
        if(p.getGameMode() == GameMode.SPECTATOR)
        {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e)
    {
        if(GamestateManager.getCurrentState() == Gamestate.RUNNING)
        {
            e.setCancelled(true);
            Bukkit.getScheduler().scheduleSyncDelayedTask(TryJump.getInstance(), new Runnable() {
                @Override
                public void run() {
                    e.getPlayer().updateInventory();
                }
            },2L);

        }else if(GamestateManager.getCurrentState() == Gamestate.WAITING)
        {
            e.setCancelled(true);
            Bukkit.getScheduler().scheduleSyncDelayedTask(TryJump.getInstance(), new Runnable() {
                @Override
                public void run() {
                    e.getPlayer().updateInventory();
                }
            }, 2L);
        }
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent e)
    {
        if(e.getItem().getItemStack().getType() == Material.GOLD_PLATE)
        {
            e.setCancelled(true);
        }
        if(GamestateManager.getCurrentState() == Gamestate.RUNNING)
        {
            e.setCancelled(true);
        }
    }

}
