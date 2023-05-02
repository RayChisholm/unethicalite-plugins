package net.unethicalite.plugins.goonhotkeyswap;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("goonhotkeyswap")
public interface GoonHotkeySwapConfig extends Config
{
	@ConfigItem(
			keyName = "gearsetone",
			name = "Gear Setup 1",
			description = "Gear Names",
			position = 0
	)
	default String username()
	{
		return "Gear";
	}

}
