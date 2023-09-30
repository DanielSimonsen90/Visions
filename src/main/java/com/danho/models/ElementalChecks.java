package com.danho.models;

import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;

public class ElementalChecks {
    private  ElementalChecks() {}
    public static final ElementalCheck[] checks = new ElementalCheck[] {
        ElementalChecks::checkAirCondition,
        ElementalChecks::checkFireCondition,
        ElementalChecks::checkWaterCondition
    };
    private static void checkAirCondition(UseContext context, PercentageRandomizer<VisionElementalTypes> randomizer) {
        if (context.player.getY() >= 220) randomizer.add(100, VisionElementalTypes.AIR);
    }
    private static void checkFireCondition(UseContext context, PercentageRandomizer<VisionElementalTypes> randomizer) {
        Biome biome = context.level.getBiome(context.player.blockPosition()).get();
        boolean isHot = biome.getBaseTemperature() >= 1.0F; // Savanna, Desert, Mesa, Nether

        if (isHot) randomizer.add(100, VisionElementalTypes.FIRE);
    }

    private static void checkWaterCondition(UseContext context, PercentageRandomizer<VisionElementalTypes> randomizer) {
        Holder<Biome> biomeHolder = context.level.getBiome(context.player.blockPosition());
        boolean isInWater = context.player.getFeetBlockState().is(Blocks.WATER);
        boolean isInDeepOceanBiome = biomeHolder.is(BiomeTags.IS_DEEP_OCEAN);
        boolean shouldFreeze = !biomeHolder.get().warmEnoughToRain(context.player.blockPosition());

        if (isInWater && isInDeepOceanBiome && !shouldFreeze) randomizer.add(100, VisionElementalTypes.WATER);
    }
}
