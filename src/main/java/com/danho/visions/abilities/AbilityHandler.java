package com.danho.visions.abilities;

import com.danho.models.ElementalVision;
import com.danho.models.Vision;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class AbilityHandler {
    public static final AbilityHandler INSTANCE = new AbilityHandler();
    private ItemStack getVisionItem() {
        Player player = Minecraft.getInstance().player;
        if (player == null) return ItemStack.EMPTY;

        // Check armor slots
        for (ItemStack itemStack : player.getArmorSlots()) {
            if (itemStack.getItem() instanceof ElementalVision) return itemStack;
        }

        // Check inventory
        for (ItemStack itemStack : player.getInventory().items) {
            if (itemStack.getItem() instanceof ElementalVision) return itemStack;
        }

        return ItemStack.EMPTY;
    }

    public void checkPassiveAbility() {
        ItemStack visionItem = getVisionItem();
        if (visionItem.isEmpty()) return;

        ElementalVision vision = (ElementalVision) visionItem.getItem();

        switch (vision.type) {
            case AIR -> AirAbility.INSTANCE.onPassiveUsed();
            default -> throw new IllegalStateException("Unexpected value: " + vision.type);
        }
    }
}
