package io.github.hadron13.gearbox.blocks.prism;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import io.github.hadron13.gearbox.register.ModBlockEntities;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;

public class PrismBlock extends Block implements IWrenchable, IBE<PrismBlockEntity> {

    public static final Property<Direction.Axis> HORIZONTAL_AXIS = BlockStateProperties.HORIZONTAL_AXIS;

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_AXIS);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(HORIZONTAL_AXIS, context.getHorizontalDirection().getAxis());
    }

    public PrismBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Class<PrismBlockEntity> getBlockEntityClass() {
        return PrismBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends PrismBlockEntity> getBlockEntityType() {
        return ModBlockEntities.PRISM.get();
    }
}
