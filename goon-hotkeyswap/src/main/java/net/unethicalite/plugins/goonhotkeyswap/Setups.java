package net.unethicalite.plugins.goonhotkeyswap;

import com.google.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.Prayer;

@Getter
public enum Setups {
    SETUP_ONE,
    SETUP_TWO,
    SETUP_THREE;

    @Setter
    private GearSetup setup;
    @Setter
    private Prayer prayer;

    public static void setSetupOne(GearSetup gearSetup)
    {
        SETUP_ONE.setSetup(gearSetup);
    }
    public static void setSetupTwo(GearSetup gearSetup)
    {
        SETUP_TWO.setSetup(gearSetup);
    }
    public static void setSetupThree(GearSetup gearSetup)
    {
        SETUP_THREE.setSetup(gearSetup);
    }

    public static void setPrayerOne(Prayer prayer)
    {
        SETUP_ONE.setPrayer(prayer);
    }
    public static void setPrayerTwo(Prayer prayer)
    {
        SETUP_TWO.setPrayer(prayer);
    }
    public static void setPrayerThree(Prayer prayer)
    {
        SETUP_THREE.setPrayer(prayer);
    }
}
