package com.edgerunners.cyberpunk.item;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class OpticalCamoItem extends ImplantItem {
    public OpticalCamoItem(Settings settings) {
        super(settings, 10.0F, 240);
    }

    @Override
    protected void onActivate(World world, PlayerEntity player, ItemStack stack) {
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, 240, 0, false, false));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 240, 1, false, false));
    }
}
