package net.unethicalite.plugins.goonbarrows.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.Prayer;
import net.runelite.api.mixins.Inject;
import net.unethicalite.plugins.goonbarrows.data.DefensivePrayer;
import net.unethicalite.plugins.goonbarrows.data.OffensivePrayer;
import net.unethicalite.plugins.goonbarrows.helpers.Setups;
import net.unethicalite.plugins.goonbarrows.helpers.GearSetup;
import net.unethicalite.plugins.goonbarrows.GoonBarrowsConfig;

@RequiredArgsConstructor
public enum OffensivePrayer {
    AUGURY(Prayer.AUGURY),
    MYSTIC_MIGHT(Prayer.MYSTIC_MIGHT),
    RIGOUR(Prayer.RIGOUR),
    EAGLE_EYE(Prayer.EAGLE_EYE),
    PIETY(Prayer.PIETY)
    ;

    @Inject
    private GoonBarrowsConfig config;
    @Getter
    private final Prayer prayer;
}
