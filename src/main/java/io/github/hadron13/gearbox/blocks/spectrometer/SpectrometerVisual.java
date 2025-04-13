package io.github.hadron13.gearbox.blocks.spectrometer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.gauge.GaugeVisual;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.instance.Instancer;
import dev.engine_room.flywheel.api.visual.DynamicVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.instance.FlatLit;
import dev.engine_room.flywheel.lib.instance.InstanceTypes;
import dev.engine_room.flywheel.lib.instance.TransformedInstance;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import dev.engine_room.flywheel.lib.visual.AbstractBlockEntityVisual;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import io.github.hadron13.gearbox.register.ModPartialModels;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.data.Couple;
import net.createmod.catnip.data.Iterate;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import java.util.ArrayList;
import java.util.function.Consumer;


public class SpectrometerVisual extends AbstractBlockEntityVisual<SpectrometerBlockEntity> implements SimpleDynamicVisual {
    protected final ArrayList<DialFace> faces;

    protected PoseStack ms;

    public SpectrometerVisual(VisualizationContext context, SpectrometerBlockEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick);

        faces = new ArrayList<>(2);

        SpectrometerBlock gaugeBlock = (SpectrometerBlock) blockState.getBlock();

        Instancer<TransformedInstance> dialModel = instancerProvider().instancer(InstanceTypes.TRANSFORMED, Models.partial(AllPartialModels.GAUGE_DIAL));
        Instancer<TransformedInstance> headModel = instancerProvider().instancer(InstanceTypes.TRANSFORMED, Models.partial(ModPartialModels.SPECTROGAUGE));

        ms = new PoseStack();
        TransformStack msr = TransformStack.of(ms);
        msr.translate(getVisualPosition());

        float progress = Mth.lerp(AnimationTickHolder.getPartialTicks(), blockEntity.prevDialState, blockEntity.dialState);

        for (Direction facing : Iterate.directions) {
            if (!gaugeBlock.shouldRenderHeadOnFace(level, pos, blockState, facing))
                continue;

            DialFace face = makeFace(facing, dialModel, headModel);

            faces.add(face);

            face.setupTransform(msr, progress);
        }
    }

    private DialFace makeFace(Direction face, Instancer<TransformedInstance> dialModel, Instancer<TransformedInstance> headModel) {
        return new DialFace(face, dialModel.createInstance(), headModel.createInstance());
    }

    @Override
    public void beginFrame(DynamicVisual.Context ctx) {
        if (Mth.equal(blockEntity.prevDialState, blockEntity.dialState))
            return;

        float progress = Mth.lerp(ctx.partialTick(), blockEntity.prevDialState, blockEntity.dialState);

        var msr = TransformStack.of(ms);

        for (DialFace faceEntry : faces) {
            faceEntry.updateTransform(msr, progress);
        }
    }

    @Override
    public void updateLight(float partialTick) {
        relight(faces.stream()
                .flatMap(Couple::stream).toArray(FlatLit[]::new));
    }

    @Override
    public void _delete() {
        faces.forEach(DialFace::delete);
    }

    @Override
    public void collectCrumblingInstances(Consumer<Instance> consumer) {
        for (DialFace face : faces) {
            face.forEach(consumer);
        }
    }

    protected class DialFace extends Couple<TransformedInstance> {

        Direction face;

        public DialFace(Direction face, TransformedInstance first, TransformedInstance second) {
            super(first, second);
            this.face = face;
        }

        private void setupTransform(TransformStack<?> msr, float progress) {
            float dialPivot = 5.75f / 16;

            msr.pushPose();
            rotateToFace(msr);

            getSecond().setTransform(ms).setChanged();

            msr.translate(0, dialPivot, dialPivot)
                    .rotate((float) (Math.PI / 2 * -progress), Direction.EAST)
                    .translate(0, -dialPivot, -dialPivot);

            getFirst().setTransform(ms).setChanged();

            msr.popPose();
        }

        private void updateTransform(TransformStack<?> msr, float progress) {
            float dialPivot = 5.75f / 16;

            msr.pushPose();

            rotateToFace(msr)
                    .translate(0, dialPivot, dialPivot)
                    .rotate((float) (Math.PI / 2 * -progress), Direction.EAST)
                    .translate(0, -dialPivot, -dialPivot);

            getFirst().setTransform(ms).setChanged();

            msr.popPose();
        }

        protected TransformStack<?> rotateToFace(TransformStack<?> msr) {
            return msr.center()
                    .rotate((float) ((-face.toYRot() - 90) / 180 * Math.PI), Direction.UP)
                    .uncenter();
        }

        private void delete() {
            getFirst().delete();
            getSecond().delete();
        }
    }



}
