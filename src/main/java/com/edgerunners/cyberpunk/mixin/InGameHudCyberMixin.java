package com.edgerunners.cyberpunk.mixin;

import com.edgerunners.cyberpunk.util.ICyberwareData;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudCyberMixin extends DrawableHelper {
    @Shadow @Final private MinecraftClient client;

    @Inject(method = "render", at = @At("TAIL"))
    private void renderEdgerunnersHud(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        if (this.client.player == null || this.client.options.hudHidden) return;
        if (!(this.client.player instanceof ICyberwareData)) return;

        ICyberwareData data = (ICyberwareData) this.client.player;
        float psycho = data.getCyberpsychosisLevel();
        int sandyTicks = data.getSandevistanTicks();

        int width = this.client.getWindow().getScaledWidth();
        int height = this.client.getWindow().getScaledHeight();

        // 1. Полноэкранный эффект Сандевистана (неоново-изумрудная/желтая хроматическая рамка как в аниме)
        if (sandyTicks > 0) {
            RenderSystem.enableBlend();
            int pulseAlpha = (int) (35 + 20 * Math.sin((this.client.player.age + tickDelta) * 0.6));
            int overlayColor = (pulseAlpha << 24) | 0x00FF99;
            fill(matrices, 0, 0, width, height, overlayColor);

            // Яркие неоновые границы экрана
            int borderColor = 0xAA00FFAA;
            fill(matrices, 0, 0, width, 4, borderColor);
            fill(matrices, 0, height - 4, width, height, borderColor);
            fill(matrices, 0, 0, 4, height, borderColor);
            fill(matrices, width - 4, 0, width, height, borderColor);
            RenderSystem.disableBlend();
        }

        // 2. Глитч-виньетка при высоком киберпсихозе (> 50%)
        if (psycho >= 50.0F) {
            RenderSystem.enableBlend();
            int redAlpha = (int) Math.min(110, (psycho - 45.0F) * 1.8F);
            int glitchColor = (redAlpha << 24) | 0xFF0022;
            fill(matrices, 0, 0, width, 6, glitchColor);
            fill(matrices, 0, height - 6, width, height, glitchColor);

            // Случайные горизонтальные глитч-полосы как при приступе у Дэвида и Мейна
            if (this.client.player.getRandom().nextFloat() < (psycho / 150.0F)) {
                int gy = this.client.player.getRandom().nextInt(height);
                int gh = 2 + this.client.player.getRandom().nextInt(5);
                fill(matrices, 0, gy, width, gy + gh, 0x66FF0033);
            }
            RenderSystem.disableBlend();
        }

        // 3. Киберпанк HUD панель в левом верхнем углу (Шкала нейронагрузки / Киберпсихоза)
        int x = 10;
        int y = 10;
        int barWidth = 110;
        int barHeight = 8;

        // Фон в стиле Cyberpunk: Edgerunners (чёрный с жёлтой/красной рамкой)
        fill(matrices, x - 2, y - 2, x + barWidth + 2, y + barHeight + 13, 0xCC0D0E14);
        int frameColor = psycho >= 75.0F ? 0xFFFF1E3C : 0xFFFCEE09; // Жёлтый Edgerunners или красный тревожный
        fill(matrices, x - 2, y - 2, x + barWidth + 2, y - 1, frameColor);
        fill(matrices, x - 2, y + barHeight + 12, x + barWidth + 2, y + barHeight + 13, frameColor);

        // Заполнение шкалы
        int filled = (int) ((psycho / 100.0F) * barWidth);
        int barColor = psycho < 50.0F ? 0xFF00F0FF : (psycho < 80.0F ? 0xFFFCEE09 : 0xFFFF1E3C);
        fill(matrices, x, y, x + barWidth, y + barHeight, 0xFF22242E);
        fill(matrices, x, y, x + filled, y + barHeight, barColor);

        TextRenderer tr = this.client.textRenderer;
        String label = String.format("НЕЙРОНАГРУЗКА: %.0f%%", psycho);
        tr.drawWithShadow(matrices, label, x, y + 10, frameColor);

        if (sandyTicks > 0) {
            String sandyLabel = String.format("SANDEVISTAN: %.1fs", sandyTicks / 20.0F);
            tr.drawWithShadow(matrices, sandyLabel, x, y + 23, 0xFF00FFAA);
        }
    }
}
