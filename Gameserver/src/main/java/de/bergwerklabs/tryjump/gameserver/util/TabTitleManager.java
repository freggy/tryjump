package de.bergwerklabs.tryjump.gameserver.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import org.bukkit.entity.Player;

public class TabTitleManager
{
    
  private static void sendPacket(Player player, Object packet)
  {
    try
    {
      Object handle = player.getClass().getMethod("getHandle", new Class[0]).invoke(player, new Object[0]);
      Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
      playerConnection.getClass().getMethod("sendPacket", new Class[] { getNMSClass("Packet") }).invoke(playerConnection, new Object[] { packet });
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }
  
  private static Class<?> getNMSClass(String class_name)
  {
    String version = org.bukkit.Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    try
    {
      return Class.forName("net.minecraft.server." + version + "." + class_name);
    }
    catch (ClassNotFoundException ex)
    {
      ex.printStackTrace();
    }
    return null;
  }
  
  public static void sendTablist(Player p, String header, String footer)
  {
    if (header == null) {
      header = "";
    }
    if (footer == null) {
      footer = "";
    }
    try
    {
      Object tabheader = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", new Class[] { String.class }).invoke(null, new Object[] { "{\"text\": \"" + header + "\"}" });
      Object tabfooter = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", new Class[] { String.class }).invoke(null, new Object[] { "{\"text\":\"" + footer + "\"}" });
      
      Constructor tablist = getNMSClass("PacketPlayOutPlayerListHeaderFooter").getConstructor(new Class[] { getNMSClass("IChatBaseComponent") });
      Object THpacket = tablist.newInstance(new Object[] { tabheader });
      Field f = THpacket.getClass().getDeclaredField("b");
      f.setAccessible(true);
      f.set(THpacket, tabfooter);
      sendPacket(p, THpacket);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
    
}