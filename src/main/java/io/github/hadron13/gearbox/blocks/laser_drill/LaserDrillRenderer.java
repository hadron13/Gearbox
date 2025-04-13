package io.github.hadron13.gearbox.blocks.laser_drill;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import io.github.hadron13.gearbox.register.ModPartialModels;
import io.github.hadron13.gearbox.render.ModRenderTypes;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class LaserDrillRenderer extends SafeBlockEntityRenderer<LaserDrillBlockEntity> {


    @Override
    public boolean shouldRender(LaserDrillBlockEntity pBlockEntity, Vec3 pCameraPos) {
        return true;
    }

    public LaserDrillRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    protected void renderSafe(LaserDrillBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {

        float speed = be.visualSpeed.getValue(partialTicks) * 3 / 10f;
        float angle = be.angle + speed * partialTicks;

        VertexConsumer vb = bufferSource.getBuffer(RenderType.solid());

        SuperByteBuffer head = CachedBuffers.partial(ModPartialModels.LASER_DRILL_HEAD, be.getBlockState());


        head.rotateCentered(angle * Mth.DEG_TO_RAD, Direction.UP)
            .renderInto(ms, vb);

        if(be.totalPower < 0.1f) {
            return;
        }

        vb = bufferSource.getBuffer(ModRenderTypes.laserBeam());

        float thickness = 1.0f;
        SuperByteBuffer thick_beam = CachedBuffers.partial(ModPartialModels.THICK_BEAM, be.getBlockState());
        thick_beam
                .translate(0.5f - thickness/2.0f, 1.0, 0.5f - thickness/2.0f)
                .scale(thickness, -be.laserLength, thickness)
                .rotateCentered(angle * Mth.DEG_TO_RAD, Direction.UP)
                .color(be.mixedColor.getRGB())
                .renderInto(ms, vb);
    }
}

