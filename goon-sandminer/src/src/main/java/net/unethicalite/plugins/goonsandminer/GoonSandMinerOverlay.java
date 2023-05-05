package src.main.java.net.unethicalite.plugins.goonsandminer;

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

public class GoonSandMinerOverlay extends OverlayPanel {
    private final Client client;
    private final GoonSandMinerPlugin plugin;
    private final GoonSandMinerConfig config;

    @Inject
    private GoonSandMinerOverlay(Client client, GoonSandMinerPlugin plugin, GoonSandMinerConfig config)
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
        if (client.getGameState().equals(GameState.LOGGED_IN))
        {
            panelComponenet.getChildren().add(TitleComponent.builder()
                    .text("Sandgoons")
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
