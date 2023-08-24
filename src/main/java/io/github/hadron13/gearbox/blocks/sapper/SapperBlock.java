package io.github.hadron13.gearbox.blocks.sapper;

import com.simibubi.create.AllShapes;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlock;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.content.kinetics.drill.DrillBlockEntity;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.utility.Iterate;
import io.github.hadron13.gearbox.register.ModBlockEntities;
import io.github.hadron13.gearbox.register.ModShapes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Function;

public class SapperBlock extends HorizontalKineticBlock implements IBE<SapperBlockEntity>, ICogWheel {

    public SapperBlock(Properties properties) {
        super(properties);
    }


    @Override
    public Axis getRotationAxis(BlockState state) {
        return state.getValue(HORIZONTAL_FACING)
                .getAxis();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return ModShapes.SAPPER.get(state.getValue(HORIZONTAL_FACING));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction preferredHorizontalFacing = getPreferredHorizontalFacing(context);
        return this.defaultBlockState()
                .setValue(HORIZONTAL_FACING,
                        preferredHorizontalFacing != null ? preferredHorizontalFacing.getCounterClockWise()
                                : context.getHorizontalDirection()
                                .getOpposite());
    }




    public static boolean hasPipeTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return state.getValue(HORIZONTAL_FACING) == face;
    }

    @Override
    public Direction getPreferredHorizontalFacing(BlockPlaceContext context) {
        Direction fromParent = super.getPreferredHorizontalFacing(context);
        if (fromParent != null)
            return fromParent;

        Direction prefferedSide = null;
        for (Direction facing : Iterate.horizontalDirections) {
            BlockPos pos = context.getClickedPos()
                    .relative(facing);
            BlockState blockState = context.getLevel()
                    .getBlockState(pos);
            if (FluidPipeBlock.canConnectTo(context.getLevel(), pos, blockState, facing))
                if (prefferedSide != null && prefferedSide.getAxis() != facing.getAxis()) {
                    prefferedSide = null;
                    break;
                } else
                    prefferedSide = facing;
        }
        return prefferedSide == null ? null : prefferedSide.getOpposite();
    }


    @Override
    public Class<SapperBlockEntity> getBlockEntityClass() {
        return SapperBlockEntity.class;
    }
    @Override
    public BlockEntityType<? extends SapperBlockEntity> getBlockEntityType() {
        return ModBlockEntities.SAPPER.get();
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }
}