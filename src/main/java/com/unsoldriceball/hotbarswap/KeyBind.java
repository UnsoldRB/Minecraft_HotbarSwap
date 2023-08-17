package com.unsoldriceball.hotbarswap;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class KeyBind
{
    public static KeyBinding key_hotbarswap;

    public static void init()
    {
        // キーバインドを作成
        key_hotbarswap = new KeyBinding("hotbar swap key", Keyboard.KEY_V, "HotbarSwap");

        // キーバインドを登録
        ClientRegistry.registerKeyBinding(key_hotbarswap);
    }
}