package com.danho.models;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

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

    public int getMaxStackSize(ItemStack stack) {
        return 1;
    }

    @Override
    public @NotNull EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.LEGS;
    }

    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        if (super.use(level, player, hand).getResult() == InteractionResult.FAIL
        || level.isClientSide()) {
            return InteractionResultHolder.pass(player.getItemInHand(hand));
        }

        ElementalVision.checkElementalCondition(new UseContext(level, player, hand));

        return InteractionResultHolder.success(player.getItemInHand(hand));
    }
}
