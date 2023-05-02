package net.unethicalite.plugins.goonhotkeyswap;

import com.google.inject.Inject;
import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.World;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.PluginChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.unethicalite.api.events.LobbyWorldSelectToggled;
import net.unethicalite.api.events.LoginStateChanged;
import net.unethicalite.api.events.WorldHopped;
import net.unethicalite.api.game.Game;
import net.unethicalite.api.game.Worlds;
import net.unethicalite.api.input.Keyboard;
import net.unethicalite.api.script.blocking_events.WelcomeScreenEvent;
import net.unethicalite.api.widgets.Widgets;
import org.jboss.aerogear.security.otp.Totp;
import org.pf4j.Extension;

import com.google.inject.Inject;
import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.NPC;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ActorDeath;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.PluginDescriptor;
import net.unethicalite.api.entities.NPCs;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.plugins.Plugins;
import net.unethicalite.api.plugins.Task;
import net.unethicalite.api.plugins.TaskPlugin;
import net.unethicalite.api.widgets.Dialog;
import net.unethicalite.api.widgets.Widgets;
import net.unethicalite.client.Static;
import org.pf4j.Extension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@PluginDescriptor(name = "Goon Hotkey Swap", enabledByDefault = false)
@Extension
@Slf4j
public class GoonHotkeySwapPlugin extends Plugin
{
	@Inject
	private GoonHotkeySwapConfig config;

	@Inject
	private Client client;

	@Inject
	private ConfigManager configManager;

	@Provides
	public GoonHotkeySwapConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(GoonHotkeySwapConfig.class);
	}

	@Subscribe
	private void onGameStateChanged(GameStateChanged e)
	{
	}

	@Subscribe
	private void onLoginStateChanged(LoginStateChanged e)
	{
	}

	@Subscribe
	private void onWorldHopped(WorldHopped e)
	{
	}

	@Subscribe
	private void onWidgetHiddenChanged(WidgetLoaded e)
	{
	}

	@Subscribe
	private void onLobbyWorldSelectToggled(LobbyWorldSelectToggled e)
	{
	}

	@Subscribe
	private void onPluginChanged(PluginChanged e)
	{
	}

	@Subscribe
	private void onGameTick(GameTick e)
	{

	}


}
