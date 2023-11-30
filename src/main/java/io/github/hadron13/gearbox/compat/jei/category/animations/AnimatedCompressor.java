package io.github.hadron13.gearbox.compat.jei.category.animations;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import io.github.hadron13.gearbox.register.ModBlocks;
import io.github.hadron13.gearbox.register.ModPartialModels;

import static io.github.hadron13.gearbox.blocks.kiln.KilnBlock.POWERED;

public class AnimatedCompressor extends AnimatedKinetics {

    @Override
    public void draw(PoseStack matrixStack, int xOffset, int yOffset) {
        matrixStack.pushPose();
        matrixStack.translate(xOffset, yOffset, 300);
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(-15.5f));
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(22.5f));
        int scale = 23;

        blockElement(ModBlocks.COMPRESSOR.getDefaultState())
                .atLocal(0, 1.65, 0)
                .rotateBlock(0, 180, 0)
                .scale(scale)
                .render(matrixStack);

        blockElement(ModPartialModels.COMPRESSOR_ROLL)
                .atLocal(0, 1.65, 0)
                .rotateBlock(-getCurrentAngle(), 0, 0)
                .scale(scale)
                .render(matrixStack);

        matrixStack.popPose();
    }
}
