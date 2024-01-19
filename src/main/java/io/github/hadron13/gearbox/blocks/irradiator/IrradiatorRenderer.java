package io.github.hadron13.gearbox.blocks.irradiator;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import io.github.hadron13.gearbox.register.ModPartialModels;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import net.minecraft.world.level.block.state.BlockState;

public class IrradiatorRenderer extends KineticBlockEntityRenderer<IrradiatorBlockEntity> {
    public IrradiatorRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }
    @Override
    protected SuperByteBuffer getRotatedModel(IrradiatorBlockEntity be, BlockState state) {
        return CachedBufferer.partial(AllPartialModels.SHAFT_HALF, state);
    }
}
