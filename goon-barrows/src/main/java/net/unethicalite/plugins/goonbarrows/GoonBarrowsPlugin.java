package net.unethicalite.plugins.goonbarrows;

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
import net.unethicalite.plugins.goonbarrows.data.Constants;
import net.unethicalite.plugins.goonbarrows.data.Room;
import net.unethicalite.plugins.goonbarrows.helpers.BarrowsBrothers;
import net.unethicalite.plugins.goonbarrows.helpers.GearSetup;
import net.unethicalite.plugins.goonbarrows.helpers.Interact;
import net.unethicalite.plugins.goonbarrows.helpers.Setups;
import org.pf4j.Extension;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.List;
import java.util.Queue;
import java.util.*;
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
	private boolean finished;
	public int CHESTS_CHECKED;
	public long chestPrice = 0;

	@Getter
	private BarrowsBrothers currentBrother;
	@Getter private boolean newRun;
	@Getter @Setter private Queue<Room> tunnelPath;
	private Room navigatingFrom;
	private Room navigatingTo;
	private boolean stuck;

	@Inject
	private ChatMessageManager chatMessageManager;
	@Inject
	private ItemManager itemManager;

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

		CHESTS_CHECKED = 0;
		newRun = true;
		finished = false;
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
		tunnelGearHandler();
		traverseTunnel();
		handleTreasureRoom();
		feroxHandler();
		useBarrowsPortal();
	}

	@Subscribe
	public void onWidgetLoaded(WidgetLoaded event)
	{
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

		if(event.getGroupId() == WidgetID.BARROWS_REWARD_GROUP_ID)
		{
			CHESTS_CHECKED++;
			ItemContainer barrowsRewardContainer = client.getItemContainer(InventoryID.BARROWS_REWARD);
			if (barrowsRewardContainer == null)
			{
				return;
			}

			Item[] items = barrowsRewardContainer.getItems();

			for (Item item : items)
			{
				long itemStack = (long) itemManager.getItemPrice(item.getId()) * (long) item.getQuantity();
				chestPrice += itemStack;
			}
			feroxTele();
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
		tunnelPath = null;
		newRun = true;
		finished = false;
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
		List<String> gearSetupFour = Arrays.stream(config.gearThree().split(","))
				.collect(Collectors.toList());


		Setups.setSetupOne(new GearSetup(gearSetupOne));
		Setups.setSetupTwo(new GearSetup(gearSetupTwo));
		Setups.setSetupThree(new GearSetup(gearSetupThree));
		Setups.setSetupFour(new GearSetup(gearSetupFour));

		Setups.setOffensivePrayerOne(config.offensivePrayerOne());
		Setups.setDefensivePrayerOne(config.defensivePrayerOne());

		Setups.setOffensivePrayerTwo(config.offensivePrayerTwo());
		Setups.setDefensivePrayerTwo(config.defensivePrayerTwo());

		Setups.setOffensivePrayerThree(config.offensivePrayerThree());
		Setups.setDefensivePrayerThree(config.defensivePrayerThree());

		Setups.setOffensivePrayerFour(config.offensivePrayerFour());
		Setups.setDefensivePrayerFour(config.defensivePrayerFour());
	}

	private void tunnelGearHandler()
	{
		if (Constants.TUNNEL_AREA.contains(client.getLocalPlayer()) && getVisibleBrother() == null)
		{
			if (Setups.SETUP_FOUR.getSetup().anyUnequipped())
			{
				Setups.SETUP_FOUR.getSetup().switchGear(50);
			}
		}
	}

	private void handleTreasureRoom()
	{
		if (Room.getCurrentRoom() == Room.C && !Room.isInCorridor())
		{
			if (getPotentialWithLastBrother() >= 756) {
				if (TileObjects.getNearest("Chest").hasAction("Open")) {
					GameThread.invoke(() -> TileObjects.getNearest("Chest").interact("Open"));
				}
				if (TileObjects.getNearest("Chest").hasAction("Search") && !onLastBrother()) {
					if (Widgets.isVisible(Widgets.get(WidgetID.BARROWS_REWARD_GROUP_ID, 0))) {
						finished = true;
					} else {
						GameThread.invoke(() -> TileObjects.getNearest("Chest").interact("Search"));
					}
				}
			}
			else
			{
				fightForPotential();
			}

		}
	}

	private void fightForPotential()
	{
		NPC target;
		target = NPCs.getNearest(n -> n.hasAction("Attack") && Reachable.isInteractable(n));
		NPC finalTarget = target;
		GameThread.invoke(() -> finalTarget.interact("Attack"));
		target = NPCs.getNearest(n -> n.getInteracting() != null && n.getInteracting().equals(Players.getLocal()));
		if (target == null || client.getLocalPlayer().isIdle())
		{
			print("Nothing to attack here");
			TileObject door;
			door = TileObjects.getNearest(o -> o.hasAction("Open") && Reachable.isInteractable(o));
			door.interact("Open");
		}
	}

	private void traverseTunnel()
	{
		if (Constants.TUNNEL_AREA.contains(client.getLocalPlayer()) && getVisibleBrother() == null)
		{
			Room current = Room.getCurrentRoom();

			if (current == Room.C){	return; }

			if (tunnelPath == null || tunnelPath.isEmpty())
			{
				tunnelPath = Room.findPath();

				navigatingTo = null;
				navigatingFrom = null;

				if (tunnelPath.isEmpty()) { return; }
			}

			if (navigatingFrom == null || navigatingTo == current)
			{
				navigatingFrom = tunnelPath.poll();
				navigatingTo = tunnelPath.peek();
			}

			TileObject door = getDoor();
			if (door == null) { return; }

			if (stuck)
			{
				door = TileObjects.getNearest(Room.DOOR_PREDICATE);
				stuck = false;
			}

			final TileObject finalDoor = door;

			GameThread.invoke(() -> finalDoor.interact("Open"));

		}
	}

	public TileObject getDoor() {
		Room roomOne;
		Room roomTwo;

		if (Room.isInCorridor()) {
			roomOne = navigatingTo;
			roomTwo = navigatingFrom;
		} else {
			roomOne = navigatingFrom;
			roomTwo = navigatingTo;
		}

		if (roomOne.getNorth() == roomTwo) {
			return roomOne.getNorthDoor();
		} else if (roomOne.getEast() == roomTwo) {
			return roomOne.getEastDoor();
		} else if (roomOne.getSouth() == roomTwo) {
			return roomOne.getSouthDoor();
		} else if (roomOne.getWest() == roomTwo) {
			return roomOne.getWestDoor();
		}

		return null;
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
				if (getVisibleBrother() == BarrowsBrothers.KARIL)
				{
					Prayers.toggle(Prayer.PIETY);
				}
				else if (getVisibleBrother() == BarrowsBrothers.AHRIM)
				{
					Prayers.toggle(Prayer.EAGLE_EYE);
				}

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
		} else if (currentBrother == null || currentBrother.isDead() || (currentBrother.isInTunnel() && !onLastBrother())) {
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
				feroxTele();
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
					feroxTele();
				}
			}
		}
	}

	private void teleHome()
	{
		SpellBook.Standard.TELEPORT_TO_HOUSE.cast();
	}

	private void feroxTele()
	{
		Interact.interactWithInventoryOrEquipment(Constants.DUELING_RING_IDS, "Rub", "Ferox Enclave", 3);
	}

	private void feroxHandler()
	{
		if (Constants.FEROX_ENCLAVE.contains(client.getLocalPlayer()))
		{
			finished = false;
			newRun = true;
			if (Combat.getMissingHealth() > 0 || Prayers.getPoints() < Skills.getLevel(Skill.PRAYER))
			{
				if (client.getLocalPlayer().isIdle()) {
					TileObjects.getNearest(ObjectID.POOL_OF_REFRESHMENT).interact("Drink");
				}
			}

			else if (!Inventory.contains((i) -> Constants.PRAYER_RESTORE_POTION_IDS.contains(i.getId()))
					|| (!Inventory.contains(Predicates.ids(Constants.DUELING_RING_IDS))
					&& !Equipment.contains(Predicates.ids(Constants.DUELING_RING_IDS)))
					|| Inventory.getCount(ItemID.COOKED_KARAMBWAN) < 9
					|| Inventory.contains(i -> i.getName().contains("Clue scroll"))
					|| Inventory.contains(Predicates.ids(Constants.BARROWS_UNDEGRADED_IDS))
					|| Inventory.contains(Predicates.ids(Constants.BARROWS_BASIC_LOOT_IDS)))
			{

				if (!Bank.isOpen() && client.getLocalPlayer().isIdle()) {
					TileObjects.getNearest("Bank chest").interact("Use");
				}

				if (Bank.isOpen())
				{
					Inventory.getAll(Predicates.ids(Constants.BARROWS_UNDEGRADED_IDS))
							.forEach(i -> Bank.depositAll(i.getId()));

					Inventory.getAll(i -> i.getName().contains("Clue scroll"))
							.forEach(i -> Bank.depositAll(i.getId()));

					Inventory.getAll(Predicates.ids(Constants.BARROWS_BASIC_LOOT_IDS))
							.forEach(i -> Bank.depositAll(i.getId()));

					if (!Inventory.contains((i) -> Constants.PRAYER_RESTORE_POTION_IDS.contains(i.getId()))) {
						if (Bank.contains(ItemID.PRAYER_POTION4)) {
							Bank.withdraw(ItemID.PRAYER_POTION4, 1, Bank.WithdrawMode.ITEM);
						} else if (Bank.contains(ItemID.PRAYER_POTION3)) {
							Bank.withdraw(ItemID.PRAYER_POTION3, 1, Bank.WithdrawMode.ITEM);
						} else if (Bank.contains(ItemID.SUPER_RESTORE4)) {
							Bank.withdraw(ItemID.SUPER_RESTORE4, 1, Bank.WithdrawMode.ITEM);
						} else if (Bank.contains(ItemID.SUPER_RESTORE3)) {
							Bank.withdraw(ItemID.SUPER_RESTORE3, 1, Bank.WithdrawMode.ITEM);
						} else {
							print("No more 3 or 4 dose prayer or super restore potions.");
						}
					}

					int foodQuantity = 10 - Inventory.getCount(ItemID.COOKED_KARAMBWAN);
					if (foodQuantity > 0) {
						if (Bank.getCount(true, ItemID.COOKED_KARAMBWAN) < foodQuantity) {
							print("Out of food. Stopping plugin.");
						}
						Bank.withdraw(ItemID.COOKED_KARAMBWAN, 1, Bank.WithdrawMode.ITEM);
					}

					if (!Inventory.contains(Predicates.ids(Constants.DUELING_RING_IDS))
							&& !Equipment.contains(Predicates.ids(Constants.DUELING_RING_IDS))) {
						if (!Bank.contains(Predicates.ids(Constants.DUELING_RING_IDS))) {
							print("Out of rings of dueling. Stopping plugin.");
						}

						Bank.withdraw(Predicates.ids(Constants.DUELING_RING_IDS), 1, Bank.WithdrawMode.ITEM);

						if (Equipment.fromSlot(EquipmentInventorySlot.RING) == null) {
								Inventory.getFirst(Predicates.ids(Constants.DUELING_RING_IDS)).interact("Wear");
						}
					}
				}
			}
			else
			{
				Bank.close();
				teleHome();
			}
		}
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

	private void useBarrowsPortal()
	{
		if (Static.getClient().isInInstancedRegion())
		{
			if (!Movement.isRunEnabled())
			{
				Movement.toggleRun();
			}
			if (Equipment.fromSlot(EquipmentInventorySlot.RING) == null) {
				Inventory.getFirst(Predicates.ids(Constants.DUELING_RING_IDS)).interact("Wear");
			}
			newRun = true;
			finished = false;
			TileObjects.getNearest(37591).interact("Enter");
		}
	}

	private int getRewardPotential()
	{
		int potential = 0;

		for (BarrowsBrothers bro : BarrowsBrothers.values())
		{
			potential += Vars.getBit(bro.getKilledVarbit());
		}

		return potential * 2 + Vars.getBit(Varbits.BARROWS_REWARD_POTENTIAL);
	}

	public int getPotentialWithLastBrother()
	{
		int potential = getRewardPotential();

		for (BarrowsBrothers bro : BarrowsBrothers.values())
		{
			if (!bro.isDead())
			{
				switch (currentBrother)
				{
					case KARIL:
					case AHRIM:
						return getRewardPotential() + 100;
					default:
						return getRewardPotential() + 117;
				}
			}
		}
		return potential;
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
