package de.bergwerklabs.tryjump.core.phase.buy.listener;

import de.bergwerklabs.tryjump.core.TryJumpSession;
import de.bergwerklabs.tryjump.core.phase.PhaseListener;
import de.bergwerklabs.tryjump.core.phase.buy.BuyPhase;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

/**
 * Created by Yannic Rieger on 28.04.2018.
 *
 * <p>
 *
 * @author Yannic Rieger
 */
public class BuyListener extends PhaseListener<BuyPhase> {

  private static final Set<Listener> LISTENERS = new HashSet<>();

  public BuyListener(BuyPhase phase, TryJumpSession session) {
    super(phase, session);
    LISTENERS.add(this);
  }

  /** Unregister all deathmatch listeners. */
  public static void unregisterListeners() {
    LISTENERS.forEach(HandlerList::unregisterAll);
  }

  /**
   * Registers listeners needed for the buy phase.
   *
   * @param session {@link Plugin} intance.
   */
  public static void registerListeners(TryJumpSession session, BuyPhase phase) {
    final PluginManager manager = Bukkit.getPluginManager();
    manager.registerEvents(new SkipSuccessfulListener(phase, session), session);
  }
}
