package de.bergwerklabs.tryjump.gameserver.command;

import de.bergwerklabs.tryjump.gameserver.TryJump;
import de.bergwerklabs.tryjump.gameserver.util.AtlantisStatsWrapper;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by nexotekHD on 06.05.2016.
 */
public class StatsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender cs, Command command, String s, String[] args) {

        if(! (cs instanceof Player))
        {
            return true;
        }


        Player p = (Player)cs;

        UUID uuidToUse = null;

        String name = "";
        if(args.length == 0)
        {
            uuidToUse = p.getUniqueId();
        }else if(args.length >= 1)
        {
            Player lookup = Bukkit.getPlayer(args[0]);
            if(lookup != null && lookup.isOnline())
            {
                uuidToUse = lookup.getUniqueId();
            }
        }
        if(uuidToUse == null)
        {
            p.sendMessage(TryJump.getInstance().getChatPrefix() + "Der Spieler " + args[0] +" befindet sich nicht auf diesem Gameserver! Du kannst aktuell nur die Stats von deinen Mitspielern abfragen!");
            return true;
        }

        String points = String.valueOf(AtlantisStatsWrapper.getPoints(uuidToUse));
        String played = String.valueOf(AtlantisStatsWrapper.getGamesPlayed(uuidToUse));
        String wins = String.valueOf(AtlantisStatsWrapper.getWins(uuidToUse));
        String kills = String.valueOf(AtlantisStatsWrapper.getKills(uuidToUse));
        String deaths = String.valueOf(AtlantisStatsWrapper.getDeaths(uuidToUse));
        String goals = String.valueOf(AtlantisStatsWrapper.getGoals(uuidToUse));
        String units = String.valueOf(AtlantisStatsWrapper.getUnits(uuidToUse));


        p.sendMessage(TryJump.getInstance().getChatPrefix() + ChatColor.AQUA + "Statistiken:");
        p.sendMessage(TryJump.getInstance().getChatPrefix() + ChatColor.WHITE + "\u25A0 " + ChatColor.GRAY + "Name: " + ChatColor.AQUA + name);
        p.sendMessage(TryJump.getInstance().getChatPrefix() + ChatColor.WHITE + "\u25A0 " + ChatColor.GRAY + "Gespielte Spiele: " + ChatColor.AQUA + played);
        p.sendMessage(TryJump.getInstance().getChatPrefix() + ChatColor.WHITE + "\u25A0 " + ChatColor.GRAY + "Gewonnene Spiele: " + ChatColor.AQUA + wins);
        p.sendMessage(TryJump.getInstance().getChatPrefix() + ChatColor.WHITE + "\u25A0 " + ChatColor.GRAY + "Punkte: " + ChatColor.AQUA + points);
        p.sendMessage(TryJump.getInstance().getChatPrefix() + ChatColor.WHITE + "\u25A0 " + ChatColor.GRAY + "Kills: " + ChatColor.GREEN + kills);
        p.sendMessage(TryJump.getInstance().getChatPrefix() + ChatColor.WHITE + "\u25A0 " + ChatColor.GRAY + "Tode: " + ChatColor.RED + deaths);
        p.sendMessage(TryJump.getInstance().getChatPrefix() + ChatColor.WHITE + "\u25A0 " + ChatColor.GRAY + "Erreichte Ziele: " + ChatColor.AQUA + goals);
        p.sendMessage(TryJump.getInstance().getChatPrefix() + ChatColor.WHITE + "\u25A0 " + ChatColor.GRAY + "Units geschafft: " + ChatColor.AQUA + units);

        return true;
    }
}
