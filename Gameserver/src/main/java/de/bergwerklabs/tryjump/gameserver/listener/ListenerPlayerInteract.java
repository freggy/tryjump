package de.bergwerklabs.tryjump.gameserver.listener;

import de.bergwerklabs.atlantis.client.bukkit.GamestateManager;
import de.bergwerklabs.atlantis.columbia.packages.gameserver.spigot.gamestate.Gamestate;
import de.bergwerklabs.tryjump.gameserver.TryJump;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

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
        if(GamestateManager.getCurrentState() == Gamestate.RUNNING)
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
        if(GamestateManager.getCurrentState() == Gamestate.RUNNING)
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
        if(GamestateManager.getCurrentState() == Gamestate.WAITING)
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
        if(GamestateManager.getCurrentState() == Gamestate.RUNNING)
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
                if(GamestateManager.getCurrentState() == Gamestate.RUNNING)
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

        // case cake upstacking
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK)
        {
            if(e.getClickedBlock().getType() == Material.CAKE_BLOCK)
            {
                if(p.getItemInHand().getType() == Material.CAKE)
                {
                    e.setCancelled(true);
                    p.sendMessage(TryJump.getInstance().getChatPrefix() + ChatColor.RED + "Du darfst keine Kuchen stapeln!");
                    p.playSound(p.getEyeLocation(), Sound.NOTE_BASS,50,1);
                    p.updateInventory();
                }
            }
        }

        // case gapple in buyphase
        if(GamestateManager.getCurrentState() == Gamestate.RUNNING)
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
