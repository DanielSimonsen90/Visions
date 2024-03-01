package com.danho.util;

import com.danho.visions.Visions;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyBinding {
    public static final String KEY_CATEGORY = "key." + Visions.MOD_ID + ".category";
    public static final String KEY_USE_ABILITY = "key." + Visions.MOD_ID + ".ability";

    public static final KeyMapping ABILITY_KEY = new KeyMapping(
        KEY_USE_ABILITY,
        KeyConflictContext.IN_GAME,
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_G, // G key by default
        KEY_CATEGORY);
}
