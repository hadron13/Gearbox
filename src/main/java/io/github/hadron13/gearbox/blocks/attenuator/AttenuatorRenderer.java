package io.github.hadron13.gearbox.blocks.attenuator;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import io.github.hadron13.gearbox.blocks.laser.Laser;
import io.github.hadron13.gearbox.blocks.laser.LaserBeamRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.level.block.state.BlockState;

public class AttenuatorRenderer extends SafeBlockEntityRenderer<AttenuatorBlockEntity> {
    @Override
    protected void renderSafe(AttenuatorBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {

        for(Laser l : be.getLasers()){
            LaserBeamRenderer.renderLaserBeamInterpolated(l, ms, bufferSource, be.getBlockPos(), partialTicks);
        }
    }
}
