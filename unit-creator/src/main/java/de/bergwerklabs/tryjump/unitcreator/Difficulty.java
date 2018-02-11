package de.bergwerklabs.tryjump.unitcreator;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * Created by Yannic Rieger on 27.09.2017.
 * <p>
 * Contains all the different difficulty types of a unit.
 *
 * @author Yannic Rieger
 */
public enum Difficulty {

    EASY(1, ChatColor.GREEN),
    MEDIUM(2, ChatColor.GOLD),
    HARD(3, ChatColor.RED),
    EXTREME(4, ChatColor.DARK_PURPLE),
    UNDEFINED(0, ChatColor.MAGIC);

    private int difValue;
    private ChatColor color;

    Difficulty(int value, ChatColor chatColor) {
        this.difValue = value;
        this.color = chatColor;
    }

    /**
     * Gets the {@link Difficulty} with the same difficulty value passed.
     *
     * @param value Difficulty value that ranges from 1 to 4.
     * @return the {@link Difficulty} with the same difficulty value. {@code Difficulty.UNDEFINED} if no fitting value can be found.
     */
    public static Difficulty getByValue(int value) {
        return Arrays.stream(Difficulty.values()).filter(difficulty -> difficulty.difValue == value).findFirst().orElse(Difficulty.UNDEFINED);
    }

    /**
     * Displays the available difficulties to a player with their {@code diffValue}.
     *
     * @param player Player to display the difficulties to.
     */
    public static void displayDifficulties(Player player) {
        Arrays.stream(Difficulty.values()).forEach(difficulty -> player.sendMessage(Main.CHAT_PREFIX + difficulty.color + difficulty.difValue + "§7 -> " + difficulty.name()));
    }
}
