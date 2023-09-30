package com.danho.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class ReturnToHomeCommand {
    public ReturnToHomeCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("home").executes(command -> {
            ServerPlayer player = command.getSource().getPlayer();
            if (player == null) return 0;

            String name = command.getSource().getServer().getWorldData().getLevelName();
            if (!name.equals("Development")) {
                player.sendSystemMessage(Component.literal("You can only use this command on the development server."));
                return 0;
            }

            player.teleportTo(114.5, 64, -77.5);
            return 1;
        }));
    }
}
