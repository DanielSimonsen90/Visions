package com.danho.visions.item;

import com.danho.visions.Visions;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Visions.MOD_ID);

    public static final RegistryObject<CreativeModeTab> visions_tab = CREATIVE_MODE_TABS.register("visions_tab", () -> CreativeModeTab.builder()
        .icon(() -> new ItemStack(ModItems.VISION.get()))
        .title(Component.translatable("creativetab.visions_tab"))
        .displayItems((pParameters, pOutput) -> ModItems.VISIONS.forEach(vision -> pOutput.accept(vision.get()))).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
