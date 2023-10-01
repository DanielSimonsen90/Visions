package com.danho.events;

import com.danho.commands.ReturnToHomeCommand;
import com.danho.visions.Visions;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;

@Mod.EventBusSubscriber(modid = Visions.MOD_ID)
public class ModEvents {
    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event) {
        new ReturnToHomeCommand(event.getDispatcher());

        ConfigCommand.register(event.getDispatcher());
    }
}
