package io.github.hadron13.gearbox.blocks.spectrometer;

import com.jozufozu.flywheel.backend.Backend;
import com.jozufozu.flywheel.core.PartialModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.gauge.GaugeBlock;
import com.simibubi.create.content.kinetics.gauge.GaugeBlockEntity;
import com.simibubi.create.content.kinetics.gauge.GaugeRenderer;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.Iterate;
import io.github.hadron13.gearbox.register.ModPartialModels;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class SpectrometerRenderer extends SafeBlockEntityRenderer<SpectrometerBlockEntity> {

    public SpectrometerRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    protected void renderSafe(SpectrometerBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {
        if (Backend.canUseInstancing(be.getLevel())) return;


        BlockState gaugeState = be.getBlockState();
        GaugeBlockEntity gaugeBE = (GaugeBlockEntity) be;

        PartialModel partialModel = ModPartialModels.SPECTROGAUGE;
        SuperByteBuffer headBuffer =
                CachedBufferer.partial(partialModel, gaugeState);
        SuperByteBuffer dialBuffer = CachedBufferer.partial(AllPartialModels.GAUGE_DIAL, gaugeState);

        float dialPivot = 5.75f / 16;
        float progress = Mth.lerp(partialTicks, gaugeBE.prevDialState, gaugeBE.dialState);

        for (Direction facing : Iterate.directions) {
            if (!((GaugeBlock) gaugeState.getBlock()).shouldRenderHeadOnFace(be.getLevel(), be.getBlockPos(), gaugeState,
                    facing))
                continue;

            VertexConsumer vb = bufferSource.getBuffer(RenderType.solid());
            rotateBufferTowards(dialBuffer, facing).translate(0, dialPivot, dialPivot)
                    .rotate(Direction.EAST, (float) (Math.PI / 2 * -progress))
                    .translate(0, -dialPivot, -dialPivot)
                    .light(light)
                    .renderInto(ms, vb);
            rotateBufferTowards(headBuffer, facing).light(light)
                    .renderInto(ms, vb);
        }
    }
    protected SuperByteBuffer rotateBufferTowards(SuperByteBuffer buffer, Direction target) {
        return buffer.rotateCentered(Direction.UP, (float) ((-target.toYRot() - 90) / 180 * Math.PI));
    }
}
