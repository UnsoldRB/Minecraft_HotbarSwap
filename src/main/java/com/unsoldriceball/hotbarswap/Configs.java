package com.unsoldriceball.hotbarswap;

import net.minecraftforge.common.config.Config;



@SuppressWarnings("unused")
@Config(modid = Main.MOD_ID)
final public class Configs
{
    @Config.Comment("If set to false, this mod won't play sound when swap.")
    public static boolean C_PLAY_SOUND = true;
    @Config.RangeInt(min = 1, max = 3)
    @Config.Comment("Select line of swap target slot.")
    public static int C_TARGET_SLOT = 1;
}