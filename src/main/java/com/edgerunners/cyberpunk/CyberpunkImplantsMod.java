package com.edgerunners.cyberpunk;

import com.edgerunners.cyberpunk.effect.ModEffects;
import com.edgerunners.cyberpunk.item.ModItems;
import com.edgerunners.cyberpunk.network.ModNetworking;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CyberpunkImplantsMod implements ModInitializer {
    public static final String MOD_ID = "cyberpunk_implants";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static final ItemGroup CYBERPUNK_GROUP = FabricItemGroupBuilder.build(
            new Identifier(MOD_ID, "cyberware"),
            () -> new ItemStack(ModItems.SANDEVISTAN)
    );

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Cyberpunk: Edgerunners [1:1 Edition]...");
        ModEffects.registerEffects();
        ModItems.registerItems();
        ModNetworking.registerServerPackets();
    }
}
