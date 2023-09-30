package com.danho.models;

import com.danho.visions.item.ModItems;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ElementalVision extends Vision {
    public ElementalVision(Properties properties) {
        super(properties);
    }

    public static void checkElementalCondition(
        @NotNull Level level,
        @NotNull Player player,
        @NotNull InteractionHand hand
    ) {
        VisionElementalTypes type = getVisionElementalType(level, player, hand);
        if (type == null) return;

        ItemStack vision = getVision(type);
        if (vision == null) throw new NullPointerException("Vision is null");

        player.setItemInHand(hand, vision);
        player.sendSystemMessage(Component.literal("You feel the power of " + type + " flowing through you."));
    }

    private static @Nullable VisionElementalTypes getVisionElementalType(
        @NotNull Level level,
        @NotNull Player player,
        @NotNull InteractionHand hand
    ) {
        VisionElementalTypes type = checkAirCondition(level, player, hand);
        if (type != null) return type;

        type = checkFireCondition(level, player, hand);
        if (type != null) return type;

        return null;
    }
    private static @Nullable ItemStack getVision(VisionElementalTypes type) {
        return switch (type) {
            case AIR -> new ItemStack(ModItems.VISION_AIR.get());
            case FIRE -> new ItemStack(ModItems.VISION_FIRE.get());
            case WATER -> new ItemStack(ModItems.VISION_WATER.get());
            case EARTH -> new ItemStack(ModItems.VISION_EARTH.get());
            case GRASS -> new ItemStack(ModItems.VISION_GRASS.get());
            case ELECTRIC -> new ItemStack(ModItems.VISION_ELECTRIC.get());
            case ICE -> new ItemStack(ModItems.VISION_ICE.get());
            case GHOST -> new ItemStack(ModItems.VISION_GHOST.get());
            default -> null;
        };
    }
    private static @Nullable VisionElementalTypes checkAirCondition(
        @NotNull Level level,
        @NotNull Player player,
        @NotNull InteractionHand hand
    ) {
        return player.getY() >= 220 ? VisionElementalTypes.AIR : null;
    }
    private static @Nullable VisionElementalTypes checkFireCondition(
        @NotNull Level level,
        @NotNull Player player,
        @NotNull InteractionHand hand
    ) {
        Biome biome = level.getBiome(player.blockPosition()).get();
        boolean isHot = biome.getBaseTemperature() >= 1.0F; // Savanna, Desert, Mesa, Nether

        return isHot ? VisionElementalTypes.FIRE : null;
    }

    public @NotNull InteractionResult useOn(
        @NotNull Level level,
        @NotNull Player player,
        @NotNull InteractionHand hand
    ) {
        var result = super.use(level, player, hand).getResult();
        if (result == InteractionResult.FAIL || level.isClientSide()) return result;

        player.getMainHandItem().hurtAndBreak(1, player, (p) -> {
            p.broadcastBreakEvent(hand);
        });

        return InteractionResult.SUCCESS;
    }
}
