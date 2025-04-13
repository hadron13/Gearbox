package io.github.hadron13.gearbox.blocks.large_laser;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import io.github.hadron13.gearbox.blocks.laser.LaserBeamRenderer;
import io.github.hadron13.gearbox.register.ModPartialModels;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

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
        if (VisualizationManager.supportsVisualization(be.getLevel()))
            return;

        BlockState blockState = be.getBlockState();

        float speed = be.visualSpeed.getValue(partialTicks) * 3 / 10f;
        float angle = be.angle + speed * partialTicks;

        VertexConsumer vb = bufferSource.getBuffer(RenderType.solid());

        SuperByteBuffer lens = CachedBuffers.partial(ModPartialModels.LARGE_LASER_LENS, blockState);

        var tfStack = TransformStack.of(ms);
        tfStack
                .translate(0.5f, 0.5f, 0.5f)
                .rotateToFace(be.getFacing())
                .rotate(angle, Z)
                .translate(-0.5f, -0.5f, -0.5f);

        lens.renderInto(ms, vb);

    }
}
