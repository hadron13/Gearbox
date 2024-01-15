package io.github.hadron13.gearbox.blocks.spectrometer;

import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.backend.instancing.blockentity.BlockEntityInstance;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.jozufozu.flywheel.util.AnimationTickHolder;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.Iterate;
import io.github.hadron13.gearbox.register.ModPartialModels;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import java.util.ArrayList;


public class SpectrometerInstance extends BlockEntityInstance<SpectrometerBlockEntity> implements DynamicInstance {
    protected final ArrayList<DialFace> faces;

    protected PoseStack ms;

    public SpectrometerInstance(MaterialManager materialManager, SpectrometerBlockEntity blockEntity) {
        super(materialManager, blockEntity);

        faces = new ArrayList<>(2);

        SpectrometerBlock gaugeBlock = (SpectrometerBlock) blockState.getBlock();

        Instancer<ModelData> dialModel = getTransformMaterial().getModel(AllPartialModels.GAUGE_DIAL, blockState);
        Instancer<ModelData> headModel = getTransformMaterial().getModel(ModPartialModels.SPECTROGAUGE, blockState);

        ms = new PoseStack();
        TransformStack msr = TransformStack.cast(ms);
        msr.translate(getInstancePosition());

        float progress = Mth.lerp(AnimationTickHolder.getPartialTicks(), blockEntity.prevDialState, blockEntity.dialState);

        for (Direction facing : Iterate.directions) {
            if (!gaugeBlock.shouldRenderHeadOnFace(world, pos, blockState, facing))
                continue;

            DialFace face = makeFace(facing, dialModel, headModel);

            faces.add(face);

            face.setupTransform(msr, progress);
        }
    }

    private DialFace makeFace(Direction face, Instancer<ModelData> dialModel, Instancer<ModelData> headModel) {
        return new DialFace(face, dialModel.createInstance(), headModel.createInstance());
    }

    @Override
    public void beginFrame() {
        SpectrometerBlockEntity gaugeBlockEntity = blockEntity;

        if (Mth.equal(gaugeBlockEntity.prevDialState, gaugeBlockEntity.dialState))
            return;

        float progress = Mth.lerp(AnimationTickHolder.getPartialTicks(), gaugeBlockEntity.prevDialState, gaugeBlockEntity.dialState);

        TransformStack msr = TransformStack.cast(ms);

        for (DialFace faceEntry : faces) {
            faceEntry.updateTransform(msr, progress);
        }
    }

    @Override
    public void updateLight() {
        super.updateLight();
        relight(pos, faces.stream()
                .flatMap(Couple::stream));
    }

    @Override
    public void remove() {
        faces.forEach(DialFace::delete);
    }

    private class DialFace extends Couple<ModelData> {

        Direction face;

        public DialFace(Direction face, ModelData first, ModelData second) {
            super(first, second);
            this.face = face;
        }

        private void setupTransform(TransformStack msr, float progress) {
            float dialPivot = 5.75f / 16;

            msr.pushPose();
            rotateToFace(msr);

            getSecond().setTransform(ms);

            msr.translate(0, dialPivot, dialPivot)
                    .rotate(Direction.EAST, (float) (Math.PI / 2 * -progress))
                    .translate(0, -dialPivot, -dialPivot);

            getFirst().setTransform(ms);

            msr.popPose();
        }

        private void updateTransform(TransformStack msr, float progress) {
            float dialPivot = 5.75f / 16;

            msr.pushPose();

            rotateToFace(msr)
                    .translate(0, dialPivot, dialPivot)
                    .rotate(Direction.EAST, (float) (Math.PI / 2 * -progress))
                    .translate(0, -dialPivot, -dialPivot);

            getFirst().setTransform(ms);

            msr.popPose();
        }

        protected TransformStack rotateToFace(TransformStack msr) {
            return msr.centre()
                    .rotate(Direction.UP, (float) ((-face.toYRot() - 90) / 180 * Math.PI))
                    .unCentre();
        }

        private void delete() {
            getFirst().delete();
            getSecond().delete();
        }
    }



}
