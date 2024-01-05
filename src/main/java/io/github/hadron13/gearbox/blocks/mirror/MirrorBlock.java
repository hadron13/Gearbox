package io.github.hadron13.gearbox.blocks.mirror;

import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;

public class MirrorBlock extends Block implements IBE<MirrorBlockEntity> {

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
    public Class<MirrorBlockEntity> getBlockEntityClass() {
        return MirrorBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends MirrorBlockEntity> getBlockEntityType() {
        return null;
    }
}
