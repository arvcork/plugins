package com.arvcork.utils;

import net.runelite.api.Item;
import net.runelite.api.ItemContainer;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ItemContainerUtils {
    /**
     * Determine if the item container has a required amount of a specific item.
     */
    public static boolean hasRequiredAmount(ItemContainer itemContainer, int itemId, int requiredCount)
    {
        if (itemContainer == null)
        {
            return false;
        }

        long containerCount = Arrays.stream(itemContainer.getItems())
                .map(Item::getId)
                .filter(i -> i == itemId)
                .count();

        return containerCount == requiredCount;
    }

    /**
     * Determine if the item container has an item.
     */
    public static boolean has(ItemContainer itemContainer, int itemId)
    {
        if (itemContainer == null)
        {
            return false;
        }

        return itemContainer.contains(itemId);
    }

    /**
     * Determine if the item container has one of the specified item ids.
     */
    public static boolean hasOneOf(ItemContainer itemContainer, Integer[] itemIds)
    {
        if (itemContainer == null)
        {
            return false;
        }

        final Set<Integer> available = streamItemIdsFromContainer(itemContainer).collect(Collectors.toSet());
        final Set<Integer> searched = new HashSet<>(Arrays.asList(itemIds));

        return ! Collections.disjoint(available, searched);
    }

    /**
     * Determine if the item container has all of the item ids.
     */
    public static boolean hasAll(ItemContainer itemContainer, Integer[] itemIds)
    {
        if (itemContainer == null)
        {
            return false;
        }

        final Set<Integer> available = streamItemIdsFromContainer(itemContainer).collect(Collectors.toSet());
        final Set<Integer> searched = new HashSet<>(Arrays.asList(itemIds));

        return available.containsAll(searched);
    }

    /**
     * Determine if the item container has enough required free space.
     */
    public static boolean hasFreeSpaces(ItemContainer itemContainer, int requiredFreeSpaces)
    {
        if (itemContainer == null)
        {
            return false;
        }

        return (28 - itemContainer.count()) >= requiredFreeSpaces;
    }

    /**
     * Get the items ids from a container as a stream.
     */
    private static Stream<Integer> streamItemIdsFromContainer(ItemContainer itemContainer)
    {
        if (itemContainer == null)
        {
            return Stream.of();
        }

        return Arrays.stream(itemContainer.getItems()).map(Item::getId);
    }
}
