package io.github.hadron13.gearbox.blocks.core_drill;

import com.simibubi.create.content.kinetics.base.SingleAxisRotatingVisual;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visual.DynamicVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.instance.AbstractInstance;
import dev.engine_room.flywheel.lib.instance.InstanceTypes;
import dev.engine_room.flywheel.lib.instance.TransformedInstance;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import io.github.hadron13.gearbox.register.ModPartialModels;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static io.github.hadron13.gearbox.blocks.core_drill.CoreDrillBlock.HORIZONTAL_FACING;
import static io.github.hadron13.gearbox.blocks.core_drill.CoreDrillBlockEntity.PUSHING;

public class CoreDrillVisual extends SingleAxisRotatingVisual<CoreDrillBlockEntity> implements SimpleDynamicVisual {

    public List<TransformedInstance> tubes = new ArrayList<>();

    public CoreDrillVisual(VisualizationContext context, CoreDrillBlockEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick, Models.partial(ModPartialModels.SHAFT_DUAL_TINY));
    }


    @Override
    public void beginFrame(DynamicVisual.Context ctx) {

        transformTubes(ctx.partialTick());
    }

    void transformTubes(float partialTick){
        Direction facing = blockEntity.getBlockState().getValue(HORIZONTAL_FACING);

        float tube_y_offset = 0;
        if(blockEntity.drillState == PUSHING){
            tube_y_offset = blockEntity.payloadOffset.getValue(partialTick) * 20/16f;
        }


        if(tubes.size() < blockEntity.tubes){
            for(int i = 0; i < blockEntity.tubes - tubes.size(); i++) {
                TransformedInstance tube = instancerProvider()
                        .instancer(InstanceTypes.TRANSFORMED, Models.partial(ModPartialModels.CORE_DRILL_TUBE))
                        .createInstance();

                tube.setIdentityTransform()
                        .translate(getVisualPosition())
                        .translate(facing.step().mul(0.5f / 16f))
                        .rotateCentered(22.5f * Mth.DEG_TO_RAD, facing.getClockWise())
                        .translate(0, 4.5 / 16f, 0)
                        .translate(0,(i+1) * -20 / 16f, 0)
                        .setChanged();
                tubes.add(tube);
            }

            updateLight(partialTick);
        }
        if(blockEntity.drillState != PUSHING)
            return;
        int i = 0;
        for(TransformedInstance tube : tubes) {
            tube
                    .setIdentityTransform()
                    .translate(getVisualPosition())
                    .translate(facing.step().mul(0.5f / 16f))
                    .rotateCentered(22.5f * Mth.DEG_TO_RAD, facing.getClockWise())
                    .translate(0, 4.5 / 16f, 0)
                    .translate(0, (i + 1)*-20/16f -tube_y_offset, 0)
                    .setChanged();
            i++;
        }
    }


    @Override
    public void collectCrumblingInstances(Consumer<@Nullable Instance> consumer) {
        super.collectCrumblingInstances(consumer);
        tubes.forEach(consumer);
    }

    @Override
    public void updateLight(float partialTick) {
        super.updateLight(partialTick);
        for(TransformedInstance tube : tubes){
            relight(tube);
        }
    }

    @Override
    protected void _delete() {
        super._delete();
        tubes.forEach(AbstractInstance::delete);
    }


}
