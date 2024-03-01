package com.danho.visions.abilities;

import net.minecraft.client.Minecraft;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

public class AirAbility extends BaseAbility {
    public static final AirAbility INSTANCE = new AirAbility();
    private static final int ABILITY_DURATION = 10;
    private static final int ABILITY_AMPLIFIER = 0;

    private AirAbility() {
        super();
    }

    @Override
    public void onPassiveUsed() {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        boolean isSprinting = player.isSprinting();
        boolean isJumping = player.isFallFlying();
        boolean isFalling = player.fallDistance > 3;
        int calculatedAbilityDuration = ABILITY_DURATION * 20;

        // Handle mob effects per condition
        if (isSprinting) player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, calculatedAbilityDuration, ABILITY_AMPLIFIER));
        if (isJumping) player.addEffect(new MobEffectInstance(MobEffects.LEVITATION, calculatedAbilityDuration, ABILITY_AMPLIFIER));
        if (isFalling) player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, calculatedAbilityDuration, ABILITY_AMPLIFIER));

        // TODO: Clear mob effects after duration, because they don't do that by default?
    }
}
