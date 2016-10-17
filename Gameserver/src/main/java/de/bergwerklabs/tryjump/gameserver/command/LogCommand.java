package de.bergwerklabs.tryjump.gameserver.command;

import de.bergwerklabs.netsyn.protocol.api.EnumPermissionGroup;
import de.bergwerklabs.tryjump.gameserver.TryJump;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * Created by Florian on 17.10.2016.
 */
public class LogCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command command, String s, String[] args) {
        if(cs instanceof Player)
            if(!EnumPermissionGroup.getByName(TryJump.getInstance().getPermissionService().getPlayerPrimaryGroup(((Player)cs).getUniqueId())).isTeam())
                return false;
        String date = new SimpleDateFormat("dd.MM#HH.mm").format(Calendar.getInstance().getTime());
        String id = "" + new Random().nextInt(999);
        String filename = "LOG_TRYJUMP_" + date + id + ".log";
        try
        {
            copyFolder("./logs/latest.log", "/mnt/cloud_storage/TEAM_SERVERLOGS/" + filename);
        }catch(Exception ex){}
        cs.sendMessage(TryJump.getInstance().getChatPrefix() + "§eDein Log wurde abgespeichert. Er ist unter der ID §c" + id + " §eeinsehbar.");
        return true;
    }

    // sorry for this giant mess
    public static void copyFolder(String src, String dest)
    {
        copyFolder(new File(src), new File(dest));
    }

    public static void copyFolder(File src, File dest)
    {
        copyFolder(src, dest, null);
    }

    public static void copyFolder(File src, File dest, List<String> ignore)
    {
        if (src.isDirectory())
        {
            if (!dest.exists())
            {
                dest.mkdir();
            }
            String files[] = src.list();
            for (String file : files)
            {
                File srcFile = new File(src, file);
                File destFile = new File(dest, file);
                copyFolder(srcFile, destFile, ignore);
            }
        } else
        {
            try
            {
                if (ignore != null && ignore.contains(src.getName()))
                {
                    System.out.println("\u001B[36mIgnore: \u001B[33m" + src.getPath().replaceAll(src.getName(), "\u001B[36m") + src.getName() + "\u001B[37m");
                } else
                {
                    InputStream in = new FileInputStream(src);
                    OutputStream out = new FileOutputStream(dest);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0)
                    {
                        out.write(buffer, 0, length);
                    }
                    in.close();
                    out.close();
                    System.out.println("\u001B[35mCopy \u001B[33m" + src.getPath().replaceAll(src.getName(), "\u001B[36m") + src.getName() + " \u001B[35mto \u001B[33m"
                            + dest.getPath().replaceAll(src.getName(), "\u001B[36m") + src.getName() + "\u001B[37m");
                }
            } catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }
}
