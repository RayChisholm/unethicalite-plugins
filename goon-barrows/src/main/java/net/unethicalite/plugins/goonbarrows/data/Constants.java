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
    public static final WorldArea BARROWS_AREA = new WorldArea(3545, 3266, 41, 43, 0);

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
