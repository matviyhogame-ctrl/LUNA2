package com.edgerunners.cyberpunk.item;

import com.edgerunners.cyberpunk.CyberpunkImplantsMod;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

public class ModItems {
    public static Item SANDEVISTAN;
    public static Item PROJECTILE_LAUNCHER;
    public static Item MANTIS_BLADES;
    public static Item GORILLA_ARMS;
    public static Item MONOWIRE;
    public static Item OPTICAL_CAMO;
    public static Item REINFORCED_TENDONS;
    public static Item IMMUNOBLOCKER;
    public static Item NEURAL_PROCESSOR;

    public static void registerItems() {
        SANDEVISTAN = register("sandevistan", new SandevistanItem(
                new Item.Settings().group(CyberpunkImplantsMod.CYBERPUNK_GROUP).maxCount(1).rarity(Rarity.EPIC)));

        PROJECTILE_LAUNCHER = register("projectile_launcher", new ProjectileLauncherItem(
                new Item.Settings().group(CyberpunkImplantsMod.CYBERPUNK_GROUP).maxCount(1).rarity(Rarity.EPIC)));

        MANTIS_BLADES = register("mantis_blades", new CyberWeaponItem(
                CyberWeaponItem.WeaponType.MANTIS_BLADES, 8, -1.9F,
                new Item.Settings().group(CyberpunkImplantsMod.CYBERPUNK_GROUP).maxCount(1).rarity(Rarity.RARE)));

        GORILLA_ARMS = register("gorilla_arms", new CyberWeaponItem(
                CyberWeaponItem.WeaponType.GORILLA_ARMS, 11, -2.7F,
                new Item.Settings().group(CyberpunkImplantsMod.CYBERPUNK_GROUP).maxCount(1).rarity(Rarity.RARE)));

        MONOWIRE = register("monowire", new CyberWeaponItem(
                CyberWeaponItem.WeaponType.MONOWIRE, 7, -1.7F,
                new Item.Settings().group(CyberpunkImplantsMod.CYBERPUNK_GROUP).maxCount(1).rarity(Rarity.RARE)));

        OPTICAL_CAMO = register("optical_camo", new OpticalCamoItem(
                new Item.Settings().group(CyberpunkImplantsMod.CYBERPUNK_GROUP).maxCount(1).rarity(Rarity.RARE)));

        REINFORCED_TENDONS = register("reinforced_tendons", new ReinforcedTendonsItem(
                new Item.Settings().group(CyberpunkImplantsMod.CYBERPUNK_GROUP).maxCount(1).rarity(Rarity.UNCOMMON)));

        IMMUNOBLOCKER = register("immunoblocker", new ImmunoblockerItem(
                new Item.Settings().group(CyberpunkImplantsMod.CYBERPUNK_GROUP).maxCount(16).rarity(Rarity.UNCOMMON)));

        NEURAL_PROCESSOR = register("neural_processor", new Item(
                new Item.Settings().group(CyberpunkImplantsMod.CYBERPUNK_GROUP).maxCount(64)));
    }

    private static Item register(String name, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(CyberpunkImplantsMod.MOD_ID, name), item);
    }
}
