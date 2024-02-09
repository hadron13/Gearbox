package io.github.hadron13.gearbox.blocks.planetary_gear;

import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.transmission.SplitShaftBlockEntity;
import io.github.hadron13.gearbox.mixin.accessor.RotationPropagatorAccessor;
import io.github.hadron13.gearbox.register.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

//  Stolen carefully  from
//
//  -* Petrolpark *-
//
//  link: https://github.com/petrolpark/Destroy/blob/1.20.1/src/main/java/com/petrolpark/destroy/block/entity/PlanetaryGearsetBlockEntity.java
public class PlanetaryGearsetBlockEntity extends SplitShaftBlockEntity {

    public PlanetaryGearsetBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public float propagateRotationTo(KineticBlockEntity target, BlockState stateFrom, BlockState stateTo, BlockPos diff, boolean connectedViaAxes, boolean connectedViaCogs) {
        if (connectedViaAxes) {
            if (ModBlocks.PLANETARY_GEARSET.has(stateTo)) return 0;
            return Math.signum(RotationPropagatorAccessor.invokeGetAxisModifier(target, directionBetween(target.getBlockPos(), getBlockPos()))) * -2;
        }

        return super.propagateRotationTo(target, stateFrom, stateTo, diff, connectedViaAxes, connectedViaCogs);
    }

    @Override
    public List<BlockPos> addPropagationLocations(IRotate block, BlockState state, List<BlockPos> neighbours) {
        super.addPropagationLocations(block, state, neighbours);
        addLargeCogwheelPropagationLocations(worldPosition, neighbours);
        return neighbours;
    }

    @Override
    public float getRotationSpeedModifier(Direction face) {
        return -2;
    }

    @Override
    protected boolean canPropagateDiagonally(IRotate block, BlockState state) {
        return true;
    }

    public static Direction directionBetween(BlockPos posFrom, BlockPos posTo) {
        for (Direction direction : Direction.values()) {
            if (posFrom.relative(direction).equals(posTo)) return direction;
        };
        return null;
    };
    public static void addLargeCogwheelPropagationLocations(BlockPos pos, List<BlockPos> neighbours) {
        BlockPos.betweenClosedStream(new BlockPos(-1, -1, -1), new BlockPos(1, 1, 1))
                .forEach(offset -> {
                    if (offset.distSqr(BlockPos.ZERO) == 2)
                        neighbours.add(pos.offset(offset));
                });
    }



}
