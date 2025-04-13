package io.github.hadron13.gearbox.blocks.useless_machine;

import com.simibubi.create.content.kinetics.base.SingleAxisRotatingVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.Models;
import io.github.hadron13.gearbox.register.ModPartialModels;

public class UselessMachineInstance extends SingleAxisRotatingVisual<UselessMachineBlockEntity> {
    public UselessMachineInstance(VisualizationContext context, UselessMachineBlockEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick, Models.partial(ModPartialModels.USELESS_COG));

    }


    //cause the block shape is full, the light is 0
    @Override
    public void updateLight(float partialTick) {
        super.updateLight(partialTick);
        relight(pos.above(), rotatingModel);
    }
}
