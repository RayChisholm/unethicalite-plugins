package net.unethicalite.plugins.goonbarrows.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.mixins.Inject;
import net.runelite.api.Prayer;
import net.unethicalite.plugins.goonbarrows.data.DefensivePrayer;
import net.unethicalite.plugins.goonbarrows.data.OffensivePrayer;
import net.unethicalite.plugins.goonbarrows.helpers.Setups;
import net.unethicalite.plugins.goonbarrows.helpers.GearSetup;
import net.unethicalite.plugins.goonbarrows.GoonBarrowsConfig;

@RequiredArgsConstructor
public enum DefensivePrayer {
    PROTECT_FROM_MELEE(Prayer.PROTECT_FROM_MELEE),
    PROTECT_FROM_RANGE(Prayer.PROTECT_FROM_MISSILES),
    PROTECT_FROM_MAGIC(Prayer.PROTECT_FROM_MAGIC)
    ;

    @Inject
    GoonBarrowsConfig config;
    @Getter
    private final Prayer prayer;
}
