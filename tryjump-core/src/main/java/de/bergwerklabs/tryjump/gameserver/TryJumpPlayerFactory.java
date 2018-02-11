package de.bergwerklabs.tryjump.gameserver;

import de.bergwerklabs.framework.bedrock.api.PlayerFactory;
import org.bukkit.entity.Player;

/**
 * Created by Yannic Rieger on 11.02.2018.
 * <p>
 *
 * @author Yannic Rieger
 */
public class TryJumpPlayerFactory implements PlayerFactory<Jumper> {

    @Override
    public Jumper createPlayer(Player player) {
        return new Jumper(player);
    }
}
