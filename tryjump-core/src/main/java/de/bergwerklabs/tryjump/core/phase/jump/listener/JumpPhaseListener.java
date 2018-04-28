package de.bergwerklabs.tryjump.core.phase.jump.listener;

import de.bergwerklabs.tryjump.core.TryJumpSession;
import de.bergwerklabs.tryjump.core.phase.PhaseListener;
import de.bergwerklabs.tryjump.core.phase.jump.JumpPhase;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

/**
 * Created by Yannic Rieger on 14.04.2018.
 *
 * <p>
 *
 * @author Yannic Rieger
 */
public abstract class JumpPhaseListener extends PhaseListener<JumpPhase> {

  private static final Set<Listener> LISTENERS = new HashSet<>();

  public JumpPhaseListener(JumpPhase phase, TryJumpSession session) {
    super(phase, session);
    LISTENERS.add(this);
  }

  public static void unregisterListeners() {
    LISTENERS.forEach(HandlerList::unregisterAll);
  }

  public static void registerListeners(TryJumpSession session, JumpPhase phase) {
    final PluginManager manager = Bukkit.getPluginManager();
    manager.registerEvents(new FoodLevelChangeListener(phase, session), session);
    manager.registerEvents(new DropItemListener(phase, session), session);
    manager.registerEvents(new PlayerDamageListener(phase, session), session);
    manager.registerEvents(new PlayerInteractListener(phase, session), session);
  }
}
