package io.github.hadron13.gearbox.compat.jei.category.animations;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import io.github.hadron13.gearbox.register.ModBlocks;
import io.github.hadron13.gearbox.register.ModPartialModels;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;

import java.awt.*;

public class AnimatedIrradiator extends AnimatedKinetics {
    public boolean basin;
    public int color = 0;
    public AnimatedIrradiator(boolean basin){this.basin = basin;}


    @Override
    public void draw(GuiGraphics graphics, int xOffset, int yOffset) {
        PoseStack matrixStack = graphics.pose();
        matrixStack.pushPose();
        matrixStack.translate(xOffset, yOffset, 200);
        matrixStack.mulPose(Axis.XP.rotationDegrees(-15.5f));
        matrixStack.mulPose(Axis.YP.rotationDegrees(22.5f));
        int scale = 24;


        blockElement(ModBlocks.IRRADIATOR.getDefaultState())
                .scale(scale)
                .render(graphics);

        blockElement(AllPartialModels.SHAFT_HALF)
                .rotateBlock(-90 + getCurrentAngle(), 90, 90) //If it works, don't touch it
                .scale(scale)
                .render(graphics);

        float offset = Mth.clamp(Mth.sin(AnimationTickHolder.getRenderTime() / 10f), 0, 1f);
        blockElement(ModPartialModels.IRRADIATOR_LENS)
                .atLocal(0, -offset * 4/16f, 0)
                .scale(scale)
                .render(graphics);

        blockElement(ModPartialModels.THICK_BEAM)
                .atLocal(0,  1 + 3/16f, 0)
                .color(color)
                .scale(scale)
                .render(graphics);

        blockElement(ModPartialModels.THICK_BEAM)
                .atLocal(0,  0.5f, 0)
                .color(color)
                .scale(scale)
                .render(graphics);

        if (basin) {
            blockElement(AllBlocks.BASIN.getDefaultState())
                    .atLocal(0, 2, 0)
                    .scale(scale)
                    .render(graphics);
        }else {
            blockElement(AllBlocks.DEPOT.getDefaultState())
                    .atLocal(0, 2, 0)
                    .scale(scale)
                    .render(graphics);
        }
        matrixStack.popPose();
    }

}
