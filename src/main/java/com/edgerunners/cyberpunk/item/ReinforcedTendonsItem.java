package com.edgerunners.cyberpunk.item;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ReinforcedTendonsItem extends ImplantItem {
    public ReinforcedTendonsItem(Settings settings) {
        super(settings, 5.0F, 45);
    }

    @Override
    protected void onActivate(World world, PlayerEntity player, ItemStack stack) {
        Vec3d look = player.getRotationVector();
        player.addVelocity(look.x * 0.75, 0.92, look.z * 0.75);
        player.velocityModified = true;
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 70, 0, false, false));
    }
}
