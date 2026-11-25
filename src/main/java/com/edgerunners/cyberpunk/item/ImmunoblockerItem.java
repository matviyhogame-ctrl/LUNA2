package com.edgerunners.cyberpunk.item;

import com.edgerunners.cyberpunk.effect.ModEffects;
import com.edgerunners.cyberpunk.util.ICyberwareData;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ImmunoblockerItem extends Item {
    public ImmunoblockerItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (!world.isClient) {
            user.removeStatusEffect(ModEffects.CYBERPSYCHOSIS);
            if (user instanceof ICyberwareData) {
                ((ICyberwareData) user).addCyberpsychosis(-45.0F);
            }
            user.heal(6.0F);
            world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_GENERIC_DRINK, SoundCategory.PLAYERS, 1.0F, 1.3F);
            if (!user.abilities.creativeMode) {
                stack.decrement(1);
            }
        }
        return TypedActionResult.success(stack, world.isClient());
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(new TranslatableText("item.cyberpunk_implants.immunoblocker.desc").formatted(Formatting.YELLOW));
    }
}
