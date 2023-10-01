package com.danho.models;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class UseContext {
    @NotNull public final Level level;
    @NotNull public final Player player;
    @NotNull public final InteractionHand hand;

    public UseContext(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        this.level = level;
        this.player = player;
        this.hand = hand;
    }
}
