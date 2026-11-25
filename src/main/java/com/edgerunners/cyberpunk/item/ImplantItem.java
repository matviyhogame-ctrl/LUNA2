package com.edgerunners.cyberpunk.item;

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

public abstract class ImplantItem extends Item {
    protected final float neuroStressCost;
    protected final int cooldownTicks;

    public ImplantItem(Settings settings, float neuroStressCost, int cooldownTicks) {
        super(settings);
        this.neuroStressCost = neuroStressCost;
        this.cooldownTicks = cooldownTicks;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (triggerImplant(world, user, stack)) {
            return TypedActionResult.success(stack, world.isClient());
        }
        return TypedActionResult.pass(stack);
    }

    public boolean triggerImplant(World world, PlayerEntity player, ItemStack stack) {
        if (player.getItemCooldownManager().isCoolingDown(this)) {
            return false;
        }
        if (!world.isClient) {
            onActivate(world, player, stack);
            player.getItemCooldownManager().set(this, cooldownTicks);
            if (player instanceof ICyberwareData) {
                ((ICyberwareData) player).addCyberpsychosis(neuroStressCost);
            }
            world.playSound(null, player.getBlockPos(), SoundEvents.BLOCK_BEACON_ACTIVATE, SoundCategory.PLAYERS, 0.9F, 1.8F);
        }
        return true;
    }

    protected abstract void onActivate(World world, PlayerEntity player, ItemStack stack);

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(new TranslatableText(this.getTranslationKey() + ".desc").formatted(Formatting.AQUA));
        tooltip.add(new TranslatableText("tooltip.cyberpunk_implants.stress", String.format("+%.0f%%", neuroStressCost)).formatted(Formatting.RED));
    }
}
