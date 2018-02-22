package de.bergwerklabs.tryjump.unitcreator;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static org.bukkit.Material.*;

/**
 * Created by Yannic Rieger on 14.05.2017.
 * <p>
 * Wrapper class for the Bukkit player object providing functionality for creating units.
 *
 * @author Yannic Rieger
 */
public class Creator {

    /**
     * Gets the current creator session.
     */
    public CreatorSession getSession() {
        return session;
    }

    /**
     * Gets the Bukkit player object.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns a value indicating whether or not the player is in creator mode.
     */
    public boolean isToggled() { return this.toggled; }

    private Player player;
    private CreatorSession session;
    private boolean toggled = false;
    private ItemStack[] contents;

    /**
     * @param player Player to wrap.
     */
    public Creator(Player player) {
        this.player = player;
        this.session = new CreatorSession(player);
    }

    /**
     * Changes the state from building to creator mode.
     */
    public void toggle() {
        player.playSound(player.getEyeLocation(), Sound.CLICK, 100, 1);
        this.toggled = !toggled;

        if (toggled) {
            this.contents = this.player.getInventory().getContents().clone();
            this.player.getInventory().clear();
            this.player.getInventory().setItem(3, this.createNamedItem("§aStartposition setzen", EMERALD));
            this.player.getInventory().setItem(4, this.createNamedItem("§4Creator-Modus beenden", NETHER_STAR));
            this.player.getInventory().setItem(5, this.createNamedItem("§cEndposition setzen", MAGMA_CREAM));
        }
        else {
            this.getPlayer().getInventory().clear();
            this.getPlayer().getInventory().setContents(this.contents);
            this.getPlayer().updateInventory();
        }
    }

    /**
     * Creates a renamed item.
     *
     * @param name Name that the item should have.
     * @param material Material the item consists of.
     * @return Renamed ItemStack
     */
    private ItemStack createNamedItem(String name, Material material) {
        final ItemStack item  = new ItemStack(material);
        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }
}
