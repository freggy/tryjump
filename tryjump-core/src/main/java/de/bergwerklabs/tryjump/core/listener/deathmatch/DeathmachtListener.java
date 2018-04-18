package de.bergwerklabs.tryjump.core.listener.deathmatch;

import de.bergwerklabs.tryjump.core.TryJump;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

/**
 * Created by Yannic Rieger on 14.04.2018.
 *
 * <p>
 *
 * @author Yannic Rieger
 */
public class DeathmachtListener implements Listener {

  private static final Set<Listener> LISTENERS = new HashSet<>();
  protected TryJump tryJump;

  DeathmachtListener(TryJump tryJump) {
    LISTENERS.add(this);
    this.tryJump = tryJump;
  }

  public static void unregisterListeners() {
    LISTENERS.forEach(HandlerList::unregisterAll);
  }
}
