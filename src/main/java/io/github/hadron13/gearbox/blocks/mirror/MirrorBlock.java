package io.github.hadron13.gearbox.blocks.mirror;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.kinetics.base.*;
import com.simibubi.create.foundation.block.IBE;
import io.github.hadron13.gearbox.register.ModBlockEntities;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.Objects;

public class MirrorBlock extends Block implements IBE<MirrorBlockEntity>, IWrenchable {

    public static final Property<Direction> HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;

    public MirrorBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_FACING);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {

        return this.defaultBlockState()
                .setValue(HORIZONTAL_FACING, context.getHorizontalDirection()
                        .getOpposite());
    }

    public BlockState updateAfterWrenched(BlockState newState, UseOnContext context) {
        if(context.getClickedFace() == Direction.UP || context.getClickedFace() == Direction.DOWN){
            Objects.requireNonNull(getBlockEntity(context.getLevel(), context.getClickedPos())).wrenched(newState);
        }
        return Block.updateFromNeighbourShapes(newState, context.getLevel(), context.getClickedPos());
    }

    public BlockState getRotatedBlockState(BlockState originalState, Direction targetedFace) {

        if (targetedFace.getAxis() == Direction.Axis.Y) {
            return originalState.setValue(HorizontalKineticBlock.HORIZONTAL_FACING, originalState
                    .getValue(HorizontalKineticBlock.HORIZONTAL_FACING).getClockWise(targetedFace.getAxis()));
        }else{
            return originalState.setValue(HORIZONTAL_FACING, targetedFace);
        }
    }
    @Override
    public Class<MirrorBlockEntity> getBlockEntityClass() {
        return MirrorBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends MirrorBlockEntity> getBlockEntityType() {
        return ModBlockEntities.MIRROR.get();
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(HORIZONTAL_FACING, rot.rotate(state.getValue(HORIZONTAL_FACING)));
    }

}
