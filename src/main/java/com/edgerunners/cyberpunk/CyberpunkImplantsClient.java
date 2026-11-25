package com.edgerunners.cyberpunk;

import com.edgerunners.cyberpunk.network.ModNetworking;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.PacketByteBuf;
import org.lwjgl.glfw.GLFW;

public class CyberpunkImplantsClient implements ClientModInitializer {
    public static KeyBinding sandevistanKey;
    public static KeyBinding launcherKey;

    @Override
    public void onInitializeClient() {
        ModNetworking.registerClientPackets();

        sandevistanKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.cyberpunk_implants.sandevistan",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_V,
                "category.cyberpunk_implants.keys"
        ));

        launcherKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.cyberpunk_implants.launcher",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_G,
                "category.cyberpunk_implants.keys"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (sandevistanKey.wasPressed()) {
                if (client.player != null) {
                    PacketByteBuf buf = PacketByteBufs.create();
                    buf.writeInt(0); // 0 = Sandevistan
                    ClientPlayNetworking.send(ModNetworking.ACTIVATE_IMPLANT_PACKET_ID, buf);
                }
            }
            while (launcherKey.wasPressed()) {
                if (client.player != null) {
                    PacketByteBuf buf = PacketByteBufs.create();
                    buf.writeInt(1); // 1 = Projectile Launcher
                    ClientPlayNetworking.send(ModNetworking.ACTIVATE_IMPLANT_PACKET_ID, buf);
                }
            }
        });
    }
}
