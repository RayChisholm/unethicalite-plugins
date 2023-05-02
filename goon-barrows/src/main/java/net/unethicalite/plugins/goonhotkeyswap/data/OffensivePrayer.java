package net.unethicalite.plugins.goonbarrows;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.Prayer;
import net.runelite.api.mixins.Inject;

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
