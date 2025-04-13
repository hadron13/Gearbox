package io.github.hadron13.gearbox.compat.jei.category.animations;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import io.github.hadron13.gearbox.register.ModBlocks;
import io.github.hadron13.gearbox.register.ModPartialModels;
import net.minecraft.client.gui.GuiGraphics;

public class AnimatedCompressor extends AnimatedKinetics {

    @Override
    public void draw(GuiGraphics graphics, int xOffset, int yOffset) {
        PoseStack matrixStack = graphics.pose();
        matrixStack.pushPose();
        matrixStack.translate(xOffset, yOffset, 300);
        matrixStack.mulPose(Axis.XP.rotationDegrees(-15.5f));
        matrixStack.mulPose(Axis.YP.rotationDegrees(22.5f));
        int scale = 23;

        blockElement(ModBlocks.COMPRESSOR.getDefaultState())
                .atLocal(0, 1.65, 0)
                .rotateBlock(0, 180, 0)
                .scale(scale)
                .render(graphics);

        blockElement(ModPartialModels.COMPRESSOR_ROLL)
                .atLocal(0, 1.65, 0)
                .rotateBlock(-getCurrentAngle(), 0, 0)
                .scale(scale)
                .render(graphics);

        matrixStack.popPose();
    }
}
