package com.edgerunners.cyberpunk.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

public class CyberpsychosisEffect extends StatusEffect {
    public CyberpsychosisEffect(StatusEffectType type, int color) {
        super(type, color);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (!entity.world.isClient) {
            // Киберпсихоз: безумная физическая сила, но галлюцинации, тремор и кровотечение из носа (урон)
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 40, amplifier + 2, false, false));
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 60, 0, false, false));

            if (amplifier >= 1) {
                if (entity.age % 30 == 0) {
                    entity.damage(DamageSource.MAGIC, 1.5F);
                }
                if (entity.age % 50 == 0) {
                    entity.world.playSound(null, entity.getBlockPos(), SoundEvents.ENTITY_ENDERMAN_SCREAM, SoundCategory.HOSTILE, 0.5F, 0.5F);
                }
            }
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
