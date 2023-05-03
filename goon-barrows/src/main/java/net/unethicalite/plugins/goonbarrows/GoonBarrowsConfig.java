package net.unethicalite.plugins.goonbarrows;

import net.runelite.api.Prayer;
import net.runelite.client.config.*;
import net.unethicalite.plugins.goonbarrows.data.DefensivePrayer;
import net.unethicalite.plugins.goonbarrows.data.OffensivePrayer;
import net.unethicalite.plugins.goonbarrows.helpers.Setups;
import net.unethicalite.plugins.goonbarrows.helpers.GearSetup;
import net.unethicalite.plugins.goonbarrows.GoonBarrowsConfig;


@ConfigGroup(GoonBarrowsConfig.GROUP)
public interface GoonBarrowsConfig extends Config
{
	String GROUP = "goonbarrows";

	@ConfigSection(
			name = "Mage Gear",
			description = "Arguments for Gear Set One",
			position = 0
	)
	String sectionOne = "Gear Set One";
	@ConfigSection(
			name = "Ahrims Setup",
			description = "Arguments for Gear Set Two",
			position = 4
	)
	String sectionTwo = "Gear Setup Two";
	@ConfigSection(
			name = "Karils Setup",
			description = "Arguments for Gear Set Three",
			position = 8
	)
	String sectionThree = "Gear Setup Three";
	@ConfigSection(
			name = "Tunnel Setup",
			description = "Arguments for Gear Set Three",
			position = 19
	)
	String sectionFour = "Tunnel Setup";


	@ConfigItem(
			keyName = "gearsetone",
			name = "Mage Gear",
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
			name = "Hotkey",
			description = "Hotkey for gear swap.",
			position = 6,
			section = sectionOne
	)
	default Keybind hotKeyOne() { return Keybind.NOT_SET; }

	@ConfigItem(
			keyName = "gearsettwo",
			name = "Ahrim Setup",
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
			name = "Hotkey",
			description = "Hotkey for gear swap.",
			position = 12,
			section = sectionTwo
	)
	default Keybind hotKeyTwo() { return Keybind.NOT_SET; }

	@ConfigItem(
			keyName = "gearsetthree",
			name = "Karil Setup",
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
			section = sectionThree
	)
	default boolean useOffensePrayerThree() { return false; }

	@ConfigItem(
			keyName = "defensePrayThree",
			name = "Enable Defensive Prayers",
			description = "Will Toggle Prayer On Switch",
			position = 15,
			section = sectionThree
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
			name = "Hotkey",
			description = "Hotkey for gear swap.",
			position = 18,
			section = sectionThree
	)
	default Keybind hotKeyThree() { return Keybind.NOT_SET; }

	@ConfigItem(
			keyName = "gearsetFour",
			name = "Karil Setup",
			description = "Gear Names",
			position = 19,
			section = sectionFour
	)
	default String gearFour()
	{
		return "Gear";
	}

	@ConfigItem(
			keyName = "offensePrayFour",
			name = "Enable Offensive Prayers",
			description = "Will Toggle Prayer On Switch",
			position = 20,
			section = sectionFour
	)
	default boolean useOffensePrayerFour() { return false; }

	@ConfigItem(
			keyName = "defensePrayFour",
			name = "Enable Defensive Prayers",
			description = "Will Toggle Prayer On Switch",
			position = 21,
			section = sectionFour
	)
	default boolean useDefensePrayerFour() { return false; }

	@ConfigItem(
			keyName = "offensePrayerFour",
			name = "Offensive Prayer",
			description = "Offensive Prayer to activate",
			position = 22,
			section = sectionFour
	)
	default OffensivePrayer offensivePrayerFour() { return OffensivePrayer.PIETY; }

	@ConfigItem(
			keyName = "defensePrayerFour",
			name = "Defensive Prayer",
			description = "Defensive Prayer to activate",
			position = 23,
			section = sectionFour
	)
	default DefensivePrayer defensivePrayerFour() { return DefensivePrayer.PROTECT_FROM_MAGIC; }

	@ConfigItem(
			keyName = "hotkeyThree",
			name = "Hotkey",
			description = "Hotkey for gear swap.",
			position = 24,
			section = sectionFour
	)
	default Keybind hotKeyFour() { return Keybind.NOT_SET; }

	@ConfigItem(
			keyName = "grabGear",
			name = "Grab Gear",
			description = "Button to grab gear",
			position = 25,
			clazz = net.unethicalite.plugins.goonbarrows.GoonBarrowsPlugin.class
	)
	default Button grabGear() { return new Button(); }

}
