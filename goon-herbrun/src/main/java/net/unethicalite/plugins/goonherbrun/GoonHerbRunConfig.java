package net.unethicalite.plugins.goonherbrun;

import net.runelite.api.ItemID;
import net.runelite.api.Prayer;
import net.runelite.client.config.*;
import net.unethicalite.plugins.goonbarrows.data.DefensivePrayer;
import net.unethicalite.plugins.goonbarrows.data.Food;
import net.unethicalite.plugins.goonbarrows.data.OffensivePrayer;
import net.unethicalite.plugins.goonbarrows.helpers.Setups;
import net.unethicalite.plugins.goonbarrows.helpers.GearSetup;
import net.unethicalite.plugins.goonbarrows.GoonBarrowsConfig;


@ConfigGroup(GoonHerbRunConfig.GROUP)
public interface GoonHerbRunConfig extends Config
{
	String GROUP = "goonherbrun";

	@ConfigItem(
			keyName = "grabGear",
			name = "Grab Gear",
			description = "Button to grab gear",
			position = 1,
			clazz = GoonHerbRunPlugin.class
	)
	default Button grabGear() { return new Button(); }

}
