package de.bergwerklabs.tryjump.gameserver.util;

import de.bergwerklabs.util.vecmath.LocationUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

public class DMMap // TODO - Merge this with SGMap at some point. Probably. Is mainly copy-pasted anyways.
{

    private static final Random random = new Random();
    private final World world;
    private ArrayList<Location> spawns;
    private final Location mid;

    private DMMap(World world, ArrayList<Location> spawns, Location mid) {
        this.world = world;
        this.spawns = spawns;
        this.mid = mid;
    }

    /**
     * Loads a map properly based on it's map config. Also sets up basic stuff
     * like game rules.
     *
     * @param name Path to the map directory
     * @return The map, loaded and wrapped
     */
    public static DMMap loadMap(String name) {
        File file = new File(name);
        if (file.exists()) {
            File config = new File(file, "mapconfig.yml");
            if (config.exists()) {
                WorldCreator wc = new WorldCreator(name);
                World world = wc.createWorld();
                world.setAutoSave(false);
                world.setPVP(true);
                world.setDifficulty(Difficulty.NORMAL);
                world.setGameRuleValue("doDaylightCycle", "false"); // TODO - Maybe put stuff like that in a general plugin config in the future? Like loading game rules dynamically...
                world.setGameRuleValue("mobGriefing", "false");
                world.setGameRuleValue("doMobSpawning", "false");
                world.setGameRuleValue("doFireTick", "false");
                world.setGameRuleValue("keepInventory", "true");
                world.setGameRuleValue("commandBlockOutput", "false");
                world.setSpawnFlags(false, false);
                for (Entity entity : world.getEntities()) {
                    if (!(entity instanceof Player) && !(entity instanceof Villager)) {
                        entity.remove();
                    }
                }
                YamlConfiguration configuration = YamlConfiguration.loadConfiguration(config);
                List<String> spawnStrings = configuration.getStringList("spawns");
                ArrayList<Location> spawns = new ArrayList<>();
                for (String s : spawnStrings) {
                    spawns.add(LocationUtil.buildLocationFromString(s, world));
                }
                String s = configuration.getString("mid");
                DMMap mapSession = new DMMap(world, spawns, LocationUtil.buildLocationFromString(s, world));
                return mapSession;
            }
        }
        return null; // This is sad.
    }

    /**
     * @return The world object of this map
     */
    public World getWorld() {
        return world;
    }

    /**
     * @return A copy of the currently open spawn points
     */
    public ArrayList<Location> getSpawns() {
        return (ArrayList<Location>) spawns.clone();
    }

    public void setSpawns(ArrayList<Location> spawns_)
    {
        this.spawns = spawns_;
    }

    /**
     * Chooses a random free spawn spot and removes it from the list.
     *
     * @return A spawn location - either random or a fallback location
     */
    public Location getRandomSpawnAndRemove() // cool enough
    {
        if (spawns.size() > 0) {
            Location loc = spawns.get(random.nextInt(spawns.size()));
            spawns.remove(loc);
            loc.add(0.5D, 0.0D, 0.5D);
            return loc;
        }
        return mid.clone();
    }

    public Location getRandomSpawn()
    {
        if (spawns.size() > 0) {
            Location loc = spawns.get(random.nextInt(spawns.size()));
            loc.add(0.5D, 0.0D, 0.5D);
            return loc;
        }
        return mid.clone();
    }



    /**
     * @return Fallback location
     */
    public Location getMid() {
        return mid.clone();
    }
}