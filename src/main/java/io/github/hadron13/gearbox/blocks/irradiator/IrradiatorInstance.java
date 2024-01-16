package io.github.hadron13.gearbox.blocks.irradiator;

import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.SingleRotatingInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class IrradiatorInstance extends SingleRotatingInstance<IrradiatorBlockEntity> {
    public IrradiatorInstance(MaterialManager materialManager, IrradiatorBlockEntity blockEntity) {
        super(materialManager, blockEntity);
    }

    @Override
    public Instancer<RotatingData> getModel(){
        BlockState referenceState = blockEntity.getBlockState();
        Direction facing = referenceState.getValue(BlockStateProperties.FACING);
        return getRotatingMaterial().getModel(AllPartialModels., referenceState, facing);
    }
}
