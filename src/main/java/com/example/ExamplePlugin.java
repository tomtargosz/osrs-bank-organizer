package com.example;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.ScriptPostFired;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;


@Slf4j
@PluginDescriptor(
	name = "Example"
)
public class ExamplePlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ExampleConfig config;

	@Override
	protected void startUp() throws Exception
	{
		log.info("Example started!");
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Example stopped!");
	}

	@Subscribe
	public void onScriptPostFired(ScriptPostFired event)
	{
		// User has opened their bank
		if (event.getScriptId() == ScriptID.BANKMAIN_BUILD)
		{
			getBankItemIDs(WidgetInfo.BANK_ITEM_CONTAINER, InventoryID.BANK);
		}
	}

	private void getBankItemIDs(WidgetInfo widgetInfo, InventoryID inventoryID)
	{
		final Widget widget = client.getWidget(widgetInfo);
		final ItemContainer itemContainer = client.getItemContainer(inventoryID);
		final Widget[] children = widget.getChildren();
		final int maxNumberOfItemsToLog = 20;

		if (itemContainer != null && children != null)
		{
			// In the bank, the first components are the bank items, followed by tabs etc. There are always 816 components regardless
			// of bank size, but we only need to check up to the bank size.
			for (int i = 0; i < itemContainer.size(); ++i)
			{
				Widget child = children[i];
				if (child != null && !child.isSelfHidden() && child.getItemId() > -1 && i < maxNumberOfItemsToLog)
				{
					log.info("name: " + child.getName());
					log.info("id: " + child.getItemId());
				}
			}
		}
	}

	@Provides
	ExampleConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ExampleConfig.class);
	}
}
