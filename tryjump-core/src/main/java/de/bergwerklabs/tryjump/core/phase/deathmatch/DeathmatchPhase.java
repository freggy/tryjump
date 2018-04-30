package de.bergwerklabs.tryjump.core.phase.deathmatch;

import de.bergwerklabs.tryjump.core.TryJumpSession;
import de.bergwerklabs.tryjump.core.phase.Phase;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

/**
 * Created by Yannic Rieger on 22.04.2018.
 *
 * <p>
 *
 * @author Yannic Rieger
 */
public class DeathmatchPhase extends Phase {

  private static final Set<Listener> LISTENERS = new HashSet<>();

  public DeathmatchPhase(TryJumpSession session) {
    super(session);
  }

  @Override
  public void start() {
    Bukkit.broadcastMessage("DEATHMATCH");
  }

  @Override
  public void stop() {}
}
