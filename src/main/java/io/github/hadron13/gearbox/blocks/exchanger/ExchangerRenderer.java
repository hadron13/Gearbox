package io.github.hadron13.gearbox.blocks.exchanger;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

public class ExchangerRenderer extends KineticBlockEntityRenderer<ExchangerBlockEntity> {

    public ExchangerRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected SuperByteBuffer getRotatedModel(ExchangerBlockEntity be, BlockState state) {
        return CachedBufferer.partial(AllPartialModels.MILLSTONE_COG, state);
    }

}