package com.edgerunners.cyberpunk.effect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class SandevistanEffect extends StatusEffect {
    public SandevistanEffect(StatusEffectType type, int color) {
        super(type, color);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (!entity.world.isClient) {
            // Сверхскорость Дэвида Мартинеса
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 10, 6, false, false));
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 10, 5, false, false));
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 10, 2, false, false));

            // Замедление врагов и летящих снарядов (стрел, фаерболов) в радиусе 18 блоков
            Box area = entity.getBoundingBox().expand(18.0);
            List<Entity> nearby = entity.world.getOtherEntities(entity, area);
            for (Entity target : nearby) {
                if (target instanceof LivingEntity) {
                    LivingEntity living = (LivingEntity) target;
                    living.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 15, 6, false, false));
                    living.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 15, 5, false, false));
                } else if (target instanceof ProjectileEntity) {
                    // Почти полная остановка пуль/стрел в воздухе как в аниме!
                    Vec3d vel = target.getVelocity();
                    target.setVelocity(vel.multiply(0.15));
                    target.velocityModified = true;
                }
            }

            // Радужно-неоновый шлейф остаточных силуэтов (Зелёный -> Жёлтый -> Голубой)
            if (entity.world instanceof ServerWorld) {
                ServerWorld sw = (ServerWorld) entity.world;
                float phase = (entity.age % 12) / 12.0F;
                float r = phase < 0.33F ? 0.0F : (phase < 0.66F ? 0.98F : 0.0F);
                float g = phase < 0.33F ? 1.0F : (phase < 0.66F ? 0.93F : 0.94F);
                float b = phase < 0.33F ? 0.66F : (phase < 0.66F ? 0.04F : 1.0F);

                DustParticleEffect afterimage = new DustParticleEffect(r, g, b, 1.35F);
                for (double h = 0.2; h <= 1.7; h += 0.35) {
                    sw.spawnParticles(afterimage, entity.getX(), entity.getY() + h, entity.getZ(), 2, 0.2, 0.05, 0.2, 0.0);
                }
            }
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
