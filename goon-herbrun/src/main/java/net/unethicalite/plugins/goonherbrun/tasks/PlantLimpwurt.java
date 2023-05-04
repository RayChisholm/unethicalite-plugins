package net.unethicalite.plugins.goonherbrun.tasks;

import javax.inject.Inject;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
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

public class PlantLimpwurt extends Task {
    @Inject private GoonHerbRunPlugin plugin;
    @Inject private GoonHerbRunConfig config;

    @Override
    public String getStatus() {
        return "Planting limpwurt seeds";
    }

    @Override
    public boolean validate() {
        if (!config.limpwurt() || !Inventory.contains(ItemID.LIMPWURT_SEED)) {
            return false;
        }

        final TileObject patch = TileObjects.getNearest(Predicates.ids(Constants.FLOWER_PATCH_IDS));

        if (patch == null) {
            return false;
        }

        final int varbitValue = Vars.getBit(plugin.getCurrentLocation().getFlowerVarbit());
        final PatchState patchState = PatchImplementation.FLOWER.forVarbitValue(varbitValue);

        return patchState != null
                && patchState.getProduce() == Produce.WEEDS
                && patchState.getCropState() == CropState.GROWING;
    }

    @Override
    public void execute() {
        final TileObject patch = TileObjects.getNearest(Predicates.ids(Constants.FLOWER_PATCH_IDS));
        if (patch == null) {
            return;
        }

        final Item seed = Inventory.getFirst(ItemID.LIMPWURT_SEED);
        if (seed == null) {
            return;
        }

        GameThread.invoke(() -> seed.useOn(patch));

        if (!Time.sleepTicksUntil(
                () -> Vars.getBit(plugin.getCurrentLocation().getFlowerVarbit()) > 3, 20)) {
            return;
        }

        final Item compost = Inventory.getFirst(Predicates.ids(Constants.COMPOST_IDS));
        if (compost == null) {
            return;
        }

        GameThread.invoke(() -> compost.useOn(patch));
        Time.sleepTicks(3);
    }
}