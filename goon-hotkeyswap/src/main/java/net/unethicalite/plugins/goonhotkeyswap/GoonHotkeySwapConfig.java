package net.unethicalite.plugins.goonhotkeyswap;

import net.runelite.api.Prayer;
import net.runelite.client.config.*;

@ConfigGroup(GoonHotkeySwapConfig.GROUP)
public interface GoonHotkeySwapConfig extends Config
{
	String GROUP = "goonhotkeyswap";

	@ConfigSection(
			name = "Gear Setup One",
			description = "Arguments for Gear Set One",
			position = 0
	)
	String sectionOne = "Gear Set One";
	@ConfigSection(
			name = "Gear Setup Two",
			description = "Arguments for Gear Set Two",
			position = 4
	)
	String sectionTwo = "Gear Setup Two";
	@ConfigSection(
			name = "Gear Setup Three",
			description = "Arguments for Gear Set Three",
			position = 8
	)
	String sectionThree = "Gear Setup Three";

	@ConfigItem(
			keyName = "gearsetone",
			name = "Gear Setup 1",
			description = "Gear Names",
			position = 1,
			section = sectionOne
	)
	default String gearOne()
	{
		return "Gear";
	}

	@ConfigItem(
			keyName = "prayerOne",
			name = "Prayer One",
			description = "Prayer to activate",
			position = 2,
			section = sectionOne
	)
	default Prayer prayerOne() { return Prayer.RIGOUR; }

	@ConfigItem(
			keyName = "hotkeyOne",
			name = "Hotkey One",
			description = "Hotkey for gear swap.",
			position = 3,
			section = sectionOne
	)
	default Keybind hotKeyOne() { return Keybind.NOT_SET; }

	@ConfigItem(
			keyName = "gearsettwo",
			name = "Gear Setup 2",
			description = "Gear Names",
			position = 4,
			section = sectionTwo
	)
	default String gearTwo()
	{
		return "Gear";
	}

	@ConfigItem(
			keyName = "prayerTwo",
			name = "Prayer Two",
			description = "Prayer to activate",
			position = 5,
			section = sectionTwo
	)
	default Prayer prayerTwo() { return Prayer.RIGOUR; }

	@ConfigItem(
			keyName = "hotkeyTwo",
			name = "Hotkey Two",
			description = "Hotkey for gear swap.",
			position = 6,
			section = sectionTwo
	)
	default Keybind hotKeyTwo() { return Keybind.NOT_SET; }

	@ConfigItem(
			keyName = "gearsetthree",
			name = "Gear Setup 1",
			description = "Gear Names",
			position = 7,
			section = sectionThree
	)
	default String gearThree()
	{
		return "Gear";
	}

	@ConfigItem(
			keyName = "prayerThree",
			name = "Prayer Three",
			description = "Prayer to activate",
			position = 8,
			section = sectionThree
	)
	default Prayer prayerThree() { return Prayer.RIGOUR; }

	@ConfigItem(
			keyName = "hotkeyThree",
			name = "Hotkey Three",
			description = "Hotkey for gear swap.",
			position = 9,
			section = sectionThree
	)
	default Keybind hotKeyThree() { return Keybind.NOT_SET; }

	@ConfigItem(
			keyName = "grabGear",
			name = "Grab Gear",
			description = "Button to grab gear",
			position = 10,
			clazz = GoonHotkeySwapPlugin.class
	)
	default Button grabGear() { return new Button(); }

}
