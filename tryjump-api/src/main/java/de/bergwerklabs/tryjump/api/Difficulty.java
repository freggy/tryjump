package de.bergwerklabs.tryjump.api;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * Created by Yannic Rieger on 11.02.2018.
 * <p>
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
     *
     * @return the {@link Difficulty} with the same difficulty value. {@code Difficulty.UNDEFINED} if no fitting value can be found.
     */
    public static Difficulty getByValue(int value) {
        return Arrays.stream(Difficulty.values())
                     .filter(difficulty -> difficulty.difValue == value).findFirst()
                     .orElse(Difficulty.UNDEFINED);
    }

    public ChatColor getColor() {
        return color;
    }

    public int getDifValue() {
        return this.difValue;
    }

    @Override
    public String toString() {
        return this.getColor().toString() + this.getDifValue() +"§7 -> " + this.name();
    }
}
