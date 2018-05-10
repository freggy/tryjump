package de.bergwerklabs.tryjump.core.shop;

import de.bergwerklabs.framework.commons.spigot.inventorymenu.InventoryMenu;
import de.bergwerklabs.framework.commons.spigot.inventorymenu.event.InventoryItemClickEvent;
import java.util.Map;
import org.bukkit.entity.Player;

/**
 * Created by Yannic Rieger on 17.04.2018.
 *
 * <p>
 *
 * @author Yannic Rieger
 */
public class MainController extends AbstractController {

  public MainController(Map<String, InventoryMenu> menus) {
    super(menus);
  }

  public void onItemClick(InventoryItemClickEvent event) {
    final String category = event.getParameter().get(0);
    final Player player = (Player) event.getEvent().getWhoClicked();
    player.openInventory(this.menus.get(category).getInventory());
  }
}
