package com.edgerunners.cyberpunk.item;

import com.edgerunners.cyberpunk.util.ICyberwareData;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CyberWeaponItem extends SwordItem {
    public enum WeaponType {
        MANTIS_BLADES,
        GORILLA_ARMS,
        MONOWIRE
    }

    private final WeaponType weaponType;

    public CyberWeaponItem(WeaponType weaponType, int attackDamage, float attackSpeed, Settings settings) {
        super(ToolMaterials.NETHERITE, attackDamage, attackSpeed, settings);
        this.weaponType = weaponType;
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!attacker.world.isClient) {
            switch (weaponType) {
                case MANTIS_BLADES:
                    target.setOnFireFor(4);
                    break;
                case GORILLA_ARMS:
                    Vec3d knock = attacker.getRotationVector();
                    target.addVelocity(knock.x * 1.7, 0.65, knock.z * 1.7);
                    target.velocityModified = true;
                    attacker.world.playSound(null, target.getBlockPos(), SoundEvents.ENTITY_IRON_GOLEM_ATTACK, SoundCategory.PLAYERS, 1.0F, 0.8F);
                    break;
                case MONOWIRE:
                    // Режущая дуга Люси с неоново-розовыми частицами
                    Box splash = target.getBoundingBox().expand(3.2);
                    List<LivingEntity> around = attacker.world.getEntitiesByClass(LivingEntity.class, splash, e -> e != attacker && e != target);
                    for (LivingEntity e : around) {
                        e.damage(DamageSource.MAGIC, 7.5F);
                    }
                    if (attacker.world instanceof ServerWorld) {
                        ((ServerWorld) attacker.world).spawnParticles(
                                new DustParticleEffect(1.0F, 0.15F, 0.55F, 1.2F),
                                target.getX(), target.getY() + 1.0, target.getZ(),
                                18, 1.5, 0.4, 1.5, 0.05
                        );
                    }
                    break;
            }
        }
        return super.postHit(stack, target, attacker);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (user.getItemCooldownManager().isCoolingDown(this)) {
            return TypedActionResult.pass(stack);
        }

        if (weaponType == WeaponType.MANTIS_BLADES) {
            // Стремительный рывок с клинками богомола
            if (!world.isClient) {
                Vec3d dash = user.getRotationVector();
                user.addVelocity(dash.x * 1.75, 0.38, dash.z * 1.75);
                user.velocityModified = true;
                user.getItemCooldownManager().set(this, 65);
                if (user instanceof ICyberwareData) ((ICyberwareData) user).addCyberpsychosis(4.0F);
                world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 1.0F, 1.5F);
            }
            return TypedActionResult.success(stack, world.isClient());
        } else if (weaponType == WeaponType.GORILLA_ARMS) {
            // Удар по земле руками гориллы (как у Мейна)
            if (!world.isClient) {
                Box slam = user.getBoundingBox().expand(4.0);
                List<LivingEntity> targets = world.getEntitiesByClass(LivingEntity.class, slam, e -> e != user);
                for (LivingEntity e : targets) {
                    e.damage(DamageSource.player(user), 9.0F);
                    e.addVelocity(0, 0.75, 0);
                    e.velocityModified = true;
                }
                user.getItemCooldownManager().set(this, 90);
                if (user instanceof ICyberwareData) ((ICyberwareData) user).addCyberpsychosis(5.0F);
                world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 0.7F, 1.2F);
            }
            return TypedActionResult.success(stack, world.isClient());
        }
        return TypedActionResult.pass(stack);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(new TranslatableText(this.getTranslationKey() + ".desc").formatted(Formatting.LIGHT_PURPLE));
    }
}
