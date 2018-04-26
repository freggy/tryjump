package de.bergwerklabs.tryjump.unitcreator;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Created by Yannic Rieger on 14.04.2018.
 *
 * <p>
 *
 * @author Yannic Rieger
 */
public class Common {

  public static File findModule(String schemName, String root) {
    if (schemName.contains("_lite")) {
      File easy = new File(root + "/units/easy/lite/" + schemName + ".schematic");
      File medium = new File(root + "/units/medium/lite/" + schemName + ".schematic");
      File hard = new File(root + "/units/hard/lite/" + schemName + ".schematic");
      File extreme = new File(root + "/units/extreme/lite/" + schemName + ".schematic");

      if (easy.exists()) return easy;
      if (medium.exists()) return medium;
      if (hard.exists()) return hard;
      if (extreme.exists()) return extreme;
    } else {
      File easy = new File(root + "/units/easy/default/" + schemName + ".schematic");
      File medium = new File(root + "/units/medium/default/" + schemName + ".schematic");
      File hard = new File(root + "/units/hard/default/" + schemName + ".schematic");
      File extreme = new File(root + "/units/extreme/default/" + schemName + ".schematic");

      if (easy.exists()) return easy;
      if (medium.exists()) return medium;
      if (hard.exists()) return hard;
      if (extreme.exists()) return extreme;
    }
    return null;
  }

  public static List<File> list(String root) {
    File easyLite = new File(root + "/units/easy/lite/");
    File mediumLite = new File(root + "/units/medium/lite/");
    File hardLite = new File(root + "/units/hard/lite/");
    File extremeLite = new File(root + "/units/extreme/lite/");

    File easyDefault = new File(root + "/units/easy/default/");
    File mediumDefault = new File(root + "/units/medium/default/");
    File hardDefault = new File(root + "/units/hard/default/");
    File extremeDefault = new File(root + "/units/extreme/default/");

    List<File> units = new ArrayList<>();

    units.addAll(Arrays.asList(Objects.requireNonNull(easyDefault.listFiles())));
    units.addAll(Arrays.asList(Objects.requireNonNull(mediumDefault.listFiles())));
    units.addAll(Arrays.asList(Objects.requireNonNull(hardDefault.listFiles())));
    units.addAll(Arrays.asList(Objects.requireNonNull(extremeDefault.listFiles())));

    units.addAll(Arrays.asList(Objects.requireNonNull(easyLite.listFiles())));
    units.addAll(Arrays.asList(Objects.requireNonNull(mediumLite.listFiles())));
    units.addAll(Arrays.asList(Objects.requireNonNull(hardLite.listFiles())));
    units.addAll(Arrays.asList(Objects.requireNonNull(extremeLite.listFiles())));

    return units;
  }
}
