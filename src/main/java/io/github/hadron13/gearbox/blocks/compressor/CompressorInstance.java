package io.github.hadron13.gearbox.blocks.compressor;

import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.simibubi.create.content.kinetics.base.ShaftInstance;
import com.simibubi.create.content.kinetics.base.SingleRotatingInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import com.simibubi.create.content.kinetics.press.MechanicalPressBlockEntity;
import io.github.hadron13.gearbox.register.ModPartialModels;
import net.minecraft.world.level.block.state.BlockState;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class CompressorInstance extends SingleRotatingInstance<CompressorBlockEntity> implements DynamicInstance{

    public CompressorInstance(MaterialManager materialManager, CompressorBlockEntity blockEntity) {
        super(materialManager, blockEntity);
    }

    @Override
    public void beginFrame() {

    }
    @Override
    protected Instancer<RotatingData> getModel() {
        BlockState state = blockEntity.getBlockState();
        return getRotatingMaterial().getModel(ModPartialModels.COMPRESSOR_ROLL, state, state.getValue(HORIZONTAL_FACING));
    }

    @Override
    public void updateLight() {
        super.updateLight();
    }

    @Override
    public void remove() {
        super.remove();
    }

}
