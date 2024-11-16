package io.github.hadron13.gearbox.blocks.laser_drill;

import com.google.common.cache.Cache;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import io.github.hadron13.gearbox.blocks.laser.LaserBeamRenderer;
import io.github.hadron13.gearbox.register.ModPartialModels;
import io.github.hadron13.gearbox.render.ModRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;

public class LaserDrillRenderer extends SafeBlockEntityRenderer<LaserDrillBlockEntity> {


    public LaserDrillRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    protected void renderSafe(LaserDrillBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {

        float speed = be.visualSpeed.getValue(partialTicks) * 3 / 10f;
        float angle = be.angle + speed * partialTicks;

        VertexConsumer vb = bufferSource.getBuffer(RenderType.solid());

        SuperByteBuffer head = CachedBufferer.partial(ModPartialModels.LASER_DRILL_HEAD, be.getBlockState());


        head.rotateCentered(Direction.UP, angle * Mth.DEG_TO_RAD)
            .renderInto(ms, vb);

        if(be.totalPower < 0.1f) {
            return;
        }

        vb = bufferSource.getBuffer(ModRenderTypes.laserBeam());

        float thickness = 1.0f;
        SuperByteBuffer thick_beam = CachedBufferer.partial(ModPartialModels.THICK_BEAM, be.getBlockState());
        thick_beam
                .translate(0.5f - thickness/2.0f, -1.0, 0.5f - thickness/2.0f)
                .scale(thickness, 1.0f, thickness)
                .rotateCentered(Direction.UP, angle * Mth.DEG_TO_RAD)
                .color(be.mixedColor.getRGB())
                .renderInto(ms, vb);
    }
}

