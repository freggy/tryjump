package de.bergwerklabs.tryjump.gameserver.command;

import net.minecraft.server.v1_8_R2.PacketPlayOutMapChunk;
import org.bukkit.craftbukkit.v1_8_R2.CraftChunk;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

/**
 * Created by nexotekHD on 25.06.2016.
 */
public class PacketMapChunk {

    private final net.minecraft.server.v1_8_R2.Chunk chunk;

    public PacketMapChunk(final org.bukkit.Chunk chunk) {
        this.chunk = ((CraftChunk)chunk).getHandle();
    }

    public final void send(final Player player) {
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(new PacketPlayOutMapChunk(chunk, true, 20));
    }

}