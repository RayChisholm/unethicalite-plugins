package net.unethicalite.plugins.goonbarrows.helpers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.Prayer;
import net.runelite.api.Varbits;
import net.runelite.api.annotations.Varbit;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.game.Vars;

@RequiredArgsConstructor
@Getter
public enum BarrowsBrothers {
    AHRIM("Ahrim", new WorldPoint(3566, 3289, 0),
            new WorldArea(3549, 9694, 13, 12, 3),
            Setups.SETUP_TWO,
            Prayer.PROTECT_FROM_MAGIC,
            1672,
            Varbits.BARROWS_KILLED_AHRIM),
    DHAROK("Dharok", new WorldPoint(3575, 3298, 0),
            new WorldArea(3548, 9710, 13, 10, 3),
            Setups.SETUP_ONE,
            Prayer.PROTECT_FROM_MELEE,
            1673,
            Varbits.BARROWS_KILLED_DHAROK),
    GUTHAN("Guthan", new WorldPoint(3577, 3283, 0),
            new WorldArea(3532, 9698, 14, 12, 3),
            Setups.SETUP_ONE,
            Prayer.PROTECT_FROM_MELEE,
            1674,
            Varbits.BARROWS_KILLED_GUTHAN),
    KARIL("Karil", new WorldPoint(3566, 3275, 0),
            new WorldArea(3544, 9678, 14, 11, 3),
            Setups.SETUP_THREE,
            Prayer.PROTECT_FROM_MISSILES,
            1675,
            Varbits.BARROWS_KILLED_KARIL),
    TORAG("Torag", new WorldPoint(3553, 3283, 0),
            new WorldArea(3563, 9682, 13, 11, 3),
            Setups.SETUP_ONE,
            Prayer.PROTECT_FROM_MELEE,
            1676,
            Varbits.BARROWS_KILLED_TORAG),
    VERAC("Verac", new WorldPoint(3557, 3298, 0),
            new WorldArea(3567, 9701, 13, 11, 3),
            Setups.SETUP_ONE,
            Prayer.PROTECT_FROM_MELEE,
            1677,
            Varbits.BARROWS_KILLED_VERAC);

    private final String name;
    private final WorldPoint location;
    private final WorldArea tomb;
    private final Setups setup;
    private final Prayer prayer;
    private final int id;
    @Getter(onMethod_ = {@Varbit})
    private final int killedVarbit;

    public boolean isDead()
    {
        return Vars.getBit(killedVarbit) > 0;
    }

    public static BarrowsBrothers getBrotherCrypt()
    {
        if (Players.getLocal().getWorldLocation().getPlane() != 3)
        {
            return null;
        }

        for (BarrowsBrothers bro : BarrowsBrothers.values())
        {
            if (bro.tomb.contains(Players.getLocal()))
            {
                return bro;
            }
        }
        return null;
    }
}
