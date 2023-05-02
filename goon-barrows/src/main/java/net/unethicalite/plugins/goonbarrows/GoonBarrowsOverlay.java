package net.unethicalite.plugins.goonbarrows;

import lombok.NoArgsConstructor;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.mixins.Inject;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

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
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text("Test")
                    .color(Color.GREEN)
                    .build());

            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Number")
                    .right(Integer.toString(client.getWorld()))
                    .build());
        }
        return super.render(graphics);
    }

}
