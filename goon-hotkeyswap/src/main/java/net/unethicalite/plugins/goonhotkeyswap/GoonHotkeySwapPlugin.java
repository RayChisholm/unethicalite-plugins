package net.unethicalite.plugins.goonhotkeyswap;

import com.google.inject.Inject;
import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Item;
import net.runelite.api.events.ConfigButtonClicked;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.HotkeyListener;
import net.unethicalite.api.items.Equipment;
import net.unethicalite.api.widgets.Prayers;
import org.pf4j.Extension;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@PluginDescriptor(name = "Goon Hotkey Swap", enabledByDefault = false)
@Extension
@Slf4j
public class GoonHotkeySwapPlugin extends Plugin
{
	public Setups setups;

	@Inject
	private GoonHotkeySwapConfig config;

	@Inject
	private Client client;

	@Inject
	private KeyManager keyManager;

	@Inject
	private ConfigManager configManager;

	@Provides
	public GoonHotkeySwapConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(GoonHotkeySwapConfig.class);
	}

	private final HotkeyListener hotkeyListenerOne = new HotkeyListener(() -> config.hotKeyOne()) {
		@Override
		public void hotkeyPressed() {
			Setups.SETUP_ONE.getSetup().switchGear(50);
			togglePrayer(Setups.SETUP_ONE);
		}
	};
	private final HotkeyListener hotkeyListenerTwo = new HotkeyListener(() -> config.hotKeyTwo()) {
		@Override
		public void hotkeyPressed() {
			Setups.SETUP_TWO.getSetup().switchGear(50);
			togglePrayer(Setups.SETUP_TWO);
		}
	};
	private final HotkeyListener hotkeyListenerThree = new HotkeyListener(() -> config.hotKeyThree()) {
		@Override
		public void hotkeyPressed() {
			Setups.SETUP_THREE.getSetup().switchGear(50);
			togglePrayer(Setups.SETUP_THREE);
		}
	};

	@Override
	protected void startUp() throws Exception
	{
		keyManager.registerKeyListener(hotkeyListenerOne);
		keyManager.registerKeyListener(hotkeyListenerTwo);
		keyManager.registerKeyListener(hotkeyListenerThree);

		updateGear();


	}

	@Override
	protected void shutDown() throws Exception
	{
		keyManager.unregisterKeyListener(hotkeyListenerOne);
		keyManager.unregisterKeyListener(hotkeyListenerTwo);
		keyManager.unregisterKeyListener(hotkeyListenerThree);
	}

	@Subscribe
	private void onGameTick(GameTick e)
	{

	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (event.getGroup().equals(GoonHotkeySwapConfig.GROUP))
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

		Setups.setPrayerOne(config.prayerOne());
		Setups.setPrayerTwo(config.prayerTwo());
		Setups.setPrayerThree(config.prayerThree());
	}

	private void togglePrayer(Setups s)
	{
		if (!Prayers.isEnabled(s.getPrayer()) && config.usePrayers())
		{
			Prayers.toggle(s.getPrayer());
		}
	}

}
