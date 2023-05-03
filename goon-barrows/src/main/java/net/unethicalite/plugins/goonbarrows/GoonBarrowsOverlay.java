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
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text("Goon Barrows")
                    .color(Color.GREEN)
                    .build());


            String brother = "";
            String status = "";
            if (plugin.getBros() != null)
            {
                brother = plugin.getBros().getName();
                status = String.valueOf(plugin.getBros().getKilledVarbit());
            }
            else
            {
                brother = "null";
            }
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Bro")
                    .right(brother)
                    .build());

            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Var:")
                    .right(status)
                    .build());

            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Sleeper:")
                    .right(String.valueOf(plugin.customSleep))
                    .build());

            String name = "";
            if (Static.getClient().getHintArrowNpc() != null)
            {
                name = Static.getClient().getHintArrowNpc().getName();
            }
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Arrow:")
                    .right(name)
                    .build());
        }
        return super.render(graphics);
    }

}
