package com.danho.visions.abilities;

import com.danho.models.MobEffectData;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
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
}
