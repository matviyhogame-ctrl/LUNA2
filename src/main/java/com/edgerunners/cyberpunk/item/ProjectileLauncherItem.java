package com.edgerunners.cyberpunk.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class ProjectileLauncherItem extends ImplantItem {
    public ProjectileLauncherItem(Settings settings) {
        super(settings, 14.0F, 100);
    }

    @Override
    protected void onActivate(World world, PlayerEntity player, ItemStack stack) {
        Vec3d look = player.getRotationVector();
        SmallFireballEntity rocket = new SmallFireballEntity(
                world, player,
                look.x * 2.5, look.y * 2.5, look.z * 2.5
        ) {
            @Override
            protected void onCollision(net.minecraft.util.hit.HitResult hitResult) {
                super.onCollision(hitResult);
                if (!this.world.isClient) {
                    this.world.createExplosion(player, this.getX(), this.getY(), this.getZ(), 2.2F, Explosion.DestructionType.NONE);
                    this.remove();
                }
            }
        };
        rocket.updatePosition(player.getX() + look.x * 1.2, player.getEyeY() - 0.1, player.getZ() + look.z * 1.2);
        world.spawnEntity(rocket);
        world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH, SoundCategory.PLAYERS, 1.0F, 0.7F);
    }
}
