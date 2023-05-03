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
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.HotkeyListener;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.coords.RectangularArea;
import net.unethicalite.api.entities.NPCs;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.movement.Movement;
import net.unethicalite.api.widgets.Dialog;
import net.unethicalite.api.game.Combat;
import net.unethicalite.api.game.GameThread;
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
import java.util.LinkedList;
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
	private final LinkedList<BarrowsBrothers> killOrder = new LinkedList<>();
	@Getter
	private BarrowsBrothers currentBrother;
	@Getter private boolean newRun;

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

		newRun = true;
		updateGear();
		resetBarrows();
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
		if (customSleep > 0) { customSleep--; }

		if (newRun) { resetBarrows(); }

		cycleCurrentBrother();
		eatFood();
		drinkPrayer();
		handleBrotherSetups();
		walkAndDig();
		tombHandler();
	}

	@Subscribe
	public void onWidgetLoaded(WidgetLoaded event)
	{
		//print("Widget loaded " + event.getGroupId());
		if (event.getGroupId() == WidgetID.BARROWS_PUZZLE_GROUP_ID)
		{
			//print("Puzzle seen");
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
			//print("Trying to invoke");
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

	private void buildKillOrder()
	{
		killOrder.add(BarrowsBrothers.DHAROK);
		killOrder.add(BarrowsBrothers.AHRIM);
		killOrder.add(BarrowsBrothers.KARIL);
		killOrder.add(BarrowsBrothers.GUTHAN);
		killOrder.add(BarrowsBrothers.TORAG);
		killOrder.add(BarrowsBrothers.VERAC);
	}

	private void resetBarrows()
	{
		for (BarrowsBrothers bro : BarrowsBrothers.values())
		{
			bro.setInTunnel(false);
		}
		newRun = true;
		buildKillOrder();
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

	private void walkAndDig()
	{
		if (Constants.BARROWS_AREA.contains(client.getLocalPlayer()) && currentBrother != null)
		{
			RectangularArea digArea = new RectangularArea(currentBrother.getLocation(), 2, 2);

			if (digArea.contains(client.getLocalPlayer()))
			{
				Inventory.getFirst(ItemID.SPADE).interact(1);
			}
			Movement.walkTo(currentBrother.getLocation());
		}
	}

	private void tombHandler()
	{
		if (BarrowsBrothers.getBrotherCrypt() != null)
		{
			if (!BarrowsBrothers.getBrotherCrypt().isDead() && getVisibleBrother() == null)
			{
				if (!currentBrother.isInTunnel())
				{
					if (Dialog.isOpen())
					{
						BarrowsBrothers.getBrotherCrypt().setInTunnel(true);
					}
					else
					{
						TileObjects.getNearest("Sarcophagus").interact("Search");
					}
				}
				else if (onLastBrother())
				{
					if (Dialog.isOpen())
					{
						Dialog.invokeDialog(DialogOption.PLAIN_CONTINUE, DialogOption.CHAT_OPTION_ONE);
					}
					else
					{
						TileObjects.getNearest("Sarcophagus").interact("Search");
					}
				}
			}
			if (BarrowsBrothers.getBrotherCrypt().isDead() || (BarrowsBrothers.getBrotherCrypt().isInTunnel() && !onLastBrother()))
			{
				TileObjects.getNearest("Staircase").interact("Climb-up");
			}
		}
	}

	private void handleBrotherSetups()
	{
		if (getVisibleBrother() != null)
		{
			if (newRun) { newRun = false; }
			if (!Prayers.isEnabled(getVisibleBrother().getPrayer()) && Prayers.getPoints() > 0)
			{
				Prayers.toggle(getVisibleBrother().getPrayer());
			}
			if (getVisibleBrother().getSetup().getSetup().anyUnequipped())
			{
				getVisibleBrother().getSetup().getSetup().switchGear(50);
			}
			if (client.getLocalPlayer().isIdle())
			{
				Static.getClient().getHintArrowNpc().interact("Attack");
			}
		}
		else if (Prayers.anyActive())
		{
			Prayers.disableAll();
		}
	}

	private void cycleCurrentBrother()
	{
		if (killOrder.isEmpty())
		{
			if (currentBrother != null && currentBrother.isDead())
			{
				currentBrother = null;
			}

			if (currentBrother == null)
			{
				for (BarrowsBrothers bro : BarrowsBrothers.values())
				{
					if (bro.isInTunnel() && !bro.isDead())
					{
						currentBrother = bro;
						break;
					}
				}
			}
		} else if (currentBrother == null || currentBrother.isDead() || currentBrother.isInTunnel()) {
			if (newRun && currentBrother == null)
			{
				currentBrother = killOrder.poll();
			}
			else if (!newRun && currentBrother != null)
			{
				currentBrother = killOrder.poll();
			}
		}
	}

	public boolean onLastBrother()
	{
		int alive = 0;
		for (BarrowsBrothers bro : BarrowsBrothers.values())
		{
			if (!bro.isDead())
			{
				alive++;
			}
		}
		return alive == 1;
	}

	private void eatFood()
	{
		if (Combat.getMissingHealth() > 20)
		{
			if (Inventory.getFirst(ItemID.COOKED_KARAMBWAN) != null) {
				Inventory.getFirst(ItemID.COOKED_KARAMBWAN).interact("Eat");
			}
			else if (Combat.getCurrentHealth() < 40)
			{
				teleHome();
			}
		}
	}

	private void drinkPrayer()
	{
		if (Prayers.getPoints() < 10 &&
				(	getVisibleBrother() == BarrowsBrothers.KARIL
				||	getVisibleBrother() == BarrowsBrothers.AHRIM
				||	getVisibleBrother() == BarrowsBrothers.DHAROK))
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

	public BarrowsBrothers getVisibleBrother()
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
