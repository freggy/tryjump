package de.bergwerklabs.tryjump.core.util;

import de.bergwerklabs.framework.commons.misc.Tuple;
import de.bergwerklabs.framework.commons.spigot.scoreboard.LabsScoreboard;
import de.bergwerklabs.framework.commons.spigot.scoreboard.Row;

/**
 * Created by Yannic Rieger on 30.04.2018.
 *
 * <p>
 *
 * @author Yannic Rieger
 */
public class Scoreboards {

  public static Tuple<LabsScoreboard, Integer> withFooter(
      String title, String id, int playerCount, int entries) {
    LabsScoreboard scoreboard = new LabsScoreboard(title, id);
    scoreboard.addRow(playerCount + entries + 2, new Row(scoreboard, "§a§a§a"));
    scoreboard.addRow(2, new Row(scoreboard, "§a§a"));
    scoreboard.addRow(1, new Row(scoreboard, "§6§m-------------"));
    scoreboard.addRow(0, new Row(scoreboard, "§ebergwerkLABS.de"));
    return new Tuple<>(scoreboard, 3);
  }
}
