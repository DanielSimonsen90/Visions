package com.danho.models;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/*
 * https://www.youtube.com/watch?v=8ubLZbekPZw&ab_channel=ModdingbyKaupenjoe
 */
public class Vision extends Item implements Equipable {
    public Vision(Properties properties) {
        super(properties);
    }

    public boolean isDamageable(ItemStack stack) {
        return true;
    }

    public int getMaxDamage(ItemStack stack) {
        return 100;
    }

    @Override
    public @NotNull EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.LEGS;
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
