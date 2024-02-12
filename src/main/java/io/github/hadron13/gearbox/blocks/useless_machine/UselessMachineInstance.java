package io.github.hadron13.gearbox.blocks.useless_machine;

import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.simibubi.create.content.kinetics.base.SingleRotatingInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import io.github.hadron13.gearbox.register.ModPartialModels;

public class UselessMachineInstance extends SingleRotatingInstance<UselessMachineBlockEntity> {
    public UselessMachineInstance(MaterialManager materialManager, UselessMachineBlockEntity blockEntity) {
        super(materialManager, blockEntity);
    }


    //cause the block shape is full, the light is 0
    @Override
    public void updateLight() {
        relight(pos.above(), rotatingModel);
    }


    @Override
    protected Instancer<RotatingData> getModel() {
        return getRotatingMaterial().getModel(ModPartialModels.USELESS_COG, blockEntity.getBlockState());
    }
}
