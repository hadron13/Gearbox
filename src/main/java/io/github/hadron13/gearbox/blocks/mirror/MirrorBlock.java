package io.github.hadron13.gearbox.blocks.mirror;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import io.github.hadron13.gearbox.register.ModBlockEntities;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;

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
        withBlockEntityDo(context.getLevel(), context.getClickedPos(),  MirrorBlockEntity::wrenched);
        return Block.updateFromNeighbourShapes(newState, context.getLevel(), context.getClickedPos());
    }
    @Override
    public Class<MirrorBlockEntity> getBlockEntityClass() {
        return MirrorBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends MirrorBlockEntity> getBlockEntityType() {
        return ModBlockEntities.MIRROR.get();
    }
}
