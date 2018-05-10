package de.bergwerklabs.tryjump.core.command;

import de.bergwerklabs.framework.commons.spigot.chat.messenger.PluginMessenger;
import de.bergwerklabs.tryjump.api.event.skip.PlayerSkipEvent;
import de.bergwerklabs.tryjump.api.event.skip.SkipSuccessfulEvent;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

/**
 * Created by Yannic Rieger on 20.04.2018.
 *
 * <p>
 *
 * @author Yannic Rieger
 */
public class SkipCommand implements CommandExecutor {

  private static boolean isEnabled = true;
  private Set<Player> alreadySkipped = new HashSet<>();
  private Collection<Player> canSkip;
  private int needed;
  private PluginMessenger messenger;
  private boolean skippable = true;

  public static void unregister() {
    isEnabled = false;
  }

  public SkipCommand(int needed, PluginMessenger messenger, Collection<Player> canSkip) {
    this.needed = needed;
    this.messenger = messenger;
    this.canSkip = canSkip;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (!label.equalsIgnoreCase("skip")) return false;
    else if (!(sender instanceof Player)) return false;
    else if (!isEnabled) return false;

    Player player = (Player) sender;

    if (!skippable) {
      this.messenger.message("§cDu kannst nicht mehr abstimmen.", player);
      return false;
    } else if (this.alreadySkipped.contains(player)) {
      this.messenger.message("§cDu hast bereits abgestimmt.", player);
      return false;
    } else if (!this.canSkip.contains(player)) {
      this.messenger.message("§cDu kannst kein §b/skip §causführen.", player);
      return false;
    }

    final PluginManager manager = Bukkit.getPluginManager();
    this.alreadySkipped.add(player);

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
    return false;
  }
}
