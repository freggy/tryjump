package de.bergwerklabs.tryjump.gameserver.util;

import de.bergwerklabs.atlantis.api.logging.AtlantisLogger;
import de.bergwerklabs.atlantis.client.base.playerdata.PlayerdataSet;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Yannic Rieger on 14.12.2017.
 * <p>
 *
 * @author Yannic Rieger
 */
public class AtlantisStatsWrapper {

    private static AtlantisLogger logger = AtlantisLogger.getLogger(AtlantisStatsWrapper.class);

    private static final String ACHIEVEMENT_GROUP = "achievements.tryjump";
    private static final String DEFAULT_GROUP = "stats.tryjump";
    private static final String BANS_GROUP = "bans.tryjump";

    private static PlayerdataSet load(UUID player) {
        logger.info("Trying to load stats of " + player);
        PlayerdataSet set = new PlayerdataSet(player);
        set.loadAndWait();
        return set;
    }


    public static void saveAchievement(UUID uuid, String key, boolean value) {
        logger.info("Setting achievement " + key + " for " + uuid + " to " + value);
        setStatistic(uuid, key, value, ACHIEVEMENT_GROUP);
    }

    public static boolean hasAchievement(UUID uuid, String key) {
        logger.info("Checking achievement " + key + " for " + uuid);
        Object val = getStatistic(uuid, key, "false", ACHIEVEMENT_GROUP).toString();
        System.out.println(val);
        return Boolean.valueOf(val.toString());
    }

    public static String getLastBan(UUID uuid) {
        logger.info("Getting last ban of " + uuid);
        return getStatistic(uuid, "tryjump.lastban", "0", BANS_GROUP).toString();
    }

    public static void setLastBan(UUID uuid, String value) {
        setStatistic(uuid, "tryjump.lastban", value, BANS_GROUP);
    }

    public static int getDeaths(UUID uuid) {
        logger.info("Getting deaths of " + uuid);
        return Integer.valueOf(getStatistic(uuid, "tryjump.deaths", "0", DEFAULT_GROUP).toString());
    }

    public static void addDeaths(UUID uuid) {
        int deaths = getDeaths(uuid);
        setStatistic(uuid, "tryjump.deaths", deaths + 1, DEFAULT_GROUP);
    }

    public static int getKills(UUID uuid) {
       return Integer.valueOf(getStatistic(uuid, "tryjump.kills", "0", DEFAULT_GROUP).toString());
    }

    public static void addKills(UUID uuid) {
        int kills = getKills(uuid);
        setStatistic(uuid, "tryjump.kills", kills + 1, DEFAULT_GROUP);
    }

    public static int getPoints(UUID uuid) {
        return Integer.valueOf(getStatistic(uuid, "tryjump.points", "0", DEFAULT_GROUP).toString());
    }

    public static void addPoints(UUID uuid, int value) {
        int points = getPoints(uuid);
        setStatistic(uuid, "tryjump.points", points + value, DEFAULT_GROUP);
    }


    public static int getGamesPlayed(UUID uuid) {
        return Integer.valueOf(getStatistic(uuid, "tryjump.played", "0", DEFAULT_GROUP).toString());
    }

    public static void addGamesPlayed(UUID uuid) {
        int played = getGamesPlayed(uuid);
        setStatistic(uuid, "tryjump.played", played + 1, DEFAULT_GROUP);
    }

    public static int getUnits(UUID uuid) {
        return Integer.valueOf(getStatistic(uuid, "stats.tryjump", "0", DEFAULT_GROUP).toString());
    }

    public static void addUntis(UUID uuid) {
        int untis = getUnits(uuid);
        setStatistic(uuid, "stats.tryjump", untis + 1, DEFAULT_GROUP);
    }

    public static int getGoals(UUID uuid) {
        return Integer.valueOf(getStatistic(uuid, "tryjump.goals", "0", DEFAULT_GROUP).toString());
    }

    public static void addGoals(UUID uuid) {
        int goals = getGoals(uuid);
        setStatistic(uuid, "tryjump.goals", goals + 1, DEFAULT_GROUP);
    }

    public static int getWins(UUID uuid) {
        return Integer.valueOf(getStatistic(uuid, "tryjump.wins", "0", DEFAULT_GROUP).toString());
    }

    public static void addWin(UUID uuid) {
        int wins = getWins(uuid);
        setStatistic(uuid, "tryjump.wins", wins + 1, DEFAULT_GROUP);
    }

    private static void setStatistic(UUID player, String key, Object value, String group) {
        PlayerdataSet set = load(player);
        if (set != null) {
            set.getGroup(group).setValue(key, value.toString());
            set.save();
        }
    }

    private static Object getStatistic(UUID player, String key, Object defaultValue, String group) {
        PlayerdataSet set = load(player);
        if (set != null) {
            return set.getGroup(group).getValue(key, defaultValue);
        }
        return defaultValue;
    }
}
