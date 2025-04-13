package io.github.hadron13.gearbox.compat.jei.category.animations;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import io.github.hadron13.gearbox.register.ModBlocks;
import io.github.hadron13.gearbox.register.ModPartialModels;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.level.block.Blocks;

public class AnimatedLaserDrill extends AnimatedKinetics {
    public int color = 0;
    public AnimatedLaserDrill(){}


    @Override
    public void draw(GuiGraphics graphics, int xOffset, int yOffset) {
        PoseStack matrixStack = graphics.pose();
        matrixStack.pushPose();
        matrixStack.translate(xOffset, yOffset, 200);
        matrixStack.mulPose(Axis.XP.rotationDegrees(-15.5f));
        matrixStack.mulPose(Axis.YP.rotationDegrees(22.5f));
        int scale = 24;


        blockElement(ModBlocks.LASER_DRILL.getDefaultState())
                .scale(scale)
                .render(graphics);


        blockElement(ModPartialModels.LASER_DRILL_HEAD)
                .rotateBlock(0, AnimationTickHolder.getRenderTime()*10f, 0)
                .atLocal(0, -offset * 4/16f, 0)
                .scale(scale)
                .render(graphics);

        blockElement(ModPartialModels.THICK_BEAM)
                .rotateBlock(0, AnimationTickHolder.getRenderTime()*10f, 0)
                .atLocal(0,  1 + 3/16f, 0)
                .color(color)
                .scale(scale)
                .render(graphics);

        blockElement(Blocks.BEDROCK.defaultBlockState())
                .atLocal(0,  2, 0)
                .color(color)
                .scale(scale)
                .render(graphics);

        matrixStack.popPose();
    }

}
