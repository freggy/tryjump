package de.bergwerklabs.tryjump.gameserver.listener;

import de.bergwerklabs.tryjump.gameserver.TryJump;
import de.bergwerklabs.tryjump.gameserver.util.RoundStats;
import de.bergwerklabs.util.GameState;
import de.bergwerklabs.util.playerdata.Currency;
import de.bergwerklabs.util.playerdata.DataRegistry;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Score;
import org.bukkit.util.Vector;

/**
 * Created by nexotekHD on 04.05.2016.
 */
public class ListenerPlayerDeath implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e)
    {
        Player p = e.getEntity();
        Player killer = p.getKiller();

        // add death to stats
        DataRegistry.DataSet set = TryJump.getInstance().getUtil().getDataRegistry().getSet(p.getUniqueId());
        DataRegistry.DataGroup group = set.getGroup("stats.tryjump");
        String value = group.getValue("tryjump.deaths", "0");
        int vlue = Integer.parseInt(value);
        group.setValue("tryjump.deaths", String.valueOf(vlue + 1));

        RoundStats deathStats = TryJump.getInstance().getGameSession().getRoundStats(p.getUniqueId());
        if(deathStats != null)
        {
            deathStats.setDeaths(deathStats.getDeaths() +1);
        }


        // achievement
        TryJump.getInstance().getAchievementManager().checkFirstBlood(p);

        if(killer != null)
        {
            //add kill to killers stats
            set = TryJump.getInstance().getUtil().getDataRegistry().getSet(killer);
            group = set.getGroup("stats.tryjump");
            value = group.getValue("tryjump.kills", "0");
            vlue = Integer.parseInt(value);
            group.setValue("tryjump.kills", String.valueOf(vlue + 1));

            // add points to killers stats
            value = group.getValue("tryjump.points", "0");
            vlue = Integer.parseInt(value);
            group.setValue("tryjump.points", String.valueOf(vlue + 15));
            killer.sendMessage(TryJump.getInstance().getChatPrefix() + "+ 15 Ranking Punkte");

            RoundStats killerStats = TryJump.getInstance().getGameSession().getRoundStats(killer.getUniqueId());
            if(killerStats != null)
            {
                killerStats.setKills(killerStats.getKills() +1);
                killerStats.setPoints(killerStats.getPoints() + 15);
            }

            // add network coins
            /*
            DataRegistry.DataGroup coins_group = set.getGroup("network.currency");
            value = coins_group.getValue("network.coins", "0");
            vlue = Integer.parseInt(value);

            killer.sendMessage(TryJump.getInstance().getChatPrefix() + ChatColor.AQUA + "Du erhältst " + ChatColor.GREEN + "5 " + ChatColor.AQUA + "Coin!");
            if(killer.hasPermission("bergwerklabs.full-join"))
            {
                coins_group.setValue("network.coins",String.valueOf((vlue +10)));
                killer.sendMessage(TryJump.getInstance().getChatPrefix() + ChatColor.GOLD + "+ " + ChatColor.YELLOW + "5 Coins " + ChatColor.GOLD + "(Premium Boost)");
            }else
            {
                coins_group.setValue("network.coins",String.valueOf((vlue +5)));
            }
            */

            Currency.addCoinsWithPremiumAmplifier(killer,5);

            // achievement
            TryJump.getInstance().getAchievementManager().checkFirstKill(killer);
            TryJump.getInstance().getAchievementManager().checkKillstreak(killer);

            killer.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 300, 1));
            killer.playSound(killer.getEyeLocation(), Sound.LEVEL_UP,100,10);
            e.setDeathMessage(TryJump.getInstance().getChatPrefix() + "Der Spieler " + TryJump.getInstance().getColor(p) + p.getName() + ChatColor.GRAY + " wurde von " + TryJump.getInstance().getColor(killer) +killer.getName() + ChatColor.GRAY + " getötet!");
        }else
        {
            e.setDeathMessage(TryJump.getInstance().getChatPrefix() + "Der Spieler " + TryJump.getInstance().getColor(p)+ p.getName() + ChatColor.GRAY + " ist gestorben!");
        }

        if(TryJump.getInstance().getGameStateManager().getState() == GameState.RUNNING_DEATHMATCH && (!TryJump.getInstance().getGameSession().isGrace()))
        {
            Score score = p.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getScore(p);
            score.setScore(score.getScore() - 1);
            if(score.getScore() <= 0)
            {

                p.setHealth(20.0);
                p.setVelocity(new Vector(0, 3, 0));
                p.setGameMode(GameMode.SPECTATOR);
                TryJump.getInstance().getGameSession().eliminate(p,true);
                Bukkit.getScheduler().scheduleSyncDelayedTask(TryJump.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        p.setFlying(true);
                    }
                },10L);
            }
        }


    }

}
