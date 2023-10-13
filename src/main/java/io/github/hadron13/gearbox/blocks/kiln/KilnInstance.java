package io.github.hadron13.gearbox.blocks.kiln;

import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.SingleRotatingInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import net.minecraft.world.level.block.state.BlockState;

import static com.simibubi.create.content.kinetics.base.HorizontalKineticBlock.HORIZONTAL_FACING;

public class KilnInstance extends SingleRotatingInstance<KilnBlockEntity> {

    public KilnInstance(MaterialManager materialManager, KilnBlockEntity blockEntity) {
        super(materialManager, blockEntity);
    }

    @Override
    protected Instancer<RotatingData> getModel() {
        BlockState state = blockEntity.getBlockState();
        return getRotatingMaterial().getModel(AllPartialModels.SHAFT_HALF, state, state.getValue(HORIZONTAL_FACING).getOpposite());
    }
}