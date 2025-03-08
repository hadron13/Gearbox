package io.github.hadron13.gearbox.compat.jei.category.animations;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import dev.architectury.platform.Mod;
import io.github.hadron13.gearbox.register.ModBlocks;
import io.github.hadron13.gearbox.register.ModPartialModels;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;

public class AnimatedReactor extends AnimatedKinetics {
    @Override
    public void draw(PoseStack poseStack, int xOffset, int yOffset) {
        poseStack.pushPose();
        poseStack.translate(xOffset, yOffset, 200);
        poseStack.mulPose(Vector3f.XP.rotationDegrees(-15.5f));
        poseStack.mulPose(Vector3f.YP.rotationDegrees(22.5f));
        int scale = 23;

        blockElement(shaft(Direction.Axis.Y))
                .rotateBlock(0, getCurrentAngle(), 0)
                .atLocal(0, 0, 0)
                .scale(scale)
                .render(poseStack);

        blockElement(ModBlocks.REACTOR.getDefaultState())
                .rotateBlock(0, 90f, 0)
                .atLocal(0, 0, 0)
                .scale(scale)
                .render(poseStack);

        blockElement(ModPartialModels.DIPPER_POLE)
                .atLocal(0, 17/16f, 0)
                .scale(scale)
                .render(poseStack);

        blockElement(AllPartialModels.MECHANICAL_MIXER_HEAD)
                .rotateBlock(0, getCurrentAngle() / 4, 0)
                .atLocal(0, 17/16f, 0)
                .scale(scale)
                .render(poseStack);

        blockElement(AllBlocks.BASIN.getDefaultState())
                .atLocal(0, 1.65, 0)
                .scale(scale)
                .render(poseStack);

        poseStack.popPose();

    }
}
