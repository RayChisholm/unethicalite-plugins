package net.unethicalite.plugins.goonbarrows.data;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import net.runelite.api.ItemID;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.widgets.WidgetInfo;

import java.util.Map;
import java.util.Set;

public class Constants {
    public static final WorldArea TUNNEL_AREA = new WorldArea(3521, 9665, 60, 60, 0);
    public static final WorldArea BARROWS_AREA = new WorldArea(3545, 3264, 41, 58, 0);
    public static final WorldArea FEROX_ENCLAVE = new WorldArea(3122, 3615, 36, 34, 0);

    public static final Set<Integer> PRAYER_RESTORE_POTION_IDS = ImmutableSet.of(
            ItemID.PRAYER_POTION1,
            ItemID.PRAYER_POTION2,
            ItemID.PRAYER_POTION3,
            ItemID.PRAYER_POTION4,
            ItemID.SUPER_RESTORE1,
            ItemID.SUPER_RESTORE2,
            ItemID.SUPER_RESTORE3,
            ItemID.SUPER_RESTORE4
    );

    public static final Set<Integer> BARROWS_BASIC_LOOT_IDS = ImmutableSet.of(
            ItemID.COINS,
            ItemID.MIND_RUNE,
            ItemID.CHAOS_RUNE,
            ItemID.BLOOD_RUNE,
            ItemID.LOOP_HALF_OF_KEY,
            ItemID.TOOTH_HALF_OF_KEY,
            ItemID.BOLT_RACK,
            ItemID.DRAGON_MED_HELM
    );

    public static final Set<Integer> BARROWS_UNDEGRADED_IDS = ImmutableSet.of(
            ItemID.AHRIMS_HOOD,
            ItemID.AHRIMS_STAFF,
            ItemID.AHRIMS_ROBETOP,
            ItemID.AHRIMS_ROBESKIRT,
            ItemID.DHAROKS_HELM,
            ItemID.DHAROKS_GREATAXE,
            ItemID.DHAROKS_PLATEBODY,
            ItemID.DHAROKS_PLATELEGS,
            ItemID.GUTHANS_HELM,
            ItemID.GUTHANS_WARSPEAR,
            ItemID.GUTHANS_PLATEBODY,
            ItemID.GUTHANS_CHAINSKIRT,
            ItemID.KARILS_COIF,
            ItemID.KARILS_CROSSBOW,
            ItemID.KARILS_LEATHERTOP,
            ItemID.KARILS_LEATHERSKIRT,
            ItemID.TORAGS_HELM,
            ItemID.TORAGS_HAMMERS,
            ItemID.TORAGS_PLATEBODY,
            ItemID.TORAGS_PLATELEGS,
            ItemID.VERACS_HELM,
            ItemID.VERACS_FLAIL,
            ItemID.VERACS_BRASSARD,
            ItemID.VERACS_PLATESKIRT
    );
    public static final Map<WidgetInfo, WidgetInfo> POSSIBLE_SOLUTIONS = ImmutableMap.of(
            WidgetInfo.BARROWS_PUZZLE_ANSWER1, WidgetInfo.BARROWS_PUZZLE_ANSWER1_CONTAINER,
            WidgetInfo.BARROWS_PUZZLE_ANSWER2, WidgetInfo.BARROWS_PUZZLE_ANSWER2_CONTAINER,
            WidgetInfo.BARROWS_PUZZLE_ANSWER3, WidgetInfo.BARROWS_PUZZLE_ANSWER3_CONTAINER
    );

    public static final Set<Integer> DUELING_RING_IDS = ImmutableSet.of(
            ItemID.RING_OF_DUELING1,
            ItemID.RING_OF_DUELING2,
            ItemID.RING_OF_DUELING3,
            ItemID.RING_OF_DUELING4,
            ItemID.RING_OF_DUELING5,
            ItemID.RING_OF_DUELING6,
            ItemID.RING_OF_DUELING7,
            ItemID.RING_OF_DUELING8
    );

    public static final int DIG_ANIM = 830;
}
