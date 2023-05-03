package net.unethicalite.plugins.goonbarrows.data;

import com.google.common.collect.ImmutableList;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.widgets.WidgetInfo;

public class Constants {
    public static final WorldArea TUNNEL_AREA = new WorldArea(3521, 9665, 60, 60, 0);
    public static final WorldArea BARROWS_AREA = new WorldArea(3545, 3266, 41, 43, 0);

    public static final ImmutableList<WidgetInfo> POSSIBLE_SOLUTIONS = ImmutableList.of(
            WidgetInfo.BARROWS_PUZZLE_ANSWER1,
            WidgetInfo.BARROWS_PUZZLE_ANSWER2,
            WidgetInfo.BARROWS_PUZZLE_ANSWER3
    );

    public static final int DIG_ANIM = 830;
}
