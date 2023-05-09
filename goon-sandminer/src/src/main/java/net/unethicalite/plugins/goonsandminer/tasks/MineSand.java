package src.main.java.net.unethicalite.plugins.goonsandminer.tasks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import net.runelite.api.TileObject;
import net.runelite.client.plugins.timetracking.farming.CropState;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.game.GameThread;
import net.unethicalite.api.game.Vars;
import net.unethicalite.plugins.goonsandminer.GoonSandMinerPlugin;
import net.unethicalite.plugins.goonsandminer.utils.Constants;
import net.unethicalite.plugins.goonsandminer.utils.tasks.*;

public class MineSand extends Task {
    @Inject private GoonSandMinerPlugin plugin;


    public String getStatus() { return "Mining Sandstone"; }

    public boolean validate() { return true; }

    public void execute() {

    }
}
