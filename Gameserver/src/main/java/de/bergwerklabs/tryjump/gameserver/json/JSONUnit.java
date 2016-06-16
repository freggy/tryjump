package de.bergwerklabs.tryjump.gameserver.json;

import java.util.ArrayList;

/**
 * Created by nexotekHD on 09.04.2016.
 */
public class JSONUnit {

    private ArrayList<JSONBlock> blocklist;

    private double startLocX;
    private double startLocY;
    private double startLocZ;

    private double endLocX;
    private double endLocY;
    private double endLocZ;

    private int difficulty; // 1 = easy; 2 = medium; 3 = hard; 4 = extreme

    public ArrayList<JSONBlock> getBlocklist() {
        return blocklist;
    }


    public int getDifficulty() {
        return difficulty;
    }

    public void setBlocklist(ArrayList<JSONBlock> blocklist) {
        this.blocklist = blocklist;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public double getStartLocX() {
        return startLocX;
    }

    public double getStartLocY() {
        return startLocY;
    }

    public double getStartLocZ() {
        return startLocZ;
    }


    public double getEndLocX() {
        return endLocX;
    }

    public double getEndLocY() {
        return endLocY;
    }

    public double getEndLocZ() {
        return endLocZ;
    }


    public void setStartLocX(double startLocX) {
        this.startLocX = startLocX;
    }

    public void setStartLocY(double startLocY) {
        this.startLocY = startLocY;
    }

    public void setStartLocZ(double startLocZ) {
        this.startLocZ = startLocZ;
    }


    public void setEndLocX(double endLocX) {
        this.endLocX = endLocX;
    }

    public void setEndLocY(double endLocY) {
        this.endLocY = endLocY;
    }

    public void setEndLocZ(double endLocZ) {
        this.endLocZ = endLocZ;
    }

}
