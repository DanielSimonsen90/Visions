package com.danho.visions.item;

import com.danho.models.Vision;
import com.danho.visions.Visions;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Visions.MOD_ID);

    //region Visions
    public static final RegistryObject<Item> VISION = ITEMS.register("vision", () -> new Vision(new Item.Properties()));
    public static final RegistryObject<Item> VISION_AIR = ITEMS.register("vision_air", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> VISION_FIRE = ITEMS.register("vision_fire", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> VISION_WATER = ITEMS.register("vision_water", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> VISION_EARTH = ITEMS.register("vision_earth", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> VISION_GRASS = ITEMS.register("vision_grass", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> VISION_ELECTRIC = ITEMS.register("vision_electric", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> VISION_ICE = ITEMS.register("vision_ice", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> VISION_GHOST = ITEMS.register("vision_ghost", () -> new Item(new Item.Properties()));
    public static final List<RegistryObject<Item>> VISIONS = List.of(
        VISION,
        VISION_AIR,
        VISION_FIRE,
        VISION_WATER,
        VISION_EARTH,
        VISION_GRASS,
        VISION_ELECTRIC,
        VISION_ICE,
        VISION_GHOST);
    //endregion Visions
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
