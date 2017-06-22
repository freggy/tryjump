package de.bergwerklabs.tryjump.gameserver;

import com.google.gson.Gson;
import de.bergwerklabs.chat.Chat;
import de.bergwerklabs.tryjump.gameserver.command.*;
import de.bergwerklabs.tryjump.gameserver.json.JSONBlock;
import de.bergwerklabs.tryjump.gameserver.json.JSONUnit;
import de.bergwerklabs.tryjump.gameserver.listener.*;
import de.bergwerklabs.tryjump.gameserver.util.DMMap;
import de.bergwerklabs.tryjump.gameserver.util.JumpStartHandler;
import de.bergwerklabs.tryjump.gameserver.util.Stoplag;
import de.bergwerklabs.util.ComponentRegistry;
import de.bergwerklabs.util.GameState;
import de.bergwerklabs.util.GameStateManager;
import de.bergwerklabs.util.LABSGameMode;
import de.bergwerklabs.util.file.WorldMaid;
import de.bergwerklabs.util.mechanic.Ranking;
import de.bergwerklabs.util.mechanic.StartTimer;
import de.bergwerklabs.util.playerdata.DataRegistry;
import de.bergwerklabs.util.playerdata.PlayerDataEntry;
import de.bergwerklabs.util.templates.cmd.CommandStart;
import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.tyrannyofheaven.bukkit.zPermissions.ZPermissionsService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by nexotekHD on 12.04.2016.
 */
public class TryJump extends LABSGameMode {


    public GameSession gameSession;

    private StartTimer timer;

    private DMMap dmSession;

    private AchievementManager achievementManager;

    private Chat chat;

    private static TryJump instance;

    private static String dmname = null;
    
    private String serverID = "TRYJUMP";

    private ZPermissionsService  permissionService;

    public HashMap<String, JSONUnit> getAllunits() { return this.allunits; }

    private HashMap<String, JSONUnit> allunits = new HashMap<>();

    @Override
    public void labsEnable() {
        instance = this;//Waru mha
        getLogger().info("Enabling TryJump ...");
        getServer().setDefaultGameMode(GameMode.ADVENTURE);
        gameSession = new GameSession();
        achievementManager = new AchievementManager();

        System.out.println("-------------------------------------");
        System.out.println("HELLO CI (TRYJUMP: TEAMS NOT ALLOWED)");
        System.out.println("-------------------------------------");

        if (getServer().getPluginManager().isPluginEnabled("LABS_Chat"))
        {
            chat = (Chat) getServer().getPluginManager().getPlugin("LABS_Chat");
        }

        chooseDMMap();

        ComponentRegistry.GlobalComponent.autoRespawn.require();
        ComponentRegistry.GlobalComponent.autoLoadDataRegistry.require();

        getServer().createWorld(new WorldCreator("jump").type(WorldType.FLAT).generatorSettings("2;0").generateStructures(false));

        try (BufferedReader br = new BufferedReader(new FileReader("./serverid.NETSYN")))
        {
            serverID = br.readLine();
        } catch (Exception e){}

        for(World w : getServer().getWorlds())
        {
            // remove mobs
            for(Entity e : w.getEntities())
            {
                if(e instanceof LivingEntity)
                {
                    e.remove();
                }
            }
            w.setTime(0);
            w.setGameRuleValue("doMobSpawning", "false");
            w.setGameRuleValue("doDaylightCycle","false");
            w.setGameRuleValue("spectatorsGenerateChunks","false");
            w.setStorm(false);
            w.setThundering(false);
        }
        buildSpawns(Bukkit.getWorld("jump"));

        Bukkit.getWorld("spawn").setSpawnLocation(-30, 108, -22);

        getServer().getPluginManager().registerEvents(new ListenerCancelStuff(), this);
        getServer().getPluginManager().registerEvents(new ListenerPlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new ListenerPlayerInteract(),this);
        getServer().getPluginManager().registerEvents(new ListenerPlayerNick(),this);
        getServer().getPluginManager().registerEvents(new ListenerEntityDamage(),this);
        getServer().getPluginManager().registerEvents(new ListenerEntityDamageByEntity(),this);
        getServer().getPluginManager().registerEvents(new ListenerPlayerMove(),this);
        getServer().getPluginManager().registerEvents(new ListenerPlayerDeath(),this);
        getServer().getPluginManager().registerEvents(new ListenerPlayerRespawn(), this);
        getServer().getPluginManager().registerEvents(new ListenerPlayerQuit(),this);
        getServer().getPluginManager().registerEvents(new ListenerInventoryClick(),this);
        getServer().getPluginManager().registerEvents(new ListenerPlayerLogin(),this);

        Stoplag stoplag = new Stoplag();
        getServer().getPluginManager().registerEvents(stoplag,this);
        stoplag.setActive(true, Bukkit.getWorld("jump"));

        timer = new StartTimer(this,2,getServer().getMaxPlayers(),new JumpStartHandler());
        getCommand("start").setExecutor(new CommandStart(this,timer));
        getCommand("stats").setExecutor(new StatsCommand());
        getCommand("skip").setExecutor(new SkipCommand());
        getCommand("fix").setExecutor(new FixCommand());
        getCommand("log").setExecutor(new LogCommand());
        getCommand("force").setExecutor(new ForceCommand());

        timer.launch();

        prepareTop10();

        getGameStateManager().setState(GameState.WAITING);

        permissionService = getServer().getServicesManager().load(ZPermissionsService.class);

    }//

    public Chat getChat() {
        return chat;
    }
    
    public String getServerID() {
        return serverID;
    }

    /**
     * builts the hall of fame in the lobby.
     * @throws IOException
     */
    /*
    private void prepareTop10() throws IOException {
        File file = new File(getDataFolder(),"config.yml");
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        cfg.addDefault("sql-ip","localhost");
        cfg.addDefault("sql-port",3306);
        cfg.addDefault("sql-username", "root");
        cfg.addDefault("sql-password", "FEED_ME_INFORMATION_NOOB");

        cfg.options().copyDefaults(true);
        cfg.save(file);

        String ip = cfg.getString("sql-ip");
        int port = cfg.getInt("sql-port");
        String username = cfg.getString("sql-username");
        String password = cfg.getString("sql-password");

        if(password.equalsIgnoreCase("FEED_ME_INFORMATION_NOOB"))
        {
            // break if not configurated
            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(this, new Runnable() {
            @Override
            public void run() {
                LeaderboardLoad loader = new LeaderboardLoad(ip,port,username,password);
                ArrayList<SortedLeaderboardEntry> leaderboard = loader.load("stats.tryjump","tryjump.points",10);
                int i = 1;
                for(SortedLeaderboardEntry entry : leaderboard)
                {
                    double headx = cfg.getDouble("board." + i + ".head.x");
                    double heady = cfg.getDouble("board." + i + ".head.y");
                    double headz = cfg.getDouble("board." + i + ".head.z");
                    String headworld = cfg.getString("board." + i + ".head.world");
                    int headdata = cfg.getInt("board." + i + ".head.data");

                    double signx = cfg.getDouble("board." + i + ".sign.x");
                    double signy = cfg.getDouble("board." + i + ".sign.y");
                    double signz = cfg.getDouble("board." + i + ".sign.z");
                    String signworld = cfg.getString("board." + i + ".sign.world");
                    int signdata = cfg.getInt("board." + i + ".sign.data");

                    Location headloc = new Location(Bukkit.getWorld(headworld),headx,heady,headz);
                    Block headblock = headloc.getBlock();
                    headblock.setType(Material.SKULL);
                    headblock.setData((byte) headdata);
                    Skull headskull = (Skull)headblock.getState();
                    headskull.setSkullType(SkullType.PLAYER);

                    Location signLoc = new Location(Bukkit.getWorld(signworld),signx,signy,signz);
                    Block signBlock = signLoc.getBlock();
                    signBlock.setType(Material.WALL_SIGN);
                    signBlock.setData((byte) signdata);
                    Sign sign = (Sign)signBlock.getState();
                    sign.setLine(0,i+". Platz");
                    sign.setLine(1,"loading ...");
                    sign.setLine(2,"");
                    sign.setLine(3,"");

                    try {
                        String name = NameResolveTask.resolveName(entry.getUuid());
                        headskull.setOwner(name);
                        sign.setLine(1,name);
                        sign.setLine(2,entry.getValue() + " Punkte");
                    } catch (NameResolveTask.CouldNotResolveNameException e) {
                        headskull.setOwner("HerrBergmann");
                        sign.setLine(1,"error"
                        );
                        sign.setLine(2,"error");
                    }
                    sign.update(true);
                    headskull.update(true);
                    i++;
                }
            }
        });
    }
    */

    /*
    private void prepareTop10()
    {
        Location loc1 = new Location(getServer().getWorld("spawn"), -367, 6, 855);
        loc1.setYaw(-90);
        Location loc2 = new Location(getServer().getWorld("spawn"), -367, 5.5, 858);
        loc2.setYaw(-90);
        Location loc3 = new Location(getServer().getWorld("spawn"), -367, 5, 852);
        loc3.setYaw(-90);
        Location loc4 = new Location(getServer().getWorld("spawn"), -367, 4, 848);
        loc4.setYaw(-90);
        Ranking.createRanking("spawn", new Location[]{
                loc1,
                loc2,
                loc3,
                loc4
        }, "stats.tryjump","tryjump.points");
    }

     */

    public ZPermissionsService getPermissionService()
    {
        return permissionService;
    }

    private void prepareTop10()
    {
        ArrayList<PlayerDataEntry> stats = new ArrayList<>();
        stats.add(new PlayerDataEntry("stats.tryjump", "tryjump.kills", "Kills"));
        stats.add(new PlayerDataEntry("stats.tryjump", "tryjump.wins", "Gew. Spiele"));
        World spawn = getServer().getWorld("spawn");
        Location locFirst = new Location(getServer().getWorld("spawn"), -25.5, 112, -9.5);
        locFirst.setYaw(180);
        Location locSecond = new Location(getServer().getWorld("spawn"), -23, 111.5, -9);
        locSecond.setYaw(180);
        Location locThird = new Location(getServer().getWorld("spawn"), -28, 111, -9.0);
        locThird.setYaw(180);
        Location locOwn = new Location(getServer().getWorld("spawn"), -30, 110, -14);
        locOwn.setYaw(-135);

        Ranking.createRanking("spawn", new Location[] {
                        locFirst, locSecond, locThird, locOwn
                }, new PlayerDataEntry("stats.tryjump", "tryjump.points", "Punkte"),
                new Location[] {
                        new Location(getServer().getWorld("spawn"), -25.5, 108.4, -10.3),
                        new Location(getServer().getWorld("spawn"), -23, 108.4, -10.3),
                        new Location(getServer().getWorld("spawn"), -28, 108.4, -10.3),
                        new Location(getServer().getWorld("spawn"), -30, 110.5, -14)
                }, stats);
    }

    public GameSession getGameSession()
    {
        return gameSession;
    }

    public AchievementManager getAchievementManager() {
        return achievementManager;
    }

    /**
     * prepares / chooses the deathmatchMap
     * @return
     */
    private boolean chooseDMMap() // TODO - Merge with chooseMap()
    {
        if (getDataFolder().exists() && getDataFolder().isDirectory()) // Don't technically need this
        {
            File mapDir = new File(getDataFolder(), "maps_dm"); // TODO - Get maps from a central repository
            if (mapDir.exists() && mapDir.isDirectory()) {
                File[] directories = mapDir.listFiles(File::isDirectory); // We only want directories.
                ArrayList<String> maps = new ArrayList<String>();
                for (File dir : directories) {
                    File mapConfig = new File(dir, "mapconfig.yml");
                    if (mapConfig.exists()) {
                        maps.add(dir.getName());
                    }
                }
                if (maps.size() > 0) // Someone forgot to put maps in here. Blame the other devs.
                {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss"); // Need to create temporary world
                    Calendar cal = Calendar.getInstance();
                    String dirName = dateFormat.format(cal.getTime()) + "_DEATHMATCH";
                    Random globalRandom = new Random();
                    String currentDMName = maps.get(globalRandom.nextInt(maps.size()));
                    this.dmname = currentDMName;
                    File map = new File(mapDir, currentDMName);
                    File root = new File(dirName);
                    try {
                        FileUtils.copyDirectory(map, root);
                        Runtime.getRuntime().addShutdownHook(new WorldMaid(dirName));
                        dmSession = DMMap.loadMap(dirName);
                        return true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }



    @Override
    public void labsDisable() {

    }

    public DMMap getDmSession() {
        return dmSession;
    }

    @Override
    public GameStateManager.MetadataHandler getMetaDataHandler() {
        return () -> { return dmname;};
    }


    public static TryJump getInstance()
    {
        return instance;
    }

    public String getColor(Player p)
    {
        Chat chat = (Chat)getServer().getPluginManager().getPlugin("LABS_Chat");
        return chat.getUserPrefix(p);
    }

    /**
     * saves the player stats for the matchup
     */
    public void end()
    {

        for(Player p : Bukkit.getOnlinePlayers())
        {
            DataRegistry.DataSet sset = TryJump.getInstance().getUtil().getDataRegistry().getSet(p.getUniqueId());
            System.out.println("Speichere Stats von " + p.getUniqueId());
            sset.save();
        }
    }

    private void buildSpawns(World world)
    {
        File spawnFile = new File("spawn.unit");
        boolean spawnUnit;
        if(spawnFile.exists())
        {
            spawnUnit = true;
        }else
        {
            spawnUnit = false;
        }

        if(spawnUnit)
        {
            Gson gson = new Gson();
            JSONUnit spawn = gson.fromJson(readFile(spawnFile),JSONUnit.class);
            getGameSession().setSpawnShift(new Location(Bukkit.getWorld("jump"),spawn.getEndLocX(),spawn.getEndLocY(),spawn.getEndLocZ()));
            for(int x = 0; x < 350; x+= 35)
            {
                Location loc = new Location(TryJump.getInstance().getServer().getWorld("jump"),x + 0.5,6,0.5);
                build(spawn,loc);

                /*
                System.out.println("preloading chunks for jump line x =" +x);
                // preload chunks
                for(int z = 0; z <= 480; z+= 16)
                {
                    Location c = new Location(TryJump.getInstance().getServer().getWorld("jump"),x + 0.5,6,z);
                    Chunk chunk = c.getChunk();
                    chunk.load();
                }
                */
            }

        }else
        {
            getGameSession().setSpawnShift(new Location(Bukkit.getWorld("jump"), 0, 0, 0));
            for(int x = 0; x < 350; x+= 35)
            {
                Location loc = new Location(TryJump.getInstance().getServer().getWorld("jump"),x + 0.5,6,0.5);
                loc.clone().add(1,0,0).getBlock().setType(Material.QUARTZ_BLOCK);
                loc.clone().add(0,0,1).getBlock().setType(Material.QUARTZ_BLOCK);
                loc.clone().add(-1,0,0).getBlock().setType(Material.QUARTZ_BLOCK);

                /*
                System.out.println("preloading chunks for jump line x =" +x);
                // preload chunks
                for(int z = 0; z <= 480; z+= 16)
                {
                    Location c = new Location(TryJump.getInstance().getServer().getWorld("jump"),x + 0.5,6,z);
                    Chunk chunk = c.getChunk();
                    chunk.load();
                }
                */
            }
        }

        System.out.println("Built SPAWNS!!!!!!");
    }
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

    private void build(JSONUnit unit,Location loca)
    {
        ArrayList<JSONBlock> blocklist = new ArrayList<JSONBlock>();

        for(JSONBlock block : unit.getBlocklist())
        {
            blocklist.add(block);
        }

        while(!blocklist.isEmpty())
        {
            JSONBlock block = blocklist.get(0);
            Location loc = loca.clone().add(block.getX(), block.getY(), block.getZ());
            loc.getBlock().setTypeIdAndData(block.getMaterial().getId(), block.getData(), true);
            blocklist.remove(0);
        }
    }

}
