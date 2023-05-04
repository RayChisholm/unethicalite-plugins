package net.unethicalite.plugins.goonbarrows;

import net.runelite.api.ItemID;
import net.runelite.api.Prayer;
import net.runelite.client.config.*;
import net.unethicalite.plugins.goonbarrows.data.DefensivePrayer;
import net.unethicalite.plugins.goonbarrows.data.Food;
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
			position = 3
	)
	String sectionTwo = "Gear Setup Two";
	@ConfigSection(
			name = "Karils Setup",
			description = "Arguments for Gear Set Three",
			position = 5
	)
	String sectionThree = "Gear Setup Three";
	@ConfigSection(
			name = "Tunnel Setup",
			description = "Arguments for Gear Set Three",
			position = 7
	)
	String sectionFour = "Tunnel Setup";
	@ConfigSection(
			name = "Food Settings",
			description = "Food Settings",
			position = 9
	)
	String sectionFive = "Food Settings";


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
			keyName = "gearsettwo",
			name = "Ahrim Setup",
			description = "Gear Names",
			position = 3,
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
			position = 4,
			section = sectionTwo
	)
	default boolean useOffensePrayerTwo() { return false; }

	@ConfigItem(
			keyName = "gearsetthree",
			name = "Karil Setup",
			description = "Gear Names",
			position = 5,
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
			position = 6,
			section = sectionThree
	)
	default boolean useOffensePrayerThree() { return false; }

	@ConfigItem(
			keyName = "gearsetFour",
			name = "Karil Setup",
			description = "Gear Names",
			position = 7,
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
			position = 8,
			section = sectionFour
	)
	default boolean useOffensePrayerFour() { return false; }

	@ConfigItem(
			keyName = "foodType",
			name = "Food Type",
			description = "Which food to use",
			position = 9,
			section = sectionFive
	)
	default Food getFood() { return Food.KARAMBWAN; }

	@ConfigItem(
			keyName = "foodCount",
			name = "Amount",
			description = "How much food to bring.",
			position = 10,
			section = sectionFive
	)
	default int foodCount() { return 8; }

	@ConfigItem(
			keyName = "grabGear",
			name = "Grab Gear",
			description = "Button to grab gear",
			position = 9,
			clazz = net.unethicalite.plugins.goonbarrows.GoonBarrowsPlugin.class
	)
	default Button grabGear() { return new Button(); }

}
