package de.bergwerklabs.tryjump.gameserver.util;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by nexotekHD on 13.04.2016.
 */
public class PlayerJumpSession {

    public UUID uuid;
    public int currentunit = 1;
    public boolean lite = false;
    public int fails = 0;
    public int fails_current_unit = 0;

    public int tokens = 100;

    public ArrayList<Block> blocklist = new ArrayList<Block>();

    public Location currentCheckpointLocation = null;

    public PlayerJumpSession(UUID uuid)
    {
        this.uuid = uuid;
    }

}
