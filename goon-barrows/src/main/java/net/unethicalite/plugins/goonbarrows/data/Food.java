package net.unethicalite.plugins.goonbarrows.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.ItemID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.runelite.api.Prayer;
import net.runelite.api.Varbits;
import net.runelite.api.annotations.Varbit;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.game.Vars;

@Getter
@RequiredArgsConstructor
public enum Food {
    KARAMBWAN(18, ItemID.COOKED_KARAMBWAN),
    LOBSTER(12, ItemID.LOBSTER);


    private int healAmount;
    private int id;

    Food(int healAmount, int id) {
        this.healAmount = healAmount;
        this.id = id;
    }
}
