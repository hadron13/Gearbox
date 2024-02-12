package io.github.hadron13.gearbox.blocks.useless_machine;

import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.foundation.block.IBE;
import io.github.hadron13.gearbox.register.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class UselessMachineBlock extends HorizontalKineticBlock implements IBE<UselessMachineBlockEntity>, ICogWheel {
    public UselessMachineBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == Direction.UP;
    }


    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return Axis.Y;
    }

    @Override
    public Class<UselessMachineBlockEntity> getBlockEntityClass() {
        return UselessMachineBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends UselessMachineBlockEntity> getBlockEntityType() {
        return ModBlockEntities.USELESS_MACHINE.get();
    }
}
