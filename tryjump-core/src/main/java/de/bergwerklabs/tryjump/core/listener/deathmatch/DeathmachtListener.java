package de.bergwerklabs.tryjump.core.listener.deathmatch;

import de.bergwerklabs.tryjump.core.TryJump;
import de.bergwerklabs.tryjump.core.TryJumpSession;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

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

  /** Unregister all deathmatch listeners. */
  public static void unregisterListeners() {
    LISTENERS.forEach(HandlerList::unregisterAll);
  }

  /**
   * Registers listeners needed for the deathmatch phase.
   *
   * @param session {@link Plugin} intance.
   */
  public static void registerListeners(TryJumpSession session) {
    final PluginManager manager = Bukkit.getPluginManager();
    final TryJump tryJump = (TryJump) session.getGame();
    manager.registerEvents(new PlayerDamageListener(tryJump), session);
    manager.registerEvents(new PlayerRespawnListener(tryJump), session);
    manager.registerEvents(new EntityDamageByEntityEventListener(tryJump), session);
  }
}
