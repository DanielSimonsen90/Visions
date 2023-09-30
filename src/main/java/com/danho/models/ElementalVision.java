package com.danho.models;

import com.danho.visions.item.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
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
        VisionElementalTypes type = null;

        if (isAirCondition(level, player, hand)) {
            type = VisionElementalTypes.AIR;
        }

        if (type == null) return;

        ItemStack vision = getVision(type);
        if (vision == null) return;

        player.setItemInHand(hand, vision);
        // You feel the power of {type} flowing through you.
        player.sendSystemMessage(Component.literal("You feel the power of " + type + " flowing through you."));
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
    private static boolean isAirCondition(
            @NotNull Level level,
            @NotNull Player player,
            @NotNull InteractionHand hand
    ) {
        return player.getY() >= 220;
    }

    public @NotNull InteractionResultHolder<ItemStack> use(
            @NotNull Level level,
            @NotNull Player player,
            @NotNull InteractionHand interactionHand
    ) {
        var result = super.use(level, player, interactionHand);

        player.getMainHandItem().hurtAndBreak(1, player, (p_220038_1_) -> {
            p_220038_1_.broadcastBreakEvent(interactionHand);
        });

        return result;
    }
}
