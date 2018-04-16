package de.bergwerklabs.tryjump.core.config;

import com.google.gson.Gson;
import de.bergwerklabs.tryjump.core.unit.strategy.SelectionStrategy;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

/**
 * Created by Yannic Rieger on 12.02.2018.
 * <p>
 *
 * @author Yannic Rieger
 */
public class Config {

    public int getRankingPointsPerUnit() {
        return rankingPointsPerUnit;
    }

    public int getCoinsPerUnit() {
        return coinsPerUnit;
    }

    public int getJumpPhaseDuration() {
        return jumpPhaseDuration;
    }

    public int getDeathmatchDuration() {
        return deathmatchDuration;
    }

    public int getZeroFailsTokenBoost() {
        return zeroFailsTokenBoost;
    }

    public SelectionStrategy getSelectionStrategy() {
        return selectionStrategy;
    }

    public List<String> getZeroFailsMessages() {
        return zeroFailsMessages;
    }

    public UnitTokens getEasy() {
        return easy;
    }

    public UnitTokens getMedium() {
        return medium;
    }

    public UnitTokens getHard() {
        return hard;
    }

    public UnitTokens getExtreme() {
        return extreme;
    }

    public boolean isTeamSession() {
        return isTeamSession;
    }

    private int rankingPointsPerUnit;
    private int coinsPerUnit;
    private int jumpPhaseDuration;
    private int deathmatchDuration;
    private int zeroFailsTokenBoost;
    private SelectionStrategy selectionStrategy;
    private List<String> zeroFailsMessages;
    private UnitTokens easy;
    private UnitTokens medium;
    private UnitTokens hard;
    private UnitTokens extreme;
    private boolean isTeamSession;

    Config(
            int rankingPointsPerUnit,
            int coinsPerUnit,
            int jumpPhaseDuration,
            int deathmatchDuration,
            int zeroFailsTokenBoost,
            @NotNull String selectionStrategy,
            @NotNull List<String> zeroFailsMessages,
            @NotNull UnitTokens easy,
            @NotNull UnitTokens medium,
            @NotNull UnitTokens hard,
            @NotNull UnitTokens extreme,
            boolean isTeamSession
    ) {
        this.rankingPointsPerUnit = rankingPointsPerUnit;
        this.coinsPerUnit = coinsPerUnit;
        this.jumpPhaseDuration = jumpPhaseDuration;
        this.deathmatchDuration = deathmatchDuration;
        this.zeroFailsTokenBoost = zeroFailsTokenBoost;
        this.selectionStrategy = SelectionStrategy.valueOf(selectionStrategy);
        this.zeroFailsMessages = zeroFailsMessages;
        this.easy = easy;
        this.medium = medium;
        this.hard = hard;
        this.extreme = extreme;
        this.isTeamSession = isTeamSession;
    }

    public static Optional<Config> read(@NotNull File config) {
        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(config), StandardCharsets.UTF_8)) {
            return Optional.of(new Gson().fromJson(reader, Config.class));
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return Optional.empty();
    }
}
