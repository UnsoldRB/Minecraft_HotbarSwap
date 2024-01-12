package com.unsoldriceball.hotbarswap;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;




public class Packet implements IMessage
{
    private UUID uuid;
    private int dim;




    //コンストラクタ
    public Packet()
    {
        // 空のコンストラクタが必要らしい。
    }


    //コンストラクタ2個目
    public Packet(UUID uuid, Integer dim)
    {
        //パケットのインスタンス作成時の引数を代入しておく。
        this.uuid = uuid;
        this.dim = dim;
    }




    //パケット送信時の送り元が行う処理
    @Override
    public void toBytes(ByteBuf buf)
    {
        //ここでデータを書き込む。
        buf.writeLong(uuid.getMostSignificantBits());
        buf.writeLong(uuid.getLeastSignificantBits());
        buf.writeInt(dim);
    }



    //パケット受信時の送り先が行う処理
    @Override
    public void fromBytes(ByteBuf buf)
    {
        //toBytesで書き込んだ順番で読み取れる。
        long mostSignificantBits = buf.readLong();
        long leastSignificantBits = buf.readLong();
        uuid = new UUID(mostSignificantBits, leastSignificantBits);
        dim = buf.readInt();
    }




    //パケット受信時に使うクラス。
    public static class Handler implements IMessageHandler<Packet, IMessage>
    {
        //メッセージを受け取った時に発動するイベント。
        @Override
        public IMessage onMessage(Packet message, MessageContext ctx)
        {
            //messageからデータを読み取る。
            UUID uuid = message.uuid;
            int dim = message.dim;


            final EntityPlayer LP = DimensionManager.getWorld(dim).getPlayerEntityByUUID(uuid);

            //プレイヤーが存在した場合(プレイヤーのデータを正常に読み取れた場合)
            if (LP != null)
            {
                //ホットバーと対象のインベントリの列を入れ替える。
                for (int _i = 0; _i < 9; _i++)
                {
                    swapItem(LP, _i, _i + (9 * Main.target_slot));
                }
                //変更の適用(通信)
                LP.inventoryContainer.detectAndSendChanges();

                //効果音再生
                if (Main.play_sound)
                {
                    LP.world.playSound(null, LP.getPosition(), SoundEvents.ENTITY_HORSE_SADDLE, SoundCategory.PLAYERS, 0.5f, 1.24f);
                }
            }
            return null;
        }



        //指定したスロット同士のアイテムを入れ替える関数。
        public static void swapItem(EntityPlayer p, int source, int target)
        {
            ItemStack i_source = p.inventory.getStackInSlot(source);
            ItemStack i_target = p.inventory.getStackInSlot(target);

            if (i_source != ItemStack.EMPTY || i_target != ItemStack.EMPTY)
            {
                p.inventory.setInventorySlotContents(source, i_target);
                p.inventory.setInventorySlotContents(target, i_source);
            }
        }
    }
}