package com.arvcork.managers;

import com.arvcork.utils.ItemContainerUtils;
import com.arvcork.TemporossEquipment;
import com.arvcork.TemporossSoloHelperPlugin;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.eventbus.Subscribe;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Slf4j
public class TemporossEquipmentManager {
    @Inject
    private Client client;

    @Inject
    public TemporossSoloHelperPlugin plugin;

    private ItemContainer playerInventory;

    private ItemContainer playerEquipment;

    @Subscribe
    public void onGameStateChanged(GameStateChanged event)
    {
        if (event.getGameState() == GameState.LOGGED_IN && plugin.isInUnkahRegion())
        {
            processItemContainers();
        }
    }

    @Subscribe
    public void onItemContainerChanged(ItemContainerChanged event)
    {
        if (plugin.isInUnkahRegion())
        {
            processItemContainers();
        }
    }

    /**
     * Process the item containers for the client and ensure that the player has the required items.
     */
    private void processItemContainers()
    {
        this.playerInventory = client.getItemContainer(InventoryID.INVENTORY);
        this.playerEquipment = client.getItemContainer(InventoryID.EQUIPMENT);
    }

    /**
     * Determine if the player has the required number of free spaces in the inventory.
     */
    public boolean doesPlayerHaveFreeSpaces()
    {
        return ItemContainerUtils.hasFreeSpaces(this.playerInventory, TemporossEquipment.MINIMUM_INVENTORY_SLOTS);
    }

    /**
     * Determine if the player has the required number of buckets of water in their inventory.
     */
    public boolean doesPlayerHaveBucketsOfWater()
    {
        return ItemContainerUtils.hasRequiredAmount(this.playerInventory, TemporossEquipment.BUCKET_OF_WATER, TemporossEquipment.BUCKETS_OF_WATER_REQUIRED);
    }

    /**
     * Determine if the player has a rope or a full set of Spirit Angler which acts as a rope.
     */
    public boolean doesPlayerHaveRope()
    {
        return ItemContainerUtils.has(this.playerInventory, ItemID.ROPE) ||
                ItemContainerUtils.hasAll(this.playerEquipment, TemporossEquipment.SPIRIT_ANGLER_OUTFIT);
    }

    /**
     * Determine if the player has a harpoon within their inventory or equipment.
     */
    public boolean doesPlayerHaveHarpoon()
    {
        return ItemContainerUtils.hasOneOf(this.playerInventory, TemporossEquipment.HARPOONS) ||
               ItemContainerUtils.hasOneOf(this.playerEquipment, TemporossEquipment.HARPOONS);
    }

    /**
     * Determine if the player has a hammer within their inventory or equipment.
     */
    public boolean doesPlayerHaveHammer()
    {
        return ItemContainerUtils.hasOneOf(this.playerEquipment, TemporossEquipment.HAMMERS) ||
                ItemContainerUtils.hasOneOf(this.playerInventory, TemporossEquipment.HAMMERS);
    }
}
