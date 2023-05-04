package net.unethicalite.plugins.goonherbrun.tasks;

import javax.inject.Inject;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.plugins.goonherbrun.GoonHerbRunConfig;
import net.unethicalite.plugins.goonherbrun.GoonHerbRunPlugin;
import net.unethicalite.plugins.goonherbrun.utils.Constants;
import net.unethicalite.plugins.goonherbrun.utils.Utils;
import net.unethicalite.plugins.goonherbrun.utils.api.ChaosMovement;
import net.unethicalite.plugins.goonherbrun.utils.tasks.Task;

public class GoToPatch extends Task {
    @Inject private GoonHerbRunPlugin plugin;

    @Inject private GoonHerbRunConfig config;

    @Override
    public String getStatus() {
        return "Going to " + plugin.getCurrentLocation().getName() + " patch";
    }

    @Override
    public boolean validate() {
        return plugin.getCurrentLocation() != null
                && !Utils.isInRegion(plugin.getCurrentLocation().getRegionId());
    }

    @Override
    public void execute() {
        WorldPoint current = Players.getLocal().getWorldLocation();

        if (plugin.getCurrentLocation().getTeleportable().teleport()) {
            Time.sleepTicksUntil(
                    () ->
                            Players.getLocal().getWorldLocation() != null
                                    && !Players.getLocal().getWorldLocation().equals(current),
                    10);
        }

        if (Players.getLocal().distanceTo(plugin.getCurrentLocation().getPatchPoint()) > 10) {
            ChaosMovement.walkTo(plugin.getCurrentLocation().getPatchPoint());
        }
    }
}