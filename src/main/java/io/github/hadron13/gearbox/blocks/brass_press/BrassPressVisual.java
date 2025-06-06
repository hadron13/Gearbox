package io.github.hadron13.gearbox.blocks.brass_press;

import com.mojang.math.Axis;
import com.simibubi.create.content.kinetics.base.RotatingInstance;
import com.simibubi.create.content.kinetics.base.ShaftVisual;
import com.simibubi.create.content.kinetics.press.MechanicalPressBlock;
import com.simibubi.create.content.kinetics.press.PressingBehaviour;
import com.simibubi.create.foundation.render.AllInstanceTypes;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visual.DynamicVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.instance.InstanceTypes;
import dev.engine_room.flywheel.lib.instance.OrientedInstance;
import dev.engine_room.flywheel.lib.instance.TransformedInstance;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import io.github.hadron13.gearbox.blocks.sapper.SapperBlockEntity;
import io.github.hadron13.gearbox.register.ModPartialModels;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.math.AngleHelper;
import org.joml.Quaternionf;

import java.util.function.Consumer;

public class BrassPressVisual extends ShaftVisual<BrassPressBlockEntity> implements SimpleDynamicVisual {
    private final OrientedInstance pressHead;
    private final OrientedInstance pressPole;
    private final BrassPressBlockEntity brassPress;
    private float lastRenderedHeadRotation = 0;


    public BrassPressVisual(VisualizationContext context, BrassPressBlockEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick);
        this.brassPress = blockEntity;

        pressHead = instancerProvider().instancer(InstanceTypes.ORIENTED, Models.partial(ModPartialModels.BRASS_PRESS_HEAD)).createInstance();

        pressPole = instancerProvider().instancer(InstanceTypes.ORIENTED, Models.partial(ModPartialModels.BRASS_PRESS_POLE)).createInstance();

        Quaternionf q = Axis.YP.rotationDegrees(AngleHelper.horizontalAngle(blockState.getValue(MechanicalPressBlock.HORIZONTAL_FACING)));

        pressPole.rotation(q);

        transformModels(partialTick);
    }

    @Override
    public void beginFrame(DynamicVisual.Context ctx) {
        transformModels(ctx.partialTick());
    }

    private void animate() {

    }

    private void transformModels(float pt) {
        float renderedHeadRotation = brassPress.getRenderedHeadRotation(AnimationTickHolder.getPartialTicks());
        float renderedHeadOffset = getRenderedHeadOffset(brassPress);
        pressHead.setVisible(true);
        pressPole.position(getVisualPosition())
                .translatePosition(0, -renderedHeadOffset, 0)
                .setChanged();

        rotateHead(renderedHeadRotation, renderedHeadOffset);

    }

    private void rotateHead(float rotation, float offset) {
        pressHead.position(getVisualPosition())
                .translatePosition(0, -offset, 0)
                .rotateYDegrees(rotation-lastRenderedHeadRotation)
                .setChanged();
        lastRenderedHeadRotation = rotation;
    }

    private float getRenderedHeadOffset(BrassPressBlockEntity press) {
        PressingBehaviour pressingBehaviour = press.getPressingBehaviour();

        return press.getRenderedHeadOffset(AnimationTickHolder.getPartialTicks())
                * pressingBehaviour.mode.headOffset;
    }

    @Override
    public void updateLight(float partialTick) {
        super.updateLight(partialTick);
        relight(pos, pressHead);
        relight(pos, pressPole);
    }

    @Override
    protected void _delete() {
        super._delete();
        pressHead.delete();
        pressPole.delete();
    }

    @Override
    public void collectCrumblingInstances(Consumer<Instance> consumer) {
        super.collectCrumblingInstances(consumer);
        consumer.accept(pressHead);
        consumer.accept(pressPole);
    }
}
