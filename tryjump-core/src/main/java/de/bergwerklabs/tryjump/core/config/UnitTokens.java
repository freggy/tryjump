package de.bergwerklabs.tryjump.core.config;

import com.google.gson.JsonObject;

/**
 * Created by Yannic Rieger on 16.04.2018.
 * <p>
 *
 * @author Yannic Rieger
 */
class UnitTokens {

    public int getNormal() {
        return normal;
    }

    public int getLite() {
        return lite;
    }

    private int lite, normal;

    UnitTokens(int lite, int normal) {
        this.lite = lite;
        this.normal = normal;
    }

    public static UnitTokens fromJson(JsonObject jsonObject) {
        return new UnitTokens(jsonObject.get("lite").getAsInt(), jsonObject.get("normal").getAsInt());
    }
}
