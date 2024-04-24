package io.github.hadron13.gearbox.blocks.centrifuge;

import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.base.SingleRotatingInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockEntity;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import io.github.hadron13.gearbox.register.ModPartialModels;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class CentrifugeInstance extends SingleRotatingInstance<CentrifugeBlockEntity> {
    public CentrifugeInstance(MaterialManager materialManager, CentrifugeBlockEntity blockEntity) {
        super(materialManager, blockEntity);
    }
    @Override
    protected Instancer<RotatingData> getModel() {
        Direction.Axis axis = KineticBlockEntityRenderer.getRotationAxisOf(blockEntity);
        Direction facing = Direction.fromAxisAndDirection(axis, Direction.AxisDirection.POSITIVE);

        return getRotatingMaterial().getModel(ModPartialModels.CENTRIFUGE_COG, blockState, facing,
                () -> this.rotateToAxis(axis));
    }

    private PoseStack rotateToAxis(Direction.Axis axis) {
        Direction facing = Direction.fromAxisAndDirection(axis, Direction.AxisDirection.POSITIVE);
        PoseStack poseStack = new PoseStack();
        TransformStack.cast(poseStack)
                .centre()
                .rotateToFace(facing)
                .multiply(Vector3f.XN.rotationDegrees(-90))
                .unCentre();
        return poseStack;
    }
}
