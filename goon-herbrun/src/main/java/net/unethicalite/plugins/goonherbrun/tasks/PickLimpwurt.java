package net.unethicalite.plugins.goonherbrun.tasks;

import javax.inject.Inject;
import net.runelite.api.TileObject;
import net.runelite.client.plugins.timetracking.farming.CropState;
import net.runelite.client.plugins.timetracking.farming.Produce;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.game.GameThread;
import net.unethicalite.api.game.Vars;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.plugins.goonherbrun.GoonHerbRunConfig;
import net.unethicalite.plugins.goonherbrun.GoonHerbRunPlugin;
import net.unethicalite.plugins.goonherbrun.PatchImplementation;
import net.unethicalite.plugins.goonherbrun.PatchState;
import net.unethicalite.plugins.goonherbrun.utils.Constants;
import net.unethicalite.plugins.goonherbrun.utils.tasks.Task;

public class PickLimpwurt extends Task {
    @Inject private GoonHerbRunPlugin plugin;
    @Inject private GoonHerbRunConfig config;

    @Override
    public String getStatus() {
        return "Picking limpwurt roots";
    }

    @Override
    public boolean validate() {
        if (!config.limpwurt() || Inventory.isFull()) {
            return false;
        }

        TileObject patch = TileObjects.getNearest(Predicates.ids(Constants.FLOWER_PATCH_IDS));

        if (patch == null) {
            return false;
        }

        int varbitValue = Vars.getBit(plugin.getCurrentLocation().getFlowerVarbit());
        PatchState patchState = PatchImplementation.FLOWER.forVarbitValue(varbitValue);

        return patchState != null
                && patchState.getProduce() == Produce.LIMPWURT
                && patchState.getCropState() == CropState.HARVESTABLE;
    }

    @Override
    public void execute() {
        TileObject patch = TileObjects.getNearest(Predicates.ids(Constants.FLOWER_PATCH_IDS));
        if (patch == null) {
            return;
        }

        GameThread.invoke(() -> patch.interact("Pick"));

        Time.sleepTicksUntil(
                () -> Inventory.isFull() || Vars.getBit(plugin.getCurrentLocation().getFlowerVarbit()) <= 3,
                100);
    }
}