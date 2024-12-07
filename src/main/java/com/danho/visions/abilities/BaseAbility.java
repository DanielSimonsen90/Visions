package com.danho.visions.abilities;

import com.danho.models.MobEffectData;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.*;

public abstract class BaseAbility {
    public abstract void onPassiveUsed();

    protected ServerPlayer getServerPlayer() {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) return null;

      assert Minecraft.getInstance().player != null;
      return server.getPlayerList().getPlayer(Minecraft.getInstance().player.getUUID());
    }

    protected int getTickDurationFromSeconds(int seconds) {
        // Default tick rate is 20 ticks per second.
        // According to Chat-GBT, Minecraft does not expose this value as a constant.
        int DEFAULT_TICK_RATE = 20;
        return seconds * DEFAULT_TICK_RATE;
    }

    // Helper method to get the block the player is looking at
    protected BlockHitResult getPlayerLookAtBlock(ServerPlayer player) {
        Vec3 eyePos = player.getEyePosition(1.0F);  // Get the player's eye position
        Vec3 lookDirection = player.getViewVector(1.0F);  // Get the direction the player is looking

        double reachDistance = 5.0;  // Max distance for ray trace, can be adjusted
        Vec3 targetPos = eyePos.add(
          lookDirection.x * reachDistance,
          lookDirection.y * reachDistance,
          lookDirection.z * reachDistance);

        // Perform the ray trace (clip) to get the BlockHitResult

      return player.level().clip(new ClipContext(eyePos, targetPos, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));  // Return the BlockHitResult
    }


}
