package de.bergwerklabs.tryjump.gameserver.util;

import de.bergwerklabs.tryjump.gameserver.TryJump;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Score;

import java.util.*;

/**
 * Created by nexotekHD on 16.04.2016.
 */
public class ItemShop {

    // Preisliste
    private HashMap<Material,Integer> pricelist = new HashMap<Material,Integer>();
    private HashMap<Short, Integer> potionPricelist = new HashMap<Short,Integer>();

    // Kategorieen
    private ItemStack waffen;
    private ItemStack lederruestung;
    private ItemStack kettenruestung;
    private ItemStack eisenruestung;
    private ItemStack diamantruestung;
    private ItemStack erfahrung;
    private ItemStack verzaubern;
    private ItemStack nahrung;
    private ItemStack traenke;
    private ItemStack spezial;

    // Inhalte
    private ItemStack wood_sword;
    private ItemStack stone_sword;
    private ItemStack iron_sword;
    private ItemStack diamond_sword;
    private ItemStack bow_sword;
    private ItemStack arrow;
    private ItemStack fishing_rod;
    private ItemStack tnt;

    private ItemStack leather_cap;
    private ItemStack leather_pants;
    private ItemStack leather_boots;
    private ItemStack leather_tunic;

    private ItemStack chain_helmet;
    private ItemStack chain_leggings;
    private ItemStack chain_boots;
    private ItemStack chain_chestplate;

    private ItemStack iron_helmet;
    private ItemStack iron_leggings;
    private ItemStack iron_boots;
    private ItemStack iron_chestplate;

    private ItemStack diamond_helmet;
    private ItemStack diamond_leggings;
    private ItemStack diamond_boots;
    private ItemStack diamond_chestplate;

    private ItemStack level_1;
    private ItemStack level_10;
    private ItemStack level_20;
    private ItemStack level_30;

    private ItemStack enchant_1;
    private ItemStack enchant_2;
    private ItemStack enchant_3;

    private ItemStack apple;
    private ItemStack steak;
    private ItemStack cake;
    private ItemStack gapple;

    private ItemStack splashpotion_of_healing;
    private ItemStack splashpotion_of_healing_ii;
    private ItemStack splashpotion_of_harming;
    private ItemStack splashpotion_of_harming_ii;

    private ItemStack[] page_waffen;
    private ItemStack[] page_lederruestung;
    private ItemStack[] page_kettenruestung;
    private ItemStack[] page_eisenruestung;
    private ItemStack[] page_diamantruestung;
    private ItemStack[] page_erfahrung;
    private ItemStack[] page_verzaubern;
    private ItemStack[] page_nahrung;
    private ItemStack[] page_traenke;
    private ItemStack[] page_spezial;



    public ItemShop()
    {
        // init categories
        waffen = stack(new ItemStack(Material.GOLD_SWORD), ChatColor.AQUA + "Waffen");
        lederruestung = stack(new ItemStack(Material.LEATHER_CHESTPLATE), ChatColor.AQUA + "Lederrüstung");
        kettenruestung = stack(new ItemStack(Material.CHAINMAIL_CHESTPLATE), ChatColor.AQUA + "Kettenrüstung");
        eisenruestung = stack(new ItemStack(Material.IRON_CHESTPLATE), ChatColor.AQUA + "Eisenrüstung");
        diamantruestung = stack(new ItemStack(Material.DIAMOND_CHESTPLATE), ChatColor.AQUA + "Diamantrüstung");
        erfahrung = stack(new ItemStack(Material.EXP_BOTTLE), ChatColor.AQUA + "Erfahrung");
        nahrung = stack(new ItemStack(Material.CAKE), ChatColor.AQUA + "Nahrung");
        traenke = stack(new ItemStack(Material.getMaterial(373)), ChatColor.AQUA + "Tränke");
        spezial = stack(new ItemStack(Material.EMERALD), ChatColor.AQUA + "Spezial");



        // init contents
        wood_sword = price(Material.WOOD_SWORD, 40);
        stone_sword = price(Material.STONE_SWORD, 200);
        iron_sword = price(Material.IRON_SWORD, 400);
        diamond_sword = price(Material.DIAMOND_SWORD, 800);
        bow_sword = price(Material.BOW,150);
        arrow = price(Material.ARROW,8);
        fishing_rod = price(Material.FISHING_ROD,100);
        tnt = price(Material.TNT,80);

        leather_cap = price(Material.LEATHER_HELMET,40);
        leather_pants = price(Material.LEATHER_LEGGINGS,48);
        leather_boots = price(Material.LEATHER_BOOTS,40);
        leather_tunic = price(Material.LEATHER_CHESTPLATE,60);

        chain_helmet = price(Material.CHAINMAIL_HELMET,100);
        chain_leggings = price(Material.CHAINMAIL_LEGGINGS,120);
        chain_boots = price(Material.CHAINMAIL_BOOTS,100);
        chain_chestplate = price(Material.CHAINMAIL_CHESTPLATE,150);

        iron_helmet = price(Material.IRON_HELMET,200);
        iron_leggings = price(Material.IRON_LEGGINGS,200);
        iron_boots = price(Material.IRON_BOOTS,200);
        iron_chestplate = price(Material.IRON_CHESTPLATE,300);

        diamond_helmet = price(Material.DIAMOND_HELMET,140);
        diamond_leggings = price(Material.DIAMOND_LEGGINGS,180);
        diamond_boots = price(Material.DIAMOND_BOOTS,180);
        diamond_chestplate = price(Material.DIAMOND_CHESTPLATE,280);

        apple = price(Material.APPLE,2);
        steak = price(Material.COOKED_BEEF,4);
        cake = price(Material.CAKE,8);
        gapple = price(Material.GOLDEN_APPLE,100);

        splashpotion_of_harming = potion(16460, 180,1);
        splashpotion_of_harming_ii = potion(16428, 250,1);
        splashpotion_of_healing = potion(16453,140,3);
        splashpotion_of_healing_ii = potion(16421,200,2);

        // build shop pages
        // waffen
        Inventory inv_waffen = Bukkit.createInventory(null,18);
        addNavigationBar(inv_waffen);
        inv_waffen.setItem(9, wood_sword);
        inv_waffen.setItem(10, stone_sword);
        inv_waffen.setItem(11,iron_sword);
        inv_waffen.setItem(13,bow_sword);
        inv_waffen.setItem(14,arrow);
        inv_waffen.setItem(16,fishing_rod);
        inv_waffen.setItem(17,tnt);
        page_waffen = inv_waffen.getContents();

        // lederrüstung
        Inventory inv_lederruestung = Bukkit.createInventory(null,18);
        addNavigationBar(inv_lederruestung);
        inv_lederruestung.setItem(11, leather_cap);
        inv_lederruestung.setItem(12,leather_pants);
        inv_lederruestung.setItem(13,leather_boots);
        inv_lederruestung.setItem(14, leather_tunic);
        page_lederruestung = inv_lederruestung.getContents();

        //kettenrüstung
        Inventory inv_kettenruestung = Bukkit.createInventory(null,18);
        addNavigationBar(inv_kettenruestung);
        inv_kettenruestung.setItem(11, chain_helmet);
        inv_kettenruestung.setItem(12, chain_leggings);
        inv_kettenruestung.setItem(13,chain_boots);
        inv_kettenruestung.setItem(14, chain_chestplate);
        page_kettenruestung = inv_kettenruestung.getContents();

        // eisenrüstung
        Inventory inv_eisenruestung = Bukkit.createInventory(null,18);
        addNavigationBar(inv_eisenruestung);
        inv_eisenruestung.setItem(11, iron_helmet);
        inv_eisenruestung.setItem(12,iron_leggings);
        inv_eisenruestung.setItem(13,iron_boots);
        inv_eisenruestung.setItem(14, iron_chestplate);
        page_eisenruestung = inv_eisenruestung.getContents();

        // diamantrüstung
        Inventory inv_diamantruestung = Bukkit.createInventory(null,18);
        addNavigationBar(inv_diamantruestung);
        inv_diamantruestung.setItem(11, diamond_helmet);
        inv_diamantruestung.setItem(12,diamond_leggings);
        inv_diamantruestung.setItem(13,diamond_boots);
        inv_diamantruestung.setItem(14, diamond_chestplate);
        page_diamantruestung = inv_diamantruestung.getContents();

        //nahrung
        Inventory inv_nahrung = Bukkit.createInventory(null,18);
        addNavigationBar(inv_nahrung);
        inv_nahrung.setItem(11, apple);
        inv_nahrung.setItem(12,steak);
        inv_nahrung.setItem(13,cake);
        inv_nahrung.setItem(15, gapple);
        page_nahrung = inv_nahrung.getContents();

        // tränke
        Inventory inv_traenke = Bukkit.createInventory(null,18);
        addNavigationBar(inv_traenke);
        inv_traenke.setItem(10, splashpotion_of_healing);
        inv_traenke.setItem(11,splashpotion_of_healing_ii);
        inv_traenke.setItem(15,splashpotion_of_harming);
        inv_traenke.setItem(16,splashpotion_of_harming_ii);
        page_traenke = inv_traenke.getContents();

        //special
        Inventory inv_special = Bukkit.createInventory(null, 18);
        addNavigationBar(inv_special);

        ItemStack extra_herzen = new ItemStack(Material.RED_ROSE);
        ItemMeta im = extra_herzen.getItemMeta();
        im.setDisplayName(ChatColor.RED + "Extra Herzen");
        List<String> lore = new ArrayList<String>();
        lore.add("");
        lore.add("" + ChatColor.GOLD + 200 + " Tokens");
        im.setLore(lore);
        extra_herzen.setItemMeta(im);
        inv_special.setItem(13, extra_herzen);
        page_spezial = inv_special.getContents();
    }

    private ItemStack stack(ItemStack is, String name)
    {
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(name);
        is.setItemMeta(im);
        return is;
    }

    private ItemStack price(Material material, int price)
    {
        pricelist.put(material, price);
        ItemStack is = new ItemStack(material);
        List<String> lore = new ArrayList<String>();
        lore.add("");
        lore.add("" + ChatColor.GOLD + price + " Tokens");
        ItemMeta im = is.getItemMeta();
        im.setLore(lore);
        is.setItemMeta(im);
        return is;
    }

    private ItemStack potion(int datavalue, int price, int amount)
    {

        ItemStack is = new ItemStack(Material.POTION,amount, (short) datavalue);
        potionPricelist.put((short)datavalue,price);
        List<String> lore = new ArrayList<String>();
        lore.add("");
        lore.add("" + ChatColor.GOLD + price + " Tokens");
        ItemMeta im = is.getItemMeta();
        im.setLore(lore);
        is.setItemMeta(im);
        return is;
    }

    private void addNavigationBar(Inventory inv)
    {
        inv.setItem(0, waffen);
        inv.setItem(1,lederruestung);
        inv.setItem(2,kettenruestung);
        inv.setItem(3, eisenruestung);
        //inv.setItem(3, diamantruestung);
        //inv.setItem(4,erfahrung);
        //inv.setItem(5, verzaubern);
        inv.setItem(5, nahrung);
        inv.setItem(6, traenke);
        inv.setItem(8, spezial);
    }


    public void open(Player p)
    {
        Inventory i = Bukkit.createInventory(p, 18, ChatColor.GOLD + "" + ChatColor.BOLD + ">> " + ChatColor.YELLOW + "TryJump Shop");
        addNavigationBar(i);
        p.openInventory(i);
    }

    //filtered before in ListenerInventoryClick.java
    public void onInventoryClick(InventoryClickEvent e)
    {
        ItemStack item = e.getInventory().getItem(e.getSlot());
        Player p = (Player)e.getWhoClicked();

        // seperate between page switch and buy
        if(e.getSlot() > 8)
        {

            // case special
            if(item.getType() == Material.RED_ROSE)
            {
                int price = 0;
                switch(item.getType())
                {
                    case RED_ROSE:
                        price = 200;
                        break;
                }
                Score balance = p.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getScore(p);
                if(balance.getScore() >= price)
                {
                    // buy
                    balance.setScore(balance.getScore()-price);
                    TryJump.getInstance().getGameSession().updateLevelBar(p);
                    switch(item.getType())
                    {
                        case RED_ROSE:
                            p.setMaxHealth(p.getMaxHealth() +2);
                            p.setHealth(p.getMaxHealth());
                            TryJump.getInstance().getAchievementManager().checkUnbesiegbar(p);
                            break;
                    }
                    p.playSound(p.getEyeLocation(), Sound.ITEM_PICKUP, 100, 1);
                }else
                {
                    p.playSound(p.getEyeLocation(),Sound.NOTE_BASS,100,1);
                    p.sendMessage(TryJump.getInstance().getChatPrefix() + ChatColor.RED + "Du hast nicht genügend Tokens, um dieses Item zu kaufen!");
                }
                return;
            }

            // case potion
            if(item.getType() == Material.POTION)
            {
                int price = potionPricelist.get(item.getDurability());
                Score balance  = p.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getScore(p);
                if(balance.getScore() >= price)
                {
                    // buy
                    balance.setScore(balance.getScore()-price);
                    TryJump.getInstance().getGameSession().updateLevelBar(p);
                    p.getInventory().addItem(new ItemStack(item.getType(), item.getAmount(), item.getDurability()));
                    p.playSound(p.getEyeLocation(),Sound.ITEM_PICKUP, 100,1);
                }else
                {
                    p.playSound(p.getEyeLocation(),Sound.NOTE_BASS,100,1);
                    p.sendMessage(TryJump.getInstance().getChatPrefix() + ChatColor.RED + "Du hast nicht genügend Tokens, um dieses Item zu kaufen!");
                }
                return;
            }

            // case normal buy
            int price = pricelist.get(item.getType());

            Score balance = p.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getScore(p);
            if(balance.getScore() >= price)
            {
                // buy
                balance.setScore(balance.getScore()-price);
                TryJump.getInstance().getGameSession().updateLevelBar(p);
                p.getInventory().addItem(new ItemStack(item.getType()));
                p.playSound(p.getEyeLocation(), Sound.ITEM_PICKUP, 100, 1);

                // achievement
                if(item.getType() == Material.TNT)
                {
                    TryJump.getInstance().getAchievementManager().checkSprengmeister(p);
                }else if(item.getType() == Material.ARROW)
                {
                    TryJump.getInstance().getAchievementManager().checkSchuetze(p);
                }

            }else
            {
                p.playSound(p.getEyeLocation(),Sound.NOTE_BASS,100,1);
                p.sendMessage(TryJump.getInstance().getChatPrefix() + ChatColor.RED + "Du hast nicht genügend Tokens, um dieses Item zu kaufen!");
            }

        }else
        {
            // case page switch
            p.playSound(p.getEyeLocation(), Sound.CLICK, 100, 1);
            switch(item.getType())
            {
                case GOLD_SWORD:
                    e.getInventory().setContents(page_waffen);
                    break;
                case LEATHER_CHESTPLATE:
                    e.getInventory().setContents(page_lederruestung);
                    break;
                case CHAINMAIL_CHESTPLATE:
                    e.getInventory().setContents(page_kettenruestung);
                    break;
                case IRON_CHESTPLATE:
                    e.getInventory().setContents(page_eisenruestung);
                    break;
                case DIAMOND_CHESTPLATE:
                    e.getInventory().setContents(page_diamantruestung);
                    break;
                case EXP_BOTTLE:

                    break;
                case ENCHANTMENT_TABLE:

                    break;
                case CAKE:
                    e.getInventory().setContents(page_nahrung);
                    break;
                case POTION:
                    e.getInventory().setContents(page_traenke);
                    break;
                case EMERALD:
                    e.getInventory().setContents(page_spezial);
                    break;
            }
        }
    }

    public void enchant(ItemStack is, Player p)
    {
        // check balance
        int price = 250;

        // check if special conditions may apply
        if(is.getType() == Material.WOOD_SWORD || is.getType() == Material.STONE_SWORD || is.getType() == Material.IRON_SWORD || is.getType() == Material.BOW)
        {
            if(is.getItemMeta().hasEnchants())
            {
                price = 400;
            }
        }

        Score balance = p.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getScore(p);
        if(balance.getScore() >= price)
        {
        }else
        {
            p.playSound(p.getEyeLocation(),Sound.NOTE_BASS,100,1);
            p.sendMessage(TryJump.getInstance().getChatPrefix() + ChatColor.RED + "Du hast nicht genügend Tokens, um dieses Item zu verzaubern!");
            return;
        }
        // case sword
        if(is.getType() == Material.WOOD_SWORD || is.getType() == Material.STONE_SWORD || is.getType() == Material.IRON_SWORD)
        {
            enchSword(is);
            p.playSound(p.getEyeLocation(), Sound.LEVEL_UP, 100, 1);
            p.updateInventory();
            balance.setScore(balance.getScore() - price);
            TryJump.getInstance().getGameSession().updateLevelBar(p);
            p.sendMessage(TryJump.getInstance().getChatPrefix() + "Die nächste Verzauberungsstufe für dein " + ChatColor.AQUA + "Schwert " + ChatColor.GRAY +"kostet dich: " + ChatColor.RED + "400 Tokens" + ChatColor.GRAY + "!");
        }

        // case bow
        if(is.getType() == Material.BOW)
        {
            enchBow(is);
            p.playSound(p.getEyeLocation(), Sound.LEVEL_UP, 100, 1);
            p.updateInventory();
            balance.setScore(balance.getScore() - price);
            TryJump.getInstance().getGameSession().updateLevelBar(p);
            p.sendMessage(TryJump.getInstance().getChatPrefix() + "Die nächste Verzauberungsstufe für deinen " + ChatColor.AQUA + "Bogen " + ChatColor.GRAY +"kostet dich: " + ChatColor.RED + "400 Tokens" + ChatColor.GRAY + "!");
        }

        // case helmet
        if(is.getType() == Material.LEATHER_HELMET || is.getType() == Material.IRON_HELMET || is.getType() == Material.DIAMOND_HELMET)
        {
            enchHelmet(is);
            p.playSound(p.getEyeLocation(), Sound.LEVEL_UP, 100, 1);
            p.updateInventory();
            balance.setScore(balance.getScore() - price);
            TryJump.getInstance().getGameSession().updateLevelBar(p);
        }

        // case chesplate
        if(is.getType() == Material.LEATHER_CHESTPLATE || is.getType() == Material.IRON_CHESTPLATE || is.getType() == Material.DIAMOND_CHESTPLATE)
        {
            enchChestplate(is);
            p.playSound(p.getEyeLocation(), Sound.LEVEL_UP, 100, 1);
            p.updateInventory();
            balance.setScore(balance.getScore() - price);
            TryJump.getInstance().getGameSession().updateLevelBar(p);
        }

        // case leggings
        if(is.getType() == Material.LEATHER_LEGGINGS || is.getType() == Material.IRON_LEGGINGS || is.getType() == Material.DIAMOND_LEGGINGS)
        {
            enchLeggings(is);
            p.playSound(p.getEyeLocation(), Sound.LEVEL_UP, 100, 1);
            p.updateInventory();
            balance.setScore(balance.getScore() - price);
            TryJump.getInstance().getGameSession().updateLevelBar(p);
        }

        // case shoes
        if(is.getType() == Material.LEATHER_BOOTS || is.getType() == Material.IRON_BOOTS || is.getType() == Material.DIAMOND_BOOTS)
        {
            enchShoes(is);
            p.playSound(p.getEyeLocation(), Sound.LEVEL_UP, 100, 1);
            p.updateInventory();
            balance.setScore(balance.getScore() - price);
            TryJump.getInstance().getGameSession().updateLevelBar(p);
        }
    }

    private void enchHelmet(ItemStack is)
    {
        int rnd = new Random().nextInt(10);
        // 90% protection
        if(rnd < 9)
        {
            int before = 0;
            if(is.getEnchantments().containsKey(Enchantment.PROTECTION_ENVIRONMENTAL))
            {
                before = is.getEnchantments().get(Enchantment.PROTECTION_ENVIRONMENTAL);
            }
            if(before < 4)
            {
                is.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,before +1);
            }
        }
        rnd = new Random().nextInt(10);
        // 40% fire protection
        if(rnd < 4)
        {
            int before = 0;
            if(is.getEnchantments().containsKey(Enchantment.PROTECTION_FIRE))
            {
                before = is.getEnchantments().get(Enchantment.PROTECTION_FIRE);
            }
            if(before < 4)
            {
                is.addEnchantment(Enchantment.PROTECTION_FIRE,before +1);
            }
        }

        rnd = new Random().nextInt(10);
        // 40% blast protection
        if(rnd < 4)
        {
            int before = 0;
            if(is.getEnchantments().containsKey(Enchantment.PROTECTION_EXPLOSIONS))
            {
                before = is.getEnchantments().get(Enchantment.PROTECTION_EXPLOSIONS);
            }
            if(before < 4)
            {
                is.addEnchantment(Enchantment.PROTECTION_EXPLOSIONS,before +1);
            }
        }
        rnd = new Random().nextInt(10);
        // 60% projectile protection
        if(rnd < 6)
        {
            int before = 0;
            if(is.getEnchantments().containsKey(Enchantment.PROTECTION_PROJECTILE))
            {
                before = is.getEnchantments().get(Enchantment.PROTECTION_PROJECTILE);
            }
            if(before < 4)
            {
                is.addEnchantment(Enchantment.PROTECTION_PROJECTILE,before +1);
            }
        }
        rnd = new Random().nextInt(10);

        // 30% water affinity
        if(rnd < 3)
        {
            int before = 0;
            if(is.getEnchantments().containsKey(Enchantment.WATER_WORKER))
            {
                before = is.getEnchantments().get(Enchantment.WATER_WORKER);
            }
            if(before < 1)
            {
                is.addEnchantment(Enchantment.WATER_WORKER,before +1);
            }
        }
    }

    private void enchLeggings(ItemStack is)
    {
        int rnd = new Random().nextInt(10);
        // 90% protection
        if(rnd < 9)
        {
            int before = 0;
            if(is.getEnchantments().containsKey(Enchantment.PROTECTION_ENVIRONMENTAL))
            {
                before = is.getEnchantments().get(Enchantment.PROTECTION_ENVIRONMENTAL);
            }
            if(before < 4)
            {
                is.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,before +1);
            }
        }
        rnd = new Random().nextInt(10);
        // 40% fire protection
        if(rnd < 4)
        {
            int before = 0;
            if(is.getEnchantments().containsKey(Enchantment.PROTECTION_FIRE))
            {
                before = is.getEnchantments().get(Enchantment.PROTECTION_FIRE);
            }
            if(before < 4)
            {
                is.addEnchantment(Enchantment.PROTECTION_FIRE,before +1);
            }
        }

        rnd = new Random().nextInt(10);
        // 40% blast protection
        if(rnd < 4)
        {
            int before = 0;
            if(is.getEnchantments().containsKey(Enchantment.PROTECTION_EXPLOSIONS))
            {
                before = is.getEnchantments().get(Enchantment.PROTECTION_EXPLOSIONS);
            }
            if(before < 4)
            {
                is.addEnchantment(Enchantment.PROTECTION_EXPLOSIONS,before +1);
            }
        }
        rnd = new Random().nextInt(10);
        // 60% projectile protection
        if(rnd < 6)
        {
            int before = 0;
            if(is.getEnchantments().containsKey(Enchantment.PROTECTION_PROJECTILE))
            {
                before = is.getEnchantments().get(Enchantment.PROTECTION_PROJECTILE);
            }
            if(before < 4)
            {
                is.addEnchantment(Enchantment.PROTECTION_PROJECTILE,before +1);
            }
        }

        rnd = new Random().nextInt(10);
        // 20% thorns
        if(rnd < 2)
        {
            int before = 0;
            if(is.getEnchantments().containsKey(Enchantment.THORNS))
            {
                before = is.getEnchantments().get(Enchantment.THORNS);
            }
            if(before < 3)
            {
                is.addEnchantment(Enchantment.THORNS,before +1);
            }
        }
        rnd = new Random().nextInt(10);
    }

    private void enchChestplate(ItemStack is)
    {
        int rnd = new Random().nextInt(10);
        // 90% protection
        if(rnd < 9)
        {
            int before = 0;
            if(is.getEnchantments().containsKey(Enchantment.PROTECTION_ENVIRONMENTAL))
            {
                before = is.getEnchantments().get(Enchantment.PROTECTION_ENVIRONMENTAL);
            }
            if(before < 4)
            {
                is.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,before +1);
            }
        }
        rnd = new Random().nextInt(10);
        // 40% fire protection
        if(rnd < 4)
        {
            int before = 0;
            if(is.getEnchantments().containsKey(Enchantment.PROTECTION_FIRE))
            {
                before = is.getEnchantments().get(Enchantment.PROTECTION_FIRE);
            }
            if(before < 4)
            {
                is.addEnchantment(Enchantment.PROTECTION_FIRE,before +1);
            }
        }

        rnd = new Random().nextInt(10);
        // 40% blast protection
        if(rnd < 4)
        {
            int before = 0;
            if(is.getEnchantments().containsKey(Enchantment.PROTECTION_EXPLOSIONS))
            {
                before = is.getEnchantments().get(Enchantment.PROTECTION_EXPLOSIONS);
            }
            if(before < 4)
            {
                is.addEnchantment(Enchantment.PROTECTION_EXPLOSIONS,before +1);
            }
        }
        rnd = new Random().nextInt(10);
        // 60% projectile protection
        if(rnd < 6)
        {
            int before = 0;
            if(is.getEnchantments().containsKey(Enchantment.PROTECTION_PROJECTILE))
            {
                before = is.getEnchantments().get(Enchantment.PROTECTION_PROJECTILE);
            }
            if(before < 4)
            {
                is.addEnchantment(Enchantment.PROTECTION_PROJECTILE,before +1);
            }
        }

        // 20% thorns
        if(rnd < 2)
        {
            int before = 0;
            if(is.getEnchantments().containsKey(Enchantment.THORNS))
            {
                before = is.getEnchantments().get(Enchantment.THORNS);
            }
            if(before < 3)
            {
                is.addEnchantment(Enchantment.THORNS,before +1);
            }
        }
    }

    private void enchShoes(ItemStack is)
    {
        int rnd = new Random().nextInt(10);
        // 90% protection
        if(rnd < 9)
        {
            int before = 0;
            if(is.getEnchantments().containsKey(Enchantment.PROTECTION_ENVIRONMENTAL))
            {
                before = is.getEnchantments().get(Enchantment.PROTECTION_ENVIRONMENTAL);
            }
            if(before < 4)
            {
                is.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,before +1);
            }
        }
        rnd = new Random().nextInt(10);
        // 40% fire protection
        if(rnd < 4)
        {
            int before = 0;
            if(is.getEnchantments().containsKey(Enchantment.PROTECTION_FIRE))
            {
                before = is.getEnchantments().get(Enchantment.PROTECTION_FIRE);
            }
            if(before < 4)
            {
                is.addEnchantment(Enchantment.PROTECTION_FIRE,before +1);
            }
        }

        rnd = new Random().nextInt(10);
        // 40% blast protection
        if(rnd < 4)
        {
            int before = 0;
            if(is.getEnchantments().containsKey(Enchantment.PROTECTION_EXPLOSIONS))
            {
                before = is.getEnchantments().get(Enchantment.PROTECTION_EXPLOSIONS);
            }
            if(before < 4)
            {
                is.addEnchantment(Enchantment.PROTECTION_EXPLOSIONS,before +1);
            }
        }
        rnd = new Random().nextInt(10);
        // 60% projectile protection
        if(rnd < 6)
        {
            int before = 0;
            if(is.getEnchantments().containsKey(Enchantment.PROTECTION_PROJECTILE))
            {
                before = is.getEnchantments().get(Enchantment.PROTECTION_PROJECTILE);
            }
            if(before < 4)
            {
                is.addEnchantment(Enchantment.PROTECTION_PROJECTILE,before +1);
            }
        }

        rnd = new Random().nextInt(10);
        // 20% deapth strider
        if(rnd < 2)
        {
            int before = 0;
            if(is.getEnchantments().containsKey(Enchantment.DEPTH_STRIDER))
            {
                before = is.getEnchantments().get(Enchantment.DEPTH_STRIDER);
            }
            if(before < 3)
            {
                is.addEnchantment(Enchantment.DEPTH_STRIDER,before +1);
            }
        }

        rnd = new Random().nextInt(10);
        // 60% deapth strider
        if(rnd < 6)
        {
            int before = 0;
            if(is.getEnchantments().containsKey(Enchantment.PROTECTION_FALL))
            {
                before = is.getEnchantments().get(Enchantment.PROTECTION_FALL);
            }
            if(before < 4)
            {
                is.addEnchantment(Enchantment.PROTECTION_FALL,before +1);
            }
        }

    }

    private void enchBow(ItemStack is)
    {
        int rnd = new Random().nextInt(10);
        // 90% power
        if(rnd < 9)
        {
            int before = 0;
            if(is.getEnchantments().containsKey(Enchantment.ARROW_DAMAGE))
            {
                before = is.getEnchantments().get(Enchantment.ARROW_DAMAGE);
            }
            if(before < 5)
            {
                is.addEnchantment(Enchantment.ARROW_DAMAGE,before +1);
            }
        }

        rnd = new Random().nextInt(10);
        // 60% knockback
        if(rnd < 6)
        {
            int before = 0;
            if(is.getEnchantments().containsKey(Enchantment.ARROW_KNOCKBACK))
            {
                before = is.getEnchantments().get(Enchantment.ARROW_KNOCKBACK);
            }
            if(before < 2)
            {
                is.addEnchantment(Enchantment.ARROW_KNOCKBACK,before +1);
            }
        }

        rnd = new Random().nextInt(100);
        // 1% infinity
        if(rnd < 1)
        {
            int before = 0;
            if(is.getEnchantments().containsKey(Enchantment.ARROW_INFINITE))
            {
                before = is.getEnchantments().get(Enchantment.ARROW_INFINITE);
            }
            if(before < 1)
            {
                is.addEnchantment(Enchantment.ARROW_INFINITE,before +1);
            }
        }

        /*
        rnd = new Random().nextInt(10);
        // 20% flame
        if(rnd < 2)
        {
            int before = 0;
            if(is.getEnchantments().containsKey(Enchantment.ARROW_FIRE))
            {
                before = is.getEnchantments().get(Enchantment.ARROW_FIRE);
            }
            if(before < 1)
            {
                is.addEnchantment(Enchantment.ARROW_FIRE,before +1);
            }
        }
        */

    }

    private void enchSword(ItemStack is)
    {
        int rnd = new Random().nextInt(10);
        // 100% sharpness
        if(rnd < 10)
        {
            int before = 0;
            if(is.getEnchantments().containsKey(Enchantment.DAMAGE_ALL))
            {
                before = is.getEnchantments().get(Enchantment.DAMAGE_ALL);
            }
            if(before < 5)
            {
                is.addEnchantment(Enchantment.DAMAGE_ALL,before +1);
            }
        }


        /*
        rnd = new Random().nextInt(10);
        // 10% fire aspect
        if(rnd < 2)
        {
            int before = 0;
            if(is.getEnchantments().containsKey(Enchantment.FIRE_ASPECT))
            {
                before = is.getEnchantments().get(Enchantment.FIRE_ASPECT);
            }
            if(before < 2)
            {
                is.addEnchantment(Enchantment.FIRE_ASPECT, before + 1);
            }
        }
        */
        rnd = new Random().nextInt(10);
        // 40% Knockback
        if(rnd < 4)
        {
            int before = 0;
            if(is.getEnchantments().containsKey(Enchantment.KNOCKBACK))
            {
                before = is.getEnchantments().get(Enchantment.KNOCKBACK);
            }
            if(before < 2)
            {
                is.addEnchantment(Enchantment.KNOCKBACK, before + 1);
            }
        }
        /*
        rnd = new Random().nextInt(10);
        // 60% Smite
        if(rnd < 6)
        {
            int before = 0;
            if(is.getEnchantments().containsKey(Enchantment.DAMAGE_UNDEAD))
            {
                before = is.getEnchantments().get(Enchantment.DAMAGE_UNDEAD);
            }
            if(before < 5)
            {
                is.addEnchantment(Enchantment.DAMAGE_UNDEAD, before + 1);
            }
        }

        */

        /*
        rnd = new Random().nextInt(10);
        // 60% Bane of Arthropods
        if(rnd < 6)
        {
            int before = 0;
            if(is.getEnchantments().containsKey(Enchantment.DAMAGE_ARTHROPODS))
            {
                before = is.getEnchantments().get(Enchantment.DAMAGE_ARTHROPODS);
            }
            if(before < 5)
            {
                is.addEnchantment(Enchantment.DAMAGE_ARTHROPODS, before + 1);
            }
        }
        */

    }

}
