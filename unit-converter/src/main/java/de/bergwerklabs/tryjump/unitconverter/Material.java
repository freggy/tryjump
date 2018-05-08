package de.bergwerklabs.tryjump.unitconverter;

/**
 * Created by Yannic Rieger on 08.05.2018.
 * <p>
 *
 * @author Yannic Rieger
 */
public enum Material {

  AIR(0);

  private int id;

  Material(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }
}
