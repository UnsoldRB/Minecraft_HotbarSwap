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

public class Packet implements IMessage {

    private UUID uuid;
    private int dim;

    public Packet()
    {
        // 空のコンストラクタが必要らしい。
    }



    public Packet(UUID uuid, Integer dim)
    {
        this.uuid = uuid;
        this.dim = dim;
    }


    @Override
    public void toBytes(ByteBuf buf) {

        buf.writeLong(uuid.getMostSignificantBits());
        buf.writeLong(uuid.getLeastSignificantBits());
        buf.writeInt(dim);
    }



    @Override
    public void fromBytes(ByteBuf buf) {
        long mostSignificantBits = buf.readLong();
        long leastSignificantBits = buf.readLong();
        uuid = new UUID(mostSignificantBits, leastSignificantBits);
        dim = buf.readInt();
    }



    public static class Handler implements IMessageHandler<Packet, IMessage> {

        @Override
        public IMessage onMessage(Packet message, MessageContext ctx) {
            UUID uuid = message.uuid;
            int dim = message.dim;

            final EntityPlayer P = DimensionManager.getWorld(dim).getPlayerEntityByUUID(uuid);

            if (P == null) return null;

            for (int i = 0; i < 9; i++)
            {
                swapItem(P, i, i + (9 * Main.TARGET_SLOT));
            }
            P.inventoryContainer.detectAndSendChanges(); //変更の適用

            if (Main.PLAY_SOUND) //効果音再生
            {
                P.world.playSound(null, P.getPosition(), SoundEvents.ENTITY_HORSE_SADDLE, SoundCategory.PLAYERS, 0.5f, 1.24f);
            }
            return null;
        }
    }



    public static void swapItem(EntityPlayer p, int source, int target) //指定したスロットのアイテムを入れ替える
    {
        ItemStack i_source = p.inventory.getStackInSlot(source);
        ItemStack i_target = p.inventory.getStackInSlot(target);

        if (i_source == ItemStack.EMPTY && i_target == ItemStack.EMPTY) return;

        p.inventory.setInventorySlotContents(source, i_target);
        p.inventory.setInventorySlotContents(target, i_source);
    }
}