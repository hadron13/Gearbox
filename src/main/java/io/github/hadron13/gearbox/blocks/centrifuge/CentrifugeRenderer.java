package io.github.hadron13.gearbox.blocks.centrifuge;

import com.jozufozu.flywheel.backend.Backend;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockEntity;
import com.simibubi.create.foundation.render.CachedBufferer;
import io.github.hadron13.gearbox.register.ModPartialModels;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;

public class CentrifugeRenderer extends KineticBlockEntityRenderer<CentrifugeBlockEntity> {
    public CentrifugeRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }
    @Override
    public void renderSafe(CentrifugeBlockEntity be, float partialTicks, PoseStack ms,
                              MultiBufferSource buffer, int light, int overlay){
        if(Backend.canUseInstancing(be.getLevel()))
            return;


        Direction.Axis axis = getRotationAxisOf(be);
        Direction facing = Direction.fromAxisAndDirection(axis, Direction.AxisDirection.POSITIVE);
        renderRotatingBuffer(be,
                CachedBufferer.partialFacingVertical(ModPartialModels.CENTRIFUGE_COG, be.getBlockState(), facing),
                ms, buffer.getBuffer(RenderType.solid()), light);
    }
}
