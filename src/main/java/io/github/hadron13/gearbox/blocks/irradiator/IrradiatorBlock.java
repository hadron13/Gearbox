package io.github.hadron13.gearbox.blocks.irradiator;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.base.KineticBlock;
import com.simibubi.create.foundation.block.IBE;
import io.github.hadron13.gearbox.register.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class IrradiatorBlock extends KineticBlock implements IBE<IrradiatorBlockEntity> {
    public IrradiatorBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == Direction.UP;
    }
    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        return !AllBlocks.BASIN.has(worldIn.getBlockState(pos.below()));
    }
    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return Direction.Axis.Y;
    }

    @Override
    public Class<IrradiatorBlockEntity> getBlockEntityClass() {
        return IrradiatorBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends IrradiatorBlockEntity> getBlockEntityType() {
        return ModBlockEntities.IRRADIATOR.get();
    }
}
