package de.bergwerklabs.tryjump.core.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created by Yannic Rieger on 20.04.2018. <p>
 *
 * @author Yannic Rieger
 */
public class SkipCommand implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    return false;
  }
}
