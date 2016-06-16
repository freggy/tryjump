package de.bergwerklabs.tryjump.gameserver.json;

import org.bukkit.Material;

/**
 * Created by nexotekHD on 09.04.2016.
 */
public class JSONBlock {

    private Material material;

    private int x;
    private int y;
    private int z;

    private byte data;

    public Material getMaterial() {
        return material;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }


    public byte getData() {
        return data;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setZ(int z) {
        this.z = z;
    }


    public void setData(byte data) {
        this.data = data;
    }
}
