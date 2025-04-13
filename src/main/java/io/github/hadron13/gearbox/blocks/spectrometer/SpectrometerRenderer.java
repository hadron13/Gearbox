package io.github.hadron13.gearbox.blocks.spectrometer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllPartialModels;

import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import io.github.hadron13.gearbox.register.ModPartialModels;
import net.createmod.catnip.data.Iterate;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;

public class SpectrometerRenderer extends SafeBlockEntityRenderer<SpectrometerBlockEntity> {

    public SpectrometerRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    protected void renderSafe(SpectrometerBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {
        if (VisualizationManager.supportsVisualization(be.getLevel())) return;

        BlockState gaugeState = be.getBlockState();
        SpectrometerBlockEntity gaugeBE = be;

        PartialModel partialModel = ModPartialModels.SPECTROGAUGE;
        SuperByteBuffer headBuffer =
                CachedBuffers.partial(partialModel, gaugeState);
        SuperByteBuffer dialBuffer = CachedBuffers.partial(AllPartialModels.GAUGE_DIAL, gaugeState);

        float dialPivot = 5.75f / 16;
        float progress = Mth.lerp(partialTicks, gaugeBE.prevDialState, gaugeBE.dialState);

        for (Direction facing : Iterate.directions) {
            if (!((SpectrometerBlock)gaugeState.getBlock()).shouldRenderHeadOnFace(be.getLevel(), be.getBlockPos(), gaugeState,
                    facing))
                continue;

            VertexConsumer vb = bufferSource.getBuffer(RenderType.solid());
            rotateBufferTowards(dialBuffer, facing).translate(0, dialPivot, dialPivot)
                    .rotate((float) (Math.PI / 2 * -progress), Direction.EAST)
                    .translate(0, -dialPivot, -dialPivot)
                    .light(light)
                    .renderInto(ms, vb);
            rotateBufferTowards(headBuffer, facing).light(light)
                    .renderInto(ms, vb);
        }
    }
    protected SuperByteBuffer rotateBufferTowards(SuperByteBuffer buffer, Direction target) {
        return buffer.rotateCentered((float) ((-target.toYRot() - 90) / 180 * Math.PI), Direction.UP);
    }
}
