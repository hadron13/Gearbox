package io.github.hadron13.gearbox.blocks.irradiator;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.SingleAxisRotatingVisual;
import dev.engine_room.flywheel.api.visual.DynamicVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.instance.InstanceTypes;
import dev.engine_room.flywheel.lib.instance.OrientedInstance;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import io.github.hadron13.gearbox.register.ModPartialModels;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.minecraft.util.Mth;

public class IrradiatorInstance extends SingleAxisRotatingVisual<IrradiatorBlockEntity> implements SimpleDynamicVisual {

    public OrientedInstance lens;

    public IrradiatorInstance(VisualizationContext context, IrradiatorBlockEntity blockEntity, float partialTicks) {
        super(context, blockEntity, partialTicks, Models.partial(AllPartialModels.SHAFT_HALF));
        lens = instancerProvider().instancer(InstanceTypes.ORIENTED, Models.partial(ModPartialModels.IRRADIATOR_LENS)).createInstance();
    }

    @Override
    public void beginFrame(DynamicVisual.Context ctx) {
        float lerpedLensPos =  Mth.lerp(AnimationTickHolder.getPartialTicks(), blockEntity.previousLensPos, blockEntity.lensPosition);

        lens.position(getVisualPosition()).translatePosition(0, lerpedLensPos, 0);

    }

    @Override
    public void updateLight(float partialTicks) {
        super.updateLight(partialTicks);
        relight(pos, lens);
    }
    @Override
    public void _delete(){
        super._delete();
        lens.delete();
    }
}
