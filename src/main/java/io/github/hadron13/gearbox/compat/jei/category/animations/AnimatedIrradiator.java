package io.github.hadron13.gearbox.compat.jei.category.animations;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import io.github.hadron13.gearbox.blocks.laser.LaserBeamRenderer;
import io.github.hadron13.gearbox.register.GearboxBlocks;
import io.github.hadron13.gearbox.register.GearboxPartialModels;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

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


        blockElement(GearboxBlocks.IRRADIATOR.getDefaultState())
                .scale(scale)
                .render(graphics);

        blockElement(AllPartialModels.SHAFT_HALF)
                .rotateBlock(-90 + getCurrentAngle(), 90, 90) //If it works, don't touch it
                .scale(scale)
                .render(graphics);

        float offset = Mth.clamp(Mth.sin(AnimationTickHolder.getRenderTime() / 10f), 0, 1f);
        blockElement(GearboxPartialModels.IRRADIATOR_LENS)
                .atLocal(0, -offset * 4/16f, 0)
                .scale(scale)
                .render(graphics);

        matrixStack.pushPose();
        TransformStack.of(matrixStack)
                        .scale(scale, -scale, scale)
                        .translate(0, 0, 0);


        MultiBufferSource.BufferSource bufferSource = graphics.bufferSource();

        float thickness = 4/16f * offset;
        float length = 2f;
        LaserBeamRenderer.renderLaserBeamCustom(thickness, 0, length, color | 0x55000000, new Vec3(0, -1, 0), new Vec3(0, 0, 0), matrixStack, bufferSource);
        matrixStack.popPose();

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
