package src.main.java.net.unethicalite.plugins.goonsandminer;

import src.main.java.net.unethicalite.plugins.goonsandminer.utils.TickScript;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.ConfigButtonClicked;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetID;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemManager;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.HotkeyListener;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.coords.RectangularArea;
import net.unethicalite.api.entities.NPCs;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.game.Combat;
import net.unethicalite.api.game.GameThread;
import net.unethicalite.api.game.Skills;
import net.unethicalite.api.game.Vars;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Equipment;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.magic.SpellBook;
import net.unethicalite.api.movement.Movement;
import net.unethicalite.api.movement.Reachable;
import net.unethicalite.api.widgets.Dialog;
import net.unethicalite.api.widgets.Prayers;
import net.unethicalite.api.widgets.Widgets;
import net.unethicalite.client.Static;
import org.pf4j.Extension;
import net.unethicalite.plugins.goonsandminer.tasks.*

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.List;
import java.util.Queue;
import java.util.*;
import java.util.stream.Collectors;

@PluginDescriptor(name = "Goon Sand Miner", description = "Mines sand", enabledByDefault = false)
@Slf4j
@Extension
public class GoonSandMinerPlugin extends TickScript {
    @Inject private GoonSandMinerConfig config;
    @Inject private GoonSandMinerOverlay overlay;
    @Inject private Client client;
    @Inject private OverlayManager overlayManager;

    @Provides
    public GoonSandMinerConfig getConfig(ConfigManager configManager) { return configManager.getConfig(GoonSandMinerConfig.class); }

    @Override
    public Logger getLogger() { return log; }

    @Override
    protected void onStart() {
        super.onStart();

        reset();

        overlayManager.add(overlay);

        addTask(MineSand.class);
        //addTask(task.class)
    }
    @Override
    protected void onStop() {
        super.onStop();
        overlayManager.remove(overlay);
    }

    private void reset() {

    }

}
