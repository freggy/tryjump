package de.bergwerklabs.tryjump.gameserver.listener;

import de.bergwerklabs.tryjump.gameserver.TryJump;
import de.bergwerklabs.util.GameState;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by nexotekHD on 14.04.2016.
 */
public class ListenerPlayerInteract implements Listener {

    private HashMap<UUID, Long> cooldown = new HashMap<UUID,Long>();
    // checkpoint cooldown of 2 sec





    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e)
    {
        Player p = e.getPlayer();
        // case checkpoint reached
        if(TryJump.getInstance().getCurrentState() == GameState.RUNNING)
        {
            if(e.getAction() == Action.PHYSICAL)
            {
                if(e.getClickedBlock().getType() == Material.GOLD_PLATE && !(cooldown(p.getUniqueId())))
                {
                    TryJump.getInstance().getGameSession().checkpointReached(p,e.getClickedBlock().getRelative(BlockFace.DOWN));
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            e.getClickedBlock().setType(Material.AIR);
                        }
                    }.runTaskLater(TryJump.getInstance(), 1L);
                }
            }
        }

        // case shop interacted
        if(TryJump.getInstance().getCurrentState() == GameState.RUNNING)
        {
            if(e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR)
            {
                if(p.getItemInHand() != null)
                {
                    if(p.getItemInHand().getType() == Material.CHEST)
                    {
                        e.setCancelled(true);
                        TryJump.getInstance().getGameSession().getItemShop().open(p);
                    }
                }
            }
        }

        // case achievements menu interacted
        if(TryJump.getInstance().getCurrentState() == GameState.WAITING)
        {
            if(e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR)
            {
                if(p.getItemInHand() != null)
                {
                    if(p.getItemInHand().getType() == Material.NETHER_STAR)
                    {
                        e.setCancelled(true);
                        TryJump.getInstance().getAchievementManager().openOverview(p);
                    }
                }
            }
        }

        // case instant tod
        if(TryJump.getInstance().getCurrentState() == GameState.RUNNING)
        {
            if(e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR)
            {
                if(p.getItemInHand() != null)
                {
                    if(p.getItemInHand().getType() == Material.INK_SACK)
                    {
                        if(!TryJump.getInstance().getGameSession().isFreezed())
                        {
                            if(!TryJump.getInstance().getGameSession().instantTodCooldown(p.getUniqueId()))
                            {
                                TryJump.getInstance().getGameSession().onJumpFail(p);
                            }else
                            {
                                p.sendMessage(TryJump.getInstance().getChatPrefix() + ChatColor.RED + "Bitte warte noch einen Moment, bevor du den Instant Tod ausführst!");
                            }
                        }
                    }
                }
            }
        }

        // case enchantming
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK)
        {
            if(e.getClickedBlock().getType() == Material.ENCHANTMENT_TABLE)
            {
                e.setCancelled(true);
                if(TryJump.getInstance().getCurrentState() == GameState.RUNNING)
                {
                    if(TryJump.getInstance().getGameSession().isBuyphase())
                    {
                        if(p.getItemInHand() != null)
                        {
                            TryJump.getInstance().getGameSession().getItemShop().enchant(p.getItemInHand(), p);
                        }
                    }
                }
            }
        }

        // case gapple in buyphase
        if(TryJump.getInstance().getCurrentState() == GameState.RUNNING)
        {
            if(TryJump.getInstance().getGameSession().isBuyphase())
            {
                if(e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR)
                {
                    if(p.getItemInHand() != null)
                    {
                        if(p.getItemInHand().getType() == Material.GOLDEN_APPLE)
                        {
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }

    }


    private boolean cooldown(UUID uuid)
    {
        if(cooldown.containsKey(uuid))
        {
            if(cooldown.get(uuid) + 2000 > System.currentTimeMillis())
            {
                return true;
            }
        }
        cooldown.put(uuid,System.currentTimeMillis());
        return false;
    }



}
