package de.bergwerklabs.tryjump.core.listener.jump;

import de.bergwerklabs.tryjump.core.TryJump;
import de.bergwerklabs.tryjump.core.TryJumpSession;
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
public abstract class JumpPhaseListener implements Listener {

  private static final Set<Listener> LISTENERS = new HashSet<>();

  protected TryJump tryJump;

  JumpPhaseListener(TryJump tryJump) {
    LISTENERS.add(this);
    this.tryJump = tryJump;
  }

  public static void unregisterListeners() {
    LISTENERS.forEach(HandlerList::unregisterAll);
  }

  public static void registerListeners(TryJumpSession session) {
    final PluginManager manager = Bukkit.getPluginManager();
    final TryJump tryJump = (TryJump) session.getGame();
    manager.registerEvents(new FoodLevelChangeListener(tryJump), session);
    manager.registerEvents(new DropItemListener(tryJump), session);
    manager.registerEvents(new PlayerDamageListener(tryJump), session);
    manager.registerEvents(new PlayerInteractListener(tryJump), session);
  }
}
