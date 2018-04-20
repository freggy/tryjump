package de.bergwerklabs.tryjump.core;

import de.bergwerklabs.framework.commons.spigot.chat.messenger.PluginMessenger;
import de.bergwerklabs.tryjump.api.event.skip.PlayerSkipEvent;
import de.bergwerklabs.tryjump.api.event.skip.SkipSuccessfulEvent;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

/**
 * Created by Yannic Rieger on 20.04.2018.
 *
 * <p>
 *
 * @author Yannic Rieger
 */
public class SkipManager {

  private Set<Player> alreadySkipped = new HashSet<>();

  public int getNeeded() {
    return needed;
  }

  private int needed;
  private PluginMessenger messenger;
  private boolean skippable = true;

  public SkipManager(int needed, PluginMessenger session) {
    this.needed = needed;
    this.messenger = session;
  }

  public void playerSkip(Player player) {
    if (!this.skippable) {
      this.messenger.message("§cDu kannst nicht mehr abstimmen.", player);
      return;
    } else if (this.alreadySkipped.contains(player)) {
      this.messenger.message("§cDu hast bereits abgestimmt.", player);
      return;
    }

    final PluginManager manager = Bukkit.getPluginManager();
    this.alreadySkipped.add(player);

    // TODO: use rank color
    this.messenger.messageAll(
        "§a"
            + player.getDisplayName()
            + " §7hat dafür gestimmt, die Wartezeit zu verkürzen §b("
            + this.alreadySkipped.size()
            + "/"
            + this.needed
            + ")");

    manager.callEvent(new PlayerSkipEvent(player, this.needed - this.alreadySkipped.size()));

    if (this.needed == this.alreadySkipped.size()) {
      this.skippable = false;
      manager.callEvent(new SkipSuccessfulEvent(this.needed, this.alreadySkipped));
    }
  }
}
