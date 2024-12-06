package com.danho.visions.abilities;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.levelgen.Heightmap;

public class AirAbility extends BaseAbility {
    public static final AirAbility INSTANCE = new AirAbility();
    private static final int ABILITY_AMPLIFIER = 2;
    private static final int MOVEMENT_SPEED_DURATION = 10;
    private static final int LEVITATION_DURATION = 2;
    private static final int SLOW_FALLING_DURATION = 3;
    private static final int FALL_THRESHOLD = 3;

    private AirAbility() {
        super();
    }

    @Override
    public void onPassiveUsed() {
        ServerPlayer player = getServerPlayer();
        if (player == null) return;

        handleSprinting(player);
        handleJumping(player);
        handleFalling(player);
        handleGliding(player);
    }

    private void handleSprinting(ServerPlayer player) {
        boolean isSprinting = player.isSprinting();

        if (isSprinting) player.addEffect(new MobEffectInstance(
          MobEffects.MOVEMENT_SPEED,
          this.getTickDurationFromSeconds(MOVEMENT_SPEED_DURATION),
          ABILITY_AMPLIFIER));
    }

    private void handleJumping(ServerPlayer player) {
        boolean isJumping = !player.onGround() && player.getDeltaMovement().y > 0;

        if (isJumping) player.addEffect(new MobEffectInstance(
          MobEffects.LEVITATION,
          this.getTickDurationFromSeconds(LEVITATION_DURATION),
          ABILITY_AMPLIFIER));
    }

    private void handleFalling(ServerPlayer player) {
        boolean isFalling = player.getY() - player.level().getHeightmapPos(
          Heightmap.Types.WORLD_SURFACE,
          new BlockPos(
            (int) player.getX(),
            (int) player.getY(),
            (int) player.getZ())
        ).getY() > FALL_THRESHOLD;

        if (isFalling) player.addEffect(new MobEffectInstance(
          MobEffects.SLOW_FALLING,
          this.getTickDurationFromSeconds(SLOW_FALLING_DURATION),
          ABILITY_AMPLIFIER));
    }

    private void handleGliding(ServerPlayer player) {
        boolean isGlidingWithElytra = player.isFallFlying();

        if (isGlidingWithElytra) player.addEffect(new MobEffectInstance(
          MobEffects.LEVITATION,
          this.getTickDurationFromSeconds(LEVITATION_DURATION),
          ABILITY_AMPLIFIER));
    }
}
