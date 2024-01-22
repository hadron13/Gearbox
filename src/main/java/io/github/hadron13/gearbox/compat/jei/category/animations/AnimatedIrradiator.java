package io.github.hadron13.gearbox.compat.jei.category.animations;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import io.github.hadron13.gearbox.register.ModBlocks;

public class AnimatedIrradiator extends AnimatedKinetics {
    public boolean basin;
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



        if (basin) {
            blockElement(AllBlocks.BASIN.getDefaultState())
                    .atLocal(0, 1.65, 0)
                    .scale(scale)
                    .render(matrixStack);
        }else {
            blockElement(AllBlocks.DEPOT.getDefaultState())
                    .atLocal(0, 1.65, 0)
                    .scale(scale)
                    .render(matrixStack);
        }
    }
}
