package io.github.hadron13.gearbox.blocks.large_laser;

import com.jozufozu.flywheel.backend.Backend;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AngleHelper;
import io.github.hadron13.gearbox.blocks.laser.LaserBeamRenderer;
import io.github.hadron13.gearbox.register.ModPartialModels;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

import static io.github.hadron13.gearbox.blocks.large_laser.LargeLaserBlock.HORIZONTAL_FACING;
import static net.minecraft.core.Direction.Axis.Z;

public class LargeLaserRenderer extends LaserBeamRenderer<LargeLaserBlockEntity> {
    public LargeLaserRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(LargeLaserBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {

        ms.pushPose();
        super.renderSafe(be, partialTicks, ms, bufferSource, light, overlay);
        ms.popPose();
        if (Backend.canUseInstancing(be.getLevel()))
            return;

        BlockState blockState = be.getBlockState();

        float speed = be.visualSpeed.getValue(partialTicks) * 3 / 10f;
        float angle = be.angle + speed * partialTicks;

        VertexConsumer vb = bufferSource.getBuffer(RenderType.solid());

        SuperByteBuffer lens = CachedBufferer.partial(ModPartialModels.LARGE_LASER_LENS, blockState);

        TransformStack.cast(ms)
                .translate(0.5f, 0.5f, 0.5f)
                .rotateToFace(be.getFacing())
                .rotate(angle, Z)
                .translate(-0.5f, -0.5f, -0.5f);

        lens.renderInto(ms, vb);

    }
}
