package io.github.hadron13.gearbox.compat.jei.category.animations;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import io.github.hadron13.gearbox.register.ModBlocks;
import io.github.hadron13.gearbox.register.ModPartialModels;
import net.minecraft.util.Mth;

public class AnimatedElectrolyzer extends AnimatedKinetics {
    @Override
    public void draw(PoseStack poseStack, int xOffset, int yOffset) {
        poseStack.pushPose();
        poseStack.translate(xOffset, yOffset, 200);
        poseStack.mulPose(Vector3f.XP.rotationDegrees(-15.5f));
        poseStack.mulPose(Vector3f.YP.rotationDegrees(22.5f));
        int scale = 23;

        blockElement(ModBlocks.ELECTROLYZER.getDefaultState())
                .atLocal(0, 0, 0)
                .scale(scale)
                .render(poseStack);

        float animation = ((Mth.sin(AnimationTickHolder.getRenderTime() / 32f) + 1) / 5) + .5f;

        blockElement(AllPartialModels.MECHANICAL_MIXER_POLE)
                .atLocal(0, animation, 0)
                .scale(scale)
                .render(poseStack);

        blockElement(ModPartialModels.ELECTROLYZER_HEAD)
                .rotateBlock(0, getCurrentAngle() * 2, 0)
                .atLocal(0, animation, 0)
                .scale(scale)
                .render(poseStack);

        blockElement(AllBlocks.BASIN.getDefaultState())
                .atLocal(0, 1.65, 0)
                .scale(scale)
                .render(poseStack);

        poseStack.popPose();
    }
}
