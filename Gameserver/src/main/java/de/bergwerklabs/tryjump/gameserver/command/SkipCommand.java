package de.bergwerklabs.tryjump.gameserver.command;

import de.bergwerklabs.tryjump.gameserver.TryJump;
import de.bergwerklabs.util.GameState;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by nexotekHD on 12.05.2016.
 */
public class SkipCommand implements CommandExecutor {

    private static ArrayList<UUID> skiped = new ArrayList<UUID>();

    @Override
    public boolean onCommand(CommandSender cs, Command command, String s, String[] args) {

        if(!(cs instanceof Player))
        {
            return true;
        }
        Player p = (Player)cs;
        if(!TryJump.getInstance().getGameSession().isBuyphase())
        {
            p.sendMessage(TryJump.getInstance().getChatPrefix() + ChatColor.RED + "Du kannst diesen Befehl nur in der Kauf-Phase ausführen!");
            return true;
        }
        if(!TryJump.getInstance().getGameSession().getIngame_players().contains(p.getUniqueId()))
        {
            p.sendMessage(TryJump.getInstance().getChatPrefix() + ChatColor.RED + "Du musst Ingame Spieler sein!");
            return true;
        }
        if(skiped.contains(p.getUniqueId()))
        {
            p.sendMessage(TryJump.getInstance().getChatPrefix() + ChatColor.RED + "Du hast diesen Befehl bereits ausgeführt!");
            return true;
        }

        skiped.add(p.getUniqueId());
        Bukkit.broadcastMessage(TryJump.getInstance().getChatPrefix() + "Der Spieler " + TryJump.getInstance().getColor(p) + p.getName() + ChatColor.GRAY + " hat dafür gestimmt, die Wartezeit zu verkürzen " + ChatColor.AQUA + "(" + skiped.size() + "/" + TryJump.getInstance().getGameSession().getIngame_players().size() +")"+ChatColor.GRAY +"! " + ChatColor.GRAY + ChatColor.ITALIC +"[/skip]");
        for(Player play : Bukkit.getOnlinePlayers())
        {
            play.playSound(play.getEyeLocation(), Sound.NOTE_PLING, 100,20);
        }

        if(skiped.size() >= TryJump.getInstance().getGameSession().getIngame_players().size())
        {
            Bukkit.broadcastMessage(TryJump.getInstance().getChatPrefix() + "Die Wartezeit wurde auf " + ChatColor.AQUA + "5 Sekunden " + ChatColor.GRAY + "verkürzt!");
            TryJump.getInstance().getGameSession().setTimeleft(5);
        }

        return true;
    }
}
