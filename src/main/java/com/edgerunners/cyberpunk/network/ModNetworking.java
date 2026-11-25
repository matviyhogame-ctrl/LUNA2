package com.edgerunners.cyberpunk.network;

import com.edgerunners.cyberpunk.CyberpunkImplantsMod;
import com.edgerunners.cyberpunk.item.ImplantItem;
import com.edgerunners.cyberpunk.item.ModItems;
import com.edgerunners.cyberpunk.util.ICyberwareData;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class ModNetworking {
    public static final Identifier ACTIVATE_IMPLANT_PACKET_ID = new Identifier(CyberpunkImplantsMod.MOD_ID, "activate_implant");
    public static final Identifier SYNC_CYBER_DATA_ID = new Identifier(CyberpunkImplantsMod.MOD_ID, "sync_cyber_data");

    public static void registerServerPackets() {
        ServerPlayNetworking.registerGlobalReceiver(ACTIVATE_IMPLANT_PACKET_ID, (server, player, handler, buf, responseSender) -> {
            int type = buf.readInt();
            server.execute(() -> {
                Item targetItem = (type == 0) ? ModItems.SANDEVISTAN : ModItems.PROJECTILE_LAUNCHER;
                for (int i = 0; i < player.inventory.size(); i++) {
                    ItemStack stack = player.inventory.getStack(i);
                    if (stack.getItem() == targetItem && stack.getItem() instanceof ImplantItem) {
                        ((ImplantItem) stack.getItem()).triggerImplant(player.world, player, stack);
                        return;
                    }
                }
            });
        });
    }

    public static void registerClientPackets() {
        ClientPlayNetworking.registerGlobalReceiver(SYNC_CYBER_DATA_ID, (client, handler, buf, responseSender) -> {
            float psycho = buf.readFloat();
            int sandyTicks = buf.readInt();
            client.execute(() -> {
                if (client.player instanceof ICyberwareData) {
                    ((ICyberwareData) client.player).setCyberpsychosisLevel(psycho);
                    ((ICyberwareData) client.player).setSandevistanTicks(sandyTicks);
                }
            });
        });
    }

    public static void syncCyberData(ServerPlayerEntity player) {
        if (player instanceof ICyberwareData) {
            ICyberwareData data = (ICyberwareData) player;
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeFloat(data.getCyberpsychosisLevel());
            buf.writeInt(data.getSandevistanTicks());
            ServerPlayNetworking.send(player, SYNC_CYBER_DATA_ID, buf);
        }
    }
}
