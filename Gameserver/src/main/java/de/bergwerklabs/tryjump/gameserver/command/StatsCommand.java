package de.bergwerklabs.tryjump.gameserver.command;

import de.bergwerklabs.tryjump.gameserver.TryJump;
import de.bergwerklabs.util.playerdata.DataRegistry;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

        DataRegistry.DataGroup set = null;
        String name = "";
        if(args.length == 0)
        {
            set = TryJump.getInstance().getUtil().getDataRegistry().getSet(p).getGroup("stats.tryjump");
            name = p.getName();
        }else if(args.length >= 1)
        {
            Player lookup = Bukkit.getPlayer(args[0]);
            if(lookup != null && lookup.isOnline())
            {
                set = TryJump.getInstance().getUtil().getDataRegistry().getSet(lookup).getGroup("stats.tryjump");
                lookup.getName();
            }
        }
        if(set == null)
        {
            p.sendMessage(TryJump.getInstance().getChatPrefix() + "Der Spieler " + args[0] +" befindet sich nicht auf diesem Gameserver! Du kannst aktuell nur die Stats von deinen Mitspielern abfragen!");
            return true;
        }

        String points = set.getValue("tryjump.points","0");
        String played = set.getValue("tryjump.played","0");
        String wins = set.getValue("tryjump.wins","0");
        String kills = set.getValue("tryjump.kills","0");
        String deaths = set.getValue("tryjump.deaths","0");
        String goals = set.getValue("tryjump.goals","0");
        String units = set.getValue("tryjump.units","0");


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
