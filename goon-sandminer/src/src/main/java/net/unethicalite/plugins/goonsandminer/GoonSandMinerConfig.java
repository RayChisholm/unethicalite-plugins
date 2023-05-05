package src.main.java.net.unethicalite.plugins.goonsandminer;

import net.runelite.api.ItemID;
import net.runelite.api.Prayer;
import net.runelite.client.config.*;

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
