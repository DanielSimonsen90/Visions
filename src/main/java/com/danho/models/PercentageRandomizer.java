package com.danho.models;

import oshi.util.tuples.Pair;

import java.lang.reflect.Array;
import java.util.*;

public class PercentageRandomizer<TItem> {
    /**
     * The number of times the randomizer should fail before returning default random item. (Not taking percentages into account)
     */
    private static final int FAIL_LIMIT = 3;

    /**
     * The internal value of the randomizer.
     * The key is the percentage and the value a list of items, as there can be multiple items with the same percentage.
     */
    private final Map<Double, List<TItem>> items = new TreeMap<>();

    /**
     * The randomizer used to get random values.
     */
    private final Random random = new Random();


    /**
     * Adds an item to the randomizer.
     * @param percentage The percentage of the item.
     * @param item The item to add.
     */
    public void add(double percentage, TItem item) {
        if (percentage < 0 || percentage > 100) throw new IllegalArgumentException("Percentage must be between 0 and 100");

        if (!items.containsKey(percentage)) items.put(percentage, new ArrayList<>());
        items.get(percentage).add(item);
    }

    /**
     * Gets a random item from the randomizer.
     * This uses a "lucky wheel" algorithm to get the random item.
     * @return A random item from {@link PercentageRandomizer#items}
     * @see PercentageRandomizer#calculateLuckyWheel()
     * @see PercentageRandomizer#getRandom(boolean)
     * @see PercentageRandomizer#FAIL_LIMIT
     */
    public TItem getRandom() {
        // If there are no items, return null.
        if (items.isEmpty()) return null;

        // Initialize the lucky wheel and fail count.
        List<Pair<Integer, TItem>> luckyWheel = calculateLuckyWheel();
        int failCount = 0;

        // Loop until the fail count is greater than the fail limit.
        while (failCount < FAIL_LIMIT) {
            // Get a random value between 0 and 100.
            double randomValue = random.nextDouble() * 100.0;
            // Store the last percentage for range comparison.
            int lastPercentage = 0;

            // Loop through the lucky wheel and get the items that correspond to the random value.
            for (Pair<Integer, TItem> pair : luckyWheel) {
                // Get the last range percentage and the current range percentage.
                Integer percentage = pair.getA(); // Note pair.getA() and pair.getB() means pair.getKey() and pair.getValue()

                // If randomValue falls into the percentage range of "pair", return the item.
                if (randomValue <= percentage
                && randomValue > lastPercentage) {
                    return pair.getB();
                }

                // Set the last percentage to the current percentage.
                lastPercentage = percentage;
            }

            // If there are no selected items, increment the fail count and try again.
            failCount++;
        }

        // If algorithm fails more than FAIL_COUNT times, return a random item without taking percentages into account.
        return luckyWheel.get(random.nextInt(luckyWheel.size())).getB();
    }

    /**
     * Gets a random item from the randomizer and clears the items.
     * This uses a "lucky wheel" algorithm to get the random item.
     * @param clear Whether to clear the items after getting a random item.
     * @return A random item from {@link PercentageRandomizer#items}
     * @see PercentageRandomizer#getRandom()
     */
    public TItem getRandom(boolean clear) {
        TItem item = getRandom();
        if (clear) items.clear();
        return item;
    }

    /**
     * Transforms {@link PercentageRandomizer#items} into a "lucky wheel"-like structure.
     * @return A list of pairs, where the key is the max percentage range and the value is the item.
     * @see PercentageRandomizer#items
     * @see PercentageRandomizer#getRandom()
     */
    private List<Pair<Integer, TItem>> calculateLuckyWheel() {
        // Calculate total percentage (all items' percentages added together).
        int totalPercentage = items.keySet().stream().mapToInt(Double::intValue).sum();
        // Calculate adjustment factor (100% / totalPercentage). Convert to 2 decimal places.
        double adjustmentFactor = Math.round((100.0 / totalPercentage) * 100.0) / 100.0;

        // Aggregate/Reduce calculatedItems from items, indexing items.keys() and multiplying by adjustmentFactor.
        // e.g. [100, [AIR, FIRE, WATER]] -> [57.14, [AIR, FIRE, WATER]]
        List<Pair<Double, List<TItem>>> calculatedItems = new ArrayList<>();

        for (Double percentage : items.keySet()) {
            List<TItem> values = this.items.get(percentage);
            // This value redefines its original percentage value.
            // e.g. 100% -> 57.14% if there are 3 items with 100% percentage.
            double calculatedPercentage = percentage * adjustmentFactor;
            calculatedItems.add(new Pair<>(calculatedPercentage, values));
        }

        // Sort calculatedItems by percentage in descending order.
        // This ensures that the max percentage range is always the first item in the list.
        calculatedItems.sort((a, b) -> b.getA().compareTo(a.getA()));

        ArrayList<Pair<Integer, TItem>> luckyWheel = new ArrayList<>();
        int minPercentage = 0; // This serves as min percentage range.

        // Loop through calculatedItems and add the max percentage range and the item to the lucky wheel.
        for (Pair<Double, List<TItem>> pair : calculatedItems) {
            // Split the percentage range into the number of items in the pair.
            // e.g. 57.14% / 3 = 19.05%
            // This is then converted to an integer to be used as the max percentage range.
            int maxPercentage = (int)(pair.getA() / pair.getB().size());

            // Loop through the items in the pair and add the max percentage range and the item to the lucky wheel.
            for (TItem item : pair.getB()) {
                // Add the max percentage range and the item to the lucky wheel.
                luckyWheel.add(new Pair<>(minPercentage + maxPercentage, item));

                // Increment the cumulative min percentage.
                minPercentage += maxPercentage;
            }
        }

        // Return the lucky wheel.
        return luckyWheel;
    }
}

/*
items: [
    [100, [AIR, FIRE, WATER]],
    [50, [AIR, FIRE]],
    [25, [EARTH]]
]

totalPercentages = items.keys().sum() = 175
adjustmentFactor = 100% / totalPercentages = 100% / 175 = 0.5714

calculatedItems: [
    [100 * adjustmentFactor = 57.14, [AIR, FIRE, WATER]],
    [50 * adjustmentFactor = 28.57, [AIR, FIRE]],
    [25 * adjustmentFactor = 14.29, [EARTH]]
]

luckyWheel variable should loop through entries of calculatedItems.
It should get a number based on length of values in the entry and the key of the entry.
For example, would index 0 be 57.14 / 3 = 19.05
luckyWheel should then accumulate the percentage ranges so that it can be compared to the randomValue.
For example, luckyWheel[0] = 19.05, luckyWheel[1] = 38.10, luckyWheel[2] = 57.15

luckyWheel = [19.05, 38.10, 57.15, 71.44, 85.73, 100.02]
luckyWheel = [
    [19, AIR],
    [38, FIRE],
    [57, WATER],
    [71, AIR],
    [85, FIRE],
    [100, EARTH]
]
randomValue should be a random number between 0 and 100.
For example, randomValue = 40.00

selectedItems should be the items that correspond to the luckyWheel index that is less than or equal to the randomValue.
For example, selectedItems = [AIR, FIRE]
selectedItems should return a random item from the selectedItems.
*/