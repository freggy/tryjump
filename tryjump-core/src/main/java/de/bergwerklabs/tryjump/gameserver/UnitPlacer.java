package de.bergwerklabs.tryjump.gameserver;

import com.google.common.base.Preconditions;
import de.bergwerklabs.tryjump.api.Unit;

import java.io.File;
import java.util.Queue;

/**
 * Created by Yannic Rieger on 11.02.2018.
 * <p>
 *
 * @author Yannic Rieger
 */
public class UnitPlacer {

    private Queue<TryJumpUnit> selectedUnits;

    public UnitPlacer(File folder) {
        Preconditions.checkNotNull(folder);
        Preconditions.checkArgument(!folder.isDirectory());

        folder.listFiles();
    }

    public void placeUnit(TryJumpUnit unit, Jumper jumper) {

    }

    public Queue<TryJumpUnit> getSelectedUnits() {
        return selectedUnits;
    }
}
