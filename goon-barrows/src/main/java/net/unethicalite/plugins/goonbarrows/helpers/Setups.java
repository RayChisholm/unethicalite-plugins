package net.unethicalite.plugins.goonbarrows.helpers;

import com.google.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.Prayer;
import net.unethicalite.api.widgets.Prayers;
import net.unethicalite.plugins.goonbarrows.data.DefensivePrayer;
import net.unethicalite.plugins.goonbarrows.data.OffensivePrayer;
import net.unethicalite.plugins.goonbarrows.helpers.Setups;
import net.unethicalite.plugins.goonbarrows.helpers.GearSetup;
import net.unethicalite.plugins.goonbarrows.GoonBarrowsConfig;

import java.time.OffsetDateTime;

@Getter
public enum Setups {
    SETUP_ONE,
    SETUP_TWO,
    SETUP_THREE,
    SETUP_FOUR;

    @Setter
    private GearSetup setup;
    @Setter
    private OffensivePrayer oprayer;
    @Setter
    private DefensivePrayer dprayer;

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
    public static void setSetupFour(GearSetup gearSetup)
    {
        SETUP_FOUR.setSetup(gearSetup);
    }

    public static void setOffensivePrayerOne(OffensivePrayer oPrayer)
    {
        SETUP_ONE.setOprayer(oPrayer);
    }
    public static void setOffensivePrayerTwo(OffensivePrayer oPrayer)
    {
        SETUP_TWO.setOprayer(oPrayer);
    }
    public static void setOffensivePrayerThree(OffensivePrayer oPrayer)
    {
        SETUP_THREE.setOprayer(oPrayer);
    }
    public static void setOffensivePrayerFour(OffensivePrayer oPrayer)
    {
        SETUP_FOUR.setOprayer(oPrayer);
    }

    public static void setDefensivePrayerOne(DefensivePrayer dPrayer)
    {
        SETUP_ONE.setDprayer(dPrayer);
    }
    public static void setDefensivePrayerTwo(DefensivePrayer dPrayer)
    {
        SETUP_TWO.setDprayer(dPrayer);
    }
    public static void setDefensivePrayerThree(DefensivePrayer dPrayer)
    {
        SETUP_THREE.setDprayer(dPrayer);
    }
    public static void setDefensivePrayerFour(DefensivePrayer dPrayer)
    {
        SETUP_FOUR.setDprayer(dPrayer);
    }

    public void toggleOffensivePrayer()
    {
        if (!Prayers.isEnabled(this.getOprayer().getPrayer()))
        {
            Prayers.toggle(this.getOprayer().getPrayer());
        }
    }
    public void toggleDefensivePrayer()
    {
        if (!Prayers.isEnabled(this.getDprayer().getPrayer()))
        {
            Prayers.toggle(this.getDprayer().getPrayer());
        }
    }

}
