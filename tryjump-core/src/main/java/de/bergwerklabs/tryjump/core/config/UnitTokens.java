package de.bergwerklabs.tryjump.core.config;

import com.google.gson.JsonObject;

/**
 * Created by Yannic Rieger on 16.04.2018.
 * <p>
 * Data class that contains token information that is used for each unit difficulty.
 *
 * @author Yannic Rieger
 */
class UnitTokens {

    /**
     * Gets the amount of tokens for the completion of a non-lite unit.
     */
    public int getNormal() {
        return normal;
    }

    /**
     * Gets the amount of tokens for the completion of a lite unit.
     */
    public int getLite() {
        return lite;
    }

    private int lite, normal;

    /**
     *
     * @param lite   the amount of tokens for the completion of a lite unit.
     * @param normal the amount of tokens for the completion of a non-lite unit.
     */
    private UnitTokens(int lite, int normal) {
        this.lite = lite;
        this.normal = normal;
    }

    /**
     * Creates an {@code UnitToken} object from a {@link JsonObject}.
     */
    public static UnitTokens fromJson(JsonObject jsonObject) {
        return new UnitTokens(jsonObject.get("lite").getAsInt(), jsonObject.get("normal").getAsInt());
    }
}
