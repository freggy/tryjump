package de.bergwerklabs.tryjump.core.phase.deathmatch.listener;

import de.bergwerklabs.tryjump.core.TryJump;
import de.bergwerklabs.tryjump.core.TryJumpSession;
import de.bergwerklabs.tryjump.core.phase.PhaseListener;
import de.bergwerklabs.tryjump.core.phase.deathmatch.DeathmatchPhase;
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
public class DeathmachtListener extends PhaseListener<DeathmatchPhase> {

  private static final Set<Listener> LISTENERS = new HashSet<>();
  protected TryJump tryJump;

  DeathmachtListener(DeathmatchPhase phase, TryJumpSession session) {
    super(phase, session);
    LISTENERS.add(this);
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
  public static void registerListeners(TryJumpSession session, DeathmatchPhase phase) {
    final PluginManager manager = Bukkit.getPluginManager();
    manager.registerEvents(new PlayerDamageListener(phase, session), session);
    manager.registerEvents(new PlayerRespawnListener(phase, session), session);
    manager.registerEvents(new EntityDamageByEntityEventListener(phase, session), session);
  }
}
