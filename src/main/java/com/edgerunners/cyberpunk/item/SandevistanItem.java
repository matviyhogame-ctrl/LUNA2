package com.edgerunners.cyberpunk.item;

import com.edgerunners.cyberpunk.effect.ModEffects;
import com.edgerunners.cyberpunk.util.ICyberwareData;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

public class SandevistanItem extends ImplantItem {
    public SandevistanItem(Settings settings) {
        super(settings, 22.0F, 160); // +22% нейронагрузки за запуск (как у Дэвида — больше 3-4 раз подряд опасно!)
    }

    @Override
    protected void onActivate(World world, PlayerEntity player, ItemStack stack) {
        int duration = 140; // 7 секунд разгона
        player.addStatusEffect(new StatusEffectInstance(ModEffects.SANDEVISTAN_OVERDRIVE, duration, 1, false, false));
        if (player instanceof ICyberwareData) {
            ((ICyberwareData) player).setSandevistanTicks(duration);
        }
        player.sendMessage(new TranslatableText("message.cyberpunk_implants.sandevistan_active").formatted(Formatting.GREEN, Formatting.BOLD), true);
    }
}
