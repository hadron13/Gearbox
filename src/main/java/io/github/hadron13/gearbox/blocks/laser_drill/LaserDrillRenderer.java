package io.github.hadron13.gearbox.blocks.laser_drill;

import com.google.common.cache.Cache;
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

public class LaserDrillRenderer extends SafeBlockEntityRenderer<LaserDrillBlockEntity> {


    public LaserDrillRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    protected void renderSafe(LaserDrillBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {


        VertexConsumer vb = bufferSource.getBuffer(RenderType.solid());

        SuperByteBuffer head = CachedBufferer.partial(ModPartialModels.LASER_DRILL_HEAD, be.getBlockState());

        head.renderInto(ms, vb);

        vb = bufferSource.getBuffer(ModRenderTypes.laserBeam());
        SuperByteBuffer beam = CachedBufferer.partial(ModPartialModels.THICK_BEAM, be.getBlockState());




    }


}

