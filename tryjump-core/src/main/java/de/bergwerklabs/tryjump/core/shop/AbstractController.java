package de.bergwerklabs.tryjump.core.shop;

import de.bergwerklabs.framework.commons.spigot.general.LabsController;
import de.bergwerklabs.framework.commons.spigot.inventorymenu.InventoryMenu;
import java.util.Map;

/**
 * Created by Yannic Rieger on 11.05.2018.
 *
 * <p>
 *
 * @author Yannic Rieger
 */
public class AbstractController implements LabsController {

  protected Map<String, InventoryMenu> menus;

  public AbstractController(Map<String, InventoryMenu> menus) {
    this.menus = menus;
  }
}
