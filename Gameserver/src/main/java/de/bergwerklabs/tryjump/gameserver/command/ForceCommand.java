package de.bergwerklabs.tryjump.gameserver.command;

import de.bergwerklabs.atlantis.client.bukkit.GamestateManager;
import de.bergwerklabs.atlantis.columbia.packages.gameserver.spigot.gamestate.Gamestate;
import de.bergwerklabs.tryjump.gameserver.TryJump;
import de.bergwerklabs.tryjump.gameserver.json.JSONUnit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created by Yannic Rieger on 22.06.2017.
 *
 * @author Yannic Rieger
 */
public class ForceCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender.hasPermission("bergwerklabs.tryjump.force") && s.equalsIgnoreCase("force") && GamestateManager.getCurrentState() == Gamestate.WAITING) {
            if (strings.length == 0) return false;
            String unitName = strings[0];
            JSONUnit normalUnit = TryJump.getInstance().getAllunits().get(unitName + ".unit");
            JSONUnit liteUnit = TryJump.getInstance().getAllunits().get(unitName + ".unit" + "_lite");

            switch (normalUnit.getDifficulty()) {
                case 1:
                    if (!TryJump.getInstance().gameSession.easy_units.containsValue(normalUnit)) {
                        TryJump.getInstance().gameSession.easy_units.remove(2);
                        TryJump.getInstance().gameSession.easy_lite_units.remove(2);

                        TryJump.getInstance().gameSession.easy_units.put(2, normalUnit);
                        TryJump.getInstance().gameSession.easy_lite_units.put(2, liteUnit);
                    }
                    break;
                case 2:
                    if (!TryJump.getInstance().gameSession.medium_units.containsValue(normalUnit)) {
                        TryJump.getInstance().gameSession.medium_units.remove(4);
                        TryJump.getInstance().gameSession.medium_lite_units.remove(4);

                        TryJump.getInstance().gameSession.medium_units.put(4, normalUnit);
                        TryJump.getInstance().gameSession.medium_lite_units.put(4, liteUnit);
                    }
                    break;
                case 3:
                    if (!TryJump.getInstance().gameSession.hard_units.containsValue(normalUnit)) {
                        TryJump.getInstance().gameSession.hard_units.remove(7);
                        TryJump.getInstance().gameSession.hard_lite_units.remove(7);

                        TryJump.getInstance().gameSession.hard_units.put(7, normalUnit);
                        TryJump.getInstance().gameSession.hard_lite_units.put(7, liteUnit);
                    }
                    break;
                case 4:
                    if (!TryJump.getInstance().gameSession.extreme_units.containsValue(normalUnit)) {
                        TryJump.getInstance().gameSession.extreme_units.remove(10);
                        TryJump.getInstance().gameSession.extreme_lite_units.remove(10);

                        TryJump.getInstance().gameSession.extreme_units.put(10, normalUnit);
                        TryJump.getInstance().gameSession.extreme_lite_units.put(10, liteUnit);
                    }
                    break;
            }
            commandSender.sendMessage(TryJump.getInstance().getChatPrefix() + "§aDie Unit §b" + unitName + " §awurde eingesetzt.");
        }
        return true;
    }
}
