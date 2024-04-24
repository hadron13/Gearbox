package io.github.hadron13.gearbox.blocks.laser_drill;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class LaserDrillRenderer extends SafeBlockEntityRenderer<LaserDrillBlockEntity> {


    public LaserDrillRenderer(BlockEntityRendererProvider.Context context) {
    }
    @Override
    protected void renderSafe(LaserDrillBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {

    }
}

