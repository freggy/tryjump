package de.bergwerklabs.tryjump.unitcreator;

import com.boydti.fawe.FaweAPI;
import com.google.common.collect.Lists;
import com.sk89q.worldedit.regions.Region;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Yannic Rieger on 23.09.2017.
 *
 * <p>Main class of the upload tool.
 *
 * @author Yannic Rieger
 */
public class Main extends JavaPlugin implements Listener {

  static Main getInstance() {
    return instance;
  }

  static final String CHAT_PREFIX = "§6>> §eUnitCreator §6| §7";

  private static Main instance;
  private final Map<UUID, Creator> creators = new HashMap<>();

  @Override
  public void onEnable() {
    this.getServer().getPluginManager().registerEvents(this, this);

    final File dataFolder = this.getDataFolder();
    dataFolder.mkdir();

    final File unitsFolder = new File(dataFolder.getAbsolutePath() + "/units");
    unitsFolder.mkdir();

    this.createFolders(unitsFolder, "easy");
    this.createFolders(unitsFolder, "medium");
    this.createFolders(unitsFolder, "hard");
    this.createFolders(unitsFolder, "extreme");
    instance = this;
  }

  private void createFolders(File folder, String diff) {
    new File(folder.getAbsolutePath() + "/" + diff + "/default").mkdirs();
    new File(folder.getAbsolutePath() + "/" + diff + "/lite").mkdirs();
  }

  @EventHandler
  public void onJoin(PlayerJoinEvent e) {
    creators.put(e.getPlayer().getUniqueId(), new Creator(e.getPlayer()));
  }

  @EventHandler
  public void onQuit(PlayerQuitEvent e) {
    creators.remove(e.getPlayer().getUniqueId());
  }

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent e) {
    final Creator creator = creators.get(e.getPlayer().getUniqueId());

    if (e.getAction() == Action.RIGHT_CLICK_AIR) {
      this.toggleCreativeInv(creator);
    }
    if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
      final Location blocLoc = e.getClickedBlock().getLocation();
      if (creator.getPlayer().getItemInHand().getType() == Material.EMERALD) {
        creator
            .getSession()
            .setStart(
                new com.sk89q.worldedit.Vector(blocLoc.getX(), blocLoc.getY(), blocLoc.getZ()));
      } else if (creator.getPlayer().getItemInHand().getType() == Material.MAGMA_CREAM) {
        creator
            .getSession()
            .setEnd(new com.sk89q.worldedit.Vector(blocLoc.getX(), blocLoc.getY(), blocLoc.getZ()));
      } else this.toggleCreativeInv(creator);
    }
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (sender instanceof Player) {
      final Player player = (Player) sender;
      final Creator creator = creators.get(((Player) sender).getUniqueId());

      if (label.equalsIgnoreCase("create")) {
        int difficulty;
        boolean isLite;
        String name;

        if (args.length == 0 || args.length < 3) {
          player.sendMessage(
              CHAT_PREFIX + "Usage: " + ChatColor.AQUA + "/create [name] [difficulty] [isLite]");
          return true;
        }

        try {
          name = args[0];
          difficulty = Integer.valueOf(args[1]);
          isLite = Boolean.valueOf(args[2]);
        } catch (Exception ex) {
          player.sendMessage(CHAT_PREFIX + ChatColor.RED + "Ein Parameter ist fehlerhaft.");
          return true;
        }

        final Region region = FaweAPI.wrapPlayer(player).getSelection();

        if (region == null) {
          player.sendMessage(CHAT_PREFIX + ChatColor.RED + "Du musst ein Gebiet markieren.");
          return false;
        }

        creator.getSession().createSchematic(name, difficulty, isLite, region);
      } else if (label.equalsIgnoreCase("load")) {
        final String name = args[0];

        if (name.isEmpty()) {
          player.sendMessage(CHAT_PREFIX + "Usage: " + ChatColor.AQUA + "/load [name]");
          return false;
        }

        creator.getSession().loadOld(player.getLocation(), args[0]);
      } else if (label.equalsIgnoreCase("submit")) {
        // TODO: copy to cloudstorage
      } else if (label.equalsIgnoreCase("pos1")) {
        creator.getPlayer().performCommand("/pos1");
      } else if (label.equalsIgnoreCase("pos2")) {
        creator.getPlayer().performCommand("/pos2");
      } else if (label.equalsIgnoreCase("cm")) {
        creator.toggle();
      } else if (label.equalsIgnoreCase("remove")) {
        creator.getSession().removeUnit();
      } else if (label.equalsIgnoreCase("delete")) {
        final String name = args[0];

        if (name.isEmpty()) {
          player.sendMessage(CHAT_PREFIX + "Usage: " + ChatColor.AQUA + "/delete [name]");
          return false;
        }

        File unit = Common.findModule(name, Main.getInstance().getDataFolder().getAbsolutePath());

        if (unit == null) {
          player.sendMessage(CHAT_PREFIX + "§cUnit konnte nicht gefunden werden.");
          return false;
        }

        boolean done = unit.delete();

        if (!done) {
          player.sendMessage(CHAT_PREFIX + "§cUnit konnte nicht gelöscht werden.");
          return false;
        }

      } else if (label.equalsIgnoreCase("listu")) {
        String page = args[0];

        if (page.isEmpty()) {
          player.sendMessage(CHAT_PREFIX + "Usage: " + ChatColor.AQUA + "/listu [page]");
          return false;
        }

        int pageInt = 0;

        try {
          pageInt = Integer.valueOf(page);
        } catch (Exception ex) {
          player.sendMessage(CHAT_PREFIX + "§cEingabe muss eine Zahl sein.");
          return false;
        }

        final String folder = Main.getInstance().getDataFolder().getAbsolutePath();
        final List<List<File>> pages = Lists.partition(Common.list(folder), 10);

        if (pages.size() == 0) {
          player.sendMessage(CHAT_PREFIX + "§7Keine Units vorhanden.");
          return false;
        }

        pages
            .get(pageInt - 1)
            .forEach(
                file -> {
                  player.sendMessage("§a" + file.getName());
                });

        player.sendMessage(CHAT_PREFIX + "§7Seite §b" + page + " §7von §b" + pages.size());
      }
      return true;
    }
    return false;
  }

  /**
   * Sets the inventory back to its initial state.
   *
   * @param creator Player
   */
  private void toggleCreativeInv(Creator creator) {
    if (creator.getPlayer().getItemInHand().getType() == Material.NETHER_STAR) {
      creator.toggle();
    }
  }
}
