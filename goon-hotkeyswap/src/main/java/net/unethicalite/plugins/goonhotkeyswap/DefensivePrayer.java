package net.unethicalite.plugins.goonhotkeyswap;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.mixins.Inject;
import net.runelite.api.Prayer;

@RequiredArgsConstructor
public enum DefensivePrayer {
    PROTECT_FROM_MELEE(Prayer.PROTECT_FROM_MELEE),
    PROTECT_FROM_RANGE(Prayer.PROTECT_FROM_MISSILES),
    PROTECT_FROM_MAGIC(Prayer.PROTECT_FROM_MAGIC)
    ;

    @Inject
    GoonHotkeySwapConfig config;
    @Getter
    private final Prayer prayer;
}
