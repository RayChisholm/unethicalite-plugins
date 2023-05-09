package src.main.java.net.unethicalite.plugins.goonsandminer;

import src.main.java.net.unethicalite.plugins.goonsandminer.utils.*;
import net.runelite.api.ItemID;
import net.runelite.api.Prayer;
import net.runelite.client.config.*;
import net.runelite.client.config.Button;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(GoonSandMinerConfig.GROUP)
public interface GoonSandMinerConfig extends Config {
    String GROUP = "goonsandminer";

    @ConfigItem(
            position = 0,
            keyName = "humidify",
            name = "Cast Humidify",
            description = "Cast humidify when needed."
    )
    default boolean humidify() { return false; }
}
