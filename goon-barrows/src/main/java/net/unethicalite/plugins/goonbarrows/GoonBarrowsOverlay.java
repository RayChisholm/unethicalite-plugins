package net.unethicalite.plugins.goonbarrows;

import com.google.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Varbits;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;
import net.unethicalite.client.Static;

import java.awt.*;

public class GoonBarrowsOverlay extends OverlayPanel {
    private final Client client;
    private final GoonBarrowsPlugin plugin;
    private final GoonBarrowsConfig config;

    @Inject
    private GoonBarrowsOverlay(Client client, GoonBarrowsPlugin plugin, GoonBarrowsConfig config)
    {
        this.client = client;
        this.plugin = plugin;
        this.config = config;
        setPosition(OverlayPosition.BOTTOM_LEFT);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        if(client.getGameState().equals(GameState.LOGGED_IN)) {
            panelComponent.getChildren().clear();

            panelComponent.getChildren().add(TitleComponent.builder()
                    .text("Goon Barrows")
                    .color(Color.GREEN)
                    .build());

            if (plugin.getVisibleBrother() != null) {
                panelComponent.getChildren().add(LineComponent.builder()
                        .left("Visible:")
                        .right(plugin.getVisibleBrother().getName())
                        .build());
            }

            if (plugin.getCurrentBrother() != null)
            {
                panelComponent.getChildren().add(LineComponent.builder()
                        .left("Current:")
                        .right(plugin.getCurrentBrother().getName())
                        .build());

                panelComponent.getChildren().add(LineComponent.builder()
                        .left("inTunnel:")
                        .right(String.valueOf(plugin.getCurrentBrother().isInTunnel()))
                        .build());
            }

            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Last One:")
                    .right(String.valueOf(plugin.onLastBrother()))
                    .build());

            panelComponent.getChildren().add(LineComponent.builder()
                    .left("New Run:")
                    .right(String.valueOf(plugin.isNewRun()))
                    .build());

        }
        return super.render(graphics);
    }

}
