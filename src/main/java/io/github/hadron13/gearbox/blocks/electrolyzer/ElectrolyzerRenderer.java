package io.github.hadron13.gearbox.blocks.electrolyzer;

import com.jozufozu.flywheel.backend.Backend;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class ElectrolyzerRenderer extends SafeBlockEntityRenderer<ElectrolyzerBlockEntity> {
    public ElectrolyzerRenderer(BlockEntityRendererProvider.Context context) {
        super();
    }

    @Override
    protected void renderSafe(ElectrolyzerBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {
        if(Backend.canUseInstancing(be.getLevel()))
            return;
    }
}
