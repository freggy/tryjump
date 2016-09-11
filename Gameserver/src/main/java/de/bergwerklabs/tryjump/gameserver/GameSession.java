package de.bergwerklabs.tryjump.gameserver;

import com.google.gson.Gson;
import de.bergwerklabs.nick.AutoNick;
import de.bergwerklabs.nick.NickEvent;
import de.bergwerklabs.tryjump.gameserver.json.JSONBlock;
import de.bergwerklabs.tryjump.gameserver.json.JSONUnit;
import de.bergwerklabs.tryjump.gameserver.util.ItemShop;
import de.bergwerklabs.tryjump.gameserver.util.PlayerJumpSession;
import de.bergwerklabs.tryjump.gameserver.util.RoundStats;
import de.bergwerklabs.tryjump.gameserver.util.TabTitleManager;
import de.bergwerklabs.util.GameState;
import de.bergwerklabs.util.Util;
import de.bergwerklabs.util.effect.HoverText;
import de.bergwerklabs.util.effect.TitleBuilder;
import de.bergwerklabs.util.entity.Marker;
import de.bergwerklabs.util.playerdata.Currency;
import de.bergwerklabs.util.playerdata.DataRegistry;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by nexotekHD on 13.04.2016.
 */
public class GameSession {

    private ArrayList<UUID> ingame_players = new ArrayList<UUID>();
    private ArrayList<UUID> specators = new ArrayList<UUID>();

    private boolean started = false;
    private boolean freezed = true;
    private boolean won = false;

    private int freezetime = 10;

    private int timeleft = 600;

    private BukkitTask hoverUpdater = null;
    private BukkitTask sbUpdater = null; // scoreboard updater + time countdown

    private Scoreboard scoreboard = null; // main scoreboard used in buyphase and deathmatch
    private Objective jumpprogress_objective = null;
    private Objective token_objective = null;
    private Objective deathmatch_lives_objective = null;

    private boolean buyphase = false;
    private boolean deathmatch = false;
    private ItemShop itemShop = null;

    private HashMap<UUID,Integer> cachedTokens = new HashMap<UUID,Integer>();

    private boolean finished = false;

    private boolean teams_allowed = false;

    private long grace = 0;

    private int jump_part_length_z = 0; // for percent calculation

    private HashMap<UUID,Long> instant_tod_cooldown = new HashMap<UUID,Long>();


    private HashMap<UUID,PlayerJumpSession> playerJumpSessions = new HashMap<UUID,PlayerJumpSession>();

    private HashMap<Integer,JSONUnit> easy_units = new HashMap<Integer, JSONUnit>();
    private HashMap<Integer,JSONUnit> easy_lite_units = new HashMap<Integer, JSONUnit>();

    private HashMap<Integer,JSONUnit> medium_units = new HashMap<Integer, JSONUnit>();
    private HashMap<Integer,JSONUnit> medium_lite_units = new HashMap<Integer, JSONUnit>();

    private HashMap<Integer,JSONUnit> hard_units = new HashMap<Integer, JSONUnit>();
    private HashMap<Integer,JSONUnit> hard_lite_units = new HashMap<Integer, JSONUnit>();

    private HashMap<Integer,JSONUnit> extreme_units = new HashMap<Integer, JSONUnit>();
    private HashMap<Integer,JSONUnit> extreme_lite_units = new HashMap<Integer, JSONUnit>();

    private Location spawnShift = null;

    private HashMap<UUID,RoundStats> roundStats = new HashMap<UUID,RoundStats>();


    /**
     * loads random units into the hashmaps over there
     */
    public GameSession()
    {

        // init scoreboard
        scoreboard = TryJump.getInstance().getServer().getScoreboardManager().getNewScoreboard();
        jumpprogress_objective = scoreboard.registerNewObjective("sidebar", "dummy");
        jumpprogress_objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        token_objective = scoreboard.registerNewObjective("belownames","dummy");
        token_objective.setDisplaySlot(DisplaySlot.PLAYER_LIST);

        initTeamsAllowedConfig();

        Gson gson = new Gson();
        // load units
        File dir = new File("units/");
        File[] list = dir.listFiles();
        ArrayList<File> units = new ArrayList<File>();
        ArrayList<File> liteunits = new ArrayList<File>();
        for(int i = 0; i < list.length; i++)
        {
            File file = list[i];
            if(file.getName().contains("_lite"))
            {
                liteunits.add(file);
            }else
            {
                units.add(file);
            }
        }

        Collections.shuffle(units);
        Collections.shuffle(liteunits);

        for(File unit : units)
        {
            String name = unit.getName();
            name = name.split(".unit",-1)[0];
            JSONUnit lite = null;
            // search for lite unit
            for(File liteunit : liteunits)
            {
                if(liteunit.getName().split(".unit",-1)[0].equalsIgnoreCase(name + "_lite"))
                {
                    lite = gson.fromJson(readFile(liteunit),JSONUnit.class);
                }
            }
            if(lite != null)
            {
                // read unit
                JSONUnit instance = gson.fromJson(readFile(unit),JSONUnit.class);
                instance.setName(unit.getName());
                lite.setName(unit.getName());
                switch (instance.getDifficulty())
                {
                    case 1:
                        if(easy_units.size() < 3)
                        {
                            easy_units.put(easy_units.size() + 1,instance);
                            easy_lite_units.put(easy_lite_units.size() + 1,lite);

                        }
                        break;
                    case 2:
                        if(medium_units.size() < 3)
                        {
                            medium_units.put(3+medium_units.size() + 1,instance);
                            medium_lite_units.put(3+medium_lite_units.size() + 1,lite);

                        }
                        break;
                    case 3:
                        if(hard_units.size() < 3)
                        {
                            hard_units.put(6+hard_units.size() + 1,instance);
                            hard_lite_units.put(6+hard_lite_units.size() + 1,lite);

                        }
                        break;
                    case 4:
                        if(extreme_units.size() < 3)
                        {
                            extreme_units.put(9+extreme_units.size() + 1,instance);
                            extreme_lite_units.put(9+extreme_lite_units.size() + 1,lite);

                        }
                        break;
                }
            }
        }


        for(int i = 1; i <= 10; i++){
            JSONUnit unit = getUnit(i,false);
            jump_part_length_z += unit.getEndLocZ();
        }


    }

    private void updateTokensInJumpScoreboard(Player p)
    {
        Scoreboard sb = p.getScoreboard();
        if(sb != null)
        {
            if(!cachedTokens.containsKey(p.getUniqueId()))
            {
                cachedTokens.put(p.getUniqueId(),0);
            }
            if(cachedTokens.containsKey(p.getUniqueId()))
            {
                int before = cachedTokens.get(p.getUniqueId());
                sb.resetScores(ChatColor.GRAY + "" + ChatColor.BOLD + before);
                Objective o = sb.getObjective(DisplaySlot.SIDEBAR);
                PlayerJumpSession session = playerJumpSessions.get(p.getUniqueId());
                cachedTokens.put(p.getUniqueId(),session.tokens);
                if(o != null)
                {
                    o.getScore(ChatColor.GRAY + "" + ChatColor.BOLD + session.tokens).setScore(102);
                }
            }
        }
        // update tokens in tablist
        for(UUID uuid : ingame_players)
        {
            Player pl = Bukkit.getPlayer(uuid);
            Scoreboard scoreb = pl.getScoreboard();
            if(scoreb != null)
            {
                Objective o = scoreb.getObjective(DisplaySlot.PLAYER_LIST);
                if(o != null)
                {
                    PlayerJumpSession session = playerJumpSessions.get(p.getUniqueId());
                    o.getScore(p).setScore(session.tokens);
                }
            }
        }
    }

    public void fix(Player p)
    {
        UUID uuid = p.getUniqueId();
        PlayerJumpSession session = playerJumpSessions.get(uuid);
        if(session.currentunit >= 10)
        {
            return;
        }
        session.currentCheckpointLocation.getChunk().unload();
        session.currentCheckpointLocation.getChunk().load();
        build(session.currentCheckpointLocation.clone(), getUnit(session.currentunit, session.lite), Bukkit.getPlayer(uuid), session.blocklist);
    }


    public RoundStats getRoundStats(UUID uuid)
    {
        if(roundStats.containsKey(uuid))
        {
            return roundStats.get(uuid);
        }
        return null;
    }

    public void setSpawnShift(Location loc)
    {
        this.spawnShift = loc;
    }

    /**
     * calculates the percentage of a number in the jump part
     * @param amount
     * @return
     */
    private int percentOfJumpPart(int amount)
    {
        return (int) (amount * 100.0f) / jump_part_length_z;
    }

    /**
     * just a debug method
     */
    private void printUnitsToConsole()
    {
        for(int i = 1; i < 11; i++)
        {
            if(i <= 3)
            {
                System.out.println(i + ": " + easy_units.get(i) + ";" + easy_lite_units.get(i));
            }
            if(i > 3 && i <= 6)
            {
                System.out.println(i + ": " + medium_units.get(i) + ";" + medium_lite_units.get(i));
            }
            if(i > 6 && i <= 9)
            {
                System.out.println(i + ": " + hard_units.get(i) + ";" + hard_lite_units.get(i));
            }
            if(i > 9)
            {
                System.out.println(i + ": " + extreme_units.get(i) + ";" + extreme_lite_units.get(i));
            }
        }
    }

    /**
     * read a file UTF8
     * @param file the file to read
     * @return the string content
     */
    private String readFile(File file)
    {
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(file.getPath()));
            return new String(encoded, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * add a player to ingame players
     * @param p the player to add
     */
    public void addGamePlayer(Player p)
    {
        if(!ingame_players.contains(p.getUniqueId()))
        {
            ingame_players.add(p.getUniqueId());
        }
    }

    /**
     * start the game. Begin with the jump part!
     */
    public void start()
    {
        if(!started)
        {
            started = true;
            TryJump.getInstance().getServer().broadcastMessage(TryJump.getInstance().getChatPrefix() + "Teleportiere ..." );
            int x = 0;
            for(UUID uuid : ingame_players)
            {
                Player p = TryJump.getInstance().getServer().getPlayer(uuid);
                Location loc = new Location(TryJump.getInstance().getServer().getWorld("jump"),x + 0.5,6,0.5);
                //loc.getBlock().setType(Material.DIAMOND_BLOCK);
                /*
                loc.clone().add(1,0,0).getBlock().setType(Material.QUARTZ_BLOCK);
                loc.clone().add(0,0,1).getBlock().setType(Material.QUARTZ_BLOCK);
                loc.clone().add(-1,0,0).getBlock().setType(Material.QUARTZ_BLOCK);
                */
                p.teleport(loc.clone().add(0, 1, 0));
                p.playSound(p.getEyeLocation(), Sound.LEVEL_UP,100,10);
                p.getInventory().clear();

                ItemStack instaDeath = new ItemStack(Material.INK_SACK, 1, (short) 1);
                ItemMeta instaDeathMeta = instaDeath.getItemMeta();
                instaDeathMeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Instant-Tod(TM) " +
                        ChatColor.RESET + ChatColor.GRAY + ChatColor.ITALIC + "<Rechtsklick>");
                instaDeath.setItemMeta(instaDeathMeta);
                p.getInventory().setItem(4, instaDeath);

                PlayerJumpSession session = new PlayerJumpSession(uuid);
                //session.currentCheckpointLocation = loc;
                session.currentCheckpointLocation = loc.clone().add(spawnShift);
                playerJumpSessions.put(uuid, session);


                // create special scoreboard
                Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
                Objective sidebar = sb.registerNewObjective("sidebar", "dummy");
                sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
                sidebar.getScore(ChatColor.YELLOW + "" + ChatColor.BOLD + "Tokens:").setScore(103);
                sidebar.getScore(ChatColor.YELLOW + "  ").setScore(101);

                Objective belownames = sb.registerNewObjective("belownames", "dummy");
                belownames.setDisplaySlot(DisplaySlot.PLAYER_LIST);

                Team underlined = sb.registerNewTeam("underlined");
                underlined.setPrefix("" +ChatColor.UNDERLINE);
                underlined.addPlayer(p);


                p.setScoreboard(sb);
                updateTokensInJumpScoreboard(p);
                x+= 35;


                // add played games to stats
                DataRegistry.DataSet set = TryJump.getInstance().getUtil().getDataRegistry().getSet(uuid);
                DataRegistry.DataGroup group = set.getGroup("stats.tryjump");
                String value = group.getValue("tryjump.played", "0");
                int vlue = Integer.parseInt(value);
                group.setValue("tryjump.played", String.valueOf(vlue + 1));

                RoundStats stats = new RoundStats();
                stats.setJump_fails(0);
                stats.setUnits(0);
                stats.setDeaths(0);
                stats.setKills(0);
                stats.setPoints(0);
                roundStats.put(uuid,stats);

            }

            // start entfreezer
            new BukkitRunnable() {
                @Override
                public void run() {
                    if(freezetime == 10 || freezetime == 5 || freezetime <= 3 && freezetime > 0)
                    {
                        TryJump.getInstance().getServer().broadcastMessage(TryJump.getInstance().getChatPrefix() + "Das Spiel startet in " + ChatColor.AQUA + "" +freezetime + " Sekunden" + ChatColor.GRAY + ".");
                        for(Player p : TryJump.getInstance().getServer().getOnlinePlayers())
                        {
                            p.playSound(p.getEyeLocation(),Sound.CLICK,100,1);
                        }
                    }
                    if(freezetime == 0)
                    {
                        freezed = false;
                        TryJump.getInstance().getServer().broadcastMessage(TryJump.getInstance().getChatPrefix() + ChatColor.AQUA + "Go!");
                        for(UUID uuid : ingame_players)
                        {
                            resetToUnit(uuid,false);
                        }

                        // build modul
                        cancel();
                    }
                    freezetime--;
                }
            }.runTaskTimer(TryJump.getInstance(), 0L, 20L);


            // start hover updater
            startHoverUpdater();
            startScoreboardUpdater();
        }

    }

    /**
     * starts a thread that updates the 1.8 Hover Text every 2 secs for each ingame player
     */
    private void startHoverUpdater()
    {
        hoverUpdater = new BukkitRunnable() {
            @Override
            public void run() {
                for(UUID uuid : ingame_players)
                {
                    PlayerJumpSession session = playerJumpSessions.get(uuid);
                    updateHoverText(session.currentunit,session.lite,uuid);
                }
            }
        }.runTaskTimer(TryJump.getInstance(), 0L, 40L);
    }

    /**
     * stops the thread that updates the 1.8 Hover Text every 2 secs for each ingame player
     */
    private void stopHoverUpdater()
    {
        hoverUpdater.cancel();
    }

    /**
     * starts the scoreboard updater for the jump part
     */
    private void startScoreboardUpdater()
    {
        sbUpdater = new BukkitRunnable() {
            @Override
            public void run() {

                // time managment
                if(!freezed)
                {
                    timeleft--;
                }

                if(timeleft <= 5)
                {
                    for(Player p : Bukkit.getOnlinePlayers())
                    {
                        p.playSound(p.getEyeLocation(),Sound.ITEM_PICKUP,100,1);
                    }
                }
                if(timeleft <= 0)
                {
                    for(UUID uuid : ingame_players)
                    {
                        RoundStats stats = getRoundStats(uuid);
                        if(stats != null)
                        {
                            stats.setJump_fails(playerJumpSessions.get(uuid).fails);
                        }
                    }
                    buyPhase();
                    cancel();
                    return;
                }

                // DisplayName
                // care for main scoreboard (no longer used in the jump phase [only in dm and buyphase])
                String displayName = "Fehler";
                int minutes = timeleft / 60;
                int seconds = timeleft % 60;
                String mins = (minutes < 10 ? "0" : "") + minutes;
                String secs = (seconds < 10 ? "0" : "") + seconds;
                String formattedTime = mins + ":" + secs;
                displayName = ChatColor.GOLD + ">> " + ChatColor.YELLOW + "TryJump " + ChatColor.GOLD + "| " + ChatColor.AQUA + formattedTime;
                jumpprogress_objective.setDisplayName(displayName);

                for(UUID uuid : ingame_players)
                {
                    Player p = Bukkit.getPlayer(uuid);
                    jumpprogress_objective.getScore(p).setScore(percentOfJumpPart(p.getLocation().getBlockZ()));
                }
                // now care for the players' scoreboard
                HashMap<UUID, Integer> percent = new HashMap<>();
                for(UUID uuid : ingame_players)
                {
                    Player p = Bukkit.getPlayer(uuid);
                    percent.put(uuid, percentOfJumpPart(p.getLocation().getBlockZ()));
                }
                
                for(UUID uuid : ingame_players)
                {
                    Player p = Bukkit.getPlayer(uuid);
                    Scoreboard sb = p.getScoreboard();
                    if(sb != null)
                    {
                        String footer = "§7Du befindest dich auf: §b" + TryJump.getInstance().getServerID().toUpperCase() + "\n\n\n§e§lZIEL§r\n";
                        sb.getObjective(DisplaySlot.SIDEBAR).setDisplayName(displayName);
                        for(UUID uuid1 : ingame_players)
                        {
                            Player pl = Bukkit.getPlayer(uuid1);
//                            sb.getObjective(DisplaySlot.SIDEBAR).getScore(pl).setScore(percentOfJumpPart(pl.getLocation().getBlockZ()));
                            sb.getObjective(DisplaySlot.SIDEBAR).getScore(pl).setScore(percent.get(uuid1));
                        }
                        for(int i = 9; i >= 0; i--)
                        {
                            ArrayList<UUID> ingame_players_r = (ArrayList<UUID>) ingame_players.clone();
                            Collections.reverse(ingame_players_r);
                            for(UUID uuid1 : ingame_players_r)
                            {
                                if(percent.get(uuid1) >= i * 10)
                                    footer += (uuid1 == uuid ? "§a" : "§c") + "█§r";
                                else
                                    footer += (uuid1 == uuid ? "§7" : "§8") + "▒§r";
                            }
                            footer += "\n";
                        }
                        footer += "§e§lSTART";
                        TabTitleManager.sendTablist(p, "§6>> §ebergwerkLABS-Servernetzwerk §6<<", footer);
                    }
                }
            }
        }.runTaskTimer(TryJump.getInstance(), 0L, 20L);
    }

    /**
     * stops the scoreboard updater for the jump part
     */
    private void stopScoreboardUpdater()
    {
        sbUpdater.cancel();
    }

    public ItemShop getItemShop() {
        return itemShop;
    }

    /**
     * prepares the buyPhase and starts the scoreboardupdater for the buyphase
     */
    public void buyPhase()
    {
        buyphase = true;
        itemShop = new ItemShop();
        for(Player p : Bukkit.getOnlinePlayers())
        {
            TabTitleManager.sendTablist(p, "§6>> §ebergwerkLABS-Servernetzwerk §6<<", "§7Du befindest dich auf: §b" + TryJump.getInstance().getServerID().toUpperCase());
        }
        stopHoverUpdater();
        hoverUpdater = new BukkitRunnable() {
            @Override
            public void run() {
                for(Player p : Bukkit.getOnlinePlayers())
                {
                    if(!deathmatch)
                    {
                        HoverText.sendHoverTextUpdate(p, ChatColor.GOLD + "" + ChatColor.BOLD + ">> " + ChatColor.YELLOW + ChatColor.BOLD + "Ausrüsten");
                    }else
                    {
                        if(teams_allowed)
                        {
                            HoverText.sendHoverTextUpdate(p, ChatColor.GOLD + "" + ChatColor.BOLD + ">> " + ChatColor.GREEN + ChatColor.BOLD + "TEAMS ERLAUBT!");
                        }else
                        {
                            HoverText.sendHoverTextUpdate(p, ChatColor.GOLD + "" + ChatColor.BOLD + ">> " + ChatColor.RED + ChatColor.BOLD + "TEAMS VERBOTEN!");
                        }

                    }

                }
            }
        }.runTaskTimer(TryJump.getInstance(), 0L, 40L);

        for(UUID uuid : ingame_players)
        {
            Player p = Bukkit.getPlayer(uuid);
            p.getInventory().clear();
            p.teleport(Bukkit.getWorld("spawn").getSpawnLocation());
            p.setScoreboard(scoreboard);
            p.sendMessage(TryJump.getInstance().getChatPrefix() + "Du kannst mit " + ChatColor.AQUA + "/skip" + ChatColor.GRAY + " die Wartezeit verkürzen, falls du schnell fertig bist.");
            ItemStack is = new ItemStack(Material.CHEST);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(""+ChatColor.GOLD + ChatColor.BOLD + ">> " + ChatColor.YELLOW + "TryJump Shop");
            is.setItemMeta(im);
            p.getInventory().setItem(4,is);
        }
        for(UUID uuid : specators)
        {
            Player p = Bukkit.getPlayer(uuid);
            if(p != null)
            {
                p.teleport(Bukkit.getWorld("spawn").getSpawnLocation());
            }
        }

        // start new scoreboardupdater
        jumpprogress_objective.unregister();
        token_objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        timeleft = 90;
        sbUpdater = new BukkitRunnable() {
            @Override
            public void run() {

                // time managment
                timeleft--;

                if(timeleft == 5)
                {
                    for(Player p : Bukkit.getOnlinePlayers())
                    {
                        for(Player pl : Bukkit.getOnlinePlayers())
                        {
                            p.hidePlayer(pl);
                        }
                    }
                }

                if(timeleft == 3)
                {
                    for(Player p : Bukkit.getOnlinePlayers())
                    {
                        for(Player pl : Bukkit.getOnlinePlayers())
                        {
                            p.showPlayer(pl);
                        }
                    }
                }

                if(timeleft <= 5)
                {
                    for(Player p : Bukkit.getOnlinePlayers())
                    {
                        p.playSound(p.getEyeLocation(),Sound.ITEM_PICKUP,100,1);
                    }
                }
                if(timeleft <= 0)
                {
                    deathmatch();
                    cancel();
                    return;
                }

                // DisplayName
                String displayName = "Fehler";
                int minutes = timeleft / 60;
                int seconds = timeleft % 60;
                String mins = (minutes < 10 ? "0" : "") + minutes;
                String secs = (seconds < 10 ? "0" : "") + seconds;
                String formattedTime = mins + ":" + secs;
                displayName = ChatColor.GOLD + ">> " + ChatColor.YELLOW + "TryJump " + ChatColor.GOLD + "| " + ChatColor.AQUA + formattedTime;
                token_objective.setDisplayName(displayName);


            }
        }.runTaskTimer(TryJump.getInstance(), 0L, 20L);

        for(UUID uuid : ingame_players)
        {
            Player p = Bukkit.getPlayer(uuid);
            updateLevelBar(p);
            spawnEnchanterHologram(p);
        }



    }

    public boolean isDeathmatch()
    {
        return deathmatch;
    }

    private void spawnEnchanterHologram(Player p)
    {
        Location enchanterLoc = new Location(Bukkit.getWorld("spawn"),-25.5,109.5,-31.5);

        try {
            new Marker(ChatColor.RED + "Rechtsklick = Verzaubern: " + ChatColor.GOLD + "250 Tokens",enchanterLoc).spawn(p);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    /**
     * prepares the DM and starts its scoreboard updater
     */
    private void deathmatch()
    {
        deathmatch = true;
        buyphase = false;
        // scoreboard
        token_objective.unregister();
        deathmatch_lives_objective = scoreboard.registerNewObjective("lives,","dummy");
        deathmatch_lives_objective.setDisplayName(".");
        deathmatch_lives_objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        timeleft = 600;
        sbUpdater = new BukkitRunnable() {
            @Override
            public void run() {

                // time managment
                timeleft--;

                if(TryJump.getInstance().getCurrentState() == GameState.CLEANUP)
                {
                    cancel();
                    return;
                }

                if(timeleft <= 5)
                {
                    for(Player p : Bukkit.getOnlinePlayers())
                    {
                        p.playSound(p.getEyeLocation(),Sound.ITEM_PICKUP,100,1);
                    }
                }
                if(timeleft <= 0)
                {
                    Bukkit.broadcastMessage(TryJump.getInstance().getChatPrefix() + ChatColor.AQUA + "Diese Runde hat keinen Gewinner!");
                    Bukkit.broadcastMessage(ChatColor.RED + "Der Server startet in 10 Sekunden neu!");
                    finished = true;
                    TryJump.getInstance().end();
                    Bukkit.getScheduler().scheduleSyncDelayedTask(TryJump.getInstance(), new Runnable() {
                        @Override
                        public void run() {
                            Bukkit.shutdown();
                        }
                    },200L);
                    cancel();
                    return;
                }

                // DisplayName
                String displayName = "Fehler";
                int minutes = timeleft / 60;
                int seconds = timeleft % 60;
                String mins = (minutes < 10 ? "0" : "") + minutes;
                String secs = (seconds < 10 ? "0" : "") + seconds;
                String formattedTime = mins + ":" + secs;
                displayName = ChatColor.GOLD + ">> " + ChatColor.YELLOW + "TryJump " + ChatColor.GOLD + "| " + ChatColor.AQUA + formattedTime;
                deathmatch_lives_objective.setDisplayName(displayName);


            }
        }.runTaskTimer(TryJump.getInstance(), 0L, 20L);


        TryJump.getInstance().getGameStateManager().setState(GameState.RUNNING_DEATHMATCH);
        TryJump.getInstance().getServer().broadcastMessage(TryJump.getInstance().getChatPrefix() + ChatColor.AQUA + "DEATHMATCH!");
        ArrayList<Location> spawns = TryJump.getInstance().getDmSession().getSpawns();
        grace = System.currentTimeMillis();
        for (UUID uuid : ingame_players) {
            Player p = Bukkit.getPlayer(uuid);
            updateLevelBar(p);
            if (ingame_players.contains(p.getUniqueId())) {
                Location location = TryJump.getInstance().getDmSession().getRandomSpawnAndRemove();
                p.teleport(location);
                p.setSaturation(20);
                deathmatch_lives_objective.getScore(p).setScore(3);
                ItemStack is = new ItemStack(Material.CHEST);
                ItemMeta im = is.getItemMeta();
                im.setDisplayName("" + ChatColor.GOLD + ChatColor.BOLD + ">> " + ChatColor.YELLOW + "TryJump Shop");
                is.setItemMeta(im);
                p.getInventory().removeItem(is);
                p.getInventory().addItem(new ItemStack(Material.COMPASS));
            } else {
                p.teleport(TryJump.getInstance().getDmSession().getMid());
            }
            p.playSound(p.getEyeLocation(), Sound.LEVEL_UP, 1.0F, 1.5F);
            p.setGameMode(GameMode.SURVIVAL);
        }
        for(UUID uuid : specators)
        {
            Player p = Bukkit.getPlayer(uuid);
            p.teleport(TryJump.getInstance().getDmSession().getMid());
        }
        TryJump.getInstance().getDmSession().setSpawns(spawns);


        // compass updater
        new BukkitRunnable() {
            @Override
            public void run() {
                for(UUID uuid : ingame_players)
                {
                    Player p = Bukkit.getPlayer(uuid);
                    double nearest = 10000000;
                    for(UUID search : ingame_players)
                    {
                        if(!search.equals(uuid))
                        {
                            Player searched = Bukkit.getPlayer(search);
                            if(searched.getLocation().distanceSquared(p.getLocation()) < nearest)
                            {
                                p.setCompassTarget(searched.getLocation());
                                nearest = searched.getLocation().distanceSquared(p.getLocation());
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(TryJump.getInstance(), 20L, 20L);

    }

    public boolean isGrace()
    {
        return (grace + 2000 > System.currentTimeMillis());
    }

    public void stopThreads()
    {
        stopHoverUpdater();
        stopHoverUpdater();
    }


    /**
     * handles a Player reaching a checkpoint (presure plate)
     * @param p the Player reaching a checkpoint
     * @param checkpoint the reached Block
     */
    public void checkpointReached(Player p, Block checkpoint)
    {

        if(buyphase == true)
        {
            return;
        }

        PlayerJumpSession session = playerJumpSessions.get(p.getUniqueId());
        //checkpoint.setType(Material.WOOD_STAIRS);
        if(session.currentCheckpointLocation.distanceSquared(checkpoint.getLocation()) > 1) //TODO: find a better option
        {
            // token stuff
            int addtokens = 0;
            if(session.currentunit <= 3)
            {
                addtokens = 200;
            }else if(session.currentunit <= 6)
            {
                addtokens = 300;
            }else if(session.currentunit <= 9)
            {
                addtokens = 400;
            }else if(session.currentunit >= 10)
            {
                addtokens = 500;
            }
            if(session.lite)
            {
                addtokens = addtokens / 2;
            }
            session.tokens += addtokens;
            token_objective.getScore(p).setScore(session.tokens);
            updateTokensInJumpScoreboard(p);
            p.playSound(p.getEyeLocation(), Sound.LEVEL_UP, 100, 10);
            String lite = "";
            if(session.lite)
            {
                lite = " Lite";
            }
            p.sendMessage(TryJump.getInstance().getChatPrefix() + "Du hast " + ChatColor.AQUA + "Unit " + session.currentunit + lite + ChatColor.GRAY + " geschafft! Du erhältst " + ChatColor.GREEN + addtokens +  " Tokens" + ChatColor.GRAY + ".");

            // add unit to stats
            DataRegistry.DataSet set = TryJump.getInstance().getUtil().getDataRegistry().getSet(p);
            DataRegistry.DataGroup group = set.getGroup("stats.tryjump");
            String value = group.getValue("tryjump.units", "0");
            int vlue = Integer.parseInt(value);
            group.setValue("tryjump.units", String.valueOf(vlue + 1));


            // add points to stats
            value = group.getValue("tryjump.points", "0");
            vlue = Integer.parseInt(value);
            group.setValue("tryjump.points", String.valueOf(vlue + 10));
            p.sendMessage(TryJump.getInstance().getChatPrefix() + "+ 10 Ranking Punkte");

            RoundStats stats = getRoundStats(p.getUniqueId());
            if(stats != null)
            {
                stats.setUnits(stats.getUnits() +1);
                stats.setPoints(stats.getPoints() + 10);
            }

            if(session.currentunit >= 10)
            {
                for(Player play : Bukkit.getOnlinePlayers())
                {
                    play.playSound(play.getEyeLocation(),Sound.WITHER_SPAWN,10,1);
                }
                TryJump.getInstance().getAchievementManager().checkFirstTry(p,session);
                TitleBuilder.broadcastTitle(Bukkit.getOnlinePlayers(),TryJump.getInstance().getColor(p) + p.getName(), ChatColor.GRAY + "hat das Ziel erreicht!",1,20,25);
                if(timeleft > 10)
                {
                    timeleft = 10;
                }

                for(Player pl : Bukkit.getOnlinePlayers())
                {
                    pl.getInventory().clear();
                }

                // add goals to stats
                value = group.getValue("tryjump.goals", "0");
                vlue = Integer.parseInt(value);
                group.setValue("tryjump.goals", String.valueOf(vlue + 1));



                return;
            }



            session.currentCheckpointLocation = checkpoint.getLocation();
            session.currentunit++;
            resetToUnit(p.getUniqueId(), false);
        }
    }

    public void externalUpdateTokens(Player p,int tokens)
    {
        token_objective.getScore(p).setScore(tokens);
        updateTokensInJumpScoreboard(p);
    }

    public void updateLevelBar(Player p)
    {
        if(deathmatch)
        {
            p.setLevel(0);
        }else if(buyphase)
        {
            p.setLevel(p.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getScore(p).getScore());
        }
    }


    public void onJumpFail(Player p)
    {
        resetToUnit(p.getUniqueId(), true);
    }

    /**
     * prepares the current unit for the player after jump_fail or checkpoint_reach
     * @param uuid the player
     * @param tp when true the player has a jump fail
     */
    private void resetToUnit(UUID uuid, boolean tp)
    {
        PlayerJumpSession session = playerJumpSessions.get(uuid);

        if(!tp)
        {
            session.lite = false;
            session.fails_current_unit = 0;
            session.blocklist.clear();
            build(session.currentCheckpointLocation.clone(), getUnit(session.currentunit, session.lite), Bukkit.getPlayer(uuid), session.blocklist);
        }else
        {
            session.fails_current_unit++;
            session.fails++;
            if(session.fails_current_unit == 3)
            {
                session.lite = true;
                for(Block b : session.blocklist)
                {
                    b.setType(Material.AIR);
                }
                build(session.currentCheckpointLocation.clone(),getUnit(session.currentunit, session.lite), Bukkit.getPlayer(uuid), session.blocklist);
            }
            Bukkit.getPlayer(uuid).teleport(session.currentCheckpointLocation.clone().add(0, 1, 0));
        }
        updateHoverText(session.currentunit, session.lite, uuid);
        String title = "";
        if(!session.lite)
        {
            switch (session.fails_current_unit)
            {
                case 0:
                    title = "";
                    break;
                case 1:
                    title = ChatColor.RED + "\u2716" + ChatColor.GRAY + "\u2716" + ChatColor.GRAY + "\u2716";
                    break;
                case 2:
                    title = ChatColor.RED + "\u2716" + ChatColor.RED + "\u2716" + ChatColor.GRAY + "\u2716";
                    break;
            }
        }else if(session.fails_current_unit == 3)
        {
            title = ChatColor.RED + "\u2716" + ChatColor.RED + "\u2716" + ChatColor.RED + "\u2716";
        }
        TitleBuilder.sendTitle(Bukkit.getPlayer(uuid), "", title, 1, 20, 25);

    }

    /**
     * updates the 1.8 Hover for a player during the jump part
     * @param currentunit the unit count
     * @param isLite self explaining
     * @param uuid
     */
    private void updateHoverText(int currentunit, boolean isLite, UUID uuid)
    {
        String lite = "";
        if(isLite)
        {
            lite = " Lite";
        }
        String difficulty = "" + ChatColor.GREEN + ChatColor.BOLD + "EASY";
        if(currentunit > 3)
        {
            difficulty = "" + ChatColor.GOLD + ChatColor.BOLD + "MEDIUM";
        }
        if(currentunit > 6)
        {
            difficulty = "" + ChatColor.RED + ChatColor.BOLD + "HARD";
        }
        if(currentunit > 9)
        {
            difficulty = "" + ChatColor.DARK_PURPLE + ChatColor.BOLD + "EXTREME";
        }
        String maxtrys = " von 3";
        if(isLite)
        {
            maxtrys = "";
        }



        //String name = unitNameList[currentunit -1];
        String name = null; // stupid
        try {
            name = getUnit(currentunit,false).getName();
            name = name.split(".unit",-1)[0];
        }catch(Exception e)
        {

        }
        if(name == null)
        {
            HoverText.sendHoverTextUpdate(Bukkit.getPlayer(uuid), ChatColor.GOLD + "" + ChatColor.BOLD + ">> " +ChatColor.YELLOW + ChatColor.BOLD + "Aktuell: " + ChatColor.GREEN + ChatColor.BOLD + "Unit " + currentunit + lite  +  ChatColor.GOLD + ChatColor.BOLD + " | " + difficulty + ChatColor.GOLD + ChatColor.BOLD + " << ");
        }else
        {
            HoverText.sendHoverTextUpdate(Bukkit.getPlayer(uuid), ChatColor.GOLD + "" + ChatColor.BOLD + ">> "  + ChatColor.GREEN + ChatColor.BOLD + "Unit " + currentunit + lite +": " +ChatColor.YELLOW + ChatColor.BOLD + name +   ChatColor.GOLD + ChatColor.BOLD + " | " + difficulty + ChatColor.GOLD + ChatColor.BOLD + " << ");
        }


    }


    /**
     * starts a thread that builds the unit block per block
     * @param loca the startLocation
     * @param unit building instructions
     * @param soundPlayer the Player instance the unit is gonna built for
     * @param logBlockList just an ArrayList for logging
     */
    /*
    private void build(Location loca, JSONUnit unit,Player soundPlayer,ArrayList<Block> logBlockList)
    {
        ArrayList<JSONBlock> blocklist = new ArrayList<JSONBlock>();

        for(JSONBlock block : unit.getBlocklist())
        {
            blocklist.add(block);
        }
        new BukkitRunnable() {
            @Override
            public void run() {

                if(!blocklist.isEmpty())
                {
                    JSONBlock block = blocklist.get(0);

                    soundPlayer.playSound(soundPlayer.getEyeLocation(), Sound.ITEM_PICKUP, 100, 1);
                    Location loc = loca.clone().add(block.getX(), block.getY(), block.getZ());
                    loc.getBlock().setType(block.getMaterial());
                    loc.getBlock().setData(block.getData());
                    logBlockList.add(loc.getBlock());
                    if(blocklist.size() == 1)
                    {
                        Location endBlock = new Location(loc.getWorld(),unit.getEndLocX(),unit.getEndLocY(),unit.getEndLocZ()).add(loca);
                        endBlock.getBlock().setType(Material.DIAMOND_BLOCK);
                        logBlockList.add(endBlock.getBlock());
                        endBlock.add(0,1,0);
                        endBlock.getBlock().setType(Material.GOLD_PLATE);
                        logBlockList.add(endBlock.getBlock());

                    }
                    blocklist.remove(0);
                }else
                {
                    cancel();
                }
            }
        }.runTaskTimer(TryJump.getInstance(), 0L, 2L);
    }

*/
    private void build(Location loca, JSONUnit unit,Player soundPlayer,ArrayList<Block> logBlockList)
    {
        buildHard(loca, unit, soundPlayer, logBlockList);
    }
    private void buildHard(Location loca, JSONUnit unit,Player soundPlayer,ArrayList<Block> logBlockList)
    {
        ArrayList<JSONBlock> blocklist = new ArrayList<JSONBlock>();

        for(JSONBlock block : unit.getBlocklist())
        {
            blocklist.add(block);
        }

        // try to preload the chunk
        /*
        try
        {
            JSONBlock b0 = blocklist.get(0);
            Block b = Bukkit.getWorld("jump").getBlockAt(b0.getX(),b0.getY(),b0.getZ());
            b.getChunk().load();
            b = Bukkit.getWorld("jump").getBlockAt((int)unit.getEndLocX(),(int)unit.getEndLocY(),(int)unit.getEndLocZ());
            b.getChunk().load();

        }catch(Exception e)
        {

        }
        */

        soundPlayer.playSound(soundPlayer.getEyeLocation(), Sound.ITEM_PICKUP, 100, 1);
        while(!blocklist.isEmpty())
        {
            JSONBlock block = blocklist.get(0);
            Location loc = loca.clone().add(block.getX(), block.getY(), block.getZ());
            loc.getBlock().setTypeIdAndData(block.getMaterial().getId(),block.getData(),true);
            logBlockList.add(loc.getBlock());
            if(blocklist.size() == 1)
            {
                Location endBlock = new Location(loc.getWorld(),unit.getEndLocX(),unit.getEndLocY(),unit.getEndLocZ()).add(loca);
                endBlock.getBlock().setType(Material.DIAMOND_BLOCK);
                logBlockList.add(endBlock.getBlock());
                endBlock.add(0,1,0);
                endBlock.getBlock().setType(Material.GOLD_PLATE);
                logBlockList.add(endBlock.getBlock());

            }
            blocklist.remove(0);
        }

    }

    /**
     * returns the required JSONUnit
     * @param nr
     * @param lite
     * @return
     */
    private JSONUnit getUnit(int nr, boolean lite)
    {
        if(nr <= 3)
        {
            if(lite)
            {
                return easy_lite_units.get(nr);
            }
            return easy_units.get(nr);
        }
        if(nr > 3 && nr <= 6)
        {
            if(lite)
            {
                return medium_lite_units.get(nr);
            }
            return medium_units.get(nr);
        }
        if(nr > 6 && nr <= 9)
        {
            if(lite)
            {
                return hard_lite_units.get(nr);
            }
            return hard_units.get(nr);
        }
        if(nr > 9)
        {
            if(lite)
            {
                return extreme_lite_units.get(nr);
            }
            return extreme_units.get(nr);
        }
        return null;
    }

    public boolean isFreezed() {
        return freezed;
    }

    public void setFreezed(boolean freezed) {
        this.freezed = freezed;
    }

    /**
     * handle on PlayerQuitEvent
     * @param p
     */
    public void onPlayerQuit(Player p)
    {
        if(ingame_players.contains(p.getUniqueId()))
        {
            eliminate(p,false);

            // 5 min ban
            if(!p.hasPermission("bergwerklabs.full-join")) {
                DataRegistry.DataSet set = Util.getUtil().getDataRegistry().getSet(p);
                DataRegistry.DataGroup group = set.getGroup("bans.tryjump");
                String value = String.valueOf(System.currentTimeMillis());
                group.setValue("tryjump.lastban", String.valueOf(value));
                System.out.println("submitting 5 min ban for " + p.getName());
                set.save();
            }
        }
        if(specators.contains(p.getUniqueId()))
        {
            specators.remove(p.getUniqueId());
        }
        if(ingame_players.size() == 1)
        {
            Player winner = TryJump.getInstance().getServer().getPlayer(ingame_players.get(0));
            win(winner);
        }

    }

    /**
     * let a Player "ausscheiden" from the Game
     * @param p
     * @param spec when true -> Player will be spectator | when false -> Player is no longer relevant
     */
    public void eliminate(Player p, boolean spec)
    {
        if(ingame_players.contains(p.getUniqueId()))
        {
            ingame_players.remove(p.getUniqueId());
            scoreboard.resetScores(p);

            if(TryJump.getInstance().getGameStateManager().getState() == GameState.RUNNING_DEATHMATCH)
            {
                for(ItemStack is : p.getInventory().getContents())
                {
                    if(is != null)
                    {
                        p.getWorld().dropItemNaturally(p.getEyeLocation(),is);
                    }
                }
                for(ItemStack is : p.getInventory().getArmorContents())
                {
                    if(is != null)
                    {
                        try
                        {
                            p.getWorld().dropItemNaturally(p.getEyeLocation(), is);
                        }catch(Exception e)
                        {

                        }

                    }
                }
            }
            Bukkit.broadcastMessage(TryJump.getInstance().getChatPrefix() + "Der Spieler " + TryJump.getInstance().getColor(p) + p.getName() + ChatColor.GRAY + " ist ausgeschieden!");

            if(spec)
            {
                if(!specators.contains(p.getUniqueId()))
                {
                    specators.add(p.getUniqueId());
                }
            }

            if(ingame_players.size() == 1)
            {
                Player winner = TryJump.getInstance().getServer().getPlayer(ingame_players.get(0));
                win(winner);
            }

        }
    }

    /**
     * let the game stop and annoucne the winner
     * @param p winner instance
     */
    private void win(Player p)
    {
        if(won)
        {
            return;
        }
        won = true;
        AutoNick nickPlugin = (AutoNick)TryJump.getInstance().getNickPlugin();

        for(Player pl : Bukkit.getOnlinePlayers())
        {
            if(nickPlugin.isPlayerNicked(pl))
            {
                nickPlugin.unnick(pl);
                if(p.getUniqueId().equals(pl.getUniqueId()))
                {
                    TitleBuilder.broadcastTitle(Bukkit.getOnlinePlayers(), TryJump.getInstance().getColor(p) + p.getName(), ChatColor.GRAY + "hat das Spiel gewonnen!", 1, 20, 25);
                }
            }
        }
        TryJump.getInstance().getGameStateManager().setState(GameState.CLEANUP);
        stopThreads();
        for(Player play : Bukkit.getOnlinePlayers())
        {
            play.playSound(play.getEyeLocation(), Sound.FIREWORK_LAUNCH,100,1);

        }
        Bukkit.broadcastMessage(TryJump.getInstance().getChatPrefix() + "Der Spieler " + TryJump.getInstance().getColor(p) + p.getName() + ChatColor.GRAY + " hat das Spiel gewonnen!");
        TitleBuilder.broadcastTitle(Bukkit.getOnlinePlayers(), TryJump.getInstance().getColor(p) + p.getName(), ChatColor.GRAY + "hat das Spiel gewonnen!", 1, 20, 25);
        finished = true;
        TryJump.getInstance().getServer().broadcastMessage(TryJump.getInstance().getChatPrefix() + ChatColor.RED + "Der Server startet in 10 Sekunden neu.");

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                TryJump.getInstance().getServer().shutdown();
            }
        }.runTaskLater(TryJump.getInstance(), 200L);



        // add wins to stats
        DataRegistry.DataSet set = TryJump.getInstance().getUtil().getDataRegistry().getSet(p);
        DataRegistry.DataGroup group = set.getGroup("stats.tryjump");
        String value = group.getValue("tryjump.wins", "0");
        int vlue = Integer.parseInt(value);
        group.setValue("tryjump.wins", String.valueOf(vlue + 1));

        // add network coins
        /*
        DataRegistry.DataGroup coins_group = set.getGroup("network.currency");
        value = coins_group.getValue("network.coins", "0");
        vlue = Integer.parseInt(value);

        p.sendMessage(TryJump.getInstance().getChatPrefix() + ChatColor.AQUA + "Du erhÃ¤ltst " + ChatColor.GREEN + "20 " + ChatColor.AQUA + "Coins für das Gewinnen dieser Runde!");

        if(p.hasPermission("bergwerklabs.full-join"))
        {
            p.sendMessage(TryJump.getInstance().getChatPrefix() + ChatColor.GOLD + "+ " + ChatColor.YELLOW + "20 Coins " + ChatColor.GOLD + "(Premium Boost)");
            coins_group.setValue("network.coins",String.valueOf((vlue +40)));
        }else
        {
            coins_group.setValue("network.coins",String.valueOf((vlue +20)));
        }

*/
        Currency.addCoinsWithPremiumAmplifier(p,20);

        Bukkit.getScheduler().scheduleSyncDelayedTask(TryJump.getInstance(), new Runnable() {
            @Override
            public void run() {
                for(Player player : Bukkit.getOnlinePlayers())
                {
                        if(player != null && player.isOnline())
                        {
                            RoundStats stats = getRoundStats(player.getUniqueId());
                            if(stats != null)
                            {
                                player.sendMessage(TryJump.getInstance().getChatPrefix() + ChatColor.AQUA + "Rundenstatistik:");
                                player.sendMessage(TryJump.getInstance().getChatPrefix() + ChatColor.WHITE + "\u25A0 " + ChatColor.GRAY + "Punkte: " + ChatColor.AQUA + stats.getPoints());
                                player.sendMessage(TryJump.getInstance().getChatPrefix() + ChatColor.WHITE + "\u25A0 " + ChatColor.GRAY + "Kills: " + ChatColor.GREEN + stats.getKills());
                                player.sendMessage(TryJump.getInstance().getChatPrefix() + ChatColor.WHITE + "\u25A0 " + ChatColor.GRAY + "Tode: " + ChatColor.RED + stats.getDeaths());
                                player.sendMessage(TryJump.getInstance().getChatPrefix() + ChatColor.WHITE + "\u25A0 " + ChatColor.GRAY + "Units geschafft: " + ChatColor.AQUA + stats.getUnits());
                            }
                        }

                }
            }
        },20L);



        Bukkit.getScheduler().scheduleSyncDelayedTask(TryJump.getInstance(), new Runnable() {
            @Override
            public void run() {
                TryJump.getInstance().end();
            }
        }, 100L);
    }

    /**
     * add a spectator
     * @param p
     */
    public void addSpectator(Player p)
    {
        System.out.println("3");
        if(!specators.contains(p.getUniqueId()))
        {
            specators.add(p.getUniqueId());
        }
        p.setGameMode(GameMode.SPECTATOR);
        p.sendMessage(TryJump.getInstance().getChatPrefix() + "Du bist nun Spectator!");
        if(ingame_players.size() > 0)
        {
            System.out.println("4");
            Player toSpec = TryJump.getInstance().getServer().getPlayer(ingame_players.get(0));
            p.teleport(toSpec.getLocation());
        }

    }

    public void unnickUpdate(NickEvent e)
    {
        Player p = e.getPlayer();
        try
        {
            int i = token_objective.getScore(e.getOldName()).getScore();
            token_objective.getScore(e.getNewName()).setScore(i);

            for(Player pl : Bukkit.getOnlinePlayers())
            {
                Scoreboard sb = pl.getScoreboard();
                if(sb != null)
                {
                    Objective o = sb.getObjective(DisplaySlot.SIDEBAR);
                    if(o != null)
                    {
                        int j = o.getScore(e.getOldName()).getScore();
                        o.getScore(e.getNewName()).setScore(j);
                    }
                }
            }



        }catch(Exception ex)
        {

        }
        try
        {
            int i = deathmatch_lives_objective.getScore(e.getOldName()).getScore();
            deathmatch_lives_objective.getScore(e.getNewName()).setScore(i);
        }catch(Exception ex)
        {

        }
        try
        {
            scoreboard.resetScores(e.getOldName());
            for(Player pl : Bukkit.getOnlinePlayers())
            {
                Scoreboard sb = pl.getScoreboard();
                if(sb != null)
                {
                    sb.resetScores(e.getOldName());
                }
            }
        }catch(Exception ex)
        {

        }

        if(TryJump.getInstance().getCurrentState() == GameState.RUNNING && !(buyphase))
        {
            if(p.getScoreboard() != null)
            {
                Scoreboard sb = p.getScoreboard();
                Team underlined = sb.registerNewTeam("t_" + p.getName());
                underlined.setPrefix("" + ChatColor.UNDERLINE);
                underlined.addPlayer(p);
            }
        }
    }


    public boolean instantTodCooldown(UUID uuid)
    {
        if(playerJumpSessions.containsKey(uuid))
        {
            if(playerJumpSessions.get(uuid).lite)
            {
                return false;
            }
        }

        if(instant_tod_cooldown.containsKey(uuid))
        {
            if(instant_tod_cooldown.get(uuid) > System.currentTimeMillis())
            {
                return true;
            }
        }
        instant_tod_cooldown.put(uuid,System.currentTimeMillis() + 2000);
        return false;
    }

    public void setTimeleft(int timeleft) {
        this.timeleft = timeleft;
    }

    public ArrayList<UUID> getIngame_players() {
        return ingame_players;
    }

    public boolean isBuyphase() {
        return buyphase;
    }

    private void initTeamsAllowedConfig()
    {
        File file = new File("plugins/Gameserver","teamsallowed.yml");
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        cfg.addDefault("teams_allowed",false);
        cfg.options().copyDefaults(true);
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        teams_allowed = cfg.getBoolean("teams_allowed");
    }

    public boolean hasFinished()
    {
        return finished;
    }
}
