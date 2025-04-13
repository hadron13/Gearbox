package io.github.hadron13.gearbox.blocks.sapper;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.RotatingInstance;
import com.simibubi.create.content.kinetics.simpleRelays.encased.EncasedCogVisual;
import com.simibubi.create.foundation.render.AllInstanceTypes;
import dev.engine_room.flywheel.api.visual.DynamicVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.instance.InstanceTypes;
import dev.engine_room.flywheel.lib.instance.OrientedInstance;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import io.github.hadron13.gearbox.register.ModPartialModels;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import static com.simibubi.create.content.kinetics.base.HorizontalKineticBlock.HORIZONTAL_FACING;
import static net.minecraft.core.Direction.*;

public class SapperVisual extends EncasedCogVisual implements SimpleDynamicVisual {

    private final RotatingInstance drillHead;
    private final OrientedInstance drillPole;
    private final SapperBlockEntity sapper;

    final Direction direction;
    private final Direction opposite;

    public SapperVisual(VisualizationContext context, SapperBlockEntity blockEntity, float partialTick) {
        super(context, blockEntity, false, partialTick, Models.partial(AllPartialModels.SHAFTLESS_COGWHEEL));
        this.sapper = blockEntity;
        direction = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING);
        opposite = direction.getOpposite();

        drillHead = instancerProvider().instancer(AllInstanceTypes.ROTATING, Models.partial(ModPartialModels.SAPPER_HEAD))
                .createInstance();

//:3
        drillHead.setup(blockEntity, blockEntity.getRenderedHeadRotationSpeed(partialTick) * 2)
                .setPosition(getVisualPosition())
                .rotateToFace(direction)
                .setChanged();

        drillPole = instancerProvider().instancer(InstanceTypes.ORIENTED, Models.partial(ModPartialModels.SAPPER_POLE)).createInstance();

        drillPole.position(getVisualPosition()).rotateToFace(direction).setChanged();

    }

    @Override
    public void beginFrame(DynamicVisual.Context ctx) {

        float ticks = AnimationTickHolder.getPartialTicks();

        float renderedHeadOffset =  sapper.getRenderedHeadOffset(ticks);

        Direction direction = blockState.getValue(HORIZONTAL_FACING);

        int x_multiplier = (direction==WEST)?  1 : (direction==EAST)?  -1 : 0;
        int z_multiplier = (direction==NORTH)? 1 : (direction==SOUTH)? -1 : 0;

        drillHead.nudge(renderedHeadOffset * x_multiplier ,  0 , renderedHeadOffset * z_multiplier);

//        renderedHeadOffset -= 1/16f;
        drillPole.translatePosition(renderedHeadOffset * x_multiplier ,  0 , renderedHeadOffset * z_multiplier);

    }


    @Override
    public void updateLight(float partialTicks) {
        super.updateLight(partialTicks);

        relight(pos.relative(blockState.getValue(HORIZONTAL_FACING).getOpposite(), 1), drillHead);
        relight(pos, drillPole);
    }

    @Override
    public void _delete() {
        super._delete();
        drillHead.delete();
        drillPole.delete();
    }
}