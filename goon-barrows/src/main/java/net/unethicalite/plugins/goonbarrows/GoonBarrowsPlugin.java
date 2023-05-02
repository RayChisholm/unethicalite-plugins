package net.unethicalite.plugins.goonbarrows;

import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.openosrs.client.game.AttackStyle;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.ConfigButtonClicked;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.HotkeyListener;
import net.unethicalite.api.entities.NPCs;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.game.Combat;
import net.unethicalite.api.items.Equipment;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.items.Items;
import net.unethicalite.api.magic.SpellBook;
import net.unethicalite.api.widgets.Prayers;
import net.unethicalite.client.Static;
import net.unethicalite.plugins.goonbarrows.helpers.BarrowsBrothers;
import org.pf4j.Extension;

import net.unethicalite.plugins.goonbarrows.data.DefensivePrayer;
import net.unethicalite.plugins.goonbarrows.data.OffensivePrayer;
import net.unethicalite.plugins.goonbarrows.helpers.Setups;
import net.unethicalite.plugins.goonbarrows.helpers.GearSetup;
import net.unethicalite.plugins.goonbarrows.GoonBarrowsConfig;
import net.unethicalite.plugins.goonbarrows.data.Constants;
import net.unethicalite.plugins.goonbarrows.GoonBarrowsOverlay;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@PluginDescriptor(name = "Goon Barrows", enabledByDefault = false)
@Extension
@Slf4j
@Singleton
public class GoonBarrowsPlugin extends Plugin
{
	public Setups setups;
	public BarrowsBrothers bros;

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
		overlayManager.add(overlay);

		keyManager.registerKeyListener(hotkeyListenerOne);
		keyManager.registerKeyListener(hotkeyListenerTwo);
		keyManager.registerKeyListener(hotkeyListenerThree);

		updateGear();


	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(overlay);

		keyManager.unregisterKeyListener(hotkeyListenerOne);
		keyManager.unregisterKeyListener(hotkeyListenerTwo);
		keyManager.unregisterKeyListener(hotkeyListenerThree);
	}

	@Subscribe
	private void onGameTick(GameTick e)
	{
		if (client.getLocalPlayer().getPlane() == 0 && !Constants.TUNNEL_AREA.contains(client.getLocalPlayer()))
		{
			Prayers.disableAll();
		}

		eatFood();
		drinkPrayer();
		inTombGearSwitch();
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

	private void inTombGearSwitch()
	{
		if (BarrowsBrothers.AHRIM.getTomb().contains(client.getLocalPlayer()))
		{
			if (BarrowsBrothers.AHRIM.getSetup().getSetup().anyUnequipped()) {
				BarrowsBrothers.AHRIM.getSetup().getSetup().switchGear(50);
			}
			if (Static.getClient().hasHintArrow())
			{
				final NPC npc = Static.getClient().getHintArrowNpc();
				if (!Prayers.isEnabled(BarrowsBrothers.AHRIM.getPrayer()) && npc.distanceTo(client.getLocalPlayer()) < 4)
				{
					Prayers.toggle(BarrowsBrothers.AHRIM.getPrayer());
				}
				else
				{
					Prayers.disableAll();
				}
				if (client.getLocalPlayer().isIdle()) {
					if (BarrowsBrothers.AHRIM.getKilledVarbit() < 1) {
						npc.interact("Attack");
					}
					else {
						TileObjects.getNearest("Staircase").interact("Climb-up");
						Prayers.disableAll();
					}
				}
			}
		}
		else if (BarrowsBrothers.DHAROK.getTomb().contains(client.getLocalPlayer()))
		{
			if (BarrowsBrothers.DHAROK.getSetup().getSetup().anyUnequipped()) {
				BarrowsBrothers.DHAROK.getSetup().getSetup().switchGear(50);
			}
			if (Static.getClient().hasHintArrow())
			{
				final NPC npc = Static.getClient().getHintArrowNpc();
				if (!Prayers.isEnabled(BarrowsBrothers.DHAROK.getPrayer()) && npc.distanceTo(client.getLocalPlayer()) < 4)
				{
					Prayers.toggle(BarrowsBrothers.DHAROK.getPrayer());
				}
				else
				{
					Prayers.disableAll();
				}
				if (client.getLocalPlayer().isIdle()) {
					if (BarrowsBrothers.DHAROK.getKilledVarbit() <= 0) {
						npc.interact("Attack");
					} else {
						TileObjects.getNearest("Staircase").interact("Climb-up");
						Prayers.disableAll();
					}
				}
			}
		}
		else if (BarrowsBrothers.GUTHAN.getTomb().contains(client.getLocalPlayer()))
		{
			if (BarrowsBrothers.GUTHAN.getSetup().getSetup().anyUnequipped()) {
				BarrowsBrothers.GUTHAN.getSetup().getSetup().switchGear(50);
			}
			if (Static.getClient().hasHintArrow())
			{
				final NPC npc = Static.getClient().getHintArrowNpc();
				if (!Prayers.isEnabled(BarrowsBrothers.GUTHAN.getPrayer()) && npc.distanceTo(client.getLocalPlayer()) < 4)
				{
					Prayers.toggle(BarrowsBrothers.GUTHAN.getPrayer());
				}
				else
				{
					Prayers.disableAll();
				}
				if (client.getLocalPlayer().isIdle()) {
					if (BarrowsBrothers.GUTHAN.getKilledVarbit() <= 0) {
						npc.interact("Attack");
					} else {
						TileObjects.getNearest("Staircase").interact("Climb-up");
						Prayers.disableAll();
					}
				}
			}
		}
		else if (BarrowsBrothers.KARIL.getTomb().contains(client.getLocalPlayer()))
		{
			if (BarrowsBrothers.KARIL.getSetup().getSetup().anyUnequipped()) {
				BarrowsBrothers.KARIL.getSetup().getSetup().switchGear(50);
			}
			if (Static.getClient().hasHintArrow())
			{
				final NPC npc = Static.getClient().getHintArrowNpc();
				if (!Prayers.isEnabled(BarrowsBrothers.KARIL.getPrayer()) && npc.distanceTo(client.getLocalPlayer()) < 4)
				{
					Prayers.toggle(BarrowsBrothers.KARIL.getPrayer());
				}
				else
				{
					Prayers.disableAll();
				}
				if (client.getLocalPlayer().isIdle()) {
					if (BarrowsBrothers.KARIL.getKilledVarbit() <= 0) {
						npc.interact("Attack");
					} else {
						TileObjects.getNearest("Staircase").interact("Climb-up");
						Prayers.disableAll();
					}
				}
			}
		}
		else if (BarrowsBrothers.TORAG.getTomb().contains(client.getLocalPlayer()))
		{
			if (BarrowsBrothers.TORAG.getSetup().getSetup().anyUnequipped()) {
				BarrowsBrothers.TORAG.getSetup().getSetup().switchGear(50);
			}
			if (Static.getClient().hasHintArrow())
			{
				final NPC npc = Static.getClient().getHintArrowNpc();
				if (!Prayers.isEnabled(BarrowsBrothers.TORAG.getPrayer()) && npc.distanceTo(client.getLocalPlayer()) < 4)
				{
					Prayers.toggle(BarrowsBrothers.TORAG.getPrayer());
				}
				else
				{
					Prayers.disableAll();
				}
				if (client.getLocalPlayer().isIdle()) {
					if (BarrowsBrothers.TORAG.getKilledVarbit() <= 0) {
						npc.interact("Attack");
					} else {
						TileObjects.getNearest("Staircase").interact("Climb-up");
						Prayers.disableAll();
					}
				}
			}
		}
		else if (BarrowsBrothers.VERAC.getTomb().contains(client.getLocalPlayer()))
		{
			if (BarrowsBrothers.VERAC.getSetup().getSetup().anyUnequipped()) {
				BarrowsBrothers.VERAC.getSetup().getSetup().switchGear(50);
			}
			if (Static.getClient().hasHintArrow())
			{
				final NPC npc = Static.getClient().getHintArrowNpc();
				if (!Prayers.isEnabled(BarrowsBrothers.VERAC.getPrayer()) && npc.distanceTo(client.getLocalPlayer()) < 4)
				{
					Prayers.toggle(BarrowsBrothers.VERAC.getPrayer());
				}
				if (client.getLocalPlayer().isIdle()) {
					if (BarrowsBrothers.VERAC.getKilledVarbit() <= 0) {
						npc.interact("Attack");
					} else {
						TileObjects.getNearest("Staircase").interact("Climb-up");
						Prayers.disableAll();
					}
				}
			}
		}
	}

	private void eatFood()
	{
		if (Combat.getMissingHealth() > 20)
		{
			if (Inventory.getFirst(ItemID.COOKED_KARAMBWAN) != null) {
				Inventory.getFirst(ItemID.COOKED_KARAMBWAN).interact("Eat");
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

}
