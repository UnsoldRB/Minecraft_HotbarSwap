package com.unsoldriceball.hotbarswap;


import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


@Mod(modid = Main.MOD_ID, acceptableRemoteVersions = "*")
public class Main
{
    final public static String MOD_ID = "hotbarswap";
    private Configuration cfg;
    public static boolean PLAY_SOUND;
    public static int TARGET_SLOT;
    public static SimpleNetworkWrapper networkWrapper;



    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) //ModがInitializeを呼び出す前に発生するイベント。
    {
        MinecraftForge.EVENT_BUS.register(this); //これでこのクラス内でForgeのイベントが動作するようになるらしい。

        //Configを起動
        cfg = new Configuration(event.getSuggestedConfigurationFile());
        this.loadConfig();

        if (event.getSide() == Side.CLIENT)
        {
            KeyBind.init(); //キーバインドを初期化
        }
        //---

        networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel("hotbarswap_packet");
        networkWrapper.registerMessage(Packet.Handler.class, Packet.class, 0, Side.SERVER);
    }



    //Config読み込み関数
    private void loadConfig(){
        cfg.load();
        PLAY_SOUND = cfg.get("general", "C_PLAY_SOUND", true).getBoolean();
        TARGET_SLOT = cfg.get("general", "C_TARGET_SLOT", 1).getInt();
    }



    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onKeyInput(KeyInputEvent event)
    {
        if (KeyBind.key_hotbarswap.isPressed())
        {
            //仕方がないのでPacketを送信する。それ以外の手段は見つからなかった。
            final EntityPlayer P = Minecraft.getMinecraft().player;
            networkWrapper.sendToServer(new Packet(P.getUniqueID(), P.dimension));
        }
    }
}