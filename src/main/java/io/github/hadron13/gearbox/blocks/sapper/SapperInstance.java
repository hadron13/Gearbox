package io.github.hadron13.gearbox.blocks.sapper;

import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.core.PartialModel;
import com.jozufozu.flywheel.core.materials.oriented.OrientedData;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.SingleRotatingInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import com.simibubi.create.content.kinetics.simpleRelays.encased.EncasedCogInstance;
import com.simibubi.create.foundation.render.AllMaterialSpecs;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import io.github.hadron13.gearbox.register.ModPartialModels;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import static com.simibubi.create.content.kinetics.base.HorizontalKineticBlock.HORIZONTAL_FACING;
import static net.minecraft.core.Direction.*;

public class SapperInstance extends EncasedCogInstance implements DynamicInstance {

    private final RotatingData drillHead;
    private final OrientedData drillPole;
    private final SapperBlockEntity sapper;

    public SapperInstance(MaterialManager materialManager, SapperBlockEntity blockEntity) {
        super(materialManager, blockEntity, false);
        this.sapper = blockEntity;
        Direction facing = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING);

        drillHead = materialManager.defaultCutout()
                .material(AllMaterialSpecs.ROTATING)
                .getModel(ModPartialModels.SAPPER_HEAD, blockState, facing.getOpposite())
                .createInstance();


        drillHead.setRotationAxis(facing.getAxis());

        drillPole = getOrientedMaterial()
                .getModel(ModPartialModels.SAPPER_POLE, blockState, facing.getOpposite())
                .createInstance();

//        drillPole.setRotation(facing.getRotation());

    }

    @Override
    protected Instancer<RotatingData> getCogModel() {
        BlockState referenceState = blockEntity.getBlockState();
        Direction facing = referenceState.getValue(HORIZONTAL_FACING);
        PartialModel partial = AllPartialModels.SHAFTLESS_COGWHEEL;


        return getRotatingMaterial().getModel(partial, referenceState, facing, () -> {
            PoseStack poseStack = new PoseStack();
            TransformStack.cast(poseStack)
                    .centre()
                    .rotateToFace(facing)
                    .multiply(Vector3f.XN.rotationDegrees(90))
                    .unCentre();
            return poseStack;
        });
    }
    @Override
    public void beginFrame() {

        float ticks = AnimationTickHolder.getPartialTicks();

        float renderedHeadOffset =  sapper.getRenderedHeadOffset(ticks);
        float speed =        sapper.getRenderedHeadRotationSpeed(ticks);


        Direction direction = blockState.getValue(HORIZONTAL_FACING);

        int x_multiplier = (direction==WEST)?  1 : (direction==EAST)?  -1 : 0;
        int z_multiplier = (direction==NORTH)? 1 : (direction==SOUTH)? -1 : 0;

        drillHead.setPosition(getInstancePosition())
                .nudge(renderedHeadOffset * x_multiplier ,  0 , renderedHeadOffset * z_multiplier)
                .setRotationalSpeed(speed * 2);

//        renderedHeadOffset -= 1/16f;
        drillPole.setPosition(getInstancePosition())
                .nudge(renderedHeadOffset * x_multiplier ,  0 , renderedHeadOffset * z_multiplier);

    }


    @Override
    public void updateLight() {
        super.updateLight();

        relight(pos.relative(blockState.getValue(HORIZONTAL_FACING).getOpposite(), 1), drillHead);
        relight(pos, drillPole);
    }

    @Override
    public void remove() {
        super.remove();
        drillHead.delete();
        drillPole.delete();
    }
}