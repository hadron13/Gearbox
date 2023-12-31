package io.github.hadron13.gearbox.compat.jei.category.animations;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import com.simibubi.create.compat.jei.category.animations.AnimatedPress;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import io.github.hadron13.gearbox.register.ModBlocks;
import io.github.hadron13.gearbox.register.ModPartialModels;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;

public class AnimatedBrassPress extends AnimatedKinetics {

    public AnimatedBrassPress(){}
    @Override
    public void draw(PoseStack matrixStack, int xOffset, int yOffset) {
        matrixStack.pushPose();
        matrixStack.translate(xOffset, yOffset, 200);
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(-15.5f));
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(22.5f));
        int scale = 24;

        blockElement(shaft(Direction.Axis.Z))
                .rotateBlock(0, 0, getCurrentAngle())
                .scale(scale)
                .render(matrixStack);

        blockElement(ModBlocks.BRASS_PRESS.getDefaultState())
                .scale(scale)
                .render(matrixStack);

        blockElement(ModPartialModels.BRASS_PRESS_POLE)
                .atLocal(0, -getAnimatedHeadOffset()  * (1f + 3/16f), 0)
                .scale(scale)
                .render(matrixStack);

        blockElement(ModPartialModels.BRASS_PRESS_HEAD)
                .rotateBlock(0, getAnimatedHeadRotation(), 0)
                .atLocal(0, -getAnimatedHeadOffset()  * (1f + 3/16f), 0)
                .scale(scale)
                .render(matrixStack);

        blockElement(AllBlocks.DEPOT.getDefaultState())
                .atLocal(0, 2, 0)
                .scale(scale)
                .render(matrixStack);

        matrixStack.popPose();
    }
    private float getAnimatedHeadOffset() {
        float cycle = (AnimationTickHolder.getRenderTime() - offset * 8) % 30;
        if (cycle < 10) {
            float progress = cycle / 10;
            return -(progress * progress * progress);
        }
        if (cycle < 15)
            return -1;
        if (cycle < 20)
            return -1 + (1 - ((20 - cycle) / 5));
        return 0;
    }
    private float getAnimatedHeadRotation(){
        float cycle = (AnimationTickHolder.getRenderTime() - offset * 8) % 60;
        if(cycle > 45){
            return Mth.clamp( 180 - (cycle - 45) * 16, 0, 180);
        }
        if(cycle > 15){
            return Mth.clamp((cycle-15) * 16, 0, 180);
        }
        return 0;
    }


}
