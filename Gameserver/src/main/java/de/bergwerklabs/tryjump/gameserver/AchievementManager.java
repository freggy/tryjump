package de.bergwerklabs.tryjump.gameserver;

import de.bergwerklabs.tryjump.gameserver.util.AtlantisStatsWrapper;
import de.bergwerklabs.tryjump.gameserver.util.PlayerJumpSession;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by nexotekHD on 07.05.2016.
 */
public class AchievementManager {

    private HashMap<UUID,HashMap<String,Boolean>> cache = new HashMap<UUID,HashMap<String,Boolean>>();



    // achievement specific attributes
    // first blood
    boolean firstblood = false;

    // first kill
    boolean firstkill = false;

    // killstreak
    HashMap<UUID, Integer> killstreak = new HashMap<UUID, Integer>();

    // sprengmeister
    HashMap<UUID, Integer> sprengmeister = new HashMap<UUID,Integer>();

    // schütze
    HashMap<UUID, Integer> schuetze = new HashMap<UUID,Integer>();


    public AchievementManager()
    {

    }

    private ArrayList<String> achievementList()
    {
        ArrayList<String> list = new ArrayList<String>();
        list.add("achievements.tryjump.firsttry");
        list.add("achievements.tryjump.firstblood");
        list.add("achievements.tryjump.firstkill");
        list.add("achievements.tryjump.killstreak");
        list.add("achievements.tryjump.unbesiegbar");
        list.add("achievements.tryjump.sprengmeister");
        list.add("achievements.tryjump.schuetze");
        return list;
    }

    private boolean hasAchievement(Player p, String key)
    {
        return hasAchivement(p.getUniqueId(),key);
    }

    private boolean hasAchivement(UUID uuid, String key)
    {
        return AtlantisStatsWrapper.hasAchievement(uuid, key);
    }
    private void setAchievement(UUID uuid,String key, boolean value)
    {
        cache.get(uuid).put(key,value);
        AtlantisStatsWrapper.saveAchievement(uuid, key, value);
    }

    private String getTitle(String key)
    {
        if(key.equalsIgnoreCase("achievements.tryjump.firsttry"))
        {
            return "First Try";
        }else if(key.equalsIgnoreCase("achievements.tryjump.firstblood"))
        {
            return "First Blood";
        }else if(key.equalsIgnoreCase("achievements.tryjump.firstkill"))
        {
            return "First Kill";
        }else if(key.equalsIgnoreCase("achievements.tryjump.killstreak"))
        {
            return "Killstreak";
        }else if(key.equalsIgnoreCase("achievements.tryjump.unbesiegbar"))
        {
            return "Unbesiegbar";
        }else if(key.equalsIgnoreCase("achievements.tryjump.sprengmeister"))
        {
            return "Sprengmeister";
        }else if(key.equalsIgnoreCase("achievements.tryjump.schuetze"))
        {
            return "Schütze";
        }
        return null;
    }
    private String getDescription(String key)
    {
        if(key.equalsIgnoreCase("achievements.tryjump.firsttry"))
        {
            return "Beende den Jump and Run Teil ohne einen Jump-Fail!";
        }else if(key.equalsIgnoreCase("achievements.tryjump.firstblood"))
        {
            return "Sei der erste, der in einer Runde stirbt!";
        }else if(key.equalsIgnoreCase("achievements.tryjump.firstkill"))
        {
            return "Sei der erste, der in einer Runde jemanden tötet!";
        }else if(key.equalsIgnoreCase("achievements.tryjump.killstreak"))
        {
            return "Töte fünf Spieler, ohne selber zu sterben!";
        }else if(key.equalsIgnoreCase("achievements.tryjump.unbesiegbar"))
        {
            return "Erreiche 20 Herzen";
        }else if(key.equalsIgnoreCase("achievements.tryjump.sprengmeister"))
        {
            return "Kaufe 20 TNT in einer Runde";
        }else if(key.equalsIgnoreCase("achievements.tryjump.schuetze"))
        {
            return "Kaufe 32 Pfeile in einer Runde";
        }
        return null;
    }

    private void announceAchievement(Player p,String key)
    {
        p.playSound(p.getEyeLocation(), Sound.WITHER_SPAWN, 10, 1);
        p.sendMessage(""  + ChatColor.GREEN+ ChatColor.STRIKETHROUGH + "----------------------------");
        p.sendMessage("" + ChatColor.STRIKETHROUGH + ChatColor.GREEN + "Errungenschaft: " + getTitle(key));
        p.sendMessage("");
        p.sendMessage(ChatColor.AQUA + getDescription(key));
        p.sendMessage(""+ ChatColor.GREEN+ ChatColor.STRIKETHROUGH + "----------------------------");
    }

    /**
     * opens the inventory that shows all the locked and unlocked achievements
     * @param p
     */
    public void openOverview(Player p)
    {

        int size = achievementList().size();
        if(size <= 9)
        {
            size = 27;
        }else if(size <= 18)
        {
            size = 36;
        }else if(size <= 27)
        {
            size = 45;
        }else if(size <= 36)
        {
            size = 54;
        }

        Inventory i = Bukkit.createInventory(p,size, ChatColor.AQUA + "Errungenschaften");
        for(int x = 0; x <= 9; x++)
        {
            i.setItem(x, new ItemStack(Material.STAINED_GLASS_PANE,1,(short)15));
        }
        for(int x = 17; x < 27; x++)
        {
            i.setItem(x, new ItemStack(Material.STAINED_GLASS_PANE,1,(short)15));
        }
        for(String s : achievementList())
        {
            if(hasAchivement(p.getUniqueId(),s))
            {
                ItemStack is = new ItemStack(Material.WOOL,1, (short) 5);
                ItemMeta im = is.getItemMeta();
                im.setDisplayName(ChatColor.GREEN + getTitle(s));
                List<String> lore = new ArrayList<String>();
                lore.add(ChatColor.YELLOW + getDescription(s));
                im.setLore(lore);
                is.setItemMeta(im);
                i.addItem(is);
            }else
            {
                ItemStack is = new ItemStack(Material.WOOL,1, (short) 14);
                ItemMeta im = is.getItemMeta();
                im.setDisplayName(ChatColor.DARK_RED + getTitle(s));
                List<String> lore = new ArrayList<String>();
                lore.add(ChatColor.RED + "???");
                im.setLore(lore);
                is.setItemMeta(im);
                i.addItem(is);
            }
        }
        p.openInventory(i);
    }


    // ---------------------------

    public void checkFirstTry(Player p,PlayerJumpSession session)
    {
        if(session.fails <= 0)
        {
            // check if player already has this achievement
            String key = "achievements.tryjump.firsttry";
            if(!hasAchivement(p.getUniqueId(),key))
            {
                setAchievement(p.getUniqueId(),key,true);
                announceAchievement(p,key);
            }

            // give fucking tokens
            p.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "MEGA TOKEN BOOST " + ChatColor.GOLD + ChatColor.BOLD + "+2000 Tokens");
            session.tokens = session.tokens + 2000;
            TryJump.getInstance().getGameSession().externalUpdateTokens(p,session.tokens);
        }
    }

    public void checkFirstBlood(Player p)
    {
        if(!firstblood)
        {
            firstblood = true;
            // check if player already has this achievement
            String key = "achievements.tryjump.firstblood";
            if(!hasAchivement(p.getUniqueId(),key))
            {
                setAchievement(p.getUniqueId(),key,true);
                announceAchievement(p,key);
            }
        }

        killstreak.put(p.getUniqueId(),0);
    }

    public void checkFirstKill(Player p)
    {
        if(!firstkill)
        {
            firstkill = true;
            // check if player already has this achievement
            String key = "achievements.tryjump.firstkill";
            if(!hasAchivement(p.getUniqueId(),key))
            {
                setAchievement(p.getUniqueId(),key,true);
                announceAchievement(p,key);
            }
        }
    }

    public void checkKillstreak(Player p)
    {
        if(!killstreak.containsKey(p.getUniqueId()))
        {
            killstreak.put(p.getUniqueId(),1);
        }else
        {
            killstreak.put(p.getUniqueId(), killstreak.get(p.getUniqueId()) +1);
            if(killstreak.get(p.getUniqueId()) == 5)
            {
                // check if player already has this achievement
                String key = "achievements.tryjump.killstreak";
                if(!hasAchivement(p.getUniqueId(),key))
                {
                    setAchievement(p.getUniqueId(),key,true);
                    announceAchievement(p,key);
                }
            }
        }
    }

    public void checkUnbesiegbar(Player p)
    {
        if(p.getMaxHealth() >= 40.0)
        {
            // check if player already has this achievement
            String key = "achievements.tryjump.unbesiegbar";
            if(!hasAchivement(p.getUniqueId(),key))
            {
                setAchievement(p.getUniqueId(),key,true);
                announceAchievement(p,key);
            }
        }
    }

    public void checkSprengmeister(Player p)
    {
        if(!sprengmeister.containsKey(p.getUniqueId()))
        {
            sprengmeister.put(p.getUniqueId(),1);
        }else
        {
            sprengmeister.put(p.getUniqueId(),sprengmeister.get(p.getUniqueId())+1);
            if(sprengmeister.get(p.getUniqueId()) == 20)
            {
                // check if player already has this achievement
                String key = "achievements.tryjump.sprengmeister";
                if(!hasAchivement(p.getUniqueId(),key))
                {
                    setAchievement(p.getUniqueId(),key,true);
                    announceAchievement(p,key);
                }
            }
        }

    }

    public void checkSchuetze(Player p)
    {
        if(!schuetze.containsKey(p.getUniqueId()))
        {
            schuetze.put(p.getUniqueId(),1);
        }else
        {
            schuetze.put(p.getUniqueId(),schuetze.get(p.getUniqueId())+1);
            if(schuetze.get(p.getUniqueId()) == 32)
            {
                // check if player already has this achievement
                String key = "achievements.tryjump.schuetze";
                if(!hasAchivement(p.getUniqueId(),key))
                {
                    setAchievement(p.getUniqueId(),key,true);
                    announceAchievement(p,key);
                }
            }
        }
    }

}
