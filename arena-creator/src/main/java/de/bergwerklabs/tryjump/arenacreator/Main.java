package de.bergwerklabs.tryjump.arenacreator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Yannic Rieger on 22.02.2018.
 *
 * <p>
 *
 * @author Yannic Rieger
 */
public class Main extends JavaPlugin implements Listener {

  static final String PREFIX = "&6>> &eArenaCreator &7";

  private final Map<UUID, CreatorSession> sessionMap = new HashMap<>();

  @Override
  public void onEnable() {
    Bukkit.getPluginManager().registerEvents(this, this);
  }

  @EventHandler
  private void onPlayerJoin(PlayerJoinEvent event) {
    final Player player = event.getPlayer();
    this.sessionMap.put(player.getUniqueId(), new CreatorSession(player));
  }

  @EventHandler
  private void onPlayerInteract(PlayerInteractEvent event) {
    final ItemStack item = event.getItem();
    if (!(item.getType() == Material.EMERALD)) return;

    final Action action = event.getAction();
    final CreatorSession session = sessionMap.get(event.getPlayer().getUniqueId());

    if (session == null) return;

    final Player player = session.getPlayer();

    if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
      session.removeSpawn();
      player.sendMessage(PREFIX + "&cLast added spawn has been removed.");
    } else if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
      Location spawn = player.getLocation();
      session.addSpawn(spawn);
      player.sendMessage(PREFIX + "&aSpawn added &7(&b" + spawn + "&7)");
    }
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (!(sender instanceof Player)) return false;

    final Player player = (Player) sender;

    if (label.equalsIgnoreCase("cm")) {
      String usage = PREFIX + "&b/cm <name> <creators>";
      if (args.length == 0) {
        this.handleErr(player, usage);
        return false;
      }

      final String arenaName = args[0];
      final String[] creators = Arrays.copyOfRange(args, 1, args.length);

      if (arenaName.isEmpty()) {
        this.handleErr(player, usage);
        return false;
      } else if (creators.length == 0) {
        this.handleErr(player, usage);
        return false;
      }

      player.getItemInHand().setType(Material.EMERALD);
    }
    return false;
  }

  private void handleErr(Player player, String usage) {
    player.sendMessage(usage);
    player.playSound(player.getEyeLocation(), Sound.NOTE_BASS, 1, 1);
  }
}
