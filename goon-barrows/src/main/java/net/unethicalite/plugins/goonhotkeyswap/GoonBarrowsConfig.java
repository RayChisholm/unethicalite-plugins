package net.unethicalite.plugins.goonbarrows;

import net.runelite.api.Prayer;
import net.runelite.client.config.*;

@ConfigGroup(GoonBarrowsConfig.GROUP)
public interface GoonBarrowsConfig extends Config
{
	String GROUP = "goonbarrows";

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
			keyName = "offensePrayOne",
			name = "Enable Offensive Prayers",
			description = "Will Toggle Prayer On Switch",
			position = 2,
			section = sectionOne
	)
	default boolean useOffensePrayerOne() { return false; }

	@ConfigItem(
			keyName = "defensePrayOne",
			name = "Enable Defensive Prayers",
			description = "Will Toggle Prayer On Switch",
			position = 3,
			section = sectionOne
	)
	default boolean useDefensePrayerOne() { return false; }

	@ConfigItem(
			keyName = "offensePrayerOne",
			name = "Offensive Prayer",
			description = "Offensive Prayer to activate",
			position = 4,
			section = sectionOne
	)
	default OffensivePrayer offensivePrayerOne() { return OffensivePrayer.PIETY; }

	@ConfigItem(
			keyName = "defensePrayerOne",
			name = "Defensive Prayer",
			description = "Defensive Prayer to activate",
			position = 5,
			section = sectionOne
	)
	default DefensivePrayer defensivePrayerOne() { return DefensivePrayer.PROTECT_FROM_MAGIC; }

	@ConfigItem(
			keyName = "hotkeyOne",
			name = "Hotkey One",
			description = "Hotkey for gear swap.",
			position = 6,
			section = sectionOne
	)
	default Keybind hotKeyOne() { return Keybind.NOT_SET; }

	@ConfigItem(
			keyName = "gearsettwo",
			name = "Gear Setup 2",
			description = "Gear Names",
			position = 7,
			section = sectionTwo
	)
	default String gearTwo()
	{
		return "Gear";
	}

	@ConfigItem(
			keyName = "offensePrayTwo",
			name = "Enable Offensive Prayers",
			description = "Will Toggle Prayer On Switch",
			position = 8,
			section = sectionTwo
	)
	default boolean useOffensePrayerTwo() { return false; }

	@ConfigItem(
			keyName = "defensePrayTwo",
			name = "Enable Defensive Prayers",
			description = "Will Toggle Prayer On Switch",
			position = 9,
			section = sectionTwo
	)
	default boolean useDefensePrayerTwo() { return false; }

	@ConfigItem(
			keyName = "offensePrayerTwo",
			name = "Offensive Prayer",
			description = "Offensive Prayer to activate",
			position = 10,
			section = sectionTwo
	)
	default OffensivePrayer offensivePrayerTwo() { return OffensivePrayer.PIETY; }

	@ConfigItem(
			keyName = "defensePrayerTwo",
			name = "Defensive Prayer",
			description = "Defensive Prayer to activate",
			position = 11,
			section = sectionTwo
	)
	default DefensivePrayer defensivePrayerTwo() { return DefensivePrayer.PROTECT_FROM_MAGIC; }

	@ConfigItem(
			keyName = "hotkeyTwo",
			name = "Hotkey Two",
			description = "Hotkey for gear swap.",
			position = 12,
			section = sectionTwo
	)
	default Keybind hotKeyTwo() { return Keybind.NOT_SET; }

	@ConfigItem(
			keyName = "gearsetthree",
			name = "Gear Setup 1",
			description = "Gear Names",
			position = 13,
			section = sectionThree
	)
	default String gearThree()
	{
		return "Gear";
	}

	@ConfigItem(
			keyName = "offensePrayThree",
			name = "Enable Offensive Prayers",
			description = "Will Toggle Prayer On Switch",
			position = 14,
			section = sectionTwo
	)
	default boolean useOffensePrayerThree() { return false; }

	@ConfigItem(
			keyName = "defensePrayThree",
			name = "Enable Defensive Prayers",
			description = "Will Toggle Prayer On Switch",
			position = 15,
			section = sectionTwo
	)
	default boolean useDefensePrayerThree() { return false; }

	@ConfigItem(
			keyName = "offensePrayerThree",
			name = "Offensive Prayer",
			description = "Offensive Prayer to activate",
			position = 16,
			section = sectionThree
	)
	default OffensivePrayer offensivePrayerThree() { return OffensivePrayer.PIETY; }

	@ConfigItem(
			keyName = "defensePrayerThree",
			name = "Defensive Prayer",
			description = "Defensive Prayer to activate",
			position = 17,
			section = sectionThree
	)
	default DefensivePrayer defensivePrayerThree() { return DefensivePrayer.PROTECT_FROM_MAGIC; }

	@ConfigItem(
			keyName = "hotkeyThree",
			name = "Hotkey Three",
			description = "Hotkey for gear swap.",
			position = 18,
			section = sectionThree
	)
	default Keybind hotKeyThree() { return Keybind.NOT_SET; }

	@ConfigItem(
			keyName = "grabGear",
			name = "Grab Gear",
			description = "Button to grab gear",
			position = 19,
			clazz = GoonBarrowsPlugin.class
	)
	default Button grabGear() { return new Button(); }

}
