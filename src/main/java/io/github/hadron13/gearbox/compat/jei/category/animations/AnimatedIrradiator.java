package io.github.hadron13.gearbox.compat.jei.category.animations;

import com.jozufozu.flywheel.util.AnimationTickHolder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import io.github.hadron13.gearbox.register.ModBlocks;
import io.github.hadron13.gearbox.register.ModPartialModels;
import net.minecraft.util.Mth;

import java.awt.*;

public class AnimatedIrradiator extends AnimatedKinetics {
    public boolean basin;
    public int color = 0;
    public AnimatedIrradiator(boolean basin){this.basin = basin;}


    @Override
    public void draw(PoseStack matrixStack, int xOffset, int yOffset) {
        matrixStack.pushPose();
        matrixStack.translate(xOffset, yOffset, 200);
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(-15.5f));
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(22.5f));
        int scale = 24;


        blockElement(ModBlocks.IRRADIATOR.getDefaultState())
                .scale(scale)
                .render(matrixStack);

        blockElement(AllPartialModels.SHAFT_HALF)
                .rotateBlock(-90 + getCurrentAngle(), 90, 90) //If it works, don't touch it
                .scale(scale)
                .render(matrixStack);

        float offset = Mth.clamp(Mth.sin(AnimationTickHolder.getRenderTime() / 10f), 0, 1f);
        blockElement(ModPartialModels.IRRADIATOR_LENS)
                .atLocal(0, -offset * 4/16f, 0)
                .scale(scale)
                .render(matrixStack);

        blockElement(ModPartialModels.THICK_BEAM)
                .atLocal(0,  1 + 3/16f, 0)
                .scale(scale)
                .render(matrixStack);

        blockElement(ModPartialModels.THICK_BEAM)
                .atLocal(0,  0.5f, 0)
                .scale(scale)
                .render(matrixStack);

        if (basin) {
            blockElement(AllBlocks.BASIN.getDefaultState())
                    .atLocal(0, 2, 0)
                    .scale(scale)
                    .render(matrixStack);
        }else {
            blockElement(AllBlocks.DEPOT.getDefaultState())
                    .atLocal(0, 2, 0)
                    .scale(scale)
                    .render(matrixStack);
        }
        matrixStack.popPose();
    }

}
