package io.github.hadron13.gearbox.compat.jei.category.animations;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import io.github.hadron13.gearbox.register.ModBlocks;
import io.github.hadron13.gearbox.register.ModPartialModels;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;

public class AnimatedLaserDrill extends AnimatedKinetics {
    public int color = 0;
    public AnimatedLaserDrill(){}


    @Override
    public void draw(PoseStack matrixStack, int xOffset, int yOffset) {
        matrixStack.pushPose();
        matrixStack.translate(xOffset, yOffset, 200);
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(-15.5f));
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(22.5f));
        int scale = 24;


        blockElement(ModBlocks.LASER_DRILL.getDefaultState())
                .scale(scale)
                .render(matrixStack);


        blockElement(ModPartialModels.LASER_DRILL_HEAD)
                .rotateBlock(0, AnimationTickHolder.getRenderTime()*10f, 0)
                .atLocal(0, -offset * 4/16f, 0)
                .scale(scale)
                .render(matrixStack);

        blockElement(ModPartialModels.THICK_BEAM)
                .rotateBlock(0, AnimationTickHolder.getRenderTime()*10f, 0)
                .atLocal(0,  1 + 3/16f, 0)
                .color(color)
                .scale(scale)
                .render(matrixStack);

        blockElement(Blocks.BEDROCK.defaultBlockState())
                .atLocal(0,  2, 0)
                .color(color)
                .scale(scale)
                .render(matrixStack);

        matrixStack.popPose();
    }

}
