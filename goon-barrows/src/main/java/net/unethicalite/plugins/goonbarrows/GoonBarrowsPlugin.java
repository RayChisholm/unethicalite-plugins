package net.unethicalite.plugins.goonbarrows;

import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.*;
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
import net.runelite.client.input.KeyManager;
import net.runelite.client.menus.WidgetMenuOption;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.HotkeyListener;
import net.runelite.client.util.QuantityFormatter;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.NPCs;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.game.Combat;
import net.unethicalite.api.game.GameThread;
import net.unethicalite.api.input.Mouse;
import net.unethicalite.api.items.Equipment;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.magic.SpellBook;
import net.unethicalite.api.widgets.Prayers;
import net.unethicalite.api.widgets.Widgets;
import net.unethicalite.client.Static;
import net.unethicalite.plugins.goonbarrows.data.Constants;
import net.unethicalite.plugins.goonbarrows.helpers.BarrowsBrothers;
import net.unethicalite.plugins.goonbarrows.helpers.GearSetup;
import net.unethicalite.plugins.goonbarrows.helpers.Setups;
import org.pf4j.Extension;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@PluginDescriptor(name = "Goon Barrows", enabledByDefault = false)
@Extension
@Slf4j
@Singleton
public class GoonBarrowsPlugin extends Plugin
{
	public Setups setups;
	@Getter
	public BarrowsBrothers bros;
	@Getter
	private Widget puzzleAnswer;
	public int customSleep;

	@Inject
	private ChatMessageManager chatMessageManager;

	@Inject
	private net.unethicalite.plugins.goonbarrows.GoonBarrowsConfig config;

	@Inject
	private Client client;

	@Inject
	private KeyManager keyManager;

	@Inject
	private ConfigManager configManager;

	@Inject
	private OverlayManager overlayManager;
	@Inject
	private GoonBarrowsOverlay overlay;

	@Provides
	public net.unethicalite.plugins.goonbarrows.GoonBarrowsConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(net.unethicalite.plugins.goonbarrows.GoonBarrowsConfig.class);
	}

	private final HotkeyListener hotkeyListenerOne = new HotkeyListener(() -> config.hotKeyOne()) {
		@Override
		public void hotkeyPressed() {
			Setups.SETUP_ONE.getSetup().switchGear(50);
			if (config.useOffensePrayerOne())
			{
				Setups.SETUP_ONE.toggleOffensivePrayer();
			}
			if (config.useDefensePrayerOne())
			{
				Setups.SETUP_ONE.toggleDefensivePrayer();
			}
		}
	};
	private final HotkeyListener hotkeyListenerTwo = new HotkeyListener(() -> config.hotKeyTwo()) {
		@Override
		public void hotkeyPressed() {
			Setups.SETUP_TWO.getSetup().switchGear(50);
			if (config.useOffensePrayerTwo())
			{
				Setups.SETUP_TWO.toggleOffensivePrayer();
			}
			if (config.useDefensePrayerTwo())
			{
				Setups.SETUP_TWO.toggleDefensivePrayer();
			}
		}
	};
	private final HotkeyListener hotkeyListenerThree = new HotkeyListener(() -> config.hotKeyThree()) {
		@Override
		public void hotkeyPressed() {
			Setups.SETUP_THREE.getSetup().switchGear(50);
			if (config.useOffensePrayerThree())
			{
				Setups.SETUP_THREE.toggleOffensivePrayer();
			}
			if (config.useDefensePrayerThree())
			{
				Setups.SETUP_THREE.toggleDefensivePrayer();
			}
		}
	};


	@Override
	protected void startUp() throws Exception
	{
		customSleep = 0;
		overlayManager.add(overlay);

		keyManager.registerKeyListener(hotkeyListenerOne);
		keyManager.registerKeyListener(hotkeyListenerTwo);
		keyManager.registerKeyListener(hotkeyListenerThree);

		updateGear();
		bros = getBrothers();

	}

	@Override
	protected void shutDown() throws Exception
	{
		puzzleAnswer = null;
		overlayManager.remove(overlay);

		keyManager.unregisterKeyListener(hotkeyListenerOne);
		keyManager.unregisterKeyListener(hotkeyListenerTwo);
		keyManager.unregisterKeyListener(hotkeyListenerThree);
	}

	@Subscribe
	private void onGameTick(GameTick e)
	{
		if (customSleep > 0)
		{
			customSleep--;
		}
		if (client.getLocalPlayer().getAnimation() == Constants.DIG_ANIM)
		{
			customSleep += 3;
		}
		if (client.getLocalPlayer().getPlane() == 0 && !Constants.TUNNEL_AREA.contains(client.getLocalPlayer()))
		{
			Prayers.disableAll();
		}
		bros = getBrothers();
		eatFood();
		drinkPrayer();
		//inTombGearSwitch();
		tombHandler();
	}

	@Subscribe
	public void onWidgetLoaded(WidgetLoaded event)
	{
		print("Widget loaded " + event.getGroupId());
		if (event.getGroupId() == WidgetID.BARROWS_PUZZLE_GROUP_ID)
		{
			print("Puzzle seen");
			final int answer = client.getWidget(WidgetInfo.BARROWS_FIRST_PUZZLE).getModelId() - 3;
			puzzleAnswer = null;

			for (Map.Entry<WidgetInfo, WidgetInfo> entry : Constants.POSSIBLE_SOLUTIONS.entrySet())
			{
				Widget widgetToCheck = Widgets.get(entry.getKey());
				if (widgetToCheck != null && widgetToCheck.getModelId() == answer)
				{
					puzzleAnswer = Widgets.get(entry.getValue());
				}
			}
			print("Trying to invoke");
			GameThread.invoke(() -> puzzleAnswer.interact("Select"));
			Time.sleepTick();
		}
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (event.getGroup().equals(GoonBarrowsConfig.GROUP))
		{
			updateGear();
		}
	}

	@Subscribe
	public void onConfigButtonPressed(ConfigButtonClicked event)
	{
		String myString = "";

		for (Item i : Equipment.getAll())
		{
			myString = myString + i.getName() + ",";
			log.debug(i.getName());
		}
		myString = myString.substring(0,myString.length()-1);

		StringSelection stringSelection = new StringSelection(myString);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, null);
	}

	private void updateGear()
	{
		List<String> gearSetupOne = Arrays.stream(config.gearOne().split(","))
				.collect(Collectors.toList());
		List<String> gearSetupTwo = Arrays.stream(config.gearTwo().split(","))
				.collect(Collectors.toList());
		List<String> gearSetupThree = Arrays.stream(config.gearThree().split(","))
				.collect(Collectors.toList());

		Setups.setSetupOne(new GearSetup(gearSetupOne));
		Setups.setSetupTwo(new GearSetup(gearSetupTwo));
		Setups.setSetupThree(new GearSetup(gearSetupThree));

		Setups.setOffensivePrayerOne(config.offensivePrayerOne());
		Setups.setDefensivePrayerOne(config.defensivePrayerOne());

		Setups.setOffensivePrayerTwo(config.offensivePrayerTwo());
		Setups.setDefensivePrayerTwo(config.defensivePrayerTwo());

		Setups.setOffensivePrayerThree(config.offensivePrayerThree());
		Setups.setDefensivePrayerThree(config.defensivePrayerThree());
	}

	private void handleOneTomb(BarrowsBrothers b)
	{
		if (b.getTomb().contains(client.getLocalPlayer())) {
			if (b.getSetup().getSetup().anyUnequipped()) {
				b.getSetup().getSetup().switchGear(50);
				print("Gear switch " + b.getName());
			}

			if (Static.getClient().hasHintArrow()) {

				final NPC npc = Static.getClient().getHintArrowNpc();

				if (!Prayers.isEnabled(b.getPrayer()) && npc.distanceTo(client.getLocalPlayer()) < 4 && Prayers.getPoints() > 1) {
					Prayers.toggle(b.getPrayer());
					print("Enable prayer " + b.getName());
				}
				if (client.getLocalPlayer().isIdle()) {
					if (client.getVarbitValue(b.getKilledVarbit()) == 0) {
						if (NPCs.getNearest(b.getId()).distanceTo(client.getLocalPlayer()) < 4) {
							print("Attack " + b.getName());
							npc.interact("Attack");
						}
					} else if (client.getVarbitValue(b.getKilledVarbit()) > 0) {
						TileObjects.getNearest("Staircase").interact("Climb-up");
						Prayers.disableAll();
					}
				}
			}
		}
	}

	private void tombHandler()
	{
		if (customSleep > 0)
		{
			TileObjects.getNearest("Sarcophagus").interact("Search");
		}
		else
		{
			handleOneTomb(BarrowsBrothers.KARIL);
			handleOneTomb(BarrowsBrothers.AHRIM);
			handleOneTomb(BarrowsBrothers.DHAROK);
			handleOneTomb(BarrowsBrothers.GUTHAN);
			handleOneTomb(BarrowsBrothers.TORAG);
			handleOneTomb(BarrowsBrothers.VERAC);
		}
	}

	private void eatFood()
	{
		if (Combat.getMissingHealth() > 20)
		{
			if (Inventory.getFirst(ItemID.COOKED_KARAMBWAN) != null) {
				Inventory.getFirst(ItemID.COOKED_KARAMBWAN).interact("Eat");
			}
			else
			{
				teleHome();
			}
		}
	}

	private void drinkPrayer()
	{
		if (Prayers.getPoints() < 10 &&
				(BarrowsBrothers.KARIL.getTomb().contains(client.getLocalPlayer())
				|| BarrowsBrothers.AHRIM.getTomb().contains(client.getLocalPlayer())
				|| BarrowsBrothers.DHAROK.getTomb().contains(client.getLocalPlayer())
				|| (Constants.TUNNEL_AREA.contains(client.getLocalPlayer()) && Static.getClient().getHintArrowNpc().distanceTo(client.getLocalPlayer()) < 4)))
		{
			if (Inventory.getFirst(ItemID.PRAYER_POTION1) != null)
			{
				Inventory.getFirst(ItemID.PRAYER_POTION1).interact("Drink");
			}
			else if (Inventory.getFirst(ItemID.PRAYER_POTION2) != null)
			{
				Inventory.getFirst(ItemID.PRAYER_POTION2).interact("Drink");
			}
			else if (Inventory.getFirst(ItemID.PRAYER_POTION3) != null)
			{
				Inventory.getFirst(ItemID.PRAYER_POTION3).interact("Drink");
			}
			else if (Inventory.getFirst(ItemID.PRAYER_POTION4) != null)
			{
				Inventory.getFirst(ItemID.PRAYER_POTION4).interact("Drink");
			}
			else
			{
				if (Combat.getCurrentHealth() < 30)
				{
					teleHome();
				}
			}
		}
	}

	private void teleHome()
	{
		SpellBook.Standard.TELEPORT_TO_HOUSE.cast();
	}

	private BarrowsBrothers getBrothers()
	{
		if (Static.getClient().hasHintArrow())
		{
			NPC npc = Static.getClient().getHintArrowNpc();
			if (npc != null)
			{
				int npc_id = npc.getId();

				if (npc_id == BarrowsBrothers.AHRIM.getId())
				{
					return BarrowsBrothers.AHRIM;
				}
				else if (npc_id == BarrowsBrothers.KARIL.getId())
				{
					return BarrowsBrothers.KARIL;
				}
				else if (npc_id == BarrowsBrothers.GUTHAN.getId())
				{
					return BarrowsBrothers.GUTHAN;
				}
				else if (npc_id == BarrowsBrothers.DHAROK.getId())
				{
					return BarrowsBrothers.DHAROK;
				}
				else if (npc_id == BarrowsBrothers.TORAG.getId())
				{
					return BarrowsBrothers.TORAG;
				}
				else if (npc_id == BarrowsBrothers.VERAC.getId())
				{
					return BarrowsBrothers.VERAC;
				}
			}
		}
		return null;
	}

	private void print(String msg)
	{
		final ChatMessageBuilder message = new ChatMessageBuilder()
				.append(ChatColorType.HIGHLIGHT)
				.append(msg)
				.append(ChatColorType.NORMAL);
		chatMessageManager.queue(QueuedMessage.builder()
						.type(ChatMessageType.ITEM_EXAMINE)
						.runeLiteFormattedMessage(message.build())
				.build());

	}

}
