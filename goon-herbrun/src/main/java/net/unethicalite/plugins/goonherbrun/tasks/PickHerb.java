package net.unethicalite.plugins.goonherbrun.tasks;

import javax.inject.Inject;
import net.runelite.api.Skill;
import net.runelite.api.TileObject;
import net.runelite.api.events.StatChanged;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.timetracking.farming.CropState;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.game.GameThread;
import net.unethicalite.api.game.Vars;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.plugins.goonherbrun.GoonHerbRunPlugin;
import net.unethicalite.plugins.goonherbrun.PatchImplementation;
import net.unethicalite.plugins.goonherbrun.PatchState;
import net.unethicalite.plugins.goonherbrun.utils.tasks.*;
import net.unethicalite.plugins.goonherbrun.utils.Constants;

public class PickHerb extends Task {
    @Inject private GoonHerbRunPlugin plugin;

    private boolean experienceReceived;

    @Override
    public String getStatus() {
        return "Picking herbs";
    }

    @Override
    public boolean validate() {
        if (Inventory.isFull()) {
            return false;
        }

        TileObject patch = TileObjects.getNearest(Predicates.ids(Constants.HERB_PATCH_IDS));

        if (patch == null) {
            return false;
        }

        int varbitValue = Vars.getBit(plugin.getCurrentLocation().getHerbVarbit());
        PatchState patchState = PatchImplementation.HERB.forVarbitValue(varbitValue);

        return patchState != null && patchState.getCropState() == CropState.HARVESTABLE;
    }

    @Override
    public void execute() {
        TileObject patch = TileObjects.getNearest(Predicates.ids(Constants.HERB_PATCH_IDS));
        if (patch == null) {
            return;
        }

        experienceReceived = false;
        GameThread.invoke(() -> patch.interact("Pick"));
        Time.sleepTicksUntil(() -> experienceReceived, 20);

        GameThread.invoke(() -> patch.interact("Pick"));
        Time.sleepTick();
        GameThread.invoke(() -> patch.interact("Pick"));

        Time.sleepTicksUntil(
                () -> Inventory.isFull() || Vars.getBit(plugin.getCurrentLocation().getHerbVarbit()) <= 3,
                100);
    }

    @Subscribe
    private void onStatChanged(StatChanged event) {
        if (plugin.isRunning() && event.getSkill() == Skill.FARMING) {
            experienceReceived = true;
        }
    }
}