package io.github.hadron13.gearbox.compat.jei.category.animations;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import io.github.hadron13.gearbox.register.ModBlocks;
import io.github.hadron13.gearbox.register.ModPartialModels;

public class AnimatedCentrifuge extends AnimatedKinetics {
    @Override
    public void draw(PoseStack matrixStack, int xOffset, int yOffset) {
        int scale = 23;
        matrixStack.pushPose();
        matrixStack.translate(xOffset, yOffset, 200);
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(-15.5f));
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(22.5f));

        blockElement(ModBlocks.CENTRIFUGE.getDefaultState())
                .scale(scale)
                .render(matrixStack);

        blockElement(ModPartialModels.CENTRIFUGE_COG)
                .rotateBlock(0, getCurrentAngle() * 2, 0)
                .scale(scale)
                .render(matrixStack);

        matrixStack.popPose();

    }
}
