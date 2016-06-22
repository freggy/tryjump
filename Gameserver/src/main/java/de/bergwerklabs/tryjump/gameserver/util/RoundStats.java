package de.bergwerklabs.tryjump.gameserver.util;

/**
 * Created by nexotekHD on 22.06.2016.
 */
public class RoundStats {

    private int points;
    private int kills;
    private int deaths;
    private int units;
    private int jump_fails;

    public void setPoints(int points) {
        this.points = points;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public void setJump_fails(int jump_fails) {
        this.jump_fails = jump_fails;
    }

    public int getPoints() {
        return points;
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getUnits() {
        return units;
    }

    public int getJump_fails() {
        return jump_fails;
    }
}
