package de.bergwerklabs.tryjump.core.listener.deathmatch;

import de.bergwerklabs.tryjump.core.TryJump;
import org.bukkit.event.Listener;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Yannic Rieger on 14.04.2018.
 * <p>
 *
 * @author Yannic Rieger
 */
public class DeathmachtListener implements Listener {

    private static final Set<Listener> LISTENERS = new HashSet<>();
    protected TryJump tryJump;

    public DeathmachtListener(TryJump tryJump) {
        LISTENERS.add(this);
        this.tryJump = tryJump;
    }
}
