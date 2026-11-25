package com.edgerunners.cyberpunk.mixin;

import com.edgerunners.cyberpunk.effect.ModEffects;
import com.edgerunners.cyberpunk.network.ModNetworking;
import com.edgerunners.cyberpunk.util.ICyberwareData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityCyberMixin extends LivingEntity implements ICyberwareData {
    @Unique
    private float cyberpsychosisLevel = 0.0F; // От 0 до 100%

    @Unique
    private int sandevistanTicks = 0;

    protected PlayerEntityCyberMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public float getCyberpsychosisLevel() {
        return this.cyberpsychosisLevel;
    }

    @Override
    public void setCyberpsychosisLevel(float level) {
        this.cyberpsychosisLevel = MathHelper.clamp(level, 0.0F, 100.0F);
    }

    @Override
    public void addCyberpsychosis(float amount) {
        setCyberpsychosisLevel(this.cyberpsychosisLevel + amount);
    }

    @Override
    public int getSandevistanTicks() {
        return this.sandevistanTicks;
    }

    @Override
    public void setSandevistanTicks(int ticks) {
        this.sandevistanTicks = Math.max(0, ticks);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void onCyberTick(CallbackInfo ci) {
        if (this.sandevistanTicks > 0) {
            this.sandevistanTicks--;
        }

        if (!this.world.isClient) {
            // Медленное естественное восстановление нервной системы вне перегрузки
            if (this.sandevistanTicks == 0 && this.cyberpsychosisLevel > 0.0F && this.age % 20 == 0) {
                addCyberpsychosis(-0.25F);
            }

            // Пороги киберпсихоза как в аниме:
            // > 50% — тремор рук (усталость/тошнота), прилив адреналина
            // > 80% — острый киберпсихоз (безумная сила, потеря контроля, урон)
            if (this.cyberpsychosisLevel >= 80.0F) {
                this.addStatusEffect(new StatusEffectInstance(ModEffects.CYBERPSYCHOSIS, 40, 1, false, false));
            } else if (this.cyberpsychosisLevel >= 50.0F) {
                this.addStatusEffect(new StatusEffectInstance(ModEffects.CYBERPSYCHOSIS, 40, 0, false, false));
            }

            if ((Object) this instanceof ServerPlayerEntity && this.age % 5 == 0) {
                ModNetworking.syncCyberData((ServerPlayerEntity) (Object) this);
            }
        }
    }

    @Inject(method = "writeNbt", at = @At("TAIL"), require = 0)
    private void writeCyberNbt(NbtCompound tag, CallbackInfo ci) {
        tag.putFloat("CyberpsychosisLevel", this.cyberpsychosisLevel);
        tag.putInt("SandevistanTicks", this.sandevistanTicks);
    }

    @Inject(method = "readNbt", at = @At("TAIL"), require = 0)
    private void readCyberNbt(NbtCompound tag, CallbackInfo ci) {
        this.cyberpsychosisLevel = tag.getFloat("CyberpsychosisLevel");
        this.sandevistanTicks = tag.getInt("SandevistanTicks");
    }
}
