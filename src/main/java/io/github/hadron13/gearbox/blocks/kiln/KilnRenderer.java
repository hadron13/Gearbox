package io.github.hadron13.gearbox.blocks.kiln;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

import static com.simibubi.create.content.kinetics.base.HorizontalKineticBlock.HORIZONTAL_FACING;

public class KilnRenderer extends KineticBlockEntityRenderer<KilnBlockEntity> {

    public KilnRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected SuperByteBuffer getRotatedModel(KilnBlockEntity be, BlockState state) {
        return CachedBufferer.partialFacing(AllPartialModels.SHAFT_HALF, state,
                state.getValue(HORIZONTAL_FACING));
    }
}