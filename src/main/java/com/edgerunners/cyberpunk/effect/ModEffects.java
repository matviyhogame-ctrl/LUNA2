package com.edgerunners.cyberpunk.effect;

import com.edgerunners.cyberpunk.CyberpunkImplantsMod;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModEffects {
    public static StatusEffect CYBERPSYCHOSIS;
    public static StatusEffect SANDEVISTAN_OVERDRIVE;

    public static void registerEffects() {
        CYBERPSYCHOSIS = Registry.register(
                Registry.STATUS_EFFECT,
                new Identifier(CyberpunkImplantsMod.MOD_ID, "cyberpsychosis"),
                new CyberpsychosisEffect(StatusEffectType.HARMFUL, 0xFF1E3C)
        );
        SANDEVISTAN_OVERDRIVE = Registry.register(
                Registry.STATUS_EFFECT,
                new Identifier(CyberpunkImplantsMod.MOD_ID, "sandevistan_overdrive"),
                new SandevistanEffect(StatusEffectType.BENEFICIAL, 0x00FFAA)
        );
    }
}
