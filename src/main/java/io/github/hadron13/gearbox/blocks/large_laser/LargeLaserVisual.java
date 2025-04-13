package io.github.hadron13.gearbox.blocks.large_laser;

import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visual.DynamicVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.instance.InstanceTypes;
import dev.engine_room.flywheel.lib.instance.TransformedInstance;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.visual.AbstractBlockEntityVisual;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import io.github.hadron13.gearbox.blocks.laser.LaserBeamBehavior;
import io.github.hadron13.gearbox.register.ModPartialModels;
import net.createmod.catnip.animation.AnimationTickHolder;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

import static net.minecraft.core.Direction.Axis.Z;

public class LargeLaserVisual extends AbstractBlockEntityVisual<LargeLaserBlockEntity> implements SimpleDynamicVisual {
    public TransformedInstance lens;

    protected float lastAngle = Float.NaN;

    public LargeLaserVisual(VisualizationContext context, LargeLaserBlockEntity blockEntity, float partialTicks) {
        super(context, blockEntity, partialTicks);
        lens = instancerProvider().instancer(InstanceTypes.TRANSFORMED, Models.partial(ModPartialModels.LARGE_LASER_LENS)).createInstance();

    }
    @Override
    public void beginFrame(DynamicVisual.Context ctx) {

        if(!blockEntity.isFront()){
            lens.setZeroTransform();
            return;
        }
        LaserBeamBehavior.LaserBeam beam = blockEntity.beamBehavior.getLaser(blockEntity.getFacing());


        float partialTicks = AnimationTickHolder.getPartialTicks();

        float speed = blockEntity.visualSpeed.getValue(partialTicks) * 3 / 10f;
        float angle = blockEntity.angle + speed * partialTicks;

        if (Math.abs(angle - lastAngle) < 0.001)
            return;

        lens.setIdentityTransform()
                .translate(getVisualPosition())
                .translate(0.5f, 0.5f, 0.5f)
                .rotateToFace(blockEntity.getFacing())
                .rotate(angle, Z)
                .translate(-0.5f, -0.5f, -0.5f);

        lastAngle = angle;

    }

    @Override
    public void updateLight(float partialTicks) {
        relight(pos.relative(blockEntity.getFacing()), lens);
    }

    @Override
    protected void _delete() {
        lens.delete();
    }

    @Override
    public void collectCrumblingInstances(Consumer<@Nullable Instance> consumer) {
        consumer.accept(lens);
    }
}
