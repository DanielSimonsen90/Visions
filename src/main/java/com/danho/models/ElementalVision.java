package com.danho.models;

import com.danho.visions.item.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ElementalVision extends Vision {
    //region statics
    private static final PercentageRandomizer<VisionElementalTypes> randomizer = new PercentageRandomizer<>();
    public static void checkElementalCondition(UseContext context) {
        VisionElementalTypes type = getVisionElementalType(context);
        if (type == null) {
            context.player.sendSystemMessage(Component.literal("You feel nothing in this area."));
            return;
        }

        ItemStack vision = getVision(type);
        if (vision == null) throw new NullPointerException("Vision is null");

        Player player = context.player;
        player.setItemInHand(context.hand, vision);
        player.sendSystemMessage(Component.literal("You feel the power of " + type + " flowing through you."));
    }

    private static @Nullable VisionElementalTypes getVisionElementalType(UseContext context) {
        for (ElementalCheck check : ElementalChecks.checks) {
            check.checkElementalType(context, randomizer);
        }

        return randomizer.getRandom(true);
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

    //endregion

    public VisionElementalTypes type;

    public ElementalVision(Properties properties, VisionElementalTypes type) {
        super(properties);
        this.type = type;
    }

    public @NotNull InteractionResultHolder<ItemStack> use(
        @NotNull Level level,
        @NotNull Player player,
        @NotNull InteractionHand hand
    ) {
        var result = super.use(level, player, hand);
        if (result.getResult() == InteractionResult.FAIL || level.isClientSide()) return result;

        player.getMainHandItem().hurtAndBreak(1, player, (p) -> {
            p.broadcastBreakEvent(hand);
        });

        return InteractionResultHolder.success(player.getItemInHand(hand));
    }
}
