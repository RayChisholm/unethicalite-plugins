package net.unethicalite.plugins.goonherbrun;

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

public class GoonHerbRunOverlay extends OverlayPanel {
    private final Client client;
    private final GoonHerbRunPlugin plugin;
    private final GoonHerbRunConfig config;

    @Inject
    private GoonHerbRunOverlay(Client client, GoonHerbRunPlugin plugin, GoonHerbRunConfig config)
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

            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Test")
                    .right("ing")
                    .build());

        }
        return super.render(graphics);
    }

}
