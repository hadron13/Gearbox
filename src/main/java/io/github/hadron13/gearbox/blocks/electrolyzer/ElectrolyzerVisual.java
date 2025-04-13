package io.github.hadron13.gearbox.blocks.electrolyzer;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.RotatingInstance;
import com.simibubi.create.foundation.render.AllInstanceTypes;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.instance.InstanceTypes;
import dev.engine_room.flywheel.lib.instance.OrientedInstance;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.visual.AbstractBlockEntityVisual;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import io.github.hadron13.gearbox.register.ModPartialModels;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class ElectrolyzerVisual extends AbstractBlockEntityVisual<ElectrolyzerBlockEntity> implements SimpleDynamicVisual {

    public final OrientedInstance pole;
    public final RotatingInstance head;

    public ElectrolyzerVisual(VisualizationContext context, ElectrolyzerBlockEntity blockEntity, float partialTicks) {
        super(context, blockEntity, partialTicks);

        head = instancerProvider().instancer(AllInstanceTypes.ROTATING, Models.partial(ModPartialModels.ELECTROLYZER_HEAD)).createInstance();

        head.setRotationAxis(Direction.Axis.Y);

        pole = instancerProvider().instancer(InstanceTypes.ORIENTED, Models.partial(AllPartialModels.MECHANICAL_MIXER_POLE)).createInstance();

        transformInstances();
    }

    public void transformInstances(){
        float renderedHeadOffset = blockEntity.getRenderedHeadOffset(AnimationTickHolder.getPartialTicks());

        pole.position(getVisualPosition())
                .translatePosition(0, -renderedHeadOffset, 0);

        float speed = blockEntity.getRenderedHeadRotationSpeed(AnimationTickHolder.getPartialTicks());

        head.setPosition(getVisualPosition())
                .nudge(0, -renderedHeadOffset, 0)
                .setRotationalSpeed(speed * 2);
    }

    @Override
    public void collectCrumblingInstances(Consumer<@Nullable Instance> consumer) {
        consumer.accept(head);
        consumer.accept(pole);
    }

    @Override
    public void updateLight(float partialTick) {
        relight(pos, pole);
        relight(pos.below(), head);
    }

    @Override
    protected void _delete() {
        pole.delete();
        head.delete();
    }

    @Override
    public void beginFrame(Context ctx) {
        transformInstances();
    }
}
